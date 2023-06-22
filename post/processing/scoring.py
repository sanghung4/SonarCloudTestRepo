from collections import Counter

import numpy as np
import pandas as pd
import typer
from elasticsearch.helpers import bulk
from elasticsearch_dsl import Search
from loguru import logger

from post.config.configuration import SETTINGS
from post.search.search_products import related_part_finder
from post.utils.utils import coalesce, isnan, mult_counter
from post.utils.utils_es import es_dsl_search_paginated, init_es_conn

app = typer.Typer()


##########################################
# THESE COMMANDS RUN PER RECORD DURING BATCH PROCESSING OF THE PDW
##########################################


def compute_part_scores(parts: list) -> dict:
    """takes a list of PART tags and attempts to score each part on the likelihood of it being the true part."""
    # start w freq of the part * 2
    part_scores = mult_counter(Counter((part["correct_text"] for part in parts)), 2)

    for part_to_score in part_scores:
        # add a score for lower ordinal mention (pos number) being better
        part_scores[part_to_score] += np.clip(
            10
            - 2
            * (
                min(
                    (
                        part["pos_n"]
                        for part in parts
                        if part["correct_text"] == part_to_score
                    )
                )
                - 1
            ),
            1,
            100,
        )

        # add a score for lower text number
        part_scores[part_to_score] += np.clip(
            10
            - 2
            * (
                min(
                    (
                        part["text_n"]
                        for part in parts
                        if part["correct_text"] == part_to_score
                    )
                )
                - 1
            ),
            1,
            100,
        )

        # add a score for higher n_words of the part being better
        part_scores[part_to_score] += (
            4
            * [
                part["n_words"]
                for part in parts
                if part["correct_text"] == part_to_score
            ][0]
        )

        # add a score for higher n_chars of the part being better
        part_scores[part_to_score] += (
            1
            * [
                len(part["correct_text"])
                for part in parts
                if part["correct_text"] == part_to_score
            ][0]
        )

        # add a score for higher n_chars of the part AS ORIGINALLY WRITTEN being better
        # (not the normalized value).  The idea is that if the part is fully written out, it's more
        # likely to be the actual part, as opposed to an abbreviation which might refer to an accessory
        part_scores[part_to_score] += (
            1
            * [
                len(part["text"])
                for part in parts
                if part["correct_text"] == part_to_score
            ][0]
        )

        # add a bonus for being the first OR second entry in PRODUCT_OVERVIEW_DESCRIPTION which is usually pretty accurate
        # NB: don't make this too high, as there are cases in which it is wildly inaccurate.
        part_scores[part_to_score] += 10 * np.sign(
            len(
                [
                    part
                    for part in parts
                    if part["correct_text"] == part_to_score
                    and part["text_n"] in (1, 2)
                    and part["source"] == "PRODUCT_OVERVIEW_DESCRIPTION"
                ]
            )
        )

        # add a bonus for being the in WEB_DESCRIPTION which can be less verbose but slightly more accurate
        part_scores[part_to_score] += 10 * np.sign(
            len(
                [
                    part
                    for part in parts
                    if part["correct_text"] == part_to_score
                    and part["pos_n"] == 1
                    and part["source"] == "WEB_DESCRIPTION"
                ]
            )
        )

    # if any of the parts are related to others, and the others are present, add those scores together.
    # EG: if PIPE is present or DRAIN WASTE VENT is present and DRAIN WASTE VENT PIPE is present, boost the latter.
    # this seems to happen not infrequently, where parts of the parsed texts add up to the actual part
    for part, score in part_scores.items():
        related_parts = related_part_finder(part)
        for related_part in related_parts:
            if related_part in part_scores:
                part_scores[related_part] += score

    # create the final record, including a normalized version of the scores
    part_scores = {key: int(val) for key, val in part_scores.items()}
    scores_sum = sum(part_scores.values())
    probable_part = max(part_scores, key=part_scores.get)
    part_scores_normalized = {k: v / scores_sum for k, v in part_scores.items()}
    res = {
        "scores": part_scores,
        "scores_normalized": part_scores_normalized,
        "probable_value": {
            "value": probable_part,
            "score": part_scores[probable_part],
            "score_normalized": part_scores_normalized[probable_part],
        },
    }

    return res


