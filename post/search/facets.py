import operator
from functools import reduce

import elasticsearch_dsl
from elasticsearch_dsl import A, AttrDict, Q

from post.utils.utils_nlp_std import text_parsable_num_atend


def facet_Q_maker(
    vals: str, all_or_none: str, fields: list, nested_path: str = None
) -> elasticsearch_dsl.Q:
    """applies a simple filter(s) on the proper field(s) which works for most facets
    for nested fields, pass the nested path"""

    queries = []
    for field in fields:
        if nested_path is None:
            queries.append(Q("terms", **{field: vals}))
        else:
            queries.append(
                Q(
                    "nested",
                    path=nested_path,
                    query=facet_Q_maker(vals, all_or_none, [field]),
                )
            )

    # combine all queries using the appropriate logic based on inclusion versus exclusion
    if all_or_none == "none":
        final_Q = reduce(operator.and_, [~qry for qry in queries])
    else:
        final_Q = reduce(operator.or_, queries)

    return final_Q


def facet_Q_maker_dims(
    vals: str, all_or_none: str, dim_type: list = None
) -> elasticsearch_dsl.Q:
    """makes a query appropriate for filtering on dimension-related fields, like SIZE"""

    queries = []
    for n in range(1, 4):
        for val in vals:
            dim_n_key = f"dim_{n}"
            query = Q(
                "nested",
                path="_DIMENSIONS",
                query=Q(
                    **{
                        "name_or_query": "term",
                        f"_DIMENSIONS.other_info.{dim_n_key}.correct_text": val,
                    }
                ),
            )

            # check type, if passed
            if dim_type is not None:
                query &= Q(
                    **{
                        "name_or_query": "terms",
                        f"_DIMENSIONS.other_info.{dim_n_key}.type.keyword": dim_type,
                    }
                ) | ~Q(
                    **{
                        "name_or_query": "exists",
                        "field": f"_DIMENSIONS.other_info.{dim_n_key}.type",
                    }
                )

            queries.append(query)

    # combine all queries using the appropriate logic based on inclusion versus exclusion
    if all_or_none == "none":
        final_Q = reduce(operator.and_, [~qry for qry in queries])
    else:
        final_Q = reduce(operator.or_, queries)

    return final_Q


def facet_A_maker(
    field: str, nested_path: str = None, nested_agg_name: str = None
) -> elasticsearch_dsl.A:
    """constructs an ES aggregation appropriate for the given field
    for nested fields, pass the nested_path"""

    if nested_path is None:
        return A("terms", size=MAX_FACET_BUCKETS, field=field)
    else:
        return A(
            "nested", path=nested_path, aggs={nested_agg_name: facet_A_maker(field)}
        )


