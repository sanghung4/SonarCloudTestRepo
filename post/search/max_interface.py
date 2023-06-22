"""Data Converters."""
import json
from time import time

from loguru import logger

#########################################################
# ES RESULTS -> APP SEARCH RESULTS CONVERTER
#########################################################

rename_dict = {
    "tech_specs_list": "technical_specifications",
    "color_finish": "colorfinish",
    "customer_number_list": "customer_number",
    "in_stock_location_list": "in_stock_location",
    "product_branch_exclusion": "territory_exclusion_list",
}

add_dict = {
    "_meta": {"engine": "post", "score": 0},
    "clean_product_brand": {"raw": "Placeholder"},
    "clean_vendor_part_nbr": {"raw": "Placeholder"},
    "clean_web_description": {"raw": "Placeholder"},
}


def convert_search_res_to_max_format(
    es_results: list, pagination_info: dict, facets: dict
) -> dict:
    """Convert ES results to AppSearch Results.

    Args:
    ----
        es_results (list): Raw ES results.

    Returns
    -------
        dict: Converted results
    """

    logger.info("Converting ES response to mirror AppSearch")
    # Remove Meta Fields from PoST
    try:
        post = [product["_source"] for product in es_results if product.get("_source")]

        # Lowercase all keys
        post_temp = []
        for product in post:
            record = {
                k.lower(): {"raw": v} for k, v in product.items() if v is not None
            }
            if "minimum_increment_qty" in record.keys():
                record["minimum_increment_qty"] = {
                    "raw": int(record["minimum_increment_qty"]["raw"])
                }
            # TODO, fix this at the ingestion layer, not here
            if "customer_part_numbers" in record.keys():
                # This is bad
                record["customer_part_numbers"] = {
                    "raw": json.loads(
                        record["customer_part_numbers"]["raw"].split(":", 1)[1]
                    )
                }
                # each dict in record["customer_part_numbers"]["raw"] needs to be a string with double quote literals for backend
                record["customer_part_numbers"]["raw"] = [
                    str(item).replace("'", '"')
                    for item in record["customer_part_numbers"]["raw"]
                ]

            if "tech_specs_list" in record.keys():
                record["tech_specs_list"] = {
                    "raw": [json.dumps(record["tech_specs_list"]["raw"])]
                }

            post_temp.append(record)
        post = post_temp
        del post_temp

        post_temp = []
        for product in post:
            for old, new in rename_dict.items():
                try:
                    product[new] = product.pop(old)
                except KeyError:
                    pass
            product.update(add_dict)
            product["_meta"]["id"] = product["id"]["raw"]
            product["internal_item_nbr"] = product["id"]
            post_temp.append(product)
        post = post_temp
        del post_temp

        final = {
            "meta": {
                "alerts": [],
                "warnings": [],
                "precision": 0,
                "page": pagination_info,
                "engine": {"name": "PoST", "type": "product search"},
                "request_id": f"{hex(int(time()))}",
            },
            "results": post,
        }
        if facets:
            final["facets"] = facets

    except Exception as e:
        logger.error("Unable to convert ES results to AppSearch format")
        logger.exception(e)
        return {}
    return final
