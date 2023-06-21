CREATE SCHEMA ODS;
CREATE TABLE ODS.VW_PRODUCT_SEARCH
(
    PRODUCT_JSON VARCHAR,
    LAST_UPDATE_DATE TIMESTAMP
);

CREATE SCHEMA DIALEXA;
CREATE TABLE DIALEXA.BRANCH
(
	ETLSOURCEID VARCHAR,
	BRANCHID VARCHAR,
	NAME VARCHAR,
	ENTITYID VARCHAR,
	ADDRESS1 VARCHAR,
	ADDRESS2 VARCHAR,
	ADDRESS3 VARCHAR,
	CITY VARCHAR,
	STATE VARCHAR,
	ZIP VARCHAR,
	PHONE VARCHAR,
	COUNTRY_REGION VARCHAR,
	WEBSITE VARCHAR,
	MONDAY_HOURS VARCHAR,
	TUESDAY_HOURS VARCHAR,
	WEDNESDAY_HOURS VARCHAR,
	THURSDAY_HOURS VARCHAR,
	FRIDAY_HOURS VARCHAR,
	SATURDAY_HOURS VARCHAR,
	LATITUDE VARCHAR,
	LONGITUDE VARCHAR,
	BRANCHMANAGER VARCHAR,
	BRANCHMANAGERPHONE VARCHAR,
	BRANCHMANAGEREMAIL VARCHAR,
	ECOMMBRANCHID VARCHAR,
	WORKDAYID VARCHAR,
	WORKDAYLOCATIONNAME VARCHAR,
	RVP VARCHAR,
	PLUMBING BIT,
	WATERWORKS BIT,
	HVAC BIT,
	BANDK BIT,
	RVPPHONE VARCHAR,
	RVPEMAIL VARCHAR,
	ACTINGBRANCHMANAGER VARCHAR,
	ACTINGBRANCHMANAGERPHONE VARCHAR,
	ACTINGBRANCHMANAGEREMAIL VARCHAR,
	BUSINESSHOURS VARCHAR
);
