import datetime
import json
import os
import re
import signal
import sys
from collections import Counter
from datetime import date

import boto3
import orjson
import pandas as pd
import typer
from loguru import logger

from post.config.configuration import APPLICATION_VERSION, SETTINGS
from post.config.parts_of_speech import POS
from post.processing.scoring import (
    compute_manf_scores,
    compute_mat_scores,
    compute_part_scores,
)
from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams
from post.search.search_products import collect_tags_into_dict
from post.utils.utils import isnan, make_worker_pool
from post.utils.utils_es import init_es_conn

logger.info(APPLICATION_VERSION)
app = typer.Typer()

# Create an SQS client
# if os.environ.get('aws_access_key_id'):
sqs = boto3.client("sqs", region_name=SETTINGS.aws_region_name)
s3 = boto3.client("s3")

# output file for backup.
RESULTS_FILENAME = f"{date.today().isoformat()}_PARSER.json"

# set the REDIS cache to ALWAYS write.  Note that for multiprocessing, the full variable name must be written out

# sub-fields to exclude in the PRODUCT_OVERVIEW_DESCRIPTION
PRODUCT_OVERVIEW_DESCRIPTION_EXCLUDE_REGEX = re.compile(
    r"(?i)^.*(APPLICATION|USED ON|WARRANTY|RECOMMENDED FOR|INCLUSIONS\/FEATURES|FEATURES|INCLUSIONS|INCLUDED).*$"
)

# SALES INFORMATION USED TO AID THE SEARCH
if SETTINGS.post_env.lower() == "lambda":
    base_path = "/tmp"
    print("Attempting to Download Sales Data")
    s3.download_file(
        SETTINGS.sales_data_bucket,
        "sales_data.csv",
        os.path.join(base_path, "sales_data.csv"),
    )
    PRODUCT_SALES_FILE = os.path.join(base_path, "sales_data.csv")
else:
    base_path = "/app/post/static_lists/products/raw"
    PRODUCT_SALES_FILE = "post/static_lists/sales/sales.csv"

DF_PRODUCT_SALES = pd.read_csv(PRODUCT_SALES_FILE, index_col=0)
# TODO: move this DML to the sales data pipeline
DF_CATEGORY_SALES = DF_PRODUCT_SALES.groupby(
    ["CATEGORY_LEVEL_1", "CATEGORY_LEVEL_2", "CATEGORY_LEVEL_3"]
).sum()


def shutdown(signum: int = 1):
    logger.info(f"😴 Caught SIGTERM, shutting down with PID {signum}")
    # Finish any outstanding requests, then...
    exit(signum)


