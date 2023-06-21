package com.reece.platform.products.service;

public class SnowflakeQueries {

    public static String Search_All_Products =
        """
		/*
		CTE_PDWCATALOG comments
		- Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function is used to get the most current change for the unique key
		- The data is limited by the METAID value since that is only reflective of inscope data
		*/
		WITH cte_pdwcatalog AS (

		   SELECT
		       pdwid,
		       etlsourceid,
		       metaid,
		       metavalue,
		       loaddt AS last_changed_date
		   FROM Morsco_edw.stage.pdwdata
		   WHERE
		       metaid IN ('3', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93',
			   '94', '95', '96', '97', '98', '99', '100', '101', '102', '103', '104', '105', '106',
			   '107', '108', '109', '110', '111', '112', '113', '114', '115', '116', '117', '118',
			   '119', '120', '121', '122', '123', '124', '125', '126', '127', '128', '129', '130',
			   '131', '132', '133', '134', '135', '136', '137', '138', '139', '140', '141', '142',
			   '143', '144', '145', '146', '147', '148', '149', '150', '151', '152', '153', '154',
			   '155', '156', '157', '158', '159', '160', '161', '162', '163', '164', '165', '166',
			   '167', '168', '169', '170', '171', '172', '173', '174', '175', '176', '177', '178',
			   '179', '180', '182', '183', '184', '215', '217', '221', '226', '232', '234', '235',
			   '236', '238', '249', '255', '265', '286', '290', '291', '292', '317', '423', '424',
			   '425', '389', '390', '391', '392', '393', '394', '395', '396', '397', '398', '399',
			   '400', '401', '402', '403', '404', '405', '426', '427', '428', '429', '430', '431',
			   '432', '433', '434', '435', '436', '437', '438', '439', '440', '441', '442', '443',
			   '444', '445', '446', '447', '448', '449', '450', '451', '452', '453', '454', '455',
			   '456', '457', '458', '459', '460', '461', '462', '463', '464', '465', '466', '467',
			   '468', '469', '470', '471', '472', '473', '474', '70004', '70012', '70017', '70024',
			   '70036', '70050', '70052', '70059', '70094', '70101', '70102', '70103', '70104',
			   '70107', '70094')
		   QUALIFY ROW_NUMBER() OVER(PARTITION BY pdwid, metaid ORDER BY pdwid DESC, loaddt DESC) = 1

		),

		/*
		CTE_PDWMETA comments
		- Since the STAGE tables are reflective of when data changed and new rows are inserted,
		 a window function is used to get the most current change for the unique key
		*/
		cte_pdwmeta AS (

		   SELECT
		       fieldid,
		       description
		   FROM morsco_edw.stage.pdwmeta
		   QUALIFY ROW_NUMBER() OVER(PARTITION BY fieldid ORDER BY loaddt DESC) = 1

		),

		/*
		PRICE_LINE_BY_BRANCH comments
		- Get a list of the branches related to the Priceline
		- Since the morsco_edw.stage tables are reflective of when data changed and new rows are inserted,
		 a window function is used to get the most current change for the unique key
		*/
		price_line_by_branch AS (

		   SELECT
		       etlsourceid,
		       pricelineid,
		       authbranchid,
		       ROW_NUMBER() OVER (
			   PARTITION BY etlsourceid, pricelineid, authbranchid ORDER BY loaddt DESC
		       ) AS product_rank
		   FROM morsco_edw.stage.pricelineauthbranch

		),

		exclusion_mapping AS (

		   SELECT
		       pdwid,
		       metaid,
		       CASE
			   WHEN metaid = 426 AND metavalue = 1 THEN 'al'
			   WHEN metaid = 427 AND metavalue = 1 THEN 'az'
			   WHEN metaid = 428 AND metavalue = 1 THEN 'ar'
			   WHEN metaid = 429 AND metavalue = 1 THEN 'ca'
			   WHEN metaid = 430 AND metavalue = 1 THEN 'co'
			   WHEN metaid = 431 AND metavalue = 1 THEN 'ct'
			   WHEN metaid = 432 AND metavalue = 1 THEN 'de'
			   WHEN metaid = 433 AND metavalue = 1 THEN 'dc'
			   WHEN metaid = 434 AND metavalue = 1 THEN 'fl'
			   WHEN metaid = 435 AND metavalue = 1 THEN 'ga'
			   WHEN metaid = 436 AND metavalue = 1 THEN 'id'
			   WHEN metaid = 437 AND metavalue = 1 THEN 'il'
			   WHEN metaid = 438 AND metavalue = 1 THEN 'in'
			   WHEN metaid = 439 AND metavalue = 1 THEN 'ia'
			   WHEN metaid = 440 AND metavalue = 1 THEN 'ks'
			   WHEN metaid = 441 AND metavalue = 1 THEN 'ky'
			   WHEN metaid = 442 AND metavalue = 1 THEN 'la'
			   WHEN metaid = 443 AND metavalue = 1 THEN 'md'
			   WHEN metaid = 444 AND metavalue = 1 THEN 'md'
			   WHEN metaid = 445 AND metavalue = 1 THEN 'ma'
			   WHEN metaid = 446 AND metavalue = 1 THEN 'mi'
			   WHEN metaid = 447 AND metavalue = 1 THEN 'mn'
			   WHEN metaid = 448 AND metavalue = 1 THEN 'ms'
			   WHEN metaid = 449 AND metavalue = 1 THEN 'mo'
			   WHEN metaid = 450 AND metavalue = 1 THEN 'mt'
			   WHEN metaid = 451 AND metavalue = 1 THEN 'ne'
			   WHEN metaid = 452 AND metavalue = 1 THEN 'nv'
			   WHEN metaid = 453 AND metavalue = 1 THEN 'nh'
			   WHEN metaid = 454 AND metavalue = 1 THEN 'nj'
			   WHEN metaid = 455 AND metavalue = 1 THEN 'nm'
			   WHEN metaid = 456 AND metavalue = 1 THEN 'ny'
			   WHEN metaid = 457 AND metavalue = 1 THEN 'nc'
			   WHEN metaid = 458 AND metavalue = 1 THEN 'nd'
			   WHEN metaid = 459 AND metavalue = 1 THEN 'oh'
			   WHEN metaid = 460 AND metavalue = 1 THEN 'ok'
			   WHEN metaid = 461 AND metavalue = 1 THEN 'or'
			   WHEN metaid = 462 AND metavalue = 1 THEN 'pa'
			   WHEN metaid = 463 AND metavalue = 1 THEN 'ri'
			   WHEN metaid = 464 AND metavalue = 1 THEN 'sc'
			   WHEN metaid = 465 AND metavalue = 1 THEN 'sd'
			   WHEN metaid = 466 AND metavalue = 1 THEN 'tn'
			   WHEN metaid = 467 AND metavalue = 1 THEN 'tx'
			   WHEN metaid = 468 AND metavalue = 1 THEN 'ut'
			   WHEN metaid = 469 AND metavalue = 1 THEN 'vt'
			   WHEN metaid = 470 AND metavalue = 1 THEN 'va'
			   WHEN metaid = 471 AND metavalue = 1 THEN 'wa'
			   WHEN metaid = 472 AND metavalue = 1 THEN 'wv'
			   WHEN metaid = 473 AND metavalue = 1 THEN 'wi'
			   WHEN metaid = 474 AND metavalue = 1 THEN 'wy'
		       END AS state_short_name
		   FROM cte_pdwcatalog

		),

		cte_territory_exclusion_list AS (

		   SELECT
		       cte_pdwcatalog.pdwid,
		       ARRAY_AGG(
			   DISTINCT exclusion_mapping.state_short_name
		       ) WITHIN GROUP (ORDER BY exclusion_mapping.state_short_name) AS territory_exclusion_list
		   from cte_pdwcatalog
		   LEFT JOIN exclusion_mapping ON cte_pdwcatalog.pdwid = exclusion_mapping.pdwid
		   GROUP BY 1

		),

		/*
		CTE_EXCL_BRANCH comments
		- Get a list of product branches to exclude
		*/
		cte_excl_branch AS (

		   SELECT
		       etlsourceid,
		       pricelineid,
		       (
			   ARRAY_AGG(
			       authbranchid
			   ) WITHIN GROUP (ORDER BY etlsourceid, pricelineid, authbranchid)
		       ) AS product_branch_exclusion
		   FROM price_line_by_branch
		   WHERE etlsourceid = 'MSC'
		       AND product_rank = 1
		   GROUP BY 1, 2

		),

		/*
		CTE_MATRIXBASISINFO comments
		- Pull underlying data to use in the price determination
		*/
		cte_matrixbasisinfo AS (
		    SELECT
			morsco_edw.ods.matrixbasisinfo.*,
			morsco_edw.ods.matrixdates.expiredate,
			morsco_edw.ods.matrixdates.vmc AS recordcount,
			ROW_NUMBER() OVER (PARTITION BY morsco_edw.ods.matrixbasisinfo.etlsourceid, COALESCE(morsco_edw.ods.matrixbasisinfo.branchid, ''),
			    COALESCE(morsco_edw.ods.matrixbasisinfo.customerid, ''),
			    COALESCE(morsco_edw.ods.matrixbasisinfo.custclassid, ''),
			    COALESCE(morsco_edw.ods.matrixbasisinfo.productid, ''),
			    COALESCE(morsco_edw.ods.matrixbasisinfo.prodsellgroupid, '')
			    ORDER BY morsco_edw.ods.matrixbasisinfo.effectivedate DESC) AS rownum
		    FROM
			morsco_edw.ods.matrixbasisinfo
		    LEFT JOIN
			morsco_edw.ods.matrixdates ON
			    morsco_edw.ods.matrixdates.etlsourceid = morsco_edw.ods.matrixbasisinfo.etlsourceid AND morsco_edw.ods.matrixdates.matrixdatesid || '~' || CAST(
				DATEDIFF(DD, '1967-12-31', CAST(morsco_edw.ods.matrixdates.effectivedate AS DATE)) AS VARCHAR
			    ) = morsco_edw.ods.matrixbasisinfo.matrixid
		    WHERE
			morsco_edw.ods.matrixbasisinfo.etlsourceid = 'MSC'
			AND morsco_edw.ods.matrixbasisinfo.vendorid IS NULL
			AND morsco_edw.ods.matrixbasisinfo.effectivedate <= CURRENT_DATE()
			AND morsco_edw.ods.matrixdates.expiredate >= CURRENT_DATE()
			AND morsco_edw.ods.matrixbasisinfo.basisnumber NOT IN ('28', '25', '29', '30', '21', '22')
		)

		/*
		UMSP_MATRIX_BASIS_INFO comments
		- Pulls anything with a basisnumber of 3 (UMSP) and list price > 0 and excluding todd pipe
		*/
		,UMSP_matrix_basis_info AS (
		    select *
		    from cte_matrixbasisinfo
		    WHERE QTYBRKFORMCONSTANT != 0 AND
		    eclipseid NOT LIKE 'TPS%' -- todd pipe is not standardized
		    AND basisnumber = 3
		),

		/*
		CMP_MATRIX_BASIS_INFO comments
		- Pulls baseline CMP prices
		*/
		cmp_matrix_basis_info AS (
		    select *
		    from cte_matrixbasisinfo
		    WHERE eclipseid LIKE '~%'
				AND custclassid LIKE '%CMP%'
				AND eclipseid NOT LIKE 'TPS%' -- todd pipe is not standardized
		    AND basisnumber = 4
		),

		/*
		ALL_MATRIX_BASIS_INFO comments
		- Pulls prices from 'ALL' custclassids
		*/
		all_matrix_basis_info AS (
		    SELECT *
		    FROM cte_matrixbasisinfo
		    WHERE eclipseid LIKE '~%'
		    AND custclassid LIKE 'ALL'
		    AND eclipseid NOT LIKE 'TPS%' -- todd pipe is not standardized
		    AND basisnumber = 4
		),

		/*
		PRODUCT_PRICING comments
		- Pulls pricing from all price sheets for each item, sorting by most recent and then highest price and keeping the top value
		*/
		product_pricing AS (
		    SELECT
			etlsourceid,
			pricesheetid,
			productpriceid,
			effectivedate,
			productid,
			listprice,
			loaddt,
			competitivemarketprice AS competitivemarketprice
		    FROM morsco_edw.stage.productpricing
		    WHERE effectivedate <= current_date()
		QUALIFY row_number() OVER (PARTITION BY etlsourceid, productid ORDER BY effectivedate DESC, loaddt  DESC, competitivemarketprice desc) = 1
		),

		/*
		MAX_PRODUCT_DATETIME comments
		- Gets the latest product date from the stage product table
		*/
		max_product_datetime AS (

		   select
		       etlsourceid,
		       productid,
		       max(exportdate) AS max_export_date,
		       max(LOADDT) AS max_loaddt
		   from morsco_edw.stage.product
		   group by 1,2

		),

		/*
		LATEST_PRODUCTS comments
		- Get a list of products
		- Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function
		 is used to get the most current change for the unique key
		*/
		latest_product AS (

		   SELECT
		       morsco_edw.stage.product.pdwid,
		       morsco_edw.stage.product.productid AS erpproductid,
		       morsco_edw.stage.product.productid,
		       morsco_edw.stage.product.pricelineid,
		       morsco_edw.stage.product.proddesc,
		       morsco_edw.stage.product.etlsourceid,
		       morsco_edw.stage.product.keywords,
		       morsco_edw.stage.product.catalognumber,
		       morsco_edw.stage.product.upc,
		       morsco_edw.stage.product.prodstatus,
		       morsco_edw.stage.product.buylineid,
		       morsco_edw.stage.product.indextype,
		       morsco_edw.stage.product.loaddt
		   FROM morsco_edw.stage.product
		   INNER JOIN max_product_datetime on morsco_edw.stage.product.etlsourceid = max_product_datetime.etlsourceid
		       and morsco_edw.stage.product.productid = max_product_datetime.productid
		       and morsco_edw.stage.product.exportdate = max_product_datetime.max_export_date
		       AND morsco_edw.stage.product.loaddt = max_product_datetime.max_loaddt
		   WHERE
		      morsco_edw.stage.product.prodstatus IN (1, 2)
		       AND morsco_edw.stage.product.pdwid IS NOT NULL
		       AND morsco_edw.stage.product.pricelineid NOT IN ('MSC-NONSTOCK','NONSTOCK', 'IGN_AVAI')
		       AND morsco_edw.stage.product.etlsourceid = 'MSC'
		       AND morsco_edw.stage.product.buylineid NOT IN ('INSTALL','MISC','XX')
		       AND NOT (TRIM(morsco_edw.stage.product.proddesc) LIKE ANY(UPPER('%<D>%'), '**%', '%�%'))
		       AND (UPPER(morsco_edw.stage.product.indextype) = 'P'
				OR morsco_edw.stage.product.indextype IS NULL
				OR morsco_edw.stage.product.indextype = '7')
		       AND morsco_edw.stage.product.commcode != 'Market Credit'

		),

		/*
		PRODUCTS_WITH_PRICING comments
		- UMSP is subbed into the CMP field if a UMSP exists for the item. Otherwise, CMP is used.
		*/
		products_with_pricing AS (
		SELECT
			      latest_product.*,
			      COALESCE(cmp_matrix_basis_info_prod.basisnumber,
				  cmp_matrix_basis_info_sell.basisnumber,
				  all_matrix_basis_info_prod.basisnumber,
				  all_matrix_basis_info_sell.basisnumber)                                          AS basis_number,
			      COALESCE(cmp_matrix_basis_info_prod.formula,
				  cmp_matrix_basis_info_sell.formula,
				  all_matrix_basis_info_prod.formula,
				  all_matrix_basis_info_sell.formula)                                              AS mbi_formula,
			      COALESCE(cmp_matrix_basis_info_prod.multiplier,
				  cmp_matrix_basis_info_sell.multiplier,
				  all_matrix_basis_info_prod.multiplier,
				  all_matrix_basis_info_sell.multiplier)         AS multiplier,
			      cmp_matrix_basis_info_prod.productid                                                      AS cmp_prod_id,
			      cmp_matrix_basis_info_sell.productid                                                      AS cmp_prod_sell_id,
			      all_matrix_basis_info_prod.productid                                                      AS all_prod_id,
			      all_matrix_basis_info_sell.productid                                                 AS all_prod_sell_id,
			      cmp_matrix_basis_info_prod.prodsellgroupid                                                AS cmp_prod_sellgroup,
			      cmp_matrix_basis_info_sell.prodsellgroupid                                                AS cmp_prod_sell_sellgroup,
			      all_matrix_basis_info_prod.prodsellgroupid                                                 AS all_prod_selllgroup,
			      all_matrix_basis_info_sell.prodsellgroupid                                                 AS all_prod_sell_sellgroup,
			      product_pricing.competitivemarketprice                                               AS cmp,
			      product_pricing.listprice                                                            AS list_price,
			      TRY_TO_DOUBLE(REPLACE(REPLACE(REPLACE(REPLACE(
									  REPLACE(REPLACE(REPLACE(mbi_formula, 'X', ''), '+', ''), '$', ''),
									  '*', ''), 'D', ''), '-', ''), 'GP', '')) AS formula_price,
			      formula_price / 100                                                                  AS formula_percentage,
			      (cmp * (formula_percentage))                                                         AS cmp_multiplier,
			      (1 - (formula_percentage))                                                           AS gp_calc,
			      CASE
				  WHEN basis_number = '3' THEN list_price
				  WHEN basis_number = '4' AND (LEFT(mbi_formula, 1) = '*' OR LEFT(mbi_formula, 1) = 'X')
				      THEN cmp * formula_price
				  WHEN basis_number = '4' AND LEFT(mbi_formula, 1) = 'D' THEN cmp / formula_price
				  WHEN basis_number = '4' AND LEFT(mbi_formula, 2) = 'GP' THEN cmp / gp_calc
				ELSE cmp
			  END AS calculated_cmp
			  FROM latest_product
			  LEFT JOIN umsp_matrix_basis_info as umsp_matrix_basis_info_prod
			      ON latest_product.productid = umsp_matrix_basis_info_prod.productid
				  AND latest_product.etlsourceid = umsp_matrix_basis_info_prod.etlsourceid
			  LEFT JOIN cmp_matrix_basis_info as cmp_matrix_basis_info_prod
			      ON latest_product.productid = cmp_matrix_basis_info_prod.productid
				  AND latest_product.etlsourceid = cmp_matrix_basis_info_prod.etlsourceid
			  LEFT JOIN cmp_matrix_basis_info as cmp_matrix_basis_info_sell
			      ON latest_product.pricelineid = cmp_matrix_basis_info_sell.prodsellgroupid
				  AND latest_product.etlsourceid = cmp_matrix_basis_info_sell.etlsourceid
			  LEFT JOIN all_matrix_basis_info as all_matrix_basis_info_prod
			      ON latest_product.productid = all_matrix_basis_info_prod.productid
				  AND latest_product.etlsourceid = all_matrix_basis_info_prod.etlsourceid
			  LEFT JOIN all_matrix_basis_info as all_matrix_basis_info_sell
			      ON latest_product.pricelineid = all_matrix_basis_info_sell.prodsellgroupid
				  AND latest_product.etlsourceid = all_matrix_basis_info_sell.etlsourceid
			      AND all_matrix_basis_info_sell.productid IS NULL
			  LEFT JOIN product_pricing
			      ON latest_product.etlsourceid = product_pricing.etlsourceid AND
				 latest_product.productid = product_pricing.productid
		    ),

		/*
		SOLD_PRODUCTS comments
		- Get a count of sold product
		*/
		sold_products AS (

		   SELECT
		       morsco_edw.ods.ledgersodetail.etlsourceid,
		       morsco_edw.ods.ledgersodetail.productid,
		       COUNT(DISTINCT morsco_edw.ods.ledgersodetail.ledgerid) AS product_sold_count
		   FROM morsco_edw.ods.ledgersoheader
		   INNER JOIN morsco_edw.ods.ledgersodetail
		       ON morsco_edw.ods.ledgersoheader.etlsourceid = morsco_edw.ods.ledgersodetail.etlsourceid
			   AND morsco_edw.ods.ledgersoheader.ledgerid = morsco_edw.ods.ledgersodetail.ledgerid
			   AND morsco_edw.ods.ledgersoheader.vmc = morsco_edw.ods.ledgersodetail.vmc
			   AND morsco_edw.ods.ledgersoheader.genid = morsco_edw.ods.ledgersodetail.genid
		   WHERE morsco_edw.ods.ledgersoheader.etlsourceid = 'MSC'
		       AND UPPER(morsco_edw.ods.ledgersoheader.orderstatus) IN ('I', 'O')
		       AND morsco_edw.ods.ledgersoheader.orderdate >= (CURRENT_DATE - 90)
		   GROUP BY 1, 2

		),

		/*
		PRODUCT_STOCK comments
		- Get list of locations with product in stock
		*/
		product_stock AS (

		   SELECT
		       productid,
		       etlsourceid,
		       ARRAY_AGG(
			   branchid
		       ) WITHIN GROUP (ORDER BY productid, etlsourceid) AS in_stock_location_list
		   FROM morsco_edw.ods.productdynam
		   WHERE etlsourceid = 'MSC'
		       AND availableqty > 0
		   GROUP BY 1, 2
		   ORDER BY 1, 2

		),

		/*
		CTE_PRODUCT comments
		-   Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function
		   is used to get the most current change for the unique key
		-   Product data is limited by filter criteria as follows:
		-\tProduct status is ‘active’
		-\tPrice Line does not equal 'MSC-NONSTOCK'
		-\tBuy Line is not ‘XX’
		-\tProduct description does not indicate it is a demo product ‘<D>’
		-\tProduct description does not include certain special characters
		-\tThe index type is a ‘P’ or is NULL
		-\tThe Product has been in stock within the last 6 months
		-   Only Eclipse data is extracted
		-   Other filters in this CTE reflect what is in the ODS.PRODUCT view
		- STAGE.PRODUCT is joined to STAGE.KEY_PRODUCT since that is reflective of the ODS.PRODUCT view
		 and to filter Products out if not in stock within the last 6 months
		*/
		cte_product AS (

		   SELECT
		       products_with_pricing.catalognumber,
		       products_with_pricing.erpproductid,
		       products_with_pricing.etlsourceid,
		       products_with_pricing.keywords,
		       products_with_pricing.pdwid,
		       products_with_pricing.buylineid,
		       products_with_pricing.pricelineid,
		       products_with_pricing.proddesc,
		       products_with_pricing.upc,
		       products_with_pricing.calculated_cmp AS cmp,
		       sold_products.product_sold_count AS product_sold_count,
		       products_with_pricing.loaddt AS last_changed_date,
		       products_with_pricing.etlsourceid || '-' || products_with_pricing.productid AS productid,
		       IFF(
			   product_stock.in_stock_location_list IS NOT NULL,
			   '"' || 'in_stock_location' || '"' || ': ' ||
			   CAST(product_stock.in_stock_location_list AS VARCHAR),
			   NULL
		       ) AS in_stock_location_list,
		       IFF(cte_excl_branch.product_branch_exclusion IS NOT NULL,
			   '"' ||
			   'product_branch_exclusion' ||
			   '"' ||
			   ': ' ||
			   CAST(cte_excl_branch.product_branch_exclusion AS VARCHAR),
			   NULL
		       ) AS product_branch_exclusion
		   FROM products_with_pricing
		   --Key Products
		   INNER JOIN morsco_edw.stage.key_product
		       ON products_with_pricing.etlsourceid = morsco_edw.stage.key_product.etlsourceid
			   AND products_with_pricing.productid = morsco_edw.stage.key_product.productid
		   --Instock Products
		   LEFT JOIN product_stock
		       ON products_with_pricing.productid = product_stock.productid
			   AND products_with_pricing.etlsourceid = product_stock.etlsourceid
		   --Product sold count
		   LEFT JOIN sold_products
		       ON products_with_pricing.etlsourceid = sold_products.etlsourceid
			   AND products_with_pricing.productid = sold_products.productid
		   --List of Branches excluded
		   LEFT JOIN cte_excl_branch
		       ON products_with_pricing.etlsourceid = cte_excl_branch.etlsourceid
			   AND products_with_pricing.pricelineid = cte_excl_branch.pricelineid

		),

		/*
		CTE_ENTITYPNIDS comments
		- Get list of eclipse part numbers, entity ids and customer part numbers
		- Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function is
		 used to get the most current change for the unique key
		*/
		cte_entitypnids AS (

		   SELECT
		       eclipsepartnum,
		       etlsourceid,
		       entityid,
		       customerpartnum,
		       loaddt AS last_changed_date
		   FROM morsco_edw.stage.entitypnids
		   WHERE customerpartnum NOT LIKE '%\u008D%'
		   QUALIFY ROW_NUMBER() OVER(PARTITION BY entitypnid ORDER BY loaddt DESC) = 1

		),

		/*
		cte_entity_objects comments
		- This assembles the list of customers and part numbers associated to a Product
		- The list of customers and parts and assembled in 2 respective lists and in JSON form
		- Creates a map of customer number and their part numbers
		*/
		cte_entity_objects AS (

		    SELECT
		       cte_product.pdwid,
		       cte_product.productid,
		       cte_product.etlsourceid,
		       cte_entitypnids.entityid,
		       ARRAY_AGG(cte_entitypnids.customerpartnum)
			   WITHIN GROUP (ORDER BY cte_product.etlsourceid, cte_product.productid,cte_entitypnids.entityid ) AS parts_array,
		       OBJECT_CONSTRUCT(cte_entitypnids.entityid, parts_array::VARIANT) AS cust_parts_dict,
		       MAX(cte_entitypnids.last_changed_date) AS last_changed_date
		   FROM  cte_entitypnids
		   INNER JOIN cte_product
		       ON cte_product.erpproductid = cte_entitypnids.eclipsepartnum
			   AND cte_product.etlsourceid = cte_entitypnids.etlsourceid
		   GROUP BY 1, 2, 3, 4

		),

		/*
		CTE_CUSTOMER_PN comments
		- This assembles the list of customers and part numbers associated to a Product
		- The list of customers and parts and assembled in 2 respective lists and in JSON form
		*/
		cte_customer_pn AS (

		   SELECT
		       cte_entity_objects.pdwid,
		       cte_entity_objects.productid,
		       cte_entity_objects.etlsourceid,
		       '"' || 'customer_number' || '":' || CAST(ARRAY_AGG(cte_entity_objects.entityid)
			       WITHIN GROUP (ORDER BY cte_entity_objects.etlsourceid, cte_entity_objects.productid ) AS VARCHAR) as customer_nbr_list,
		       '"' || 'customer_part_nbr_list' || '":' || CAST(ARRAY_UNION_AGG(cte_entity_objects.parts_array) AS VARCHAR) as customer_part_nbr_list,
		       '"' || 'customer_part_numbers' || '":' || CAST(ARRAY_AGG(cte_entity_objects.cust_parts_dict) AS VARCHAR) as customer_part_numbers,
		       MAX(cte_entity_objects.last_changed_date) AS last_changed_date
		   FROM cte_entity_objects
		   GROUP BY 1, 2, 3
		   ORDER BY 2

		),

		/*
		PRODUCT_FIELD_VALUES comments
		- Each feature is a name/value pair from the product meta table
		*/
		product_field_values AS (

		   SELECT
		       fieldid,
		       'LABEL' AS label,
		       NULL AS value,
		       CAST(REPLACE(description, 'TS ATTRIB NAME', '') AS INT) AS ts_seq
		   FROM cte_pdwmeta
		   WHERE description LIKE 'TS ATTRIB NAME%'
		       AND fieldid IN ('82', '83', '84', '85', '86', '87', '88', '89', '90', '91',
			   '92', '93', '94', '95', '96', '97', '98', '99', '100', '101', '102',
			   '103', '104', '105', '106', '107', '108', '109', '110', '111', '112',
			   '113', '114', '115', '116', '117', '118', '119', '120', '121', '122',
			   '123', '124', '125', '126', '127', '128', '129', '130', '290')
		       AND fieldid != '"'
		   UNION ALL
		   SELECT
		       fieldid,
		       NULL AS label,
		       'VALUE' AS value,
		       CAST(REPLACE(description, 'TS ATTRIB VALUE', '') AS INT) AS ts_seq
		   FROM cte_pdwmeta
		   WHERE description LIKE 'TS ATTRIB VALUE%'
		       AND fieldid IN ('131', '132', '133', '134', '135', '136', '137', '138', '139', '140',
			   '141', '142', '143', '144', '145', '146', '147', '148', '149', '150',
			   '151', '152', '153', '154', '155', '156', '157', '158', '159', '160',
			   '161', '162', '163', '164', '165', '166', '167', '168', '169', '170',
			   '171', '172', '173', '174', '175', '176', '177', '178', '179', '180')
		       AND fieldid != '"'
		   ORDER BY 1

		),

		/*
		DISTINCT_PRODUCT_FIELD_VALUES comments
		- Duplicate technical specifications (name) are deduplicated, essentially taking the first of the duplicate
		*/
		distinct_product_field_values AS (

		   SELECT DISTINCT
		       fieldid,
		       value,
		       ts_seq,
		       REPLACE(label, ' ', '_') AS label
		   FROM product_field_values

		),

		/*
		PRODUCT_TECH_SPECS comments
		- This assembles the list of technical specification features for a Product
		- Each feature is a name/value pair which is placed in JSON format
		- Duplicate technical specifications (name) will be deduplicated, essentially taking the first of the duplicate
		- STAGE.PDWMETA (see CTA) is joined to STAGE.PDWCATALOG (see CTE) to match up the name/value pair to a Product
		 technical key to be later joined to the Product
		*/
		product_tech_specs AS (

		   SELECT
		       cte_product.pdwid,
		       cte_product.productid,
		       cte_product.etlsourceid,
		       distinct_product_field_values.ts_seq,
		       MAX(
			   IFF(
			       distinct_product_field_values.label IS NOT NULL,
			       '"' || REPLACE(LOWER(metavalue) || '"', ' ', '_'),
			       NULL
			   )
		       ) ||
		       ': ' ||
		       MAX(
			   IFF(
			       distinct_product_field_values.value IS NOT NULL,
			       '"' || REPLACE(metavalue, '\\"', '\\\\"') || '"',
			       ' '
			   )
		       ) AS ts_nested_value,
		       MAX(
			   IFF(
			       distinct_product_field_values.label IS NOT NULL,
			       '"' || REPLACE(LOWER(metavalue) || '"', ' ', '_'),
			       NULL
			   )
		       ) AS ts_label,
		       MAX(distinct_product_field_values.value) AS ts_value
		   FROM cte_pdwcatalog
		   INNER JOIN cte_product
		       ON cte_pdwcatalog.pdwid = cte_product.pdwid
			   AND cte_pdwcatalog.etlsourceid = cte_product.etlsourceid
		   --ASSEMBLE THE NAME AND VALUE PAIR TO BE ABLE TO JOIN UP INTO A LIST
		   INNER JOIN distinct_product_field_values
		       ON cte_pdwcatalog.metaid = distinct_product_field_values.fieldid
		   GROUP BY 1, 2, 3, 4
		   HAVING IFNULL(ts_nested_value, ' ') > ' ' AND IFNULL(ts_value, ' ') > ' '
		   QUALIFY
		       ROW_NUMBER() OVER(
			   PARTITION BY
			       cte_product.productid, ts_label
			   ORDER BY distinct_product_field_values.ts_seq
		       ) = 1
		   ORDER BY 2, 3

		),

		/*
		CTE_TECH_SPECS comments
		- This assembles the list of technical specification features for a Product
		- Each feature is a name/value pair which is placed in JSON format
		- Duplicate technical specifications (name) will be deduplicated, essentially taking the first of the duplicate
		- METAID/FIELDID is the filter to limit the number of features to 50
		- STAGE.PDWMETA (see CTA) is joined to STAGE.PDWCATALOG (see CTE) to match up the name/value pair to a Product
		 technical key to be later joined to the Product
		*/
		cte_tech_specs AS (

		   SELECT DISTINCT
		       pdwid,
		       productid,
		       etlsourceid,
		       '"' || 'technical_specifications' || '"' || ': ' || '[{' ||
		       (
			   LISTAGG(
			       DISTINCT ts_nested_value, ','
			   ) WITHIN GROUP (ORDER BY ts_nested_value ) OVER (PARTITION BY productid )
		       )
		       || '}]' AS tech_specs_list
		   FROM product_tech_specs

		),

		/*
		PRODUCT_LIST comments
		- MAX function is used since the data needs to be collapsed to the Product level
		- To support incremental loading for the Search Index, the greatest LOADDT is calculated
		- STAGE.PDWCATALOG (see CTEs) is joined to STAGE.PRODUCT (see CTE) to bring together the technical specifications
		- STAGE.ENTITYPNDS (see CTE) is joined to STAGE.PRODUCT (see CTE) to bring together the customer numbers and part numbers
		*/
		products_list AS (

		   SELECT
		       cte_product.pdwid AS internal_item_nbr,
		       cte_product.productid AS id,
		       cte_product.productid AS erp_product_id,
		       -- BUYLINE
		       MAX(cte_product.buylineid) AS buylineid,
		       -- ADDITIONAL PRODUCT CODES
		       MAX(
			   IFF(cte_pdwcatalog.metaid = '3', cte_pdwcatalog.metavalue, cte_product.catalognumber)
		       ) AS vendor_part_nbr,
		       MAX(
			   IFF(cte_pdwcatalog.metaid = '70059', cte_pdwcatalog.metavalue, cte_product.upc)
		       ) AS upc_id,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70052' THEN cte_pdwcatalog.metavalue END
		       ) AS unspc_id,
		       -- SEARCH KEYWORDS
		       MAX(keywords) AS search_keyword_text,
		       -- PRODUCT CATEGORIES
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '423' THEN cte_pdwcatalog.metavalue END
		       ) AS category_1_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '424' THEN cte_pdwcatalog.metavalue END
		       ) AS category_2_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '425' THEN cte_pdwcatalog.metavalue END
		       ) AS category_3_name,
		       -- PRODUCT NAME & DESCRIPTION
		       LTRIM(
			   MAX(
			       IFF(
				   cte_pdwcatalog.metaid = '70017',
				   cte_pdwcatalog.metavalue,
				   ' ' || cte_product.proddesc
			       )
			   )
		       ) AS web_description,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70004' THEN cte_pdwcatalog.metavalue END
		       ) AS product_overview_description,
		       -- MANUFACTURER INFO
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70024' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_full_name,
		       -- TECHNICAL, FEATURES AND BENEFITS
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '217' THEN cte_pdwcatalog.metavalue END
		       ) AS feature_benefit_list_text,
		       -- IMAGES
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70050' THEN cte_pdwcatalog.metavalue END
		       ) AS thumbnail_image_url_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '234' THEN cte_pdwcatalog.metavalue END
		       ) AS medium_image_url_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70012' THEN cte_pdwcatalog.metavalue END
		       ) AS full_image_url_name,
		       -- TECHNICAL DOCUMENTS
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '291' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_catalog_doc_file_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '292' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_spec_tech_doc_file_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '238' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_msds_doc_file_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '226' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_install_instruction_doc_file_name,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '236' THEN cte_pdwcatalog.metavalue END
		       ) AS mfr_item_data_doc_file_name,
		       -- ENVIRONMENTAL OPTIONS
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '232' THEN cte_pdwcatalog.metavalue END
		       ) AS low_lead_compliant_flag,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '235' THEN cte_pdwcatalog.metavalue END
		       ) AS mercury_free_flag,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '286' THEN cte_pdwcatalog.metavalue END
		       ) AS water_sense_compliant_flag,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '215' THEN cte_pdwcatalog.metavalue END
		       ) AS energy_star_flag,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '221' THEN cte_pdwcatalog.metavalue END
		       ) AS hazardous_material_flag,
		       -- PACKAGE DIMENSIONS
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '249' THEN cte_pdwcatalog.metavalue END
		       ) AS package_weight_nbr,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70101' THEN cte_pdwcatalog.metavalue END
		       ) AS package_width_nbr,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70102' THEN cte_pdwcatalog.metavalue END
		       ) AS package_length_nbr,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70103' THEN cte_pdwcatalog.metavalue END
		       ) AS package_height_nbr,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70104' THEN cte_pdwcatalog.metavalue END
		       ) AS package_volume_nbr,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70107' THEN cte_pdwcatalog.metavalue END
		       ) AS package_volume_uom_code,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '255' THEN cte_pdwcatalog.metavalue END
		       ) AS package_weight_uom_code,
		       -- MINIMUM INCREMENT QUANTITY
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '317' THEN cte_pdwcatalog.metavalue END
		       ) AS minimum_increment_qty,
		       -- BOOST SEARCH VALUES
		       MAX(
			   CASE
			       WHEN
				   cte_pdwcatalog.metaid = '172' AND IFNULL(
				       cte_pdwcatalog.metavalue, '0'
				   ) != '0'
			       THEN cte_pdwcatalog.metavalue
			   END
		       ) AS product_search_boost_1_nbr,
		       MAX(
			   CASE
			       WHEN
				   cte_pdwcatalog.metaid = '173' AND IFNULL(
				       cte_pdwcatalog.metavalue, '0'
				   ) != '0'
			       THEN cte_pdwcatalog.metavalue
			   END
		       ) AS product_search_boost_2_nbr,
		       MAX(
			   CASE
			       WHEN
				   cte_pdwcatalog.metaid = '174' AND IFNULL(
				       cte_pdwcatalog.metavalue, '0'
				   ) != '0'
			       THEN cte_pdwcatalog.metavalue
			   END
		       ) AS product_search_boost_3_nbr,
		       MAX(
			   CASE
			       WHEN
				   cte_pdwcatalog.metaid = '176' AND IFNULL(
				       cte_pdwcatalog.metavalue, '0'
				   ) != '0'
			       THEN cte_pdwcatalog.metavalue
			   END
		       ) AS product_search_boost_4_nbr,
		       --- Technical Specification
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '70094' THEN cte_pdwcatalog.metavalue END
		       ) AS product_line,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '389' THEN cte_pdwcatalog.metavalue END
		       ) AS material,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '390' THEN cte_pdwcatalog.metavalue END
		       ) AS color_finish,
		       MAX(CASE WHEN cte_pdwcatalog.metaid = '391' THEN cte_pdwcatalog.metavalue END
		       ) AS size,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '392' THEN cte_pdwcatalog.metavalue END
		       ) AS length,
		       MAX(CASE WHEN cte_pdwcatalog.metaid = '393' THEN cte_pdwcatalog.metavalue END
		       ) AS width,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '394' THEN cte_pdwcatalog.metavalue END
		       ) AS height,
		       MAX(CASE WHEN cte_pdwcatalog.metaid = '395' THEN cte_pdwcatalog.metavalue END
		       ) AS depth,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '396' THEN cte_pdwcatalog.metavalue END
		       ) AS voltage,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '397' THEN cte_pdwcatalog.metavalue END
		       ) AS tonnage,
		       MAX(CASE WHEN cte_pdwcatalog.metaid = '398' THEN cte_pdwcatalog.metavalue END
		       ) AS btu,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '399' THEN cte_pdwcatalog.metavalue END
		       ) AS pressure_rating,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '400' THEN cte_pdwcatalog.metavalue END
		       ) AS temperature_rating,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '402' THEN cte_pdwcatalog.metavalue END
		       ) AS inlet_size,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '403' THEN cte_pdwcatalog.metavalue END
		       ) AS flow_rate,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '404' THEN cte_pdwcatalog.metavalue END
		       ) AS capacity,
		       MAX(
			   CASE WHEN cte_pdwcatalog.metaid = '405' THEN cte_pdwcatalog.metavalue END
		       ) AS wattage,
		       --- Exclusion and Product Sold
		       MAX(cte_product.product_sold_count) AS product_sold_count,
		       --- CDC
		       MAX(
			   GREATEST(
			       IFNULL(cte_product.last_changed_date, '1900-01-02'),
			       IFNULL(cte_pdwcatalog.last_changed_date, '1900-01-02'),
			       IFNULL(cte_customer_pn.last_changed_date, '1900-01-02')
			   )
		       ) AS last_update_date,
		       -- pricing
		       MAX(cte_product.cmp)::DECIMAL(18,2) AS cmp,
		       -- lists
		       MAX(cte_customer_pn.customer_nbr_list) AS customer_number_list,
		       MAX(cte_customer_pn.customer_part_nbr_list) AS customer_part_number_list,
		       MAX(cte_customer_pn.customer_part_numbers) AS customer_part_numbers,
		       MAX(cte_tech_specs.tech_specs_list) AS tech_specs_list,
		       MAX(cte_product.product_branch_exclusion) AS product_branch_exclusion,
		       MAX(cte_product.in_stock_location_list) AS in_stock_location_list,
		       MAX(cte_territory_exclusion_list.territory_exclusion_list::varchar) AS territory_exclusion_list
		   FROM cte_product
		   LEFT JOIN cte_pdwcatalog
		       ON cte_product.pdwid = cte_pdwcatalog.pdwid
		   LEFT JOIN cte_tech_specs
		       ON cte_product.pdwid = cte_tech_specs.pdwid
			   AND cte_product.productid = cte_tech_specs.productid
			   AND cte_product.etlsourceid = cte_tech_specs.etlsourceid
		   LEFT JOIN cte_customer_pn
		       ON cte_product.pdwid = cte_customer_pn.pdwid
			   AND cte_product.productid = cte_customer_pn.productid
			   AND cte_product.etlsourceid = cte_customer_pn.etlsourceid
		   LEFT JOIN cte_territory_exclusion_list
		       ON cte_product.pdwid = cte_territory_exclusion_list.pdwid
		   GROUP BY 1, 2

		),

		/*
		CTE_CREATE_JSON comments
		- This will do the primary assembly of the data together into a JSON construct
		- The format is structured into JSON with double quotes escape by a backslash to ensure the validity of the object
		- To support incremental loading for the Search Index, the greatest LOADDT is calculated
		*/
		cte_create_json AS (

		   SELECT
		       customer_number_list,
		       customer_part_number_list,
		       customer_part_numbers,
		       tech_specs_list,
		       in_stock_location_list,
		       product_branch_exclusion,
		       territory_exclusion_list,
		       last_update_date,
		       CAST(
			   OBJECT_CONSTRUCT(
			       'btu', btu,
			       'capacity', capacity,
			       'category_1_name', category_1_name,
			       'category_2_name', category_2_name,
			       'category_3_name', category_3_name,
			       'cmp', cmp,
			       'color/finish', color_finish,
			       'depth', depth,
			       'energy_star_flag', energy_star_flag,
			       'erp_product_id', erp_product_id,
			       'feature_benefit_list_text', feature_benefit_list_text,
			       'flow_rate', flow_rate,
			       'full_image_url_name', full_image_url_name,
			       'hazardous_material_flag', hazardous_material_flag,
			       'height', height,
			       'id', id,
			       'inlet_size', inlet_size,
			       'internal_item_nbr', internal_item_nbr,
			       'last_update_date', last_update_date,
			       'length', length,
			       'low_lead_compliant_flag', low_lead_compliant_flag,
			       'material', material,
			       'medium_image_url_name', medium_image_url_name,
			       'mercury_free_flag', mercury_free_flag,
			       'mfr_catalog_doc_file_name', mfr_catalog_doc_file_name,
			       'mfr_full_name', mfr_full_name,
			       'mfr_install_instruction_doc_file_name', mfr_install_instruction_doc_file_name,
			       'mfr_item_data_doc_file_name', mfr_item_data_doc_file_name,
			       'mfr_msds_doc_file_name', mfr_msds_doc_file_name,
			       'mfr_spec_tech_doc_file_name', mfr_spec_tech_doc_file_name,
			       'minimum_increment_qty', minimum_increment_qty,
			       'package_height_nbr', package_height_nbr,
			       'package_length_nbr', package_length_nbr,
			       'package_volume_nbr', package_volume_nbr,
			       'package_volume_uom_code', package_volume_uom_code,
			       'package_weight_nbr', package_weight_nbr,
			       'package_weight_uom_code', package_weight_uom_code,
			       'package_width_nbr', package_width_nbr,
			       'pressure_rating', pressure_rating,
			       'product_line', product_line,
			       'product_overview_description', product_overview_description,
			       'product_search_boost_1_nbr', product_search_boost_1_nbr,
			       'product_search_boost_2_nbr', product_search_boost_2_nbr,
			       'product_search_boost_3_nbr', product_search_boost_3_nbr,
			       'product_search_boost_4_nbr', product_search_boost_4_nbr,
			       'product_sold_count', product_sold_count,
			       'search_keyword_text', search_keyword_text,
			       'size', size,
			       'temperature_rating', temperature_rating,
			       'thumbnail_image_url_name', thumbnail_image_url_name,
			       'tonnage', tonnage,
			       'unspc_id', unspc_id,
			       'upc_id', upc_id,
			       'vendor_part_nbr', vendor_part_nbr,
			       'voltage', voltage,
			       'water_sense_compliant_flag', water_sense_compliant_flag,
			       'wattage', wattage,
			       'web_description', web_description,
			       'width', width
			   )
			   AS VARCHAR) AS product_details
		   FROM products_list


		),

		/*
		Final comments
		- This SQL simply strings together all the JSON constructs together to create the final JSON for the Product
		*/
		final AS (

		   SELECT
		       last_update_date,
		       SUBSTRING(product_details, 1, LENGTH(product_details) - 1) ||
		       IFF(tech_specs_list IS NOT NULL, ',' || tech_specs_list, '') ||
		       IFF(customer_number_list IS NOT NULL, ',' || customer_number_list, '') ||
		       IFF(customer_part_number_list IS NOT NULL, ',' || customer_part_number_list, '') ||
		       IFF(customer_part_numbers IS NOT NULL, ',' || customer_part_numbers, '') ||
		       IFF(in_stock_location_list IS NOT NULL, ',' || in_stock_location_list, '') ||
		       IFF(product_branch_exclusion IS NOT NULL, ',' || product_branch_exclusion, '') ||
		       IFF(territory_exclusion_list IS NOT NULL, ',' || '"territory_exclusion_list":' || territory_exclusion_list, '') ||
		       '}' AS product_json
		   FROM cte_create_json

		)



		SELECT product_json FROM final""";