def compute_manf_scores(manfs: list) -> dict:
    """takes a list of MANUFACTURER tags and attempts to score each part on the likelihood of it being the true manf."""
    # start w freq of the part * 2
    manf_scores = mult_counter(Counter((manf["correct_text"] for manf in manfs)), 2)

    for manf_to_score in manf_scores:
        # add a score for lower ordinal mention being better
        manf_scores[manf_to_score] += 10 - 2 * (
            min(
                (
                    manf["pos_n"]
                    for manf in manfs
                    if manf["correct_text"] == manf_to_score
                )
            )
            - 1
        )

        # add a HUGE bonus for being the in MFR_FULL_NAME which, when populated, is correct
        manf_scores[manf_to_score] += 100 * np.sign(
            len(
                [
                    manf
                    for manf in manfs
                    if manf["correct_text"] == manf_to_score
                    and manf["source"] == "MFR_FULL_NAME"
                ]
            )
        )

    # create the final record, including a normalized version of the scores
    manf_scores = {key: int(val) for key, val in manf_scores.items()}
    scores_sum = sum(manf_scores.values())
    probable_manf = max(manf_scores, key=manf_scores.get)
    manf_scores_normalized = {k: v / scores_sum for k, v in manf_scores.items()}
    res = {
        "scores": manf_scores,
        "scores_normalized": manf_scores_normalized,
        "probable_value": {
            "value": probable_manf,
            "score": manf_scores[probable_manf],
            "score_normalized": manf_scores_normalized[probable_manf],
        },
    }

    return res


def compute_mat_scores(mats: list) -> dict:
    """takes a list of MATERIAL tags and attempts to score each part on the likelihood of it being the true material."""
    # start w freq of the part * 2
    mat_scores = mult_counter(Counter((mat["correct_text"] for mat in mats)), 2)

    for mat_to_score in mat_scores:
        # add a score for lower ordinal mention being better
        mat_scores[mat_to_score] += 10 - 2 * (
            min((mat["pos_n"] for mat in mats if mat["correct_text"] == mat_to_score))
            - 1
        )

    # create the final record, including a normalized version of the scores
    mat_scores = {key: int(val) for key, val in mat_scores.items()}
    scores_sum = sum(mat_scores.values())
    probable_mat = max(mat_scores, key=mat_scores.get)
    mat_scores_normalized = {k: v / scores_sum for k, v in mat_scores.items()}
    res = {
        "scores": mat_scores,
        "scores_normalized": mat_scores_normalized,
        "probable_value": {
            "value": probable_mat,
            "score": mat_scores[probable_mat],
            "score_normalized": mat_scores_normalized[probable_mat],
        },
    }

    return res


##########################################
# THESE COMMANDS RUN AFTER THE ENTIRE BATCH IS DONE
##########################################


