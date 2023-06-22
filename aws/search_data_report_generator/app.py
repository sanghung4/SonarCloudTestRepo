"""Saves Search Data in Multiple Formats."""

import datetime

import elasticsearch
import pandas as pd
from elasticsearch_dsl import Search
from loguru import logger

from post.utils.utils_es import init_es_conn

CONN = init_es_conn()


def get_search_data(
    conn: elasticsearch.Elasticsearch,
    start_date: datetime.date,
    end_date: datetime.date,
    search_index_name: str = "search",
) -> pd.DataFrame():
    """Gets raw data from search data index.

    Args:
        conn (ElasticSearch): Elasticsearch connection object.
        start_date(datetime.date): Start date of search data.
        end_date(datetime.date): End date of search data.
        search_index_name(str): Name of search data index.

    Returns
    -------
        pd.Dataframe: Dataframe containing the search data documents.
    """
    # construct the search
    logger.debug("Fetching search data")
    s = Search(using=conn, index=search_index_name).filter(
        "range", date_searched={"gte": start_date, "lt": end_date}
    )

    # construct list from scan on hits
    hits_list = []  # 🔫
    for hit in s.scan():
        hit_dict = {}
        hit_dict["id"] = hit.meta.id
        hit_dict.update(hit.to_dict())
        hits_list.append(hit_dict)

    return pd.DataFrame(hits_list)


# %%
# %%
def flatten_search_data(search_data: pd.DataFrame) -> pd.DataFrame:
    """Takes raw search data DF and converts to tidy format.

    Each row is a search result tied to a search ID.

    Args:
        search_data (pd.DataFrame): Raw search data.

    Returns
    -------
        pd.DataFrame: Expanded search data.
    """
    temp = pd.DataFrame()
    index_num = 0
    for _idx, row in search_data.iterrows():
        for _idx2, product in enumerate(row.search_results.values()):
            product_row = pd.DataFrame(product, index=[index_num])
            meta = pd.DataFrame(row).T
            meta.index = [index_num]

            final = pd.concat([meta, product_row], axis=1)
            temp = pd.concat([temp, final], axis=0)
            index_num += 1
    return temp


# %%


def flatten_search_data_wide(search_data: pd.DataFrame) -> pd.DataFrame:
    """Creates a widened search data format.

    Searches are combined, results are appended horizontaly.

    Args:
        search_data (pd.DataFrame): Prepared search data

    Returns
    -------
        pd.DataFrame: Widened search data.
    """
    logger.debug("Widening search data - this may take some time")
    temp = pd.DataFrame()
    for idx, row in search_data.iterrows():
        product_row = pd.DataFrame()
        meta = pd.DataFrame(row).T
        meta.index = [idx]
        meta_new = {k: str(list(v.values())[0]) for k, v in meta.to_dict().items()}
        for idx2, product in enumerate(row.search_results.values()):
            if idx2 == 24:
                break
            product_row_new_index = [f"Result {idx2+1}"]
            product.update(meta_new)
            img_url = product["FULL_IMAGE_URL_NAME"]
            if img_url:
                img_url = "https" + img_url[img_url.index(":") :]
                product["FULL_IMAGE_URL_NAME"] = f'=IMAGE("{img_url}","",0)'

            product_row_new = pd.DataFrame(product, index=product_row_new_index).T
            product_row = pd.concat([product_row, product_row_new], axis=1)

        product_index = [
            f"{meta['search_term'].values[0]}#####{x}" for x in product_row.index
        ]
        product_row = product_row.set_axis(product_index, axis=0)
        temp = pd.concat([temp, product_row], axis=0)

    temp.insert(0, "id", temp.index)
    temp.reset_index(inplace=True)
    temp = temp.drop("index", axis=1)
    search, attr = temp["id"].str.split("#####").str
    meta_placeholder = temp["Result 1"].copy()
    temp.loc[attr.isin(meta_new.keys())] = ""
    temp.insert(0, "Search", search)
    temp.insert(1, "Attribute", attr)
    temp.insert(2, "Metadata", meta_placeholder)
    temp = temp[
        ~temp.Attribute.isin(
            [
                "search_term",
                "search_results",
                "MFR_FULL_NAME",
                "PRODUCT_OVERVIEW_DESCRIPTION",
            ]
        )
    ]
    temp["Metadata"] = temp.apply(
        lambda x: ""
        if x["Attribute"]
        in [
            "VENDOR_PART_NBR",
            "FULL_IMAGE_URL_NAME",
            "WEB_DESCRIPTION",
        ]
        else (x["Metadata"]),
        axis=1,
    )
    temp.drop("id", inplace=True, axis=1)

    return temp


# %%2
def prepare_search_data(search_data: pd.DataFrame) -> pd.DataFrame:
    """Prepares search data for widening.

    Aggregates by search term.

    Args:
        search_data (pd.DataFrame): Search data.

    Returns
    -------
        pd.DataFrame: Aggregated search data.
    """
    logger.debug("Filtering and aggregating search data")
    search_data["search_term"] = search_data["search_term"].str.upper()
    r = (
        search_data.groupby(["search_term"])
        .agg(
            avg_time=("time_taken", "median"),
            date_searched=("date_searched", "min"),
            times_searched=("id", "nunique"),
            search_results=("search_results", "first"),
            source=("search_source", pd.Series.mode),
        )
        .reset_index()
        .sort_values("times_searched", ascending=False)
        .copy()
    )
    return r


## Order of Operations
### Get Search Data -> Prepare it for Widening -> Widen it -> Save
if __name__ == "__main__":
    today = datetime.date.today()
    week_ago = today - datetime.timedelta(days=7)
    filename = f"SEARCH_REPORT_{today.isoformat()}.csv"
    data = get_search_data(init_es_conn(), week_ago, today)
    data = prepare_search_data(data)
    data = flatten_search_data_wide(data)
    logger.info(f"Saving results to {filename}")
    data.to_csv(filename, index=False)
    logger.info("Done! 📊")