def parse_product_record(row: pd.Series) -> bool:
    """take a row in the PDW and construct a search string
    categorize all of them and get attributes back in JSON
    top-level to be compatible with multiprocessing
    .
    """
    try:
        id = row.name
    except Exception:
        logger.warning(f"Unable to set ID for {row}")
        id = "IRRETRIEVABLE"

    try:
        row["TERRITORY_EXCLUSION_LIST"] = orjson.loads(row["TERRITORY_EXCLUSION_LIST"])
    except Exception:
        logger.error(f"Unable to parse TERRITORY_EXCLUSION_LIST for ID: {id}")
        row["TERRITORY_EXCLUSIOn_LIST"] = []

    try:
        row["TECH_SPECS_LIST"] = orjson.loads(row["TECH_SPECS_LIST"])
    except Exception:
        logger.error(f"Unable to parse TECH_SPECS_LIST for ID: {id}")
        row["TECH_SPECS_LIST"] = []

    try:
        row["IN_STOCK_LOCATION_LIST"] = orjson.loads(row["IN_STOCK_LOCATION_LIST"])
    except Exception:
        logger.error(f"Unable to parse IN_STOCK_LOCATION_LIST for ID: {id}")
        row["IN_STOCK_LOCATION_LIST"] = []

    try:
        # a list to hold all results
        all_tags = []

        # use PRODUCT_OVERVIEW_DESCRIPTION if it has ";" which generally means a high-quality description,
        # although other times, it is a very low-quality one
        if (
            not isnan(row["PRODUCT_OVERVIEW_DESCRIPTION"])
            and ";" in row["PRODUCT_OVERVIEW_DESCRIPTION"]
        ):
            split_strs = row["PRODUCT_OVERVIEW_DESCRIPTION"].split(";")

            # exclude certain phrases that don't contain useful information.
            search_strings = [
                ss.strip()
                for ss in split_strs
                if not PRODUCT_OVERVIEW_DESCRIPTION_EXCLUDE_REGEX.search(ss)
            ]

            # some keywords in PRODUCT_OVERVIEW_DESCRIPTION phrases indicate that a PART should not be parsed;
            # disallow any PARTS from being picked up in such strings.
            NON_PART_INDICATORS = [
                "TYPE",
                "SIZE",
                "MATERIAL",
                "CONNECTION",
                "OPTIONS",
                "DIMENSIONS",
                "DETAILS",
            ]
            search_strings_words = [
                list(map(str.upper, ss.split())) for ss in search_strings
            ]
            search_strings_anypos = [
                ss if not any((npi in ssw for npi in NON_PART_INDICATORS)) else ""
                for ss, ssw in zip(search_strings, search_strings_words)
            ]
            search_strings_nonpart = [
                ss if any((npi in ssw for npi in NON_PART_INDICATORS)) else ""
                for ss, ssw in zip(search_strings, search_strings_words)
            ]

            tags_nontype = tag_parts_of_speech_ngrams(
                search_strings_anypos,
                allow_spellcheck=False,
                source="PRODUCT_OVERVIEW_DESCRIPTION",
                allow_actions=False,
            )
            tags_type = tag_parts_of_speech_ngrams(
                search_strings_nonpart,
                allow_spellcheck=False,
                source="PRODUCT_OVERVIEW_DESCRIPTION",
                disallowed_cats=["PART"],
                allow_actions=False,
            )

            all_tags += tags_nontype + tags_type

        # use TECH_SPECS_LIST
        # TODO: TEMP DISABLE
        # if not isnan(row["TECH_SPECS_LIST"]):
        if False:
            tech_specs = orjson.loads("{" + row["TECH_SPECS_LIST"] + "}")[
                "technical_specifications"
            ][0]
            TECH_SPECS_IGNORE = [
                "used_on_item",
                "used_on_model/brand",
                "used_on_item_part_no",
                "applicable_standard",
                "application",
                "options",
                "inclusions/features",
            ]
            search_strings = [
                key + " " + val
                for key, val in tech_specs.items()
                if key not in TECH_SPECS_IGNORE and "details" not in key
            ]
            tags = tag_parts_of_speech_ngrams(
                search_strings,
                allow_spellcheck=False,
                source="TECH_SPECS_LIST",
                allow_actions=False,
            )
            all_tags += tags

        # use WEB_DESCRIPTION with "," as a delimiter
        if not isnan(row["WEB_DESCRIPTION"]):
            search_strings = row["WEB_DESCRIPTION"].split(",")
            tags = tag_parts_of_speech_ngrams(
                search_strings,
                allow_spellcheck=False,
                source="WEB_DESCRIPTION",
                allow_actions=False,
            )
            all_tags += tags

        # use SEARCH_KEYWORD_TEXT
        if not isnan(row["SEARCH_KEYWORD_TEXT"]):
            tags = tag_parts_of_speech_ngrams(
                row["SEARCH_KEYWORD_TEXT"],
                allow_spellcheck=False,
                source="SEARCH_KEYWORD_TEXT",
                allow_actions=False,
            )
            all_tags += tags

        # use MFR_FULL_NAME
        if not isnan(row["MFR_FULL_NAME"]):
            tags = tag_parts_of_speech_ngrams(
                row["MFR_FULL_NAME"],
                allow_spellcheck=False,
                source="MFR_FULL_NAME",
                allow_actions=False,
            )
            all_tags += tags

        # use VENDOR_PART_NBR
        if not isnan(row["VENDOR_PART_NBR"]):
            tags = tag_parts_of_speech_ngrams(
                row["VENDOR_PART_NBR"],
                allow_spellcheck=False,
                source="VENDOR_PART_NBR",
                allow_actions=False,
            )
            all_tags += tags

        # consolidate, meaning collect a list of objects by part of speech.
        # prefix the parsed fields with "_" by convention
        doc2 = collect_tags_into_dict(all_tags)

        # don't store certain POS and/or fields for speed and space
        for key in list(doc2):
            pos = key
            if (
                pos not in POS
                or "stored_in_es" not in POS[pos]
                or not POS[pos]["stored_in_es"]
            ):
                del doc2[key]

        # turn the original record into dict
        doc1 = row.to_dict()

        # APPEND UNDERSCORES FOR ES
        doc2 = {"_" + k: v for k, v in doc2.items()}

        # combine
        doc = {**doc1, **doc2}

        # compute a PROBABLE PART via score at this step to avoid ES complication downstream
        if "_PART" in doc:
            doc["_PART_SCORES"] = compute_part_scores(doc["_PART"])

        # compute a PROBABLE MANUFACTURER via score at this step
        if "_MANUFACTURER" in doc:
            doc["_MANUFACTURER_SCORES"] = compute_manf_scores(doc["_MANUFACTURER"])

        # compute a PROBABLE MATERIAL via score at this step
        if "_MATERIAL" in doc:
            doc["_MATERIAL_SCORES"] = compute_mat_scores(doc["_MATERIAL"])

        # note time of creation and version
        doc["record_created"] = datetime.datetime.today()
        doc["application_version"] = APPLICATION_VERSION

        # look up sales info if available
        MSC = row["ID"]
        (
            product_sales,
            category_sales,
            product_qty,
            category_qty,
            product_invoiced,
            category_invoiced,
        ) = (None,) * 6
        if MSC in DF_PRODUCT_SALES.index:
            product_sales = DF_PRODUCT_SALES.loc[MSC, "SALES_AMOUNT"]
            product_qty = DF_PRODUCT_SALES.loc[MSC, "SALES_QTY"]
            product_invoiced = DF_PRODUCT_SALES.loc[MSC, "INVOICED_COUNT"]
            category = tuple(
                DF_PRODUCT_SALES.loc[
                    MSC, ["CATEGORY_LEVEL_1", "CATEGORY_LEVEL_2", "CATEGORY_LEVEL_3"]
                ]
            )
            if category in DF_CATEGORY_SALES.index:
                category_sales = DF_CATEGORY_SALES.loc[category, "SALES_AMOUNT"]
                category_qty = DF_CATEGORY_SALES.loc[category, "SALES_QTY"]
                category_invoiced = DF_CATEGORY_SALES.loc[category, "INVOICED_COUNT"]
        sales_info = {
            "product_sales": None if isnan(product_sales) else int(product_sales),
            "category_sales": None if isnan(category_sales) else int(category_sales),
            "product_qty": None if isnan(product_qty) else int(product_qty),
            "category_qty": None if isnan(category_qty) else int(category_qty),
            "product_invoiced": None
            if isnan(product_invoiced)
            else int(product_invoiced),
            "category_invoiced": None
            if isnan(category_invoiced)
            else int(category_invoiced),
        }
        doc["sales_info"] = sales_info

        return (id, doc)

    except Exception as ex:
        logger.error(f"parse_product_record failed for row {row}")
        logger.exception(ex)
        if id is not None:
            return (id, None)
        return ()


