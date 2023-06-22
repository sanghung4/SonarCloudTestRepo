# PoST 📮
## Part of Speech Tagger
[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)
[![QA](https://github.com/morsco-reece/innovation-post/actions/workflows/Lint.yml/badge.svg)](https://github.com/morsco-reece/innovation-post/actions/workflows/Lint.yml)

# Running Stuff

Refer to the README.md files for the individual packages for instructions on running.

## Setup

1. Install Poetry
   1. On win, add poetry .exe to PATH
2. Create poetry environment
   1. `poetry shell`
3. Install dependencies
   1. `poetry install`
4. Install pre-commit hooks
   1. `pre-commit install`

### Pre-Commit

`pre-commit` will runs a series of checks before allowing you to commit.

You can run `pre-commit run` run to check if you will pass the checks. However,
you can just do `git commit` and the checks will run automatically.
For checks like `black` formatting, `isort` dependency sorting, `trailing whitespace` and
others; it will often modify those files in place. Simply re-stage those files and try committing again.

Issues relating to dependencies / linting will require you to fix those issues before committing

**Danger ⚠️**
You can bypass the pre-commit checks by running `git commit --no-verify`. Don't do this because you are
lazy or rushed.

### Poetry

Please refer to the [poetry documentation](https://python-poetry.org/docs/basic-usage/)

### Versioning & Deploying to MaX Dev

> #### Any changes in the version within the pyproject.toml will trigger an automatic deployment to MaX DEV.

Comply with [PEP 440](https://peps.python.org/pep-0440/) for versioning.
![pep440-cheatsheet](https://cdn-images-1.medium.com/max/1071/1*0hpnAVl61AG3KwFuRIkHhg.png)

This is simple and easy to do via the `poetry version` command. A full guide and walkthrough can be found [here](https://python-poetry.org/docs/cli/#version)

When working on a feature/improvement, ensure that the branch you want to merge to main has the version set to a pre-release or pre-patch.


#### Patching
If the branch you are working on is currently on `x.x.xa0` and you make changes, then bump the patch version with `poetry version prerelease`. The resulting version will become `x.x.xa1`

#### Major and Minor Releases
Once a version is ready to be released fully you can update it to a release version using
`poetry version major/minor/patch`

> *⚠️ Complete releases will automatically be found by Circle CI and will be deployed.*

## Deployment
When deploying on a single instance it is recommended to use the `docker-compose.yml`
based deployment. This includes failure recovery and better internal networking.

1. Make sure that the `/test_ui/Dockerfile` API_URL is set to the final deployment URL.
   1. For local deployments it should be `http://localhost/api`
   2. For a dev deployment it would be something like `https://devpost.reece.com/api`
2. Ensure that the correct indices are set within the config files
3. Set the config files using the compose file by setting `POST_ENV`. This happens on line 19.
4. To build, launch, and exit in headless
   1. `docker compose up --build --force-recreate -d`
5. To view logs after a headless deployment
   1. `docker compose logs -f -t <OPTIONAL NAME OF SERVICE>`
   2. If no service name is provided it will show logs for all containers
   3. Currently services are one of (api, frontend)

> 💡 For a large release with many changing dependencies, fixes, etc., It would be wise to run `docker system prune` prior to running the deployment commands to ensure no cached layers make it to the final running container images.


### Backing Up Elasticsearch Index
First, ensure that `index.blocks.write` is set to `true`
```
PUT /<INDEX_NAME>/_settings
{
  "settings": {
    "index.blocks.write": true
  }
}
```
Then you will be able to copy the index via
```
POST <INDEX_NAME>/_clone/<BACKUP_INDEX_NAME>
```

### Deploying to Dev
Ssh into the box. Its listed in AWS as dev post ui. Cd into the dir, git pull. Update the

```
docker-compose POST_ENV=CONSUMER
```

Update the test_ui/Dockerfile variable for api url to https://devpost.reece.com/api/

Then

```
docker compose up --build --force-recreate -d
```
Delete the file

```
post/cache/pos_cache.pkl
```
which will force the POS meta-data to be re-computed.

Then you must run a new tagging batch.

Then you must run scoring, if it is not re-run by the tagging batch automatically.

Then, re-make the lambda function if using the REST API endpoint which utilizes that.

# Sales Data

PoST relies on summarized sales data in order to rank products

<details>
   <summary>Sales Data View & Refresh</summary>
   There is a view within Snowflake called

   `morsco_edw.innovate_tm_schema.innovation_sales`

   This data **should** be refreshed monthly.

   There is a lambda function in the dev environment called `dev-post-sales-downloader`. The code and image for it are contained within the `aws/` folder of this repository. Due to the shakiness of the network and Snowflakes sluggishness to fetch and rollup the data the lambda will often time out.

   This data is meant to be automatically placed in a S3 bucket called `dev-post-sales-download` and keyed with `sales_data.csv`.

   Since the data is archived in snowflake there is no requirement to keep monthly records of it.


</details>

### Plan A:

Run the `dev-post-sales-downloader` lambda and hope it doesn't time out.

If it doesn't you should be done.

### Plan B

1. Go to Snowflake, and query for
```sql
select * from morsco_edw.innovate_tm_schema.innovation_sales
```
2. Download all the data to a csv called `sales_data.csv`
3. Upload it to the `dev-post-saled-download` S3 bucket, replacing the current sales data within the bucket