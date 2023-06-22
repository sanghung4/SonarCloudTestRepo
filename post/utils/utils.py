"""General Utilities."""
import copy
import datetime
import glob
import math
import multiprocessing
import os
import sys
import time
from collections import Counter
from multiprocessing import Pool

import numpy as np
import pandas as pd
from loguru import logger

############################################
# MISC
############################################


def rename_df_cols_by_position(df, mapper):
    """rename df columns by position (as opposed to index)
    mapper is a dict where keys = ordinal column position and vals = new titles
    unclear that using the native df rename() function produces the correct results when renaming by position
    .
    """
    new_cols = [
        df.columns[i] if i not in mapper.keys() else mapper[i]
        for i in range(0, len(df.columns))
    ]
    df.columns = new_cols
    return df


def rename_duplicate_df_cols(df):
    """when a DF has duplicate names, suffixes all duplicates with '_' + an increasing number."""
    col_list = list(df.columns)
    col_occurrences = [
        len([y for y in col_list[:idx] if y == x]) for idx, x in enumerate(col_list)
    ]  # the number of same name occurring prior to each name
    df.columns = [
        col + ("" if occ == 0 else "_" + str(occ + 1))
        for col, occ in zip(col_list, col_occurrences)
    ]  # suffixed with the occurrences
    return df


def make_df_32bit(df):
    if type(df) == pd.Series:
        if df.dtype == np.float64:
            return df.astype(np.float32)
        if df.dtype == np.int64:
            return df.astype(np.int32)
    else:
        df.loc[:, df.dtypes == np.float64] = df.loc[:, df.dtypes == np.float64].astype(
            np.float32
        )
        df.loc[:, df.dtypes == np.int64] = df.loc[:, df.dtypes == np.int64].astype(
            np.int32
        )
    return df


# tests for None and np.nan across multiple data types, and within a series / array
def isnan(x):
    if x is None:
        return True
    if x is pd.NaT or x is pd.NA:
        return True
    type_x = type(x)
    if type_x in [datetime.datetime, datetime.date, pd.Timestamp, dict, tuple]:
        return False
    if type_x == str:
        return x == ""
    if type_x in [pd.Series, np.ndarray]:
        return pd.Series([isnan(y) for y in x], index=x.index)
    if type_x == list:
        return [isnan(y) for y in x]
    return math.isnan(float(x))


# returns first non-null value
def coalesce(itrbl):
    return next((item for item in itrbl if not isnan(item)), None)


# returns y if x is nan else x
def ifnan(x, y):
    if x is None:
        return y
    type_x = type(x)
    if type(x) in [str, datetime.datetime, datetime.date, pd.Timestamp]:
        return x
    if type_x in [pd.Series, np.ndarray]:
        return pd.Series([ifnan(z, y) for z in x], index=x.index)
    try:
        return x if not math.isnan(float(x)) else y
    except Exception:
        return x


# checks for lock first on a file before attempting to delete, and retries a few times
def robust_file_delete(filepath, tries=1):
    MAX_TRIES = 5
    if not os.path.exists(filepath):
        return False

    try:
        os.remove(filepath)
        return True

    except Exception:
        if tries <= MAX_TRIES:
            time.sleep(0.02)
            return robust_file_delete(filepath, tries + 1)

        logger.error(
            f"Delete of file {filepath} failed bc file remained locked after {MAX_TRIES} tries."
        )
        return False


def clean_up_temp_files(id, temp_dir):
    """removes all temp files for this id in dir."""
    logger.info(f"Deleting temp files for id {id} ...")
    temp_files = glob.glob(f"{temp_dir}/{id}*")
    for tf in temp_files:
        robust_file_delete(tf)


# add Counter allowing negative values (which are disallowed in the implementation of +)
def add_counters(counters):
    res = Counter()
    for c in counters:
        res.update(c)
    return res


# multiply all values in a Counter by a factor
def mult_counter(counter, factor):
    counter_copy = copy.deepcopy(counter)
    for k in counter_copy:
        counter_copy[k] *= factor
    return counter_copy


# merge two dictionaries, adding the values
# works recursively if values are dicts
def add_dicts(d1, d2):
    res = dict()
    for k in d1.keys() | d2.keys():
        v1 = d1.get(k, 0)
        v2 = d2.get(k, 0)
        if type(v1) == dict and type(v2) == dict:
            res.update({k: add_dicts(v1, v2)})
        elif type(v1) == dict:  # v2 is empty
            res.update({k: v1})
        elif type(v2) == dict:  # v1 is empty
            res.update({k: v2})
        else:  # they are both non-dict values
            res.update({k: v1 + v2})
    return res


############################################
# MULTITHREADING AND MULTIPROCESSING
############################################
N_POOL_WORKERS_WIN = int(multiprocessing.cpu_count() * 1)
N_POOL_WORKERS_LINUX = int(multiprocessing.cpu_count() * 0.1)
N_POOL_WORKERS = N_POOL_WORKERS_LINUX if sys.platform == "linux" else N_POOL_WORKERS_WIN
GLOBAL_POOL = None


def make_worker_pool(n_pool_workers=N_POOL_WORKERS, use_global=True):
    global GLOBAL_POOL
    if use_global:
        if GLOBAL_POOL is None:
            GLOBAL_POOL = Pool(processes=n_pool_workers)
        return GLOBAL_POOL
    return Pool(processes=n_pool_workers)  # a pool to run multiple iterations at once
