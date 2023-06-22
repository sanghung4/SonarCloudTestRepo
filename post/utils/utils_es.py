"""ElasticSearch Utilities."""
import datetime
import json
import operator
import typing
from functools import reduce

import numpy as np
from elasticsearch import Elasticsearch
from elasticsearch_dsl import Q, Search
from loguru import logger

from post.config.configuration import SETTINGS


def init_es_conn() -> Elasticsearch:
    """:return: ES Connection object"""
    logger.debug(f"Opening Elasticsearch Connection {SETTINGS.elastic_use_cloud}")
    if SETTINGS.elastic_use_cloud == 1:
        return Elasticsearch(
            cloud_id=SETTINGS.elastic_cloud_id,
            api_key=SETTINGS.elastic_api_key,
            timeout=SETTINGS.elastic_conn_timeout,
            request_timeout=SETTINGS.elastic_conn_timeout,
        )
    conn = Elasticsearch(
        f"{SETTINGS.elastic_host}:{SETTINGS.elastic_port}",
        timeout=SETTINGS.elastic_conn_timeout,
        request_timeout=SETTINGS.elastic_conn_timeout,
    )
    return conn


def test_es_conn() -> bool:
    conn_es = Elasticsearch(
        f"{SETTINGS.elastic_host}:{SETTINGS.elastic_port}",
        timeout=SETTINGS.elastic_conn_timeout,
        request_timeout=SETTINGS.elastic_conn_timeout,
    )
    return conn_es.ping()


# JSON serializer for objects not serializable by default json code
def json_serial(obj):
    if isinstance(obj, (datetime.datetime, datetime.date)):
        return obj.isoformat()
    raise TypeError("Type %s not serializable" % type(obj))


# makes json for es queries
def make_es_json(fields, vals, wrappers=None):
    # turn vars into json
    qry_dict = dict(zip(fields, vals))
    if wrappers is not None:
        for wr in wrappers:
            if wr in ["must", "must_not", "filter", "should"]:
                qry_dict = {wr: [qry_dict]}
            else:
                qry_dict = {wr: qry_dict}
    qry_text = json.dumps(qry_dict, default=json_serial)
    return qry_text


def es_dsl_search_paginated(
    s: Search,
    records_per_page: int = 5000,
    page: int = 1,
    print_query_params_to_stdout: bool = True,
    download_all_records: bool = False,
) -> typing.Tuple[list, dict, dict]:
    """takes an elasticsearch dsl search object, runs it and returns the specified page
    to return all results, set download_all_records = True
    returns the consolidated hits plus pagination information plus aggregations, if the latter two exist
    """

    if records_per_page == 0:
        logger.info("records_per_page is 0, returning empty list")
        res = s.execute()
        pagination_info = {
            "current": 1,
            "total_pages": 1,
            "total_results": 0,
            "size": 0,
        }
        return (
            [],
            pagination_info,
            None if "aggregations" not in res else res["aggregations"],
        )

    # prevent user from sending None to records_per_page or page
    records_per_page = 5000 if records_per_page is None else records_per_page
    page = 1 if page is None else page

    # print generated query if desired
    query_params = json.dumps(s.to_dict(), default=json_serial)
    if print_query_params_to_stdout:
        logger.info(f"ES query params are {query_params}")

    # set page size and start page
    start_record = records_per_page * ((1 if page is None else page) - 1)
    end_record = start_record + records_per_page
    s = s[start_record:end_record]

    # run query
    res = s.execute()
    res_dicts = [x.to_dict() for x in res["hits"]["hits"]]

    # get more (all) pages if this one is full and download_all_records is True
    pagination_info = None
    if len(res_dicts) == records_per_page and download_all_records:
        res_dicts += es_dsl_search_paginated(
            s, records_per_page, page + 1, download_all_records=download_all_records
        )[0]

    elif records_per_page != 0:
        # return the current pagination information
        pagination_info = {
            "current": page,
            "total_pages": int(
                np.ceil(res["hits"]["total"]["value"] / records_per_page)
            ),
            "total_results": res["hits"]["total"]["value"],
            "size": len(res_dicts),
        }

    return (
        res_dicts,
        pagination_info,
        None if "aggregations" not in res else res["aggregations"],
    )


def es_q_combiner(
    field: str,
    vals: typing.Iterable,
    name_or_query: str = "term",
    join_func: typing.Callable = operator.or_,
    boost: float = 0,
    additional_Q: Q = None,
) -> Q:
    """returns a composite ES DSL query for a specified field for multiple values
    Send operator.or_ to join all by OR, operator.and_ to join all by AND
    change name_or_query to change the query type, which is 'term' by default
    boost boosts the search terms; send -1 to boost by the terms order; 0 to not boost; else a static number.
    additional_Q appends another Q to EACH subquery.
    """
    return reduce(
        join_func,
        (
            Q(
                **{
                    "name_or_query": name_or_query,
                    field: {
                        "value": val,
                        "boost": 0
                        if boost == 0
                        else max(0, 10 - idx)
                        if boost == -1
                        else boost,
                    },
                }
            )
            & (Q() if additional_Q is None else additional_Q)
            for idx, val in enumerate(vals)
        ),
    )


# deletes a record from Elasticsearch
def ES_delete(conn, index, _id):
    s = Search(using=conn, index=index)
    s = s.query("ids", values=_id)
    res = s.delete()
    return res
