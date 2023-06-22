import json
import os
import time
from io import StringIO

import boto3
import pandas as pd
from snowflake.connector import connect as snowflake_connection

##########################
# GET VARS
##########################
SNOWFLAKE_ACCOUNT = os.getenv("SNOWFLAKE_ACCOUNT")
SNOWFLAKE_ACCOUNT = (
    SNOWFLAKE_ACCOUNT if SNOWFLAKE_ACCOUNT is not None else "morsco.east-us-2.azure"
)

SNOWFLAKE_USER = os.getenv("SNOWFLAKE_USER")
SNOWFLAKE_PASSWORD = os.getenv("SNOWFLAKE_PASSWORD")

SNOWFLAKE_QUERY = os.getenv("SNOWFLAKE_QUERY")
SNOWFLAKE_QUERY = (
    SNOWFLAKE_QUERY
    if SNOWFLAKE_QUERY is not None
    else "select * from MORSCO_EDW.INNOVATE_TM_SCHEMA.INNOVATION_PRODUCT"
)

S3_BUCKET = os.getenv("S3_BUCKET")


def handler(event, context):
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

    print(f"Event: {event}")
    print(f"Conext: {context}")

    try:
        print("Initiating ❄️ Connection")
        conn = snowflake_connection(
            account=SNOWFLAKE_ACCOUNT, user=SNOWFLAKE_USER, password=SNOWFLAKE_PASSWORD
        )

        print("Getting data ...")
        df = pd.read_sql(sql=SNOWFLAKE_QUERY, con=conn)
        print(f"Fetched data: {len(df)} rows")

        ##########################
        # TRANSFORMATIONS 🦋
        ##########################
        print("Sorting and dropping duplicates and missing values")
        df.sort_values("INTERNAL_ITEM_NBR", inplace=True)
        df.dropna(subset=("ID", "ERP_PRODUCT_ID", "INTERNAL_ITEM_NBR"), inplace=True)
        df.drop_duplicates(
            subset=("ID", "ERP_PRODUCT_ID", "INTERNAL_ITEM_NBR"), inplace=True
        )

        print("Fixing date formats")
        df["LAST_UPDATE_DATE"] = df["LAST_UPDATE_DATE"].dt.strftime(
            "%Y-%m-%dT%H:%M:%SZ"
        )

        print("Jsonifying In Stock Location List")
        df["IN_STOCK_LOCATION_LIST"] = df["IN_STOCK_LOCATION_LIST"].apply(
            fix_in_stock_location_list
        )
        print("Jsonifying Territory Exclusion List")
        df["TERRITORY_EXCLUSION_LIST"] = df["TERRITORY_EXCLUSION_LIST"].apply(
            lambda x: json.dumps(json.loads(x)) if x is not None else []
        )

        print("Jsonifying Tech Specs")
        df["TECH_SPECS_LIST"] = df["TECH_SPECS_LIST"].apply(fix_tech_specs_list)

        print("Converting Flags to Boolean")
        flag_columns = df.filter(regex="_FLAG$").columns
        df[flag_columns] = df[flag_columns].fillna("").applymap(parse_str_to_bool)

        ##########################
        # METADATA
        ##########################
        filename = f"batch-{int(time.time())}-{len(df)}.csv"  # e.g. "output.csv"
        print(f"Filename: {filename}")

        ##########################
        # SAVE TO S3 🪣
        ##########################
        print("Uploading file to S3")
        csv_buffer = StringIO()
        df.to_csv(csv_buffer, index=False)

        s3 = boto3.client("s3")
        s3.put_object(Bucket=S3_BUCKET, Key=filename, Body=csv_buffer.getvalue())

        return "Query Execution Successful 🤗"

    except Exception as e:
        raise (e)
