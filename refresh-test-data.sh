#!/bin/bash
# delete the test customers, which handles deleting all their data
PGPASSWORD=reece-punchout psql -h localhost -U reece-punchout -d reece-punchout -c \
  "DELETE FROM customer where id = '46021ee0-86f1-43e3-ad2d-f55e10b24c50'"

# import the data
PGPASSWORD=reece-punchout psql -h localhost -U reece-punchout -d reece-punchout -a -f test-data.sql