    public static String Search_All_Changed_Products = Search_All_Products + " \n WHERE LAST_UPDATE_DATE > ?";

    public static String Fetch_All_Products =
        """
					       /*
					       CTE_PDWCATALOG comments
					       - Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function is used to get the most current change for the unique key
					       - The data is limited by the METAID value since that is only reflective of inscope data
					       */
					WITH cte_pdwcatalog AS (
					          
					   SELECT
					       pdwid,
					       etlsourceid,
					       metaid,
					       metavalue,
					       loaddt AS last_changed_date
					   FROM Morsco_edw.stage.pdwdata
					   WHERE
					       metaid IN ('3', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93',
					       '94', '95', '96', '97', '98', '99', '100', '101', '102', '103', '104', '105', '106',
					       '107', '108', '109', '110', '111', '112', '113', '114', '115', '116', '117', '118',
					       '119', '120', '121', '122', '123', '124', '125', '126', '127', '128', '129', '130',
					       '131', '132', '133', '134', '135', '136', '137', '138', '139', '140', '141', '142',
					       '143', '144', '145', '146', '147', '148', '149', '150', '151', '152', '153', '154',
					       '155', '156', '157', '158', '159', '160', '161', '162', '163', '164', '165', '166',
					       '167', '168', '169', '170', '171', '172', '173', '174', '175', '176', '177', '178',
					       '179', '180', '182', '183', '184', '215', '217', '221', '226', '232', '234', '235',
					       '236', '238', '249', '255', '265', '286', '290', '291', '292', '317', '423', '424',
					       '425', '389', '390', '391', '392', '393', '394', '395', '396', '397', '398', '399',
					       '400', '401', '402', '403', '404', '405', '426', '427', '428', '429', '430', '431',
					       '432', '433', '434', '435', '436', '437', '438', '439', '440', '441', '442', '443',
					       '444', '445', '446', '447', '448', '449', '450', '451', '452', '453', '454', '455',
					       '456', '457', '458', '459', '460', '461', '462', '463', '464', '465', '466', '467',
					       '468', '469', '470', '471', '472', '473', '474', '70004', '70012', '70017', '70024',
					       '70036', '70050', '70052', '70059', '70094', '70101', '70102', '70103', '70104',
					       '70107', '70094')
					   QUALIFY ROW_NUMBER() OVER(PARTITION BY pdwid, metaid ORDER BY pdwid DESC, loaddt DESC) = 1
					          
					),
					          
					/*
					MAX_PRODUCT_DATETIME comments
					- Gets the latest product date from the stage product table
					*/
					max_product_datetime AS (
					          
					   select
					       etlsourceid,
					       productid,
					       max(exportdate) AS max_export_date,
					       max(LOADDT) AS max_loaddt
					   from morsco_edw.stage.product
					   group by 1,2
					          
					),
					          
					/*
					LATEST_PRODUCTS comments
					- Get a list of products
					- Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function
					 is used to get the most current change for the unique key
					*/
					latest_product AS (
					          
					   SELECT
					       morsco_edw.stage.product.pdwid,
					       morsco_edw.stage.product.productid,
					       morsco_edw.stage.product.proddesc,
					       morsco_edw.stage.product.etlsourceid
					   FROM morsco_edw.stage.product
					   INNER JOIN max_product_datetime on morsco_edw.stage.product.etlsourceid = max_product_datetime.etlsourceid
					       and morsco_edw.stage.product.productid = max_product_datetime.productid
					       and morsco_edw.stage.product.exportdate = max_product_datetime.max_export_date
					       AND morsco_edw.stage.product.loaddt = max_product_datetime.max_loaddt
					   WHERE
					      morsco_edw.stage.product.prodstatus IN (1, 2)
					       AND morsco_edw.stage.product.pdwid IS NOT NULL
					       AND morsco_edw.stage.product.pricelineid NOT IN ('MSC-NONSTOCK','NONSTOCK', 'IGN_AVAI')
					       AND morsco_edw.stage.product.etlsourceid = 'MSC'
					       AND morsco_edw.stage.product.buylineid NOT IN ('INSTALL','MISC','XX')
					       AND NOT (TRIM(morsco_edw.stage.product.proddesc) LIKE ANY(UPPER('%<D>%'), '**%', '%�%'))
					       AND (UPPER(morsco_edw.stage.product.indextype) = 'P'
					        OR morsco_edw.stage.product.indextype IS NULL
					        OR morsco_edw.stage.product.indextype = '7')
					       AND morsco_edw.stage.product.commcode != 'Market Credit'
					          
					),
					          
					/*
					CTE_PRODUCT comments
					-   Since the STAGE tables are reflective of when data changed and new rows are inserted, a window function
					   is used to get the most current change for the unique key
					-   Product data is limited by filter criteria as follows:
					-  Product status is ‘active’
					-  Price Line does not equal 'MSC-NONSTOCK'
					-  Buy Line is not ‘XX’
					-  Product description does not indicate it is a demo product ‘<D>’
					-  Product description does not include certain special characters
					-  The index type is a ‘P’ or is NULL
					-  The Product has been in stock within the last 6 months
					-   Only Eclipse data is extracted
					-   Other filters in this CTE reflect what is in the ODS.PRODUCT view
					- STAGE.PRODUCT is joined to STAGE.KEY_PRODUCT since that is reflective of the ODS.PRODUCT view
					 and to filter Products out if not in stock within the last 6 months
					*/
					cte_product AS (
					          
					   SELECT
					       latest_product.pdwid,
					       latest_product.proddesc,
					       latest_product.etlsourceid || '-' || latest_product.productid AS productid
					          
					   FROM latest_product
					   --Key Products
					   INNER JOIN morsco_edw.stage.key_product
					       ON latest_product.etlsourceid = morsco_edw.stage.key_product.etlsourceid
					       AND latest_product.productid = morsco_edw.stage.key_product.productid
					),
					          
					/*
					PRODUCT_LIST comments
					- MAX function is used since the data needs to be collapsed to the Product level
					- To support incremental loading for the Search Index, the greatest LOADDT is calculated
					- STAGE.PDWCATALOG (see CTEs) is joined to STAGE.PRODUCT (see CTE) to bring together the technical specifications
					- STAGE.ENTITYPNDS (see CTE) is joined to STAGE.PRODUCT (see CTE) to bring together the customer numbers and part numbers
					*/
					products_list AS (
					          
					   SELECT
					          
					       cte_product.productid AS id,
					       -- PRODUCT NAME & DESCRIPTION
					       LTRIM(
					       MAX(
					           IFF(
					           cte_pdwcatalog.metaid = '70017',
					           cte_pdwcatalog.metavalue,
					           ' ' || cte_product.proddesc
					           )
					       )
					       ) AS web_description
					          
					   FROM cte_product
					   LEFT JOIN cte_pdwcatalog
					       ON cte_product.pdwid = cte_pdwcatalog.pdwid
					   GROUP BY 1
					          
					),
					          
					/*
					CTE_CREATE_JSON comments
					- This will do the primary assembly of the data together into a JSON construct
					- The format is structured into JSON with double quotes escape by a backslash to ensure the validity of the object
					- To support incremental loading for the Search Index, the greatest LOADDT is calculated
					*/
					cte_create_json AS (
					          
					   SELECT
					       CAST(
					       OBJECT_CONSTRUCT(
					           'id', id,
					           'name', web_description
					       )
					       AS VARCHAR) AS product_details
					   FROM products_list
					          
					          
					),
					          
					/*
					Final comments
					- This SQL simply strings together all the JSON constructs together to create the final JSON for the Product
					*/
					final AS (
					          
					   SELECT
					      product_details AS product_json
					   FROM cte_create_json
					          
					)
					          
					SELECT product_json FROM final""";
}
