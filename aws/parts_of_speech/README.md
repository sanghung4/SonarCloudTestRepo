# Parts of Speech Lambda

## Building

Be in the root directory of the project
```bash
pwd
/Users/kahlil/Documents/innovation-post
```

Run the build command
```bash
docker build -t dev-post-tagger -f aws/parts_of_speech/Dockerfile .
```

## Uploading to ECR

#### Log into ECR
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 773663557741.dkr.ecr.us-east-1.amazonaws.com
```

#### Build the image
```bash
docker build -t dev-post-tagger -f aws/parts_of_speech/Dockerfile .
```

#### Tag the image
```bash
docker tag dev-post-tagger:latest 773663557741.dkr.ecr.us-east-1.amazonaws.com/dev-post-tagger:latest
```

#### Push the image
```bash
docker push 773663557741.dkr.ecr.us-east-1.amazonaws.com/dev-post-tagger:latest
```

# Example Synchronous Invokation
```python
import json

import boto3

# This is the lambda function URL
FUNCTION_URL = "https://cmrnlajwwvfrxuenpt6bytpxum0inzxa.lambda-url.us-east-1.on.aws/"

FUNCTION_NAME = "dev-post-tagger"

# This is synchronous, check the docs for async methods and collection
REQUEST_TYPE = "RequestResponse"

# Initialize the client. This assumes your ENV variables are set containing the keys
lambda_client = boto3.client("lambda", region_name="us-east-1")


input_event = json.dumps({"text": "Kohler is good"})

response = lambda_client.invoke(
                        FunctionName=FUNCTION_NAME,
                        InvocationType=REQUEST_TYPE,
                        Payload=input_event,
                    )

print(response["Payload"].read().decode("utf-8"))
```
