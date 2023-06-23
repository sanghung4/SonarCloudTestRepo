#!/bin/bash
# import the data
PGPASSWORD=reece-punchout psql -h localhost -U reece-punchout -d reece-punchout -a -f src/main/resources/test-data.sql
