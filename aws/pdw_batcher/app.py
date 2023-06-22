"""PDW Batcher."""
import json
import time

import boto3

BATCH_SIZE = 375


def lambda_handler(event, context):
    """Lambda Handler Function."""
    print(event)  # Keep for debugging
    # Parsing the event object
    obj = event.get("Records", [{}])[0].get("s3", {}).get("object", {}).get("key")
    rows = int(obj.split("-")[-1].split(".")[0])

    region = "us-east-1"
    queue_url = "https://sqs.us-east-1.amazonaws.com/773663557741/dev-post-batch-queue"
    sqs = boto3.client("sqs", region_name=region)
    ecs = boto3.client("ecs", region_name=region)

    print(f"total rows: {rows}")
    for count in range(0, rows, BATCH_SIZE):
        start = count + 1 if count > 0 else count  # Necessary to not duplicate records
        end = count + BATCH_SIZE
        if end > rows:  # End index of batch is greater than row etc.,
            end = rows
        message_body = json.dumps(
            {"start": start, "end": end, "s3": obj, "sent": str(time.time())}
        )

        ecs.run_task(
            cluster="dev-post-processing-cluster",
            count=1,
            enableECSManagedTags=True,
            launchType="FARGATE",
            networkConfiguration={
                "awsvpcConfiguration": {
                    "subnets": ["subnet-0de85c9efcf9f2f9c"],  # Private Subnet 003
                    "securityGroups": [
                        "sg-05db2bd037c59d584",  # dev-post-elasticsearch-sg
                        "sg-02e55e2ce92844f17",  # dev-post-consumer-sg
                    ],
                    "assignPublicIp": "DISABLED",
                }
            },
            propagateTags="TASK_DEFINITION",
            startedBy="dev-pdw-batcher lambda",
            taskDefinition="dev-post-consumer-td",
        )
        print(f"sending message: {message_body}")
        sqs.send_message(QueueUrl=queue_url, MessageBody=message_body)

    return {"statusCode": 200, "body": event.get("obj", "")}
