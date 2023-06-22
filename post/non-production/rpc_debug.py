from collections import Counter
from itertools import chain
from itertools import chain
import numpy as np
import pandas as pd
import typer, json
import requests, timeit
from time import time
from elasticsearch_dsl import Search
from loguru import logger
from functools import partial

from post.config.configuration import SETTINGS
from post.search.search_products import related_part_finder
from post.utils.utils import coalesce, isnan, mult_counter, make_worker_pool
from post.utils.utils_es import es_dsl_search_paginated, init_es_conn
from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams

import boto3

# This is the lambda function URL
FUNCTION_URL = "https://cmrnlajwwvfrxuenpt6bytpxum0inzxa.lambda-url.us-east-1.on.aws/"
FUNCTION_NAME = "dev-post-tagger"

# This is synchronous, check the docs for async methods and collection
REQUEST_TYPE = "RequestResponse"

# Initialize the client. This assumes your ENV variables are set containing the keys
lambda_config = boto3.session.Config(connect_timeout=5, read_timeout=500)
lambda_client = boto3.client("lambda", region_name="us-east-1", config=lambda_config)


def get_pim_tags():
    # ES connection
    conn_es = init_es_conn()

    # the ids in the PIM extract
    PIM_ids = pd.read_csv("post/non-production/PIM_id_list.csv", header=None).iloc[:, 0]

    ##########################################
    # download the entire parsed PDW from ES
    ##########################################
    search = Search(using=conn_es, index=SETTINGS.elastic_index_products)
    # only retrieve necessary fields
    search = search.source(
        [
            "_PART_SCORES.probable_value.value",
            "_MANUFACTURER_SCORES.probable_value.value",
            "_MATERIAL.correct_text",
            "_COLOR.correct_text",
            "_DIMENSIONS.other_info",
            "_VOLTAGE.correct_text",
            "_HEAT.correct_text",
            "_PRESSURE_WGT.correct_text",
            "_TEMPERATURE.correct_text",
            "_FLOW.correct_text",
            "_CAPACITY.correct_text",
            "_POWER.correct_text",
        ]
    )
    res, _, _ = es_dsl_search_paginated(search, 50000, None, download_all_records=True)
    source = [r["_source"] for r in res]
    df_res = pd.DataFrame.from_records(source, index=[int(r["_id"]) for r in res])

    # take intersection of PIM and PDW
    df_res = df_res[df_res.index.isin(PIM_ids)]

    return


def one_scout_test(text):
    url_template = "http://localhost:5000/categorize_text?text={}"

    # make the GET request
    print(f"Started parsing text: {text}...")
    start_time = time()
    response = requests.get(url_template.format(text))

    # parse results
    res = response.content
    seconds = int(time() - start_time)
    print(f"Finished parsing text after {seconds} seconds: {text}")

    return


def one_scout_test_lambda(text):
    # make the GET request
    print(f"Started parsing text: {text}...")
    start_time = time()

    # call lambda
    input_event = json.dumps({"text": text})
    response = lambda_client.invoke(
        FunctionName=FUNCTION_NAME, InvocationType=REQUEST_TYPE, Payload=input_event
    )

    # parse results
    res = response["Payload"].read().decode("utf-8")
    seconds = int(time() - start_time)
    print(f"Finished parsing text after {seconds} seconds: {text}")

    return


def one_scout_test_lambda_restapi(text):
    url_template = "http://localhost:5000/categorize_text_lambda?text={}"

    # make the GET request
    print(f"Started parsing text: {text}...")
    start_time = time()
    response = requests.get(url_template.format(text))

    # parse results
    res = response.content
    seconds = int(time() - start_time)
    print(f"Finished parsing text after {seconds} seconds: {text}")

    return


def scout_tests():
    """send a bunch of phrases to POST to check the speed of async requests"""

    # load phrases
    phrases = pd.read_csv(
        "post/non-production/scout_test_phrases.csv", header=None, delimiter="|"
    ).iloc[:, 0]

    # split phrases, if desired
    SPLIT_PHRASES = True
    if SPLIT_PHRASES:
        split_phrases = phrases.apply(lambda x: x.replace(";", ",").split(","))
        phrases = pd.Series(chain.from_iterable(split_phrases))

    # order phrases by length descending
    phrases = phrases.sort_values(
        key=lambda ser: ser.apply(len), ascending=False
    ).reset_index(drop=True)

    # for debug only
    # phrases = phrases[0:10]
    # phrases = [' FURNISH AND INSTALL ALEONARD THERMOSTATIC MIXING VALVE MODEL No.170-LF AND AN ADA DRAIN OFFSET BELOW THE SINK']
    print(f"There are {len(phrases)} phrases.")

    # for timeit
    def wrapper_function(pool):
        # res = pool.map(one_scout_test, phrases)
        # res = pool.map(one_scout_test_lambda, phrases, chunksize=1)
        res = pool.map(one_scout_test_lambda_restapi, phrases, chunksize=1)
        pool.close()
        pool.join()

    # time async calls
    pool = make_worker_pool(16)
    partial_func = partial(wrapper_function, pool=pool)
    time = timeit.timeit(partial_func, number=1)
    print("Execution time:", time)

    return


# entry point.
if __name__ == "__main__":
    # get_pim_tags()
    scout_tests()

    # tag_parts_of_speech_ngrams("FURNISH AND INSTALL A LEONARDTHERMOSTATIC MIXING VALVE MODEL No. 170-LF ANDAN ADA DRAIN OFFSET BELOW THE LAVATORY")

    Debug = True
