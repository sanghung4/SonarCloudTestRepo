import os
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
    else "select * from MORSCO_EDW.INNOVATE_TM_SCHEMA.INNOVATION_SALES"
)

S3_BUCKET = os.getenv("S3_BUCKET")


def handler(event, context):
    print(context)
    print(event)
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
        df.sort_values("PRODUCTID", inplace=True)
        df.dropna(subset=["PRODUCTID"], inplace=True)
        df = df[df["PRODUCTID"].str.startswith("MSC")]

        ##########################
        # METADATA
        ##########################
        filename = "sales_data.csv"

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
