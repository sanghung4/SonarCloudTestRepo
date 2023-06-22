import json
import re
from datetime import datetime

from aws_lambda_powertools import Logger, Tracer
from aws_lambda_powertools.utilities import parameters
from aws_lambda_powertools.utilities.typing import LambdaContext
from elasticsearch import Elasticsearch

tracer = Tracer()
logger = Logger(log_uncaught_exceptions=True, service="create-index")

INDEX = "post-products"
ALIAS = f"{INDEX}-alias"
PATTERN = f"{INDEX}-*"


@tracer.capture_lambda_handler
@logger.inject_lambda_context(log_event=True)
def lambda_handler(event, context: LambdaContext):
    """Lambda Handler Function."""

    if event.get("indexName") is None:
        raise ValueError("indexName is required")
    else:
        index_name = event.get("indexName")

    secrets = json.loads(parameters.secrets.get_secret("post-api"))
    es = Elasticsearch(
        cloud_id=secrets["elastic_cloud_id"],
        api_key=secrets["elastic_api_key"],
        timeout=900,
        request_timeout=900,
    )

    # Check if we can connect to ElasticSearch
    if es.ping():
        logger.info("Connected to ElasticSearch")
    else:
        logger.exception("Unable to connect to ElasticSearch")
        raise Exception("Unable to connect to ElasticSearch")

    # Check if the index exists
    if not es.indices.exists(index=index_name):
        logger.exception(
            f"Index {index_name} does not exist. Please create the index first."
        )
        raise Exception(
            f"Index {index_name} does not exist. Please create the index first."
        )

    alias_exists = es.indices.exists_alias(name=ALIAS)
    logger.info(f'Alias "{ALIAS}" exists: {alias_exists}')
    if not alias_exists:
        logger.info(f'Creating alias "{ALIAS}" for index "{index_name}"')
        es.indices.put_alias(index=index_name, name=ALIAS)

    update_actions = {"actions": []}

    if alias_exists:
        logger.info(f'Removing alias "{ALIAS}" for index "{PATTERN}"')
        update_actions["actions"].append({"remove": {"alias": ALIAS, "index": PATTERN}})

    update_actions["actions"].append({"add": {"alias": ALIAS, "index": index_name}})

    # Update the alias
    es.indices.update_aliases(body=update_actions)
    aliases = es.indices.get_alias(index=index_name)

    # Check if the alias is created
    if ALIAS in aliases[index_name]["aliases"]:
        logger.info(f'Alias "{ALIAS}" is created for index "{index_name}"!')
    else:
        logger.exception("Alias was not created.")
        raise Exception("Alias was not created.")

    # Retrieve the matching indices
    logger.info(f"Retrieving indices matching pattern {PATTERN}")
    indices = es.indices.get(index=PATTERN)
    index_pattern = r"post-products-\d{14}"

    # Extract the index names
    index_names = list(indices.keys())
    matched_indices = [index for index in index_names if re.match(index_pattern, index)]

    # Sort the indices based on the datetime in the name
    sorted_indices = sorted(
        matched_indices,
        key=lambda index: datetime.strptime(
            re.search(r"\d{14}", index).group(), "%Y%m%d%H%M%S"
        ),
        reverse=True,
    )

    # Get the sublist of all but the top 2 most recent indices
    old_indices = sorted_indices[2:]

    # Delete the indices in the sublist
    logger.warning("Deleting old indices:")
    for index in old_indices:
        logger.warning(f'Deleting index "{index}"')
        es.indices.delete(index=index)
