import json
import time
from io import StringIO
from typing import Any, Dict

import boto3
import pandas as pd
from aws_lambda_powertools import Logger
from aws_lambda_powertools.utilities import parameters
from aws_lambda_powertools.utilities.typing import LambdaContext
from snowflake.connector import connect as snowflake_connection

logger = Logger(log_uncaught_exceptions=True, service="pdw-downloader")


@logger.inject_lambda_context(log_event=True)
def handler(event: Dict[str, Any], context: LambdaContext):
    def fix_tech_specs_list(value):
        if value is not None:
            value = value.split("[")[1].strip("]")
            return json.dumps(json.loads(value))
        return {}

    def fix_in_stock_location_list(value):
        try:
            if value is None:
                return []
            return json.dumps(json.loads(value.split(":")[1].strip()))
        except Exception:
            return []

    def parse_str_to_bool(value: str) -> bool:
        if value == "":
            return None
        if "y" in value.lower():
            return True
        return False

    logger.info(f"Event: {event}")
    logger.info(f"Conext: {context}")

    try:
        secrets = json.loads(parameters.get_secret("post-api"))
        logger.info("Initiating ❄️ Connection")

        conn = snowflake_connection(
            account=secrets["snowflake_account"],
            user=secrets["snowflake_user"],
            password=secrets["snowflake_password"],
        )

        logger.info("Getting data ...")
        df = pd.read_sql(sql=secrets["snowflake_query"], con=conn)
        logger.info(f"Fetched data: {len(df)} rows")
        logger.info(df.columns)

        ##########################
        # TRANSFORMATIONS 🦋
        ##########################
        logger.info("Sorting and dropping duplicates and missing values")
        df.sort_values("INTERNAL_ITEM_NBR", inplace=True)
        df.dropna(subset=("ID", "ERP_PRODUCT_ID", "INTERNAL_ITEM_NBR"), inplace=True)
        df.drop_duplicates(
            subset=("ID", "ERP_PRODUCT_ID", "INTERNAL_ITEM_NBR"), inplace=True
        )

        logger.info("Fixing date formats")
        df["LAST_UPDATE_DATE"] = df["LAST_UPDATE_DATE"].dt.strftime(
            "%Y-%m-%dT%H:%M:%SZ"
        )

        logger.info("Jsonifying In Stock Location List")
        df["IN_STOCK_LOCATION_LIST"] = df["IN_STOCK_LOCATION_LIST"].apply(
            fix_in_stock_location_list
        )
        logger.info("Jsonifying Territory Exclusion List")
        df["TERRITORY_EXCLUSION_LIST"] = df["TERRITORY_EXCLUSION_LIST"].apply(
            lambda x: json.dumps(json.loads(x)) if x is not None else []
        )

        logger.info("Jsonifying Tech Specs")
        df["TECH_SPECS_LIST"] = df["TECH_SPECS_LIST"].apply(fix_tech_specs_list)

        logger.info("Converting Flags to Boolean")
        flag_columns = df.filter(regex="_FLAG$").columns
        df[flag_columns] = df[flag_columns].fillna("").applymap(parse_str_to_bool)

        ##########################
        # METADATA
        ##########################
        filename = f"batch-{int(time.time())}-{len(df)}.csv"  # e.g. "output.csv"
        logger.info(f"Filename: {filename}")

        ##########################
        # SAVE TO S3 🪣
        ##########################
        logger.info("Uploading file to S3")
        csv_buffer = StringIO()
        df.to_csv(csv_buffer, index=False)

        s3 = boto3.client("s3")
        s3.put_object(
            Bucket=secrets["s3_bucket"], Key=filename, Body=csv_buffer.getvalue()
        )

        return {"statusCode": 200, "s3": f"s3://{secrets['s3_bucket']}/{filename}"}

    except Exception as e:
        logger.exception(str(e))
        raise (e)
