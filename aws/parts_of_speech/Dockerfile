FROM amazon/aws-lambda-python:3.8

# Set the poetry version to install
ENV POETRY_VERSION=1.1.13

# Install build dependencies
RUN yum update \
    && yum install -y \
    curl \
    build-essential \
    gcc gcc-c++

# Install poetry
RUN pip install poetry

# Set working directory and copy dependency locks
WORKDIR ${LAMBDA_TASK_ROOT}
COPY poetry.lock pyproject.toml ${LAMBDA_TASK_ROOT}/

# Install dependencies
RUN poetry config virtualenvs.create false && poetry install --only pos-tagger-image --no-interaction --no-ansi --no-root

# Copy static lists. Not sure this is necessary
COPY post/static_lists/ ${LAMBDA_TASK_ROOT}/post/static_lists/
COPY post/processing/tag_parts_of_speech.py ${LAMBDA_TASK_ROOT}/post/processing/tag_parts_of_speech.py

# Copy configs
COPY post/config/parts_of_speech.py ${LAMBDA_TASK_ROOT}/post/config/parts_of_speech.py
COPY post/config/configuration.py ${LAMBDA_TASK_ROOT}/post/config/configuration.py

# Copy utils
COPY post/utils/utils_nlp_pos.py ${LAMBDA_TASK_ROOT}/post/utils/utils_nlp_pos.py
COPY post/utils/utils_nlp_std.py ${LAMBDA_TASK_ROOT}/post/utils/utils_nlp_std.py
COPY post/utils/utils_redis.py ${LAMBDA_TASK_ROOT}/post/utils/utils_redis.py
COPY post/utils/utils.py ${LAMBDA_TASK_ROOT}/post/utils/utils.py
COPY post/utils/symspell ${LAMBDA_TASK_ROOT}/post/utils/symspell

# Copy cache
COPY post/cache/pos_cache.pkl ${LAMBDA_TASK_ROOT}/post/cache/pos_cache.pkl

# Copy lambda entrypoint
COPY aws/parts_of_speech/app.py ${LAMBDA_TASK_ROOT}

# Set environment vars for config
ENV ELASTIC_INDEX_SEARCH="mock"
ENV ELASTIC_INDEX_PRODUCTS="mock"
ENV USE_WORD_CATEGORY_CACHE=0
ENV REDIS_SERVER="mock"
ENV POS_CACHE_FILEPATH=${LAMBDA_TASK_ROOT}/post/cache/pos_cache.pkl

# Entrypoint
CMD [ "app.lambda_handler" ]