# define a dictionary mapping (custom) facet names to 1. how they are aggregated / computed;
# not all fields are simply an aggregation of one field but require filters or combinations of fields.
# and 2. a function which returns the filter necessary for filtering on that facet in search, given the seach value
MAX_FACET_BUCKETS = 20
NO_LIMIT_FACET = ["in_stock_location"]
# A filter cannot filter it's own facets
FACETS = {
    "mfr_full_name": {
        "es_aggs": [facet_A_maker("_MANUFACTURER_SCORES.probable_value.value")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_MANUFACTURER_SCORES.probable_value.value"]
        ),
    },
    "flow_rate": {
        "es_aggs": [facet_A_maker("_FLOW.correct_text", "_FLOW", "flow_rate_0")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_FLOW.correct_text"], "_FLOW"
        ),
    },
    "capacity": {
        "es_aggs": [facet_A_maker("_VOLUME.correct_text", "_VOLUME", "capacity_0")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_VOLUME.correct_text"], "_VOLUME"
        ),
    },
    "hazardous_material_flag": {
        "es_aggs": [
            A("terms", size=MAX_FACET_BUCKETS, field="HAZARDOUS_MATERIAL_FLAG")
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["HAZARDOUS_MATERIAL_FLAG"]
        ),
    },
    "wattage": {
        "es_aggs": [facet_A_maker("_POWER.correct_text", "_POWER", "wattage_0")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_POWER.correct_text"], "_POWER"
        ),
    },
    "voltage": {
        "es_aggs": [facet_A_maker("_VOLTAGE.correct_text", "_VOLTAGE", "voltage_0")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_VOLTAGE.correct_text"], "_VOLTAGE"
        ),
    },
    "water_sense_compliant_flag": {
        "es_aggs": [
            A("terms", size=MAX_FACET_BUCKETS, field="WATER_SENSE_COMPLIANT_FLAG")
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["WATER_SENSE_COMPLIANT_FLAG"]
        ),
    },
    "id": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["ERP_PRODUCT_ID"]
        ),
    },
    "category_1_name": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="CATEGORY_1_NAME")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["CATEGORY_1_NAME"]
        ),
    },
    "category_2_name": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="CATEGORY_2_NAME")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["CATEGORY_2_NAME"]
        ),
    },
    "category_3_name": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="CATEGORY_3_NAME")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["CATEGORY_3_NAME"]
        ),
    },
    "material": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="_MATERIAL.correct_text")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_MATERIAL.correct_text"]
        ),
    },
    "pressure_rating": {
        "es_aggs": [
            facet_A_maker(
                "_PRESSURE_WGT.correct_text", "_PRESSURE_WGT", "pressure_rating_0"
            )
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_PRESSURE_WGT.correct_text"], "_PRESSURE_WGT"
        ),
    },
    "inlet_size": {
        "es_aggs": [
            A("terms", size=MAX_FACET_BUCKETS, field="_CONNECTION.correct_text")
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_CONNECTION.correct_text"]
        ),
    },
    "energy_star_flag": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="ENERGY_STAR_FLAG")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["ENERGY_STAR_FLAG"]
        ),
    },
    "low_lead_compliant_flag": {
        "es_aggs": [
            A("terms", size=MAX_FACET_BUCKETS, field="LOW_LEAD_COMPLIANT_FLAG")
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["LOW_LEAD_COMPLIANT_FLAG"]
        ),
    },
    "mercury_free_flag": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="MERCURY_FREE_FLAG")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["MERCURY_FREE_FLAG"]
        ),
    },
    "colorfinish": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="_COLOR.correct_text")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_COLOR.correct_text"]
        ),
    },
    "btu": {
        "es_aggs": [facet_A_maker("_HEAT.correct_text", "_HEAT", "btu_0")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_HEAT.correct_text"], "_HEAT"
        ),
    },
    "temperature_rating": {
        "es_aggs": [
            facet_A_maker(
                "_TEMPERATURE.correct_text", "_TEMPERATURE", "temperature_rating_0"
            )
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["_TEMPERATURE.correct_text"], "_TEMPERATURE"
        ),
    },
    "size": {
        "es_aggs": [
            facet_A_maker(
                "_DIMENSIONS.other_info.dim_1.correct_text", "_DIMENSIONS", "size_0"
            ),
            facet_A_maker(
                "_DIMENSIONS.other_info.dim_2.correct_text", "_DIMENSIONS", "size_1"
            ),
            facet_A_maker(
                "_DIMENSIONS.other_info.dim_3.correct_text", "_DIMENSIONS", "size_2"
            ),
        ],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker_dims(
            vals,
            all_or_none,
        ),
    },
    "height": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker_dims(
            vals, all_or_none, ["HEIGHT"]
        ),
    },
    "depth": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker_dims(
            vals, all_or_none, ["DEPTH"]
        ),
    },
    "width": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker_dims(
            vals, all_or_none, ["WIDTH"]
        ),
    },
    "length": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker_dims(
            vals, all_or_none, ["LENGTH"]
        ),
    },
    "territory_exclusion_list": {
        "es_aggs": [],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["TERRITORY_EXCLUSION_LIST"]
        ),
    },
    "in_stock_location": {
        "es_aggs": [A("terms", size=1000, field="IN_STOCK_LOCATION_LIST")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["IN_STOCK_LOCATION_LIST"]
        ),
    },
    "product_line": {
        "es_aggs": [A("terms", size=MAX_FACET_BUCKETS, field="PRODUCT_LINE")],
        "es_filter_func": lambda vals, all_or_none: facet_Q_maker(
            vals, all_or_none, ["PRODUCT_LINE"]
        ),
    },
}

