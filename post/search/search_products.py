"""Module Docstring."""
import operator
import sys
import typing
from functools import reduce
from itertools import chain
from threading import Thread
from time import time

import elasticsearch_dsl
import pandas as pd
from elasticsearch_dsl import Q, Search
from loguru import logger

from post.config.configuration import SETTINGS
from post.config.parts_of_speech import POS, POS_ATTRIBUTES_TYPE, POS_UNITS_TYPE
from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams
from post.search.actions import invoke_action, related_part_finder
from post.search.facets import FACETS, add_facet_aggs_to_search, process_facet_aggs
from post.search.log_search import log_search
from post.utils.utils import coalesce, ifnan, isnan
from post.utils.utils_es import es_dsl_search_paginated, es_q_combiner, init_es_conn
from post.utils.utils_nlp_pos import get_pos_syns

# read in the search boosts
SEARCH_BOOSTS_FILEPATH = "post/search/boosts.csv"
DF_BOOSTS = pd.read_csv(SEARCH_BOOSTS_FILEPATH, header=0, index_col=False)


def collect_tags_into_dict(
    tags: list, prefix: str = "", fields: typing.Iterable = None
) -> dict:
    """given a list of tags, puts them into a dict with POS as keys and lists of tags as values
    fields is an iterable of tag fields to return (if None, all fields are returned)
    format_for_search will further format the dict into the structure which the product searcher expects
    also numbers the POS by occurrence (pos_n).
    """
    try:
        # construct the dict
        dct = {
            prefix
            + tag1["pos"]: [
                {
                    key: val
                    for key, val in tag2.items()
                    if fields is None or key in fields
                }
                for tag2 in tags
                if tag2["pos"] == tag1["pos"]
            ]
            for tag1 in tags
        }

        # number the pos (cld be combined with the above step, but do separately for clarity)
        for key, lst in dct.items():
            for idx, _ in enumerate(lst):
                dct[key][idx]["pos_n"] = idx + 1

        return dct

    except Exception as dict_error:
        logger.critical("Unable to convert POS list to dict")
        logger.exception(dict_error)
        sys.exit(1)


def product_search_from_str(
    search_meta: dict,
    text: str = None,
    page_n: int = None,
    page_size: int = 24,
    filters: dict = None,
    return_facets: bool = False,
) -> typing.Tuple[list, dict, dict]:
    """Search for products based on a given string.

    Args:
    ----
        text (str): Text to search upon
        search_meta (dict): Metadata, includes src, dst
        page_n (int, optional): Page number. Defaults to None.
        max_related_parts: limit the number of possibilities returned.
        filters: dictionary of facet filters.
        return_facets: returns all possible values of facets for all records in the search for all pages

    Returns
    -------

    """
    logger.info(f"Starting product search for text {text}")
    start_time = time()
    time()

    try:
        if text is None or not text.strip():
            tag_dict = {}
        else:
            # tag the string
            tags = tag_parts_of_speech_ngrams(text)

            # consolidate the tags by part of speech
            tag_dict = collect_tags_into_dict(tags)
            logger.info(f"The parsed search query is {tag_dict}")

        # do the search and limit results
        res, pagination_info, facets = product_search(
            query=tag_dict,
            records_per_page=page_size,
            page_n=page_n,
            filters=filters,
            return_facets=return_facets,
        )
        Thread(
            target=log_search,
            args=[
                init_es_conn(),
                res,
                text,
                tag_dict,
                search_meta,
                time() - start_time,
            ],
        ).start()

        return res, pagination_info, facets

    except Exception as search_error:
        logger.critical(f"Failed search for string '{text}'")
        logger.exception(search_error)
        return []


