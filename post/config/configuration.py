import json
import logging
import os
import sys
from typing import Any, Literal

import boto3
import toml
from loguru import logger
from pydantic import BaseSettings, root_validator

############################################
# VERSION
############################################
try:
    with open("pyproject.toml", "r") as poetry:
        pyproject = toml.load(poetry)
        APPLICATION_VERSION = pyproject["tool"]["poetry"]["version"]
        logger.info(f"Application Version: {APPLICATION_VERSION}")
except Exception:
    APPLICATION_VERSION = "unset"


class POSTSettings(BaseSettings):
    application_version: str = APPLICATION_VERSION
    post_env: str = "dev"

    aws_sqs_queue_name: str = None
    aws_region_name: str = "us-east-1"

    pdw_data_bucket: str = None
    sales_data_bucket: str = None

    debug: Literal[0, 1] = 0
    elastic_host: str = None
    elastic_port: int = 9200
    elastic_index_products: str
    elastic_index_search: str
    elastic_api_key: str = None
    elastic_cloud_id: str = None
    elastic_use_cloud: int = 0
    elastic_conn_timeout: int = 900
    redis_server: str
    redis_port: str = 6379
    secrets_manager_key: str = None

    lambda_function_url: str = (
        "https://cmrnlajwwvfrxuenpt6bytpxum0inzxa.lambda-url.us-east-1.on.aws/"
    )
    lambda_function_name: str = "dev-post-tagger"

    lambda_request_type: str = "RequestResponse"

    use_word_category_cache: int
    force_POS_recalc: int = 0
    pos_cache_filepath: str = "post/cache/pos_cache.pkl"

    @root_validator
    def _populate_from_secrets_manager(cls, values):
        if values.get("secrets_manager_key", None) is not None:
            client = boto3.client(
                "secretsmanager",
                region_name="us-east-1",
            )

            try:
                # Get secret string value
                secrets = json.loads(
                    client.get_secret_value(SecretId=values["secrets_manager_key"])[
                        "SecretString"
                    ]
                )
                for k, v in secrets.items():
                    values[k] = v
            except boto3.exceptions.botocore.exceptions.ClientError:
                logger.exception(
                    f"Secrets Manager Key {values['secrets_manager_key']} not found"
                )
                return values
            except json.decoder.JSONDecodeError:
                logger.exception("Could not parse payload from Secrets Manager")
                return values

        return values

    class Config:
        post_env_file = os.getenv("POST_ENV", "dev").lower()
        env_file: str = f"post/config/{post_env_file}.env"
        env_file_encoding = "utf-8"


class EndpointFilter(logging.Filter):
    def __init__(
        self,
        path: str,
        *args: Any,
        **kwargs: Any,
    ):
        super().__init__(*args, **kwargs)
        self._path = path

    def filter(self, record: logging.LogRecord) -> bool:
        return record.getMessage().find(self._path) == -1


# Instatiate Settings
SETTINGS = POSTSettings()


# Configure Loguru Logging Level
if SETTINGS.debug == 0:
    # Disable Ping Enpoint Access Logging as it is too noisy
    uvicorn_logger = logging.getLogger("uvicorn.access")
    uvicorn_logger.addFilter(EndpointFilter(path="/health"))

    logger.remove()
    logger.add(
        sys.stderr,
        level="INFO",
        serialize=True,
    )

logger.info(f"POST Environment: {SETTINGS.post_env}")
