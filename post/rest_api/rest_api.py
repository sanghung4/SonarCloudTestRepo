"""POST REST API ENTRYPOINT."""
import asyncio
import json
import multiprocessing
import sys
from datetime import date
from functools import partial
from typing import Annotated, Dict, Union

import boto3
import typer
from elasticsearch_dsl import Search
from elasticsearch_dsl.query import MatchAll, Term, Terms
from fastapi import FastAPI, HTTPException, Query, status
from fastapi.middleware.cors import CORSMiddleware
from loguru import logger

from post.config.configuration import SETTINGS
from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams
from post.rest_api.models.maxresult import MaxResult
from post.rest_api.models.maxsearch import MaxSearch, MaxSearchPage
from post.search.max_interface import (
    convert_search_res_to_max_format,
)
from post.search.repository import ElasticsearchRepository
from post.search.search_products import product_search_from_str
from post.utils.utils_es import init_es_conn

#######################################
# constants
#######################################
REST_API_HOSTNAME_PROD = "0.0.0.0"
REST_API_HOSTNAME_DEBUG = "0.0.0.0"
REST_API_PORT_PROD = 5000
REST_API_PORT_DEBUG = 5000
UVICORN_WORKERS_WIN = int(multiprocessing.cpu_count() * 0.9)
UVICORN_WORKERS_LINUX = int(multiprocessing.cpu_count() * 0.5)
UVICORN_WORKERS = (
    UVICORN_WORKERS_LINUX if sys.platform == "linux" else UVICORN_WORKERS_WIN
)
UVICORN_WORKERS_WIN = int(multiprocessing.cpu_count() * 0.9)
UVICORN_WORKERS_LINUX = int(multiprocessing.cpu_count() * 0.5)
UVICORN_WORKERS = (
    UVICORN_WORKERS_LINUX if sys.platform == "linux" else UVICORN_WORKERS_WIN
)
UVICORN_WORKERS = 1

#######################################
# error messages
#######################################
NO_DATA_MESSAGE = {"message": "No data available for the requested parameters."}
BAD_PARAMETERS_MESSAGE = {"message": "Improper or missing parameters passed."}


#######################################
# AWS stuff for lambda function endpoint
#######################################
# This is the lambda function URL

# Initialize the client. This assumes your ENV variables are set containing the keys
lambda_config = boto3.session.Config(connect_timeout=5, read_timeout=500)
lambda_client = boto3.client("lambda", region_name="us-east-1", config=lambda_config)


