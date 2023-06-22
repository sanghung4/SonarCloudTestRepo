import sys
from datetime import date

import orjson
import pandas as pd
import typer
from loguru import logger

from post.config.configuration import ELASTIC_INDEX_PRODUCTS
from post.config.parts_of_speech import POS
from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams
from post.search.search_products import collect_tags_into_dict
from post.utils.utils import isnan, make_worker_pool
from post.utils.utils_es import init_es_conn

# columns from which to make lists of texts to search
COLUMNS_TO_SEARCH = ["MFR_FULL_NAME", "SEARCH_KEYWORD_TEXT", "WEB_DESCRIPTION"]
RESULTS_FILENAME = f"{date.today().isoformat()}_PARSER.json"


# take a row in the PDW and construct a search string
# categorize all of them and get attributes back in JSON
# top-level to be compatible with multiprocessing
def parse_pdw_record(row: pd.Series) -> bool:
    # make the search string(s)
    search_strings = []

    # search the unstructured columns
    for col in COLUMNS_TO_SEARCH:
        if not isnan(row[col]):
            search_strings += [row[col]]

    # search PRODUCT_OVERVIEW_DESCRIPTION which is well delimited by ;
    try:
        if not isnan(row["PRODUCT_OVERVIEW_DESCRIPTION"]):
            pod_phrases = row["PRODUCT_OVERVIEW_DESCRIPTION"].split(";")
            for pod_phrase in pod_phrases:
                if len(pod_phrase):
                    search_strings += [pod_phrase]
    except Exception as col_error:
        logger.error("Issue parsing Product Overview Description")
        logger.exception(col_error)
        sys.exit(1)

    # tag
    tags = tag_parts_of_speech_ngrams(search_strings)

    # consolidate, meaning collect a list of objects by part of speech.
    # prefix the parsed fields with "_" by convention
    doc2 = collect_tags_into_dict(tags)

    # don't store certain fields for speed and space
    for key in list(doc2):
        pos = key[1:]
        if (
            pos not in POS
            or "stored_in_es" not in POS[pos]
            or not POS[pos]["stored_in_es"]
        ):
            del doc2[key]

    # turn the orignal record into dict
    doc1 = row.to_dict()

    # combine
    doc = {**doc1, **doc2}
    id = row.name

    return (id, doc)


def main(
    n_pools: int,
    start_row: int,
    end_row: int,
    chunk_size: int,
    products_filepath: str,
) -> None:
    print(f"products_filepath: {products_filepath}")
    # ES conn
    conn_es = init_es_conn()

    # load extract from snowflake
    raw_products_df = pd.read_csv(products_filepath).dropna(subset="INTERNAL_ITEM_NBR")
    raw_products_df.loc[:, "INTERNAL_ITEM_NBR"] = raw_products_df[
        "INTERNAL_ITEM_NBR"
    ].astype(
        int
    )  # same as eclipse ID
    raw_products_df = raw_products_df.set_index("INTERNAL_ITEM_NBR").sort_index(
        ascending=False
    )

    # choose subset of rows
    raw_products_df = raw_products_df.iloc[start_row:end_row, :]

    # make pool
    pool = make_worker_pool(n_pools)

    # loop over chunks
    def chunker(seq, size):
        return (seq[pos : pos + size] for pos in range(0, len(seq), size))

    for chunk in chunker(raw_products_df, chunk_size):
        logger.info(f"Parsing IDs {chunk.index.values[0]} to {chunk.index.values[-1]}")

        # parse records asyncronously
        results = pool.map(parse_pdw_record, (row for _, row in chunk.iterrows()))
        results = list(filter(None, results))  # bad results

        with open(RESULTS_FILENAME, "a", encoding="utf-8") as fout:
            fout.write(orjson.dumps(results).decode() + "\n")

        # create ES bulk instructions; delete old and add new
        body = []
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
            es_results = conn_es.bulk(index=ELASTIC_INDEX_PRODUCTS, body=body)
            print(es_results)
        except Exception as es_error:
            logger.error("Unable to write bulk batch to ES")
            logger.exception(es_error)

        """try:
            failed_deletes = sum(
                [
                    item["delete"]["_shards"]["failed"]
                    for item in es_results["items"]
                    if "delete" in item.keys()
                ]
            )
            failed_indexes = sum(
                [
                    item["index"]["_shards"]["failed"]
                    for item in es_results["items"]
                    if "index" in item.keys()
                ]
            )
            if failed_deletes:
                logger.debug(f"There were {failed_deletes} failed deletes.")
            if failed_indexes:
                logger.error(f"There were {failed_indexes} failed indexes.")
                raise f"There were {failed_indexes} failed indexes."
        except KeyError as kerror:
            logger.error("Issue collection failed deletes and indexes")
            logger.exception(kerror)"""


# entry point.  Pass # of pools to use as parameter
if __name__ == "__main__":
    typer.run(main)