def product_search(
    query: dict = None,
    records_per_page: int = 5000,
    page_n: int = 1,
    orig_search_failed: bool = False,
    filters: dict = None,
    return_facets: bool = False,
) -> typing.Tuple[list, dict, dict]:
    """returns rows from the PDW matching the specified search attributes
    query should be a dict of (fields to search) for keys and (search_terms) for values
    search_terms may be a string or a list but should be exact, corrected text, now raw user input.
    To send raw input, use product_search_from_str which will call POST first to normalize and correct text.
    filters are strict, literal inclusions or exclusions applied after the search is finished.
    There are logic differences in how queries and filters are handled.
    """

    # open a connection.
    conn_es = init_es_conn()
    search = Search(using=conn_es, index=SETTINGS.elastic_index_products)

    def format_search_terms_for_es(
        terms: typing.Union[str, list],
        use_text_fields: bool = False,
        custom_field: str = None,
        add_prefix: str = None,
    ) -> list:
        """splits search terms (which can be a str, list of strings, or list of dicts) into a list which ES will accept
        use_text_field will access the correct_text and text sub-fields
        use_custom field will access that field
        add_prefix appends a string to the return vals
        .
        """
        if terms and len(terms) > 0:
            if isinstance(terms, list) or terms.count(",") == 0:
                out_list = terms if isinstance(terms, list) else [terms]
            else:
                out_list = terms.split(",")
            if use_text_fields:
                return [
                    ifnan(add_prefix, "") + coalesce((x["correct_text"], x["text"]))
                    for x in out_list
                ]
            if custom_field is not None:
                return [x[custom_field] for x in out_list]
            return out_list
        return None

    def get_search_facets(search: Search) -> dict:
        """
        Returns a dict of facets for the search.
        """
        try:
            search = add_facet_aggs_to_search(search)
            facet_aggs = es_dsl_search_paginated(search, 0)[2]
            facets = process_facet_aggs(facet_aggs)
            return facets
        except Exception as facet_error:
            logger.exception(f"Failed to get facets for search {search}")
            logger.exception(facet_error)
            return {}

    # initialize the facets dict
    facets = None

    ################
    # First, check ACTIONS.  These are somewhat hard-coded algos that are triggered by a specific search term.
    # They may add various parts or combos of parts to the query, or affect other things like the related parts function.
    ################
    if "ACTION" in query and not orig_search_failed:
        for action_tag in query["ACTION"]:
            query = invoke_action(action_tag, query)

    ################
    # Attempting to match various IDs.  (Filter Context)
    ################

    # if MSC number(s) are present, return the exact doc(s).
    if "MSC" in query:
        # MSC is called ID in the PDW
        MSC_Q = Q(
            "terms", ID=format_search_terms_for_es(query["MSC"], use_text_fields=True)
        )
        search = search.filter(MSC_Q)
        res, pagination_info, _ = es_dsl_search_paginated(
            search, records_per_page, page_n
        )
        if len(res) != 0:
            return (
                res,
                pagination_info,
                get_search_facets(search) if return_facets else None,
            )

    if "UPC" in query:
        UPC_Q = Q(
            "terms",
            UPC_ID=format_search_terms_for_es(query["UPC"], use_text_fields=True),
        )
        search = search.filter(UPC_Q)
        logger.debug("FOUND_UPC")
        logger.debug(search.to_dict())
        res, pagination_info, _ = es_dsl_search_paginated(
            search, records_per_page, page_n
        )
        if len(res) != 0:
            return (
                res,
                pagination_info,
                get_search_facets(search) if return_facets else None,
            )

    # reset the search, then if id(s) are present, return the exact doc(s).  Also check the MSC field for it.
    search = Search(using=conn_es, index=SETTINGS.elastic_index_products)
    if "ID" in query:
        ID_Q = (
            Q(
                "ids",
                values=format_search_terms_for_es(query["ID"], use_text_fields=True),
            )
            | Q(
                "terms",
                ID=format_search_terms_for_es(
                    query["ID"], use_text_fields=True, add_prefix="MSC-"
                ),
            )
            | Q(
                "terms",
                VENDOR_PART_NBR=format_search_terms_for_es(
                    query["ID"], use_text_fields=True
                ),
            )
        )
        search = search.filter(ID_Q)
        res, pagination_info, _ = es_dsl_search_paginated(
            search, records_per_page, page_n
        )
        if len(res) != 0:
            return (
                res,
                pagination_info,
                get_search_facets(search) if return_facets else None,
            )

    # reset the search, then if model number(s) are present, search on those.
    # also if the ID wasn't matched, also run it against MODEL #s here.
    search = Search(using=conn_es, index=SETTINGS.elastic_index_products)

    ################
    # check manufacturer  (Filter Context) - moved prior to MODEL check to handle MODEL #s duplicated across manufacturers.
    ################
    if "MANUFACTURER" in query and query["MANUFACTURER"] is not None:
        # search the PROBABLE MANF
        manf_Q = es_q_combiner(
            "_MANUFACTURER_SCORES.probable_value.value",
            format_search_terms_for_es(query["MANUFACTURER"], use_text_fields=True),
        )
        search = search.filter(manf_Q)

    ################
    # check MODEL
    ################
    # different ways that model numbers could be expressed
    (
        id_type_terms,
        id_type_terms_wo_chars_upper,
        id_type_terms_wo_lastpart_upper,
        id_type_terms_wo_chars_or_lastpart_upper,
    ) = ([], [], [], [])

    # check ID if it wasn't matched before
    if "ID" in query:
        id_type_terms += [i["correct_text"] for i in query["ID"]]

    # MODEL terms and their variants
    if "MODEL" in query:
        id_type_terms += [model["correct_text"] for model in query["MODEL"]]
        # if a model includes multi-words, add each word as a searching term
        id_type_terms += [
            model["correct_text"].split(" ")
            if len(model["correct_text"].split(" ")) > 1
            else []
            for model in query["MODEL"]
        ][0]

        id_type_terms_wo_chars_upper += [
            model["other_info"]["wo_chars_upper"] for model in query["MODEL"]
        ]
        id_type_terms_wo_lastpart_upper += [
            model["other_info"]["wo_lastpart_upper"]
            for model in query["MODEL"]
            if "wo_lastpart_upper" in model["other_info"]
        ]

        # limit length here, otherwise too many bad matches result
        id_type_terms_wo_chars_or_lastpart_upper += [
            model["other_info"]["wo_lastpart_upper"]
            for model in query["MODEL"]
            if "wo_lastpart_upper" in model["other_info"]
            and len(model["other_info"]["wo_lastpart_upper"]) >= 4
        ]

    # search different types / variations of how a MODEL number might be written.
    if len(id_type_terms) != 0:
        # direct match on model text.
        model_Q = Q(
            "terms",
            _MODEL__text=format_search_terms_for_es(id_type_terms),
        )

        # direct match on model text wo special characters and UPPER
        if len(id_type_terms_wo_chars_upper) != 0:
            model_Q |= Q(
                "terms",
                _MODEL__other_info__wo_chars_upper=format_search_terms_for_es(
                    id_type_terms_wo_chars_upper
                ),
            )

        # direct match on last part of model text wo special characters and UPPER
        # also allow the entire model text to match the text wo lastpart of models in the PDW
        terms = id_type_terms_wo_chars_or_lastpart_upper + id_type_terms_wo_chars_upper
        if len(terms) != 0:
            model_Q |= Q(
                "terms",
                _MODEL__other_info__wo_chars_or_lastpart_upper=format_search_terms_for_es(
                    terms
                ),
            )

        # run the MODEL search
        search = search.filter(model_Q)

    ###
    # ignore the model number and other IDs from here on out and construct a composite query on the remaining terms.
    # use a TERM query because the flattened tag objects will not support TERMS
    # each field type will query for all available passed values in an OR fashion
    # but the field type queries are strung together in an AND fashion
    ###

    def extract_category_items(pos: str) -> list:
        """finds search terms which are categories and expands them into their components."""
        category_terms = [x for x in query[pos] if x["is_category"]]
        items_in_categories = list(
            chain.from_iterable(
                [
                    POS[pos]["dict_cats"][coalesce((x["correct_text"], x["text"]))]
                    for x in category_terms
                ]
            )
        )
        return items_in_categories

    ################
    # check attributes type POS  (Filter Context)
    ################
    for att_type_pos in POS_ATTRIBUTES_TYPE:
        if att_type_pos in query and query[att_type_pos] is not None:
            # first, deal with any terms that are CATEGORIES; replace them with the items they represent
            items_in_categories = extract_category_items(att_type_pos)

            # then parse the non category terms
            non_category_terms = [
                x for x in query[att_type_pos] if not x["is_category"]
            ]
            items_from_terms = format_search_terms_for_es(
                non_category_terms, use_text_fields=True
            )

            # combine all items
            all_items = ifnan(items_in_categories, []) + ifnan(items_from_terms, [])

            # if this POS is scored, search the probable value.
            if (
                "has_probable_scores" in POS[att_type_pos]
                and POS[att_type_pos]["has_probable_scores"]
            ):
                att_type_Q = es_q_combiner(
                    "_" + att_type_pos + "_SCORES.probable_value.value",
                    all_items,
                )

            # otherwise, just search the text, using AND logic.
            else:
                att_type_Q = es_q_combiner(
                    "_" + att_type_pos + ".correct_text",
                    all_items,
                    join_func=operator.and_,
                )

            search = search.filter(att_type_Q)

    ################
    # search for units type terms  (Filter Context)
    ################
    unit_type_Qs = []  # a list of units type terms ES queries
    for unit_type_pos in POS_UNITS_TYPE:
        if unit_type_pos in query and query[unit_type_pos] is not None:
            unit_type_Qs = []  # a list of queries for this unit_type_pos

            for term in query[unit_type_pos]:
                value, units = term["other_info"]["value"], term["other_info"]["units"]

                # the query says that the value AND the units must match
                unit_type_pos_Q = Q(
                    **{
                        "name_or_query": "term",
                        "_" + unit_type_pos + ".other_info.value": value,
                    }
                ) & Q(
                    **{
                        "name_or_query": "term",
                        "_" + unit_type_pos + ".other_info.units.keyword": units,
                    }
                )

                unit_type_Qs.append(
                    Q("nested", path="_" + unit_type_pos, query=unit_type_pos_Q)
                )

    # consolidate all and add
    if len(unit_type_Qs) != 0:
        combined_unit_type_Qs = reduce(operator.or_, unit_type_Qs)
        search = search.filter(combined_unit_type_Qs)

    ################
    # iterate over dimensions and make a Q for each one.  (Query Context)
    ################
    if "DIMENSIONS" in query and query["DIMENSIONS"] is not None:
        dim_Qs = []  # a list of dimension ES queries

        def one_dim_Q_maker(
            value: float,
            units: str,
            dim_type: str,
            part_ns_to_match: typing.Union[int, typing.Iterable],
            comparison_operator: operator = operator.eq,
        ) -> elasticsearch_dsl.Q:
            """send this a value and unit of ONE dimension part and the possible part_ns it can match.
            comparison_operator may be operator.eq for exact match, or operator.gt or operator.lt for ranges
            """
            one_dim_Qs = []
            if isinstance(part_ns_to_match, int):
                part_ns_to_match = [part_ns_to_match]

            for part_n_to_match in part_ns_to_match:
                dim_n_key = "dim_" + str(part_n_to_match + 1)
                range_was_specified = comparison_operator in [
                    operator.lt,
                    operator.le,
                    operator.gt,
                    operator.ge,
                ]
                one_dim_Q = Q()

                # value must match
                if not isnan(value):
                    # equality search
                    if not range_was_specified:
                        one_dim_Q &= Q(
                            **{
                                "name_or_query": "term",
                                f"_DIMENSIONS.other_info.{dim_n_key}.value": value,
                            }
                        )

                    else:
                        Q()
                        one_dim_Q &= Q(
                            **{
                                "name_or_query": "range",
                                f"_DIMENSIONS.other_info.{dim_n_key}.value": {
                                    (
                                        "gte"
                                        if comparison_operator is operator.ge
                                        else "gt"
                                        if comparison_operator is operator.gt
                                        else "lte"
                                        if comparison_operator is operator.le
                                        else "lt"
                                        if comparison_operator is operator.lt
                                        else None
                                    ): value
                                },
                            }
                        )

                    # units must be blank or match
                    if not isnan(units):
                        one_dim_Q &= Q(
                            **{
                                "name_or_query": "term",
                                f"_DIMENSIONS.other_info.{dim_n_key}.units.keyword": units,
                            }
                        ) | ~Q(
                            **{
                                "name_or_query": "exists",
                                "field": f"_DIMENSIONS.other_info.{dim_n_key}.units",
                            }
                        )

                    # if user passes type, searched type must
                    # 1. match or 2. be blank if a range was not specified.
                    if dim_type is not None:
                        dim_type_Q = Q(
                            **{
                                "name_or_query": "terms",
                                f"_DIMENSIONS.other_info.{dim_n_key}.type.keyword": dim_type,
                            }
                        )

                        if not range_was_specified:
                            dim_type_Q |= ~Q(
                                **{
                                    "name_or_query": "exists",
                                    "field": f"_DIMENSIONS.other_info.{dim_n_key}.type",
                                }
                            )

                        one_dim_Q &= dim_type_Q

                    # disallow thickness to be returned in the catch-all if user does not pass type
                    else:
                        one_dim_Q &= ~Q(
                            **{
                                "name_or_query": "terms",
                                f"_DIMENSIONS.other_info.{dim_n_key}.type.keyword": [
                                    "THICKNESS"
                                ],
                            }
                        )

                one_dim_Qs.append(Q("nested", path="_DIMENSIONS", query=one_dim_Q))

            # return the consolidated dim_Q for this one dim.
            return reduce(operator.or_, one_dim_Qs)

        # loop over all dimension tuples
        for dim_tag in query["DIMENSIONS"]:
            # the dimensions object's details are in other_info
            dim = dim_tag["other_info"]

            # the operator is equals by default, unless there was an operator specified in the preceding tag.
            oper = (
                operator.eq
                if dim_tag["prec_tag"] is None
                or dim_tag["prec_tag"]["pos"] != "OPERATOR"
                else POS["OPERATOR"]["operators"][dim_tag["prec_tag"]["correct_text"]]
            )

            # 0 = allow any searched dimension to match any other parsed dimension
            # 1 = searched dimensions must match exactly and be in same order as product dimensions
            DIM_SEARCH_METHOD = 0
            for dim_n in range(0, 3):
                dim_n_key = "dim_" + str(dim_n + 1)
                if dim_n_key in dim:
                    dim_Qs.append(
                        one_dim_Q_maker(
                            dim[dim_n_key]["value"],
                            dim[dim_n_key]["units"],
                            dim[dim_n_key]["type"],
                            range(0, 3) if DIM_SEARCH_METHOD == 0 else dim_n,
                            oper,
                        )
                    )

        # consolidate all and add
        combined_dim_Qs = reduce(operator.and_, dim_Qs)
        search = search.query(combined_dim_Qs)

    ################
    # check parts  (Query Context)
    ################
    if "PART" in query and query["PART"] is not None:
        part_Qs = []

        def one_part_Q_maker(part: str, boost_factor: float = 0) -> elasticsearch_dsl.Q:
            """makes a Q for one part."""
            # query on the actual part being present in one of the PART tags for this product.
            part_tag_Q = Q(
                **{
                    "name_or_query": "term",
                    "_PART.correct_text": {
                        "value": part,
                        "boost": boost_factor,
                    },
                }
            )

            # now query the PROBABLE PART and the part distribution score,
            # both of which estimate the probability of the most likely part
            parts_value_Q = Q(
                **{
                    "name_or_query": "term",
                    "_PART_SCORES.probable_value.value.keyword": {
                        "value": part,
                        "boost": boost_factor,
                    },
                }
            )

            parts_dist_Q = Q(
                "function_score",
                field_value_factor={
                    "field": "_PART_SCORES.probable_value.search_score",
                    "factor": 250,
                    "missing": 0,
                },
            )

            parts_prob_Q = Q(
                "nested",
                path="_PART_SCORES.probable_value",
                query=parts_value_Q & parts_dist_Q,
            )

            # combine partQs appropriately and create the final query for this part
            # the OR on the stats_Q is only necessary if the parts stats have not been run (which should not be the case)
            part_Q = part_tag_Q & parts_prob_Q
            return part_Q

        # first, find any parts representing categories and expand them into the constituent parts
        # and make a part tag for each one
        items_in_categories = extract_category_items("PART")
        for item in items_in_categories:
            query["PART"].append({"pos": "PART", "correct_text": item, "text": item})

        # then loop over all parts and make Qs
        for _part_idx, part_tag in enumerate([x for x in query["PART"]]):
            # make a Q for this part, boosting
            part_Qs.append(
                one_part_Q_maker(
                    coalesce((part_tag["correct_text"], part_tag["text"])), 20
                )
            )

            # add related parts, as defined by things that END with the passed part,
            # like TOILET WATER SUPPLY for WATER SUPPLY
            # search the corrected text, the original text, and the synonyms if any
            part_text_syns = get_pos_syns("PART", part_tag["correct_text"])
            part_texts = set(
                [
                    part_tag["correct_text"],
                    part_tag["text"].upper(),
                ]
                + part_text_syns
            )
            part_texts = {
                pt for pt in part_texts if len(pt) >= 3
            }  # remove short words and blanks

            related_parts_end = related_part_finder(
                part_texts, max_related_parts=50, end_to_search="end"
            )

            # if the original search failed, as a catch-all,
            # add related parts, as defined by things that START with the passed part,
            # like TOILET TANK for TOILET.  This would be a bad rule generally as you would not want to
            # EG return PIPE BRACKET if PIPE was searched for, but, if nothing was returned originally,
            # we at least give the user something this way.
            related_parts_start = []
            if orig_search_failed:
                related_parts_start = related_part_finder(
                    part_texts, max_related_parts=50, end_to_search="start"
                )

            # now make a query for each related part.
            related_parts_all = related_parts_end + related_parts_start
            for rp in related_parts_all:
                part_Qs.append(one_part_Q_maker(rp, 0))

        # add all part Qs to the search
        if len(part_Qs) != 0:
            combined_part_Qs = reduce(operator.or_, part_Qs)
            search = search.query(combined_part_Qs)

    ################
    # add "OTHER" terms.
    ################
    if "OTHER" in query and query["OTHER"] is not None:
        other_Q = es_q_combiner(
            "_OTHER__text",
            format_search_terms_for_es(query["OTHER"], use_text_fields=True),
            boost=5,
        )

        # If only OTHER terms exist, use "must", else, use "should".  (Query Context)
        if len(search.to_dict()) == 0:
            must, should = other_Q, Q()
        else:
            must, should = Q(), other_Q

        # you must add Q("match_all") to should and set minimum_should_match=1; minimum_should_match=0 is not honored
        other_Q = Q(
            "bool",
            must=[must],
            should=[should, Q("match_all")],
            minimum_should_match=1,
        )
        search = search.query(other_Q)

    ################
    # boost certain high-level things  (Query Context)
    ################

    # boost the presence of an THUMBNAIL_IMAGE_URL_NAME, extremely heavily
    # and PRODUCT_OVERVIEW_DESCRIPTION and/or WEB_DESCRIPTION, which are better than the SEARCH_KEYWORDS
    img_and_desc_Q = Q(
        "bool",
        should=[
            Q("exists", field="THUMBNAIL_IMAGE_URL_NAME", boost=50000),
            Q("exists", field="PRODUCT_OVERVIEW_DESCRIPTION", boost=10),
            Q("exists", field="WEB_DESCRIPTION", boost=10),
            Q(
                "match_all"
            ),  # you must add this and set minimum_should_match=1; minimum_should_match=0 is not honored
        ],
        minimum_should_match=1,
    )
    search = search.query(img_and_desc_Q)

    # boost by the product's INVOICED AMOUNT for ordering purposes
    ordering_Q = Q(
        "function_score",
        field_value_factor={
            "field": "sales_info.product_invoiced_pctrank",
            "factor": 2000,
            "missing": 0,
        },
    )
    search = search.query(ordering_Q)

    # boost by any custom fields specified in boosts.csv
    custom_boost_Qs = []
    for field, value, boost in DF_BOOSTS.itertuples(index=False):
        custom_boost_Qs.append(
            Q(
                **{
                    "name_or_query": "term",
                    field: {
                        "value": value,
                        "boost": boost,
                    },
                }
            )
        )
    # you must add this and set minimum_should_match=1; minimum_should_match=0 is not honored
    custom_boost_Qs.append(Q("match_all"))
    all_custom_boost_Qs = Q(
        "bool",
        must=Q("match_all"),
        should=custom_boost_Qs,
        minimum_should_match=1,
    )
    search = search.query(all_custom_boost_Qs)
    logger.info(f"Query check if empty: {query}")
    if not query:
        logger.info("No query passed to search")
        search = search.query(Q("match_all"))

    ################
    # apply filters, if passed
    ################
    if filters is not None:
        for all_or_none, filters in filters.items():
            for filter in filters:
                facet = next(iter(filter))
                vals = filter[facet]
                if isinstance(vals, str):
                    vals = [vals]

                # construct the appropriate query for this facet and add to search.
                facet_Q_function = FACETS[facet]["es_filter_func"]
                es_filter = facet_Q_function(vals, all_or_none)
                search = search.filter(es_filter)

    ################
    # generate facets, if desired
    ################
    if return_facets:
        facets = get_search_facets(search)

    ################
    # run the composite query
    ################
    res, pagination_info, _undo = es_dsl_search_paginated(
        search, records_per_page, page_n
    )

    # HACK TO EXCLUDE ROWS WITH MISSING INTERNAL IDS.  THESE SEEM TO BE JUNK RECORDS AND WE NEED TO
    # FIGURE OUT WHAT TO DO WITH THEM LATER.  SUCH RECORDS HAVE IDS FILLEDNA WITH NEGATIVE NUMBERS
    res = [r for r in res if int(r["_id"]) > 0]

    # see if the original search failed, IE it returned no records.
    # in that case, call myself again, widening the search criteria, unless filters were applied.
    if (
        not orig_search_failed
        and len(res) == 0
        and page_n == 1
        and records_per_page != 0
        and not filters
    ):
        return product_search(
            query,
            records_per_page,
            page_n,
            orig_search_failed=True,
            return_facets=return_facets,
        )

    return res, pagination_info, facets
