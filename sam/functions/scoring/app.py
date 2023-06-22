from typing import Any, Dict

from aws_lambda_powertools import Logger
from aws_lambda_powertools.utilities.typing import LambdaContext

from post.processing.scoring import calc_cross_sectional_stats

logger = Logger(log_uncaught_exceptions=True, service="scoring-tagger")


@logger.inject_lambda_context(log_event=True)
def lambda_handler(event: Dict[str, Any], context: LambdaContext):
    """Lambda Handler Function."""
    calc_cross_sectional_stats(event["indexName"])
