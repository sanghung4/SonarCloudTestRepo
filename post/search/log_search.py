"""Log Search + Results."""
from datetime import datetime
from time import time

import elasticsearch
from loguru import logger

from post.config.configuration import SETTINGS

ATTRIBUTES_TO_SAVE = [
    "WEB_DESCRIPTION",
    "VENDOR_PART_NBR",
    "FULL_IMAGE_URL_NAME",
]


def log_search(
    con: elasticsearch.Elasticsearch,
    results: dict,
    term: str,
    tags_list: list,
    search_meta: dict,
    time_taken: int,
) -> bool:
    """Log Search to Elasticsearch.

    Args:
    ----
        con (elasticsearch.Elasticsearch): Elasticsearch connection obj
        results (dict): First 50 search results, MSC's
        tags_list (list): list of tags
        term (str): Search term entered
        search_meta (dict): Search metadata.

    Returns
    -------
        bool: _description_
    """
    document_id = f"{time()}_{hash(term)}"
    products_cleaned = {}
    try:
        if results is not None:
            for product_record in results:
                key = product_record["_source"]["ID"]
                product_dict = {
                    k: v
                    for k, v in product_record["_source"].items()
                    if k in ATTRIBUTES_TO_SAVE
                }
                products_cleaned[key] = product_dict
                len(products_cleaned)
        else:
            products_cleaned = None
    except Exception:
        logger.error("Unable to collect results for search")
        products_cleaned = {}
    logger.debug(products_cleaned)

    body = {
        "search_term": term,
        "search_tags": {"tags": tags_list},
        "search_results": products_cleaned,
        "date_searched": datetime.now().isoformat(),
        "search_source": search_meta["SOURCE"]
        if isinstance(search_meta, dict)
        else search_meta.SOURCE,
        "search_dest": search_meta["DEST"]
        if isinstance(search_meta, dict)
        else search_meta.DEST,
        "time_taken": time_taken,
        "total_result_count": 0 if products_cleaned is None else len(products_cleaned),
    }

    try:
        result = con.index(
            index=SETTINGS.elastic_index_search, id=document_id, document=body
        )
        logger.info(f"Saved search results for {result['_id']}")
        return True
    except Exception:
        logger.error("Could not log search")
        return False