@app.command()
def calc_cross_sectional_stats(index_name: str = None) -> None:
    """for each category, computes the count and percentage of each probable part found in it
    also, normalizes part and category sales info across the cross-section
    .
    """
    conn_es = init_es_conn()

    # the sales-info related stats we will be calculating.
    METRIC_SOURCES = ["product", "category"]
    METRICS = ["sales", "qty", "invoiced"]
    METRIC_TRANSFORMS = ["pctrank", "pctlog"]

    ##########################################
    # download the entire parsed PDW from ES
    ##########################################
    search = Search(using=conn_es, index=index_name or SETTINGS.elastic_index_products)
    # only retrieve necessary fields
    search = search.source(
        [
            "_ID",
            "CATEGORY_1_NAME",
            "CATEGORY_2_NAME",
            "CATEGORY_3_NAME",
            "_PART_SCORES.probable_value.value",
            "_PART_SCORES.probable_value.score",
            "_PART_SCORES.probable_value.score_normalized",
        ]
        + [
            f"sales_info.{source}_{metric}"
            for source in METRIC_SOURCES
            for metric in METRICS
        ]
    )
    res, _, _ = es_dsl_search_paginated(search, 50000, None, download_all_records=True)

    logger.info(f"downloaded {len(res)} records from ES")

    ##########################################
    # cross-sectional stats having to do with part distributions
    ##########################################

    # convert list of dictionaries to dataframe and normalize the json data in _source column to a flat table
    res = [rec for rec in res if isinstance(rec, dict)]
    df_products = pd.DataFrame(res).drop_duplicates(subset="_id").set_index("_id")
    df_products = pd.json_normalize(df_products["_source"]).set_index(df_products.index)

    # rename cols, alphabetize and drop "TBC" as it's not a meaningful cat
    df_products.columns = [col.split(".")[-1] for col in df_products.columns]
    df_products = df_products[sorted(df_products.columns)]

    # count the parts by category and part
    GROUPBY_1 = [
        "CATEGORY_1_NAME",
        "CATEGORY_2_NAME",
        "CATEGORY_3_NAME",
        "value",
    ]
    df_products["dummy"] = 1
    df_cats_grouped = df_products.groupby(GROUPBY_1).agg(count=("dummy", "count"))

    # count the number of parts within each category
    GROUPBY_2 = ["CATEGORY_1_NAME", "CATEGORY_2_NAME", "CATEGORY_3_NAME"]
    df_cats_grouped["cat_total_count"] = df_cats_grouped.groupby(GROUPBY_2).transform(
        np.sum
    )

    # count the number of parts over all categories
    df_cats_grouped["part_total_count"] = (
        df_cats_grouped["count"].groupby(["value"]).transform(np.sum)
    )

    # compute the pct of cateogry which is that part AND the percentage of each part in each category
    df_cats_grouped["pct_cat_which_is_part"] = (
        df_cats_grouped["count"] / df_cats_grouped["cat_total_count"]
    )
    df_cats_grouped["pct_part_in_cat"] = (
        df_cats_grouped["count"] / df_cats_grouped["part_total_count"]
    )

    # join back to products
    df_products = df_products.merge(
        df_cats_grouped[["pct_cat_which_is_part", "pct_part_in_cat"]],
        how="left",
        left_on=GROUPBY_1,
        right_index=True,
    )

    # compute a distribution score
    def compute_search_score(row: pd.Series) -> float:
        """the formula for the final search score, put here as a separate function for clarity."""
        # TBC is not a reliable category; assume zero confidence in probability analysis
        if isnan(row["CATEGORY_3_NAME"]) or row["CATEGORY_3_NAME"] == "TBC":
            vals = [row["score_normalized"], 0, 0]

        # else, normal calculation
        else:
            vals = row[["score_normalized", "pct_cat_which_is_part", "pct_part_in_cat"]]

        if coalesce(vals) is None:
            return np.nan
        return round(np.nanmean(vals), 3)

    df_products["search_score"] = df_products.apply(compute_search_score, axis=1)

    ##########################################
    # cross-sectional stats having to do with sales information
    ##########################################
    for metric in METRICS:
        # compute percentile of product metric and a log measure also
        df_products[f"product_{metric}_pctrank"] = df_products[
            f"product_{metric}"
        ].rank(pct=True)

        max_log = np.log(np.nanmax(df_products[f"product_{metric}"]))
        df_products[f"product_{metric}_pctlog"] = df_products[
            f"product_{metric}"
        ].apply(lambda x: None if isnan(x) or x <= 0 else np.log(x) / max_log)

        # compute percentile of category metric
        df_products[f"category_{metric}_pctrank"] = df_products.loc[
            ~df_products["CATEGORY_3_NAME"].isin(["TBC", None]), f"category_{metric}"
        ].rank(pct=True)

        max_log = np.log(np.nanmax(df_products[f"category_{metric}"]))
        df_products[f"category_{metric}_pctlog"] = df_products[
            f"category_{metric}"
        ].apply(lambda x: None if isnan(x) or x <= 0 else np.log(x) / max_log)

    ##########################################
    # compute average score by category - this is not saved at the moment but may be useful information to peruse
    ##########################################

    df_products.groupby(["CATEGORY_1_NAME", "CATEGORY_2_NAME", "CATEGORY_3_NAME"]).agg(
        avg_search_score=("search_score", np.mean)
    )

    ##########################################
    # write back to ES
    ##########################################

    # create ES bulk instructions; delete old and add new
    logger.info("Bulk uploading PART STATS to ES.")
    _PART_SCORES_fields = ["pct_cat_which_is_part", "pct_part_in_cat", "search_score"]
    sales_info_fields = [
        f"{source}_{metric}_{trans}"
        for source in METRIC_SOURCES
        for metric in METRICS
        for trans in METRIC_TRANSFORMS
    ]
    df_products = df_products[df_products.index.notnull()]
    json_res = df_products[_PART_SCORES_fields + sales_info_fields].to_dict(
        orient="index"
    )
    body = []
    for key, val in json_res.items():
        id = key
        doc = {
            "_PART_SCORES": {
                "probable_value": {
                    key2: val2
                    for key2, val2 in val.items()
                    if not isnan(val2) and key2 in _PART_SCORES_fields
                }
            },
            "sales_info": {
                key2: val2
                for key2, val2 in val.items()
                if not isnan(val2) and key2 in sales_info_fields
            },
        }

        body.append(
            {"_op_type": "update", "_id": id, "doc": doc, "doc_as_upsert": True}
        )

    success, failures = bulk(
        conn_es,
        stats_only=True,
        index=index_name or SETTINGS.elastic_index_products,
        actions=body,
        chunk_size=5000,
        max_retries=3,
    )

    if failures:
        raise Exception("ES bulk import failed")
    else:
        logger.info("Bulk upload complete.")
    logger.info(f"Successfully updated {success} products.")
    logger.info("Done.")

# entry point
if __name__ == "__main__":
    app()
