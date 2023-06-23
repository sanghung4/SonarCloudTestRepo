# Reece Connect (Front-end)

This project contains the frontend code for the Reece Connect. For more documentation please read the following:

### First-time QuickStart
1. Set up "AWS CLI" from [this page](https://reeceusa.atlassian.net/wiki/spaces/ECOMM/pages/195919873/Development+On-boarding+Checklist#Prerequisites) 
2. Run `aws --profile circle-shared --region us-east-1 codeartifact login --tool npm --repository reece-ecomm --domain reece --domain-owner 694955555685`. I recommend to assign this as an alias.
3. Run `yarn install --frozen-lockfile`
4. Run `yarn run husky install`.
5. Start with `yarn start`. This will spin up a server on [http://localhost:3000](http://localhost:3000) by default if there isn't any other instance running on port 3000.

### Useful commands
- `yarn start` - start a runtime
- `yarn test` - basic unit test
- `yarn test:coverage` - unit test with coverage report
- `yarn run check` - Check with eslint and prettier
- `docker compose up` - Start up the wiremock docker container for mocking back-end data responses.