# programmatically generate the aggregations for certain dimension fields as they follow a similar pattern
# these aggregations count the values of the dimensions correct_text when the type matches
FACETS_DIMENSIONS = ["height", "depth", "width", "length"]
for fdmin in FACETS_DIMENSIONS:
    for dim_n in range(0, 3):
        dim_n_key = "dim_" + str(dim_n + 1)
        FACETS[fdmin]["es_aggs"].append(
            A(
                "nested",
                path="_DIMENSIONS",
                aggs={
                    "agg_filter": A(
                        "filter",
                        Q(
                            **{
                                "name_or_query": "term",
                                f"_DIMENSIONS.other_info.{dim_n_key}.type.keyword": fdmin.upper(),
                            }
                        ),
                        aggs={
                            f"{fdmin}_{dim_n}": A(
                                "terms",
                                size=MAX_FACET_BUCKETS,
                                field=f"_DIMENSIONS.other_info.{dim_n_key}.correct_text",
                            )
                        },
                    )
                },
            )
        )


def add_facet_aggs_to_search(
    search: elasticsearch_dsl.Search,
) -> elasticsearch_dsl.Search:
    """Given a search object, adds aggregations for all facets to it"""
    for facet, facet_info in FACETS.items():
        for agg_n, agg in enumerate(facet_info["es_aggs"]):
            search.aggs.bucket(facet + "_" + str(agg_n), agg)
    return search


def process_facet_aggs(facet_attrdict: AttrDict) -> dict:
    """Turns raw ES aggs into facets to be used by search."""

    facets_in = facet_attrdict.to_dict()
    facets_out = {}
    for facet, facet_info in FACETS.items():
        if facet + "_0" in facets_in:
            facets_out[facet] = []

            # concatenate the results of all aggs for this facet and put into the desired format.
            data = {}
            for agg_n, _ in enumerate(FACETS[facet]["es_aggs"]):
                agg_name = facet + "_" + str(agg_n)
                agg_dict = facet_attrdict[agg_name]

                # the buckets may be returned at different levels in the AttrDict object.
                buckets = None
                if "buckets" in agg_dict:
                    buckets = agg_dict["buckets"]
                elif agg_name in agg_dict and "buckets" in agg_dict[agg_name]:
                    buckets = agg_dict[agg_name]["buckets"]
                elif (
                    "agg_filter" in agg_dict
                    and "buckets" in agg_dict["agg_filter"][agg_name]
                ):
                    buckets = agg_dict["agg_filter"][agg_name]["buckets"]

                # append buckets if found.  In the case where we concatenate aggs, the bucket may exist more than once,
                # in which case, add them together
                if buckets is not None:
                    for bucket in buckets:
                        if "key_as_string" in bucket.to_dict().keys():
                            key, doc_count = (
                                bucket["key_as_string"],
                                bucket["doc_count"],
                            )
                        else:
                            key, doc_count = bucket["key"], bucket["doc_count"]
                        if key in data:
                            data[key] += doc_count
                        else:
                            data[key] = doc_count

            # convert to a list, then sort and re-apply the limit in case we concatenated aggs
            if len(data) != 0:
                data = [
                    {"value": key, "count": doc_count}
                    for key, doc_count in data.items()
                ]

                # determine whether to use a numeric or alphabetic sort string
                # if 8 of the first 10 buckets start with a number, use a numeric one, else, alphabetic
                number_tests = [
                    text_parsable_num_atend(datum["value"], 0) for datum in data
                ]
                pct_numeric = len([nt for nt in number_tests if len(nt) != 0]) / len(
                    number_tests
                )
                if pct_numeric >= 0.80:

                    def sort_key(x):
                        return text_parsable_num_atend(x["value"], 0, True)

                else:

                    def sort_key(x):
                        return x["value"]

                # sort
                if facet in NO_LIMIT_FACET:
                    data = sorted(data, key=sort_key)
                else:
                    data = sorted(data, key=sort_key)[0:MAX_FACET_BUCKETS]
            elif len(data) == 0:
                data = []

            facets_out[facet].append({"type": "value", "data": data})

    return facets_out
