create or replace view MORSCO_EDW.INNOVATE_TM_SCHEMA.INNOVATION_SALES(
	PRODUCTID,
    CATEGORY_LEVEL_1,
    CATEGORY_LEVEL_2,
    CATEGORY_LEVEL_3,
    MANUFACTURER,
    MODEL_NUMBER,
    PRODUCT_NAME,
    SALES_QTY,
    SALES_AMOUNT,
    INVOICED_COUNT)
    as

WITH cte_product_sales AS (
SELECT productid,SUM(ORDERQTY) as qty, SUM(EXTPRICE) AS sales,
count(1) as INVOICED_COUNT
FROM morsco_edw.ods.invoicedsales
WHERE orderdate >= TO_VARCHAR(DATEADD(month, -12, CURRENT_DATE()), 'YYYYMMDD')
GROUP BY productid
),
cte_product_categories AS (
SELECT ID, CATEGORY_1_NAME, CATEGORY_2_NAME, CATEGORY_3_NAME, MFR_FULL_NAME, WEB_DESCRIPTION, VENDOR_PART_NBR
FROM innovate_tm_schema.innovation_product
)
SELECT
cte_product_sales.productid as PRODUCTID,
cte_product_categories.CATEGORY_1_NAME as CATEGORY_LEVEL_1,
cte_product_categories.CATEGORY_2_NAME as CATEGORY_LEVEL_2,
cte_product_categories.CATEGORY_3_NAME as CATEGORY_LEVEL_3,
cte_product_categories.MFR_FULL_NAME as MANUFACTURER,
cte_product_categories.VENDOR_PART_NBR as MODEL_NUMBER,
cte_product_categories.WEB_DESCRIPTION as PRODUCT_NAME,
cte_product_sales.qty as SALES_QTY,
cte_product_sales.sales as SALES_AMOUNT,
cte_product_sales.INVOICED_COUNT as INVOICED_COUNT
FROM
cte_product_sales
LEFT JOIN cte_product_categories ON cte_product_categories.ID = cte_product_sales.productid;

