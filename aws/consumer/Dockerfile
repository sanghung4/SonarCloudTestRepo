FROM python:3.9-slim-buster AS poetry
RUN pip install poetry
WORKDIR /app
COPY pyproject.toml poetry.lock /app/
RUN poetry export --without dev -f requirements.txt --output requirements.txt

FROM python:3.9-slim-buster
WORKDIR /app
COPY --from=poetry /app/requirements.txt /app/
RUN apt-get update && apt-get upgrade -y && apt-get install gcc curl build-essential g++ -y

RUN python -m pip install --upgrade pip
RUN pip install -r requirements.txt
COPY post/ post/
COPY post/processing/parse_and_tag_product_list.py .
#ENV aws_access_key_id xxxxx
#ENV aws_secret_access_key xxxxx
ENV POST_ENV CONSUMER

CMD ["python", "parse_and_tag_product_list.py", "sqs-listener"]