"""PDW Batcher."""
import os

import boto3
from aws_lambda_powertools import Logger, Tracer

tracer = Tracer()
logger = Logger(log_uncaught_exceptions=True, service="pdw-batcher")

BATCH_SIZE = os.getenv("BATCH_SIZE", 100)

s3 = boto3.client("s3")


@tracer.capture_lambda_handler
@logger.inject_lambda_context(log_event=True)
def lambda_handler(event, context):
    """Lambda Handler Function."""
    parts = event["s3"].replace("s3://", "").split("/")
    # The first item in parts is the bucket name, the rest is the key
    bucket_name = parts.pop(0)
    key = "/".join(parts)
    obj = s3.get_object(Bucket=bucket_name, Key=key)
    data = obj["Body"].read().decode("utf-8")

    lines = data.split("\n")
    rows = len(lines) - 1 if lines[-1] == "" else len(lines)

    logger.info(f"total rows: {rows}")
    chunks = []
    for count in range(0, rows, BATCH_SIZE):
        start = count + 1 if count > 0 else count  # Necessary to not duplicate records
        end = count + BATCH_SIZE
        end = min(end, rows)

        chunks.append({"start": start, "end": end, "s3": event["s3"]})

    logger.info(f"Number of chunks {len(chunks)}")

    return {"statusCode": 200, "batches": chunks}
