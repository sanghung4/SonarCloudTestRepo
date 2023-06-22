"""Redis Utilities."""

import numpy as np
import redis
from loguru import logger

from post.config.configuration import SETTINGS


def get_redis_conn(print_msg=True):
    if print_msg:
        logger.info(
            f"Connecting to redis DB at {SETTINGS.redis_server}: {SETTINGS.redis_port}"
        )
    tries = 0
    success = False

    # this is mainly to keep retrying when multiprocess enabled jobs cause locks
    MAX_TRIES = 50
    while not success and tries < MAX_TRIES:
        try:
            conn = redis.Redis(
                host=SETTINGS.redis_server, port=SETTINGS.redis_port, socket_timeout=60
            )
            conn.ping()
            success = True

        except Exception as e:
            logger.warning(
                f"Connection to redis DB at {SETTINGS.redis_server}: {SETTINGS.redis_port} failed on try {tries} with error {e}...  Retrying"
            )
            tries += 1

    if tries == MAX_TRIES:
        raise ConnectionRefusedError("Failed to connect to DB.")

    return conn


def to_redis_dict(dct):
    """makes sure all the items in a dict are redis-type compatible, to make sure redis can store it.
    pausing work on this function and pursuing a json serialization strategy instead
    .
    """
    for k, v in dct.items():
        if isinstance(v, dict):
            dct[k] = to_redis_dict(v)
        elif v is None:
            dct[k] = np.nan
        elif isinstance(v, bool):
            dct[k] = 1 if v else 0
        # upon finishing: add more types / cases here
    return dct
