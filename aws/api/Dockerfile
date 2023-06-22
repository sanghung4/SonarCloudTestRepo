
###############################################
# Base Image
###############################################
FROM python:3.9.16-slim-bullseye as python-base

ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PIP_NO_CACHE_DIR=off \
    PIP_DISABLE_PIP_VERSION_CHECK=on \
    PIP_DEFAULT_TIMEOUT=100 \
    POETRY_VERSION=1.2.2  \
    POETRY_HOME="/opt/poetry" \
    POETRY_VIRTUALENVS_IN_PROJECT=true \
    POETRY_NO_INTERACTION=1 \
    PYSETUP_PATH="/usr/code" \
    VENV_PATH="/usr/code/.venv"

# prepend poetry and venv to path
ENV PATH="$POETRY_HOME/bin:$VENV_PATH/bin:$PATH"


###############################################
# Builder Image
###############################################
FROM python-base as builder-base
RUN apt-get update \
    && apt-get install --no-install-recommends -y \
    curl \
    build-essential \
    gcc

# install poetry - respects $POETRY_VERSION & $POETRY_HOME
RUN curl -sSL https://install.python-poetry.org | python3 -

# copy project requirement files here to ensure they will be cached.
WORKDIR $PYSETUP_PATH
COPY poetry.lock pyproject.toml ./

# install runtime deps - uses $POETRY_VIRTUALENVS_IN_PROJECT internally
RUN poetry install --without dev


###############################################
# Production Image
###############################################
FROM python-base as production

RUN groupadd -g 999 app && \
    useradd -r -u 999 -g app app && \
    mkdir $PYSETUP_PATH && \
    chown app:app $PYSETUP_PATH

WORKDIR $PYSETUP_PATH

COPY --chown=app:app --from=builder-base $PYSETUP_PATH $PYSETUP_PATH
COPY --chown=app:app post $PYSETUP_PATH/post

ENV POST_ENV DEV

CMD ["uvicorn", "post.rest_api.rest_api:app", "--host", "0.0.0.0", "--port", "8000"]