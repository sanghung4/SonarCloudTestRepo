FROM python:3.9-slim-buster AS poetry
RUN pip install poetry
WORKDIR /app
COPY pyproject.toml poetry.lock /app/
RUN poetry export --without dev -f requirements.txt --output requirements.txt

FROM amazon/aws-lambda-python:3.9
RUN yum install gcc-c++ python3-devel -y
COPY --from=poetry /app/requirements.txt ${LAMBDA_TASK_ROOT}

RUN pip install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"
COPY post/ ${LAMBDA_TASK_ROOT}/post/
COPY pyproject.toml ${LAMBDA_TASK_ROOT}
COPY post/processing/parse_and_tag_product_list.py ${LAMBDA_TASK_ROOT}

ENV POST_ENV LAMBDA
ENV ELASTIC_USE_CLOUD 1
ENV SECRETS_MANAGER_KEY post-api

# Copy function code
COPY sam/functions/consumer/app.py ${LAMBDA_TASK_ROOT}

# Entrypoint
CMD [ "app.lambda_handler" ]