#######################################
# start API and define routes
#######################################
app = FastAPI(
    title="PoST",
    description="Part of Speech Tagger",
    version=SETTINGS.application_version,
    contact={"name": "Kahlil Wehmeyer", "email": "Kahlil.Wehmeyer@austinai.io"},
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Establish elasticsearch connection object.
repository = ElasticsearchRepository(
    session=init_es_conn(), index_name=SETTINGS.elastic_index_products
)


@app.get("/health", status_code=status.HTTP_200_OK)
def ping() -> Dict[str, str]:
    """Pings the API to verify connectivity.

    Returns
    -------
        bool: True
    """
    logger.debug("Pinged! 🏓")
    return {"status": "UP"}


@app.get("/categorize_text", deprecated=True)
async def categorize_text(text: str):
    """Categorize a piece of text.

    Args:
    ----
        text (str): Text to be categorized

    Returns
    -------
        dict: Dict containing text objects, their category and confidence
    """
    # run text categorizer
    logger.debug("Categorizing text")

    # call async
    loop = asyncio.get_running_loop()
    partial_func = partial(tag_parts_of_speech_ngrams, texts=text)
    categorized_text = await loop.run_in_executor(None, partial_func)

    if categorized_text is None or len(categorized_text) == 0:
        logger.warning("No data processed for categorize_text")
        return NO_DATA_MESSAGE

    return categorized_text


@app.get("/categorize_text_lambda")
async def categorize_text_lambda(text: str):
    """Categorize a piece of text using code in AWS lambda as opposed to locally.

    Args:
    ----
        text (str): Text to be categorized

    Returns
    -------
        dict: Dict containing text objects, their category and confidence
    """
    # run text categorizer
    logger.info("Categorizing text via lambda")

    # call async
    loop = asyncio.get_running_loop()
    input_event = json.dumps({"text": text})
    partial_func = partial(
        lambda_client.invoke,
        FunctionName=SETTINGS.lambda_function_name,
        InvocationType=SETTINGS.lambda_request_type,
        Payload=input_event,
    )
    response = await loop.run_in_executor(None, partial_func)
    categorized_text: str = response["Payload"].read().decode("utf-8")

    if categorized_text is None or len(categorized_text) == 0:
        logger.warning("No data processed for categorize_text")
        return NO_DATA_MESSAGE

    return json.loads(categorized_text)


@app.get("/product_search")
async def product_search(text: str, page_n: int = 1, src: str = "", dest: str = ""):
    """Search for a product.

    Args:
    ----
        text (str): Product search string
        page_n (int): Which page of results to get, or None to get all

    Returns
    -------
        dict: Matched products
    """

    logger.debug("Searching for products")
    loop = asyncio.get_running_loop()
    partial_func = partial(
        product_search_from_str,
        text=text,
        search_meta={"SOURCE": src, "DEST": dest},
        page_n=page_n,
        return_facets=True,
    )
    (
        product_search_results,
        _,
        _,
    ) = await loop.run_in_executor(None, partial_func)

    if product_search_results is None or len(product_search_results) == 0:
        logger.warning(f"No data processed for product_search\n{text}")
        raise HTTPException(status_code=204, detail="Item not found")

    return product_search_results


@app.post("/search")
async def max_search(search: MaxSearch) -> MaxResult:
    """Search via MaX."""

    logger.debug(f"Searching for products {search}")
    loop = asyncio.get_running_loop()
    partial_func = partial(
        product_search_from_str,
        text=search.query,
        search_meta=search.search_meta,
        page_n=search.page.current,
        page_size=search.page.size,
        filters=search.filters,
        return_facets=True,
    )

    product_search_results, pagination_info, facets = await loop.run_in_executor(
        None, partial_func
    )

    if search.page.size != 0 and (
        product_search_results is None or len(product_search_results) == 0
    ):
        logger.warning(f"No data processed for product_search\n{search.query}")
        raise HTTPException(status_code=204, detail="Item not found")

    results = convert_search_res_to_max_format(
        product_search_results, pagination_info=pagination_info, facets=facets
    )
    logger.debug(f"Returning results for {search}")
    return MaxResult(**results)


@app.get("/search/{id}")
async def get_product_by_id(id: str):
    """Search for a single product using its ID."""

    if id.startswith("MSC"):
        search = Search().query(MatchAll()).filter(Term(ERP_PRODUCT_ID=id))
        results, pagnation_info, facets = repository.search(search=search)
        results = convert_search_res_to_max_format(
            results, pagination_info=pagnation_info, facets=facets
        )
    else:
        results = repository.get(id=id)
        results_list = [results]
        results = convert_search_res_to_max_format(
            results_list, pagination_info=MaxSearchPage(), facets={}
        )
    logger.debug(f"Returning results for {id}: {results}")

    return MaxResult(**results)


@app.get("/search/")
async def get_products_by_ids(ids: Annotated[Union[list[str], None], Query()] = None):
    """Search for multiple products using a list of product IDs ."""
    if ids[0].startswith("MSC"):
        search = Search().query(MatchAll()).filter(Terms(ERP_PRODUCT_ID=ids))
        results, pagnation_info, facets = repository.search(search=search)
        results = convert_search_res_to_max_format(
            results, pagination_info=pagnation_info, facets=facets
        )
    else:
        results = repository.get_many(ids=ids)
        results = results["docs"]

        results = convert_search_res_to_max_format(
            results, pagination_info=MaxSearchPage(), facets={}
        )
    return MaxResult(**results)


def main(debug: bool = False, logfile: bool = False) -> None:
    """Entry point to start FastAPI.

    Args:
    ----
        debug (bool, optional): Use Debug Ports or Prod Ports. Defaults to True.
        log_to_file (bool, optional): Create a log file. Defaults to True.
    """
    if logfile:
        log_file_name = f"{date.today().isoformat()}_POST.log"
        logger.info(f"Start Log File at {log_file_name}")
        logger.add(log_file_name, rotation="50 MB", retention="14 days")

    # if debugging, don't use the cache except to write
    if debug:
        SETTINGS.use_word_category_cache = 1

    # run gunicorn with uvicorn workers, if linux
    if sys.platform == "linux":
        from gunicorn.app.base import BaseApplication

        logger.info(f"Running gunicorn with {UVICORN_WORKERS} workers.")

        # see https://fastapi.tiangolo.com/deployment/server-workers/
        class GunicornApp(BaseApplication):
            def __init__(self, app, options=None):
                self.options = options or {}
                self.application = app
                super().__init__()

            def load_config(self):
                config = {
                    key: value
                    for key, value in self.options.items()
                    if key in self.cfg.settings and value is not None
                }
                for key, value in config.items():
                    self.cfg.set(key.lower(), value)

            def load(self):
                return self.application

        bind_URL = f"{REST_API_HOSTNAME_DEBUG if debug else REST_API_HOSTNAME_PROD}:{REST_API_PORT_DEBUG if debug else REST_API_PORT_PROD}"
        gunicorn_options = {
            "workers": UVICORN_WORKERS,
            "worker_class": "uvicorn.workers.UvicornWorker",
            "bind": bind_URL,
        }
        GunicornApp("rest_api:app", gunicorn_options).run()

    # else run uvicorn in win
    else:
        import uvicorn

        logger.info(f"Running uvicorn with {UVICORN_WORKERS} workers.")
        uvicorn.run(
            "rest_api:app",
            host=REST_API_HOSTNAME_DEBUG if debug else REST_API_HOSTNAME_PROD,
            port=REST_API_PORT_DEBUG if debug else REST_API_PORT_PROD,
            workers=UVICORN_WORKERS,
        )

    return


# REST API entry point
if __name__ == "__main__":
    typer.run(main)