@app.command()
def parse_batch(
    n_pools: int,
    start_row: int,
    end_row: int,
    chunk_size: int,
    products_filepath: str,
    debug: bool,
    index_name: str,
) -> None:
    """Parses a batch of records from the PDW.

    Args:
    ----
        n_pools (int): Number of threads to user
        start_row (int): Starting Index
        end_row (int): End Index
        chunk_size (int): Records per chunk
        products_filepath (str): Filepath to PDW csv
        debug (bool): Debugging

    Raises
    ------
        f: _description_

    Returns
    -------
        _type_: _description_
    """
    # test ES conn and REDIS conn upfront; keep the former
    conn_es = init_es_conn()
    # with get_redis_conn():
    #     pass

    # load extract which should have been exported already from snowflake
    raw_products_df = pd.read_csv(products_filepath)
    # Replace missing vendor part numbers with 1 - MSC number
    raw_products_df["INTERNAL_ITEM_NBR"].fillna(
        raw_products_df["ERP_PRODUCT_ID"].apply(lambda x: 1 - int(x.split("-")[1])),
        inplace=True,
    )
    raw_products_df.loc[:, "INTERNAL_ITEM_NBR"] = raw_products_df[
        "INTERNAL_ITEM_NBR"
    ].astype(
        int
    )  # same as eclipse ID
    raw_products_df = raw_products_df.set_index("INTERNAL_ITEM_NBR").sort_index(
        ascending=False
    )

    if SETTINGS.post_env.lower() != "lambda":
        pool = make_worker_pool(n_pools)
        # choose subset of rows

    if debug:
        logger.warning("You are in debug mode.")
        raw_products_df = raw_products_df[raw_products_df["ID"] == "MSC-101754"]
        pool = make_worker_pool(1)
    else:
        raw_products_df = raw_products_df.iloc[start_row:end_row, :]

    # loop over chunks
    def chunker(seq, size):
        return (seq[pos : pos + size] for pos in range(0, len(seq), size))

    for chunk in chunker(raw_products_df, chunk_size):
        logger.info(f"Parsing IDs {chunk.index.values[0]} to {chunk.index.values[-1]}")
        if SETTINGS.post_env.lower() == "lambda":
            logger.info("Environment is Lambda, parsing synchronously")
            raw_results = [parse_product_record(row) for _, row in chunk.iterrows()]
        else:
            logger.info(
                f"Parsing IDs {chunk.index.values[0]} to {chunk.index.values[-1]}"
            )
            # parse records asyncronously
            raw_results = pool.map(
                parse_product_record, (row for _, row in chunk.iterrows())
            )
        results = list(filter(lambda x: x[1] is not None, raw_results))  # Good results
        failed = list(filter(lambda x: x[1] is None, raw_results))  # bad results

        if len(failed) > 0:
            logger.error(f"Records failed to parse: {failed}")

        # with open(RESULTS_FILENAME, "a", encoding="utf-8") as fout:

        # create ES bulk instructions; delete old and add new
        body = []
        logger.info(f"about to load {len(results)} records into ES")
        for res in results:
            # this cleans up the doc into "proper" format so ES can ingest it
            id = res[0]
            doc = orjson.dumps(res[1]).decode()

            body.append(
                {"delete": {"_id": id}}
            )  # to do: make sure this doesn't cause crash when record is not there
            body.append({"index": {"_id": id}})
            body.append(doc)

        logger.info(
            f"Storing res to ES for IDs {chunk.index.values[0]} to {chunk.index.values[-1]}"
        )
        try:
            es_results = conn_es.bulk(index=index_name, body=body)
            if es_results["errors"]:
                raise Exception("ES bulk import failed")

        except Exception as es_error:
            logger.error("Unable to write bulk batch to ES")
            logger.error(es_error)

        try:
            failed_indexes = []
            for item in es_results["items"]:
                if "index" in item.keys() and "error" in item["index"].keys():
                    failed_indexes.append(item)
            len(failed_indexes)

            delete_statuses = Counter()
            for item in es_results["items"]:
                if "delete" in item.keys() and "result" in item["delete"].keys():
                    delete_statuses.update([item["delete"].get("result")])

            if delete_statuses:
                logger.debug(f"Here are the statuses for 'deletes'\n{delete_statuses}")
            if failed_indexes:
                logger.error(f"There were {len(failed_indexes)} failed indexes.")
                logger.error(failed_indexes)

        except KeyError:
            logger.error(
                f"Unable to compute the number of failed deletes or indexes.  The bulk command was:\n{body}"
            )


