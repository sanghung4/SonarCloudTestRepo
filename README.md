# Account Management Service

This API manages account via Okta.

### Building

Use maven to build an image: `./mvnw spring-boot:build-image`.

Run image with: `docker-compose up`.

For further details on running this application locally, see the [Development On-boarding](https://reeceusa.atlassian.net/wiki/spaces/ECOMM/pages/195919873/Development+On-boarding+Checklist) document.

# Populate Local DB
## Legacy Accounts
You will need a CSV file containing columns `first_name`, `last_name`, `email`, and `erp_account_id` as columns.

To import this data, connect to your local database using psql on the command line and run the following command

`\copy legacy_users(first_name, last_name, email, erp_account_id) from '/Users/seth/Projects/reece/documentation/InsiteProdAccounts.csv' with DELIMITER ',' CSV HEADER;`
