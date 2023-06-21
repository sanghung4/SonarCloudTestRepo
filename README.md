# Products Core Service
[![Coverage Status](https://coveralls.io/repos/github/morsco-reece/products-core-service/badge.svg?branch=master&t=iGwrJo)](https://coveralls.io/github/morsco-reece/products-core-service?branch=master)

This API manages products. It connects to Elasticsearch for searching and pulling products, and to Eclipse service to pull product price and availability.

### Building

Use maven to build an image: `./mvnw spring-boot:build-image`

### Running

Use maven to run the app: `./mvnw spring-boot:run`

### Testing

Use maven to test the app: `./mvnw test`

For further details on running this application locally, see the [Development On-boarding](https://reeceusa.atlassian.net/wiki/spaces/ECOMM/pages/195919873/Development+On-boarding+Checklist) document.