def process_message(message_body):
    print(f"processing message: {message_body}")
    # do what you want with the message here
    data = json.loads(message_body) if isinstance(message_body, str) else message_body
    print(f"data: {data}")
    data_file = data.get("s3")
    if not data_file:
        logger.warning("no s3 key set in message")
        return
    start = data.get("start")
    if not start:
        logger.warning("no start key set in message")
        return
    end = data.get("end")
    if not end:
        logger.warning("no end key set in message")
        return
    n_pools = os.cpu_count()
    batch = 500

    if not os.path.exists(
        os.path.join(base_path, f"/post/static_lists/products/raw/{data_file}")
    ):
        print("downloading data")
        file_name = "/".join(data_file.split("/"))[-1]
        path = os.path.join(base_path, file_name)
        s3.download_file(
            data_file.split("/")[2]
            if data_file.startswith("s3://")
            else SETTINGS.pdw_data_bucket,
            "/".join(data_file.replace("s3://", "").split("/")[1:]),
            path,
        )

    logger.info(
        f"Processing batch with the following params:\n \Pools: {n_pools}\nStart: {start}\nEnd: {end}\nChunksize: {batch}\nPath: {path}\n"
    )
    index_name = message_body.get("indexName", SETTINGS.elastic_index_products)
    parse_batch(n_pools, start, end, batch, path, False, index_name=index_name)


@app.command()
def sqs_listener():
    """Main function to run in AWS ECS
    (1) Listens to SQS queue
    (2) Picks up messages
    (3) Starts processing batch using `process_batch()`
    (4) Repeat.
    """
    loops = 0
    loop_stop = 10
    signal.signal(signal.SIGTERM, shutdown)
    while True:
        logger.info(f"🔄 Looping {loops+1} time out of {loop_stop}")

        if loops == loop_stop:
            logger.info("Haven't found any new messages in a while, going to sleep.")
            shutdown(1)

        messages = sqs.receive_message(
            QueueUrl=SETTINGS.aws_sqs_queue_name,
            MaxNumberOfMessages=1,  # Read only one message at a time
            WaitTimeSeconds=20,  # Wait up to 20 seconds for a message to arrive
        )
        for message in messages.get("Messages", []):
            logger.debug(message)
            sqs.delete_message(
                QueueUrl=SETTINGS.aws_sqs_queue_name,
                ReceiptHandle=message.get("ReceiptHandle"),
            )
            process_message(message.get("Body", ""))
        if len(messages.get("Messages", [])) > 0:
            loops = 0
        else:
            loops += 1


# entry point.
if __name__ == "__main__":
    logger.debug("Setting log level to INFO")
    logger.remove()
    logger.add(sys.stderr, level="INFO")
    app()
    signal.signal(signal.SIGTERM, shutdown)
