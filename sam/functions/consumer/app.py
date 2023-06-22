from typing import Any, Dict

from aws_lambda_powertools import Logger
from aws_lambda_powertools.utilities.typing import LambdaContext

from post.processing.parse_and_tag_product_list import process_message

logger = Logger(log_uncaught_exceptions=True, service="post-tagger")


@logger.inject_lambda_context(log_event=True)
def lambda_handler(event: Dict[str, Any], context: LambdaContext):
    """Lambda Handler Function."""
    process_message(
        {
            "indexName": event["indexName"],
            "s3": event["batch"]["s3"],
            "start": event["batch"]["start"],
            "end": event["batch"]["end"],
        }
    )
