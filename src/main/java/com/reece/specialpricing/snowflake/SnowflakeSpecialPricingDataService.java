package com.reece.specialpricing.snowflake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnowflakeSpecialPricingDataService extends JpaRepository<SnowflakeSpecialPrice, SnowflakeSpecialPriceId>, QuerydslPredicateExecutor<SnowflakeSpecialPrice> {
    @Query(value="ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON';", nativeQuery = true)
    void alterSessionformat();

    @Query(value = "WITH typical_price_for_customer_type AS (\n" +
                    "        /*\n" +
                    "         Calculate the average price for a product at the branch, market, region and division level\n" +
                    "         These averages are grouped by the customer rate card and product id.\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            customerratecard        AS customer_rate_card,\n" +
                    "            productid               AS product_id,\n" +
                    "            AVG(price_bybranch)     AS avg_price_by_branch,\n" +
                    "            AVG(price_bymarket)     AS avg_price_by_market,\n" +
                    "            AVG(price_byregion)     AS avg_price_by_region,\n" +
                    "            AVG(price_bydivision)   AS avg_price_by_division\n" +
                    "        FROM morsco_edw.consolidation.contractperformance\n" +
                    "        GROUP BY 1,2\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "    pdw_catalog AS (\n" +
                    "        /*\n" +
                    "         Pulls in the desired product metadata that describes additional attributes of a product. Fetches only the\n" +
                    "         most recent value for a given metaid by using a windowing function and ranking them by when it was loaded in\n" +
                    "         descending order and using only the top ranked value.\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            pdwid                                           AS pdwid,\n" +
                    "            metaid                                          AS metaid,\n" +
                    "            metavalue                                       AS metavalue\n" +
                    "--             CASE WHEN metaid = '70024' THEN metavalue END   AS manufacturer_full_name,\n" +
                    "--             CASE WHEN metaid = '3' THEN metavalue END       AS manufacturer_part_number,\n" +
                    "--             CASE WHEN metaid = '13' THEN metavalue END      AS brand_name\n" +
                    "        FROM Morsco_edw.stage.pdwdata\n" +
                    "        WHERE\n" +
                    "            metaid IN ('70024','3', '13')\n" +
                    "        QUALIFY ROW_NUMBER() OVER(PARTITION BY pdwid, metaid ORDER BY pdwid DESC, loaddt DESC) = 1\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "    pdw_values AS (\n" +
                    "        /*\n" +
                    "         Pulls in the desired product metadata that describes additional attributes of a product. Fetches only the\n" +
                    "         most recent value for a given metaid by using a windowing function and ranking them by when it was loaded in\n" +
                    "         descending order and using only the top ranked value.\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            pdwid                                           AS pdwid,\n" +
                    "            \"'3'\"                                           AS manufacturer_part_number,\n" +
                    "            \"'13'\"                                          AS brand_name,\n" +
                    "            \"'70024'\"                                       AS manufacturer_full_name\n" +
                    "        FROM pdw_catalog\n" +
                    "        PIVOT (max(metavalue) for metaid IN ('3', '13', '70024')) as pivot_table\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "\n" +
                    "    latest_products AS (\n" +
                    "        SELECT\n" +
                    "            morsco_edw.stage.product.pdwid                                                    AS pdwid,\n" +
                    "            morsco_edw.stage.product.etlsourceid || '-' || morsco_edw.stage.product.productid AS product_id,\n" +
                    "            morsco_edw.stage.product.catalognumber                                            AS catalog_number\n" +
                    "        FROM morsco_edw.stage.product\n" +
                    "        QUALIFY ROW_NUMBER() OVER(\n" +
                    "            PARTITION BY morsco_edw.stage.product.etlsourceid,\n" +
                    "                morsco_edw.stage.product.productid\n" +
                    "            ORDER BY morsco_edw.stage.product.exportdate DESC,\n" +
                    "                morsco_edw.stage.product.loaddt DESC\n" +
                    "            ) = 1\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "    product_attributes AS (\n" +
                    "        /*\n" +
                    "         Fetches the most recent product information by using a windowing function and ranking them by when it was loaded, in\n" +
                    "         descending order, and using only the top ranked value. It calculates a product id that so that it can be joined\n" +
                    "         with the contract performance data and match it's product id format.\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            latest_products.pdwid                                           AS pdwid,\n" +
                    "            latest_products.product_id                                      AS product_id,\n" +
                    "            MAX(IFF(pdw_values.manufacturer_full_name IS NOT NULL,\n" +
                    "                pdw_values.manufacturer_full_name,\n" +
                    "                pdw_values.brand_name))                                     AS manufacturer_full_name,\n" +
                    "            MAX(IFF(pdw_values.manufacturer_part_number IS NOT NULL,\n" +
                    "                pdw_values.manufacturer_part_number,\n" +
                    "                latest_products.catalog_number))                            AS manufacturer_part_number\n" +
                    "        FROM latest_products\n" +
                    "        LEFT JOIN pdw_values\n" +
                    "            ON latest_products.pdwid = pdw_values.pdwid\n" +
                    "        GROUP BY 1,2\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "    contract_performance AS (\n" +
                    "        /*\n" +
                    "         Select columns from the contract performance view needed to calculate the special pricing.\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            morsco_edw.consolidation.contractperformance.basis                   AS basis_name,\n" +
                    "            morsco_edw.consolidation.contractperformance.basisnumber             AS basis_number,\n" +
                    "            morsco_edw.consolidation.contractperformance.customername            AS customer_name,\n" +
                    "            morsco_edw.consolidation.contractperformance.customerid              AS customer_id,\n" +
                    "            morsco_edw.consolidation.contractperformance.homebranchid            AS branch_id,\n" +
                    "            morsco_edw.consolidation.contractperformance.customerratecard        AS customer_rate_card,\n" +
                    "            morsco_edw.consolidation.contractperformance.outsidesalespersonid    AS sales_person,\n" +
                    "            morsco_edw.consolidation.contractperformance.pricelineid             AS price_line,\n" +
                    "            morsco_edw.consolidation.contractperformance.productid               AS product_id,\n" +
                    "            morsco_edw.consolidation.contractperformance.productdescription      AS product_description,\n" +
                    "            morsco_edw.consolidation.contractperformance.standardcost            AS standard_cost,\n" +
                    "            product_attributes.manufacturer_full_name                            AS manufacturer,\n" +
                    "            product_attributes.manufacturer_part_number                          AS manufacturer_part_number,\n" +
                    "            morsco_edw.consolidation.contractperformance.cmp                     AS cmp,\n" +
                    "            morsco_edw.consolidation.contractperformance.ratecardprice           AS rate_card_price,\n" +
                    "            morsco_edw.consolidation.contractperformance.calculatedprice         AS current_special_price,\n" +
                    "            morsco_edw.consolidation.contractperformance.qtysold                 AS customer_sales_qty,\n" +
                    "            morsco_edw.consolidation.contractperformance.sales                   AS customer_sales_value,\n" +
                    "            morsco_edw.consolidation.contractperformance.customercontractterritory AS customer_contract_territory\n" +
                    "        FROM morsco_edw.consolidation.contractperformance\n" +
                    "        LEFT JOIN product_attributes\n" +
                    "            ON morsco_edw.consolidation.contractperformance.productid = product_attributes.product_id\n" +
                    "        WHERE basis IS NOT NULL\n" +
                    "\n" +
                    "    ),\n" +
                    "\n" +
                    "    special_pricing AS (\n" +
                    "        /*\n" +
                    "         The columns from the `contract_performance` CTE and the\n" +
                    "         `typical_price_for_customer_type` CTE. There is a calculated\n" +
                    "         column to return the typical price, for the product, for it's customer type.\n" +
                    "         `typical_price_for_customer_type`:\n" +
                    "            There is a hierarchy of locality to the typical price. They are compared to the current special price\n" +
                    "            in the following order:\n" +
                    "                1. branch\n" +
                    "                2. market\n" +
                    "                3. region\n" +
                    "                4. division\n" +
                    "                5. rate card\n" +
                    "            If any are higher than the special price, take the sum of it and the special price and divide by 2.\n" +
                    "            If the special price is higher than no action is taken.\n" +
                    "         Filter out where basis is null or empty\n" +
                    "         */\n" +
                    "\n" +
                    "        SELECT\n" +
                    "            contract_performance.basis_name,\n" +
                    "            contract_performance.basis_number,\n" +
                    "            contract_performance.branch_id,\n" +
                    "            contract_performance.cmp,\n" +
                    "            contract_performance.current_special_price,\n" +
                    "            contract_performance.customer_id,\n" +
                    "            contract_performance.customer_name,\n" +
                    "            contract_performance.customer_rate_card,\n" +
                    "            contract_performance.customer_sales_qty,\n" +
                    "            contract_performance.customer_sales_value,\n" +
                    "            contract_performance.standard_cost,\n" +
                    "            contract_performance.manufacturer,\n" +
                    "            contract_performance.manufacturer_part_number,\n" +
                    "            contract_performance.price_line,\n" +
                    "            contract_performance.product_description,\n" +
                    "            contract_performance.product_id,\n" +
                    "            contract_performance.rate_card_price,\n" +
                    "            contract_performance.sales_person,\n" +
                    "            contract_performance.customer_contract_territory,\n"  +
                    "            typical_price_for_customer_type.avg_price_by_branch,\n" +
                    "            typical_price_for_customer_type.avg_price_by_market,\n" +
                    "            typical_price_for_customer_type.avg_price_by_region,\n" +
                    "            typical_price_for_customer_type.avg_price_by_division,\n" +
                    "            CASE\n" +
                    "                WHEN typical_price_for_customer_type.avg_price_by_branch > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN typical_price_for_customer_type.avg_price_by_branch\n" +
                    "                WHEN typical_price_for_customer_type.avg_price_by_market > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN typical_price_for_customer_type.avg_price_by_market\n" +
                    "                WHEN typical_price_for_customer_type.avg_price_by_region > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN typical_price_for_customer_type.avg_price_by_region\n" +
                    "                WHEN typical_price_for_customer_type.avg_price_by_division > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN typical_price_for_customer_type.avg_price_by_division\n" +
                    "                WHEN contract_performance.rate_card_price > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN contract_performance.rate_card_price\n" +
                    "                ELSE\n" +
                    "                    contract_performance.current_special_price\n" +
                    "            END                     AS typical_price,\n" +
                    "\n" +
                    "            CASE\n" +
                    "                WHEN ZEROIFNULL(contract_performance.current_special_price) = 0\n" +
                    "                        OR TRIM(contract_performance.current_special_price) = ''\n" +
                    "                    THEN typical_price\n" +
                    "                WHEN ZEROIFNULL(typical_price) > ZEROIFNULL(contract_performance.current_special_price)\n" +
                    "                    THEN DIV0((ZEROIFNULL(contract_performance.current_special_price) + ZEROIFNULL(typical_price)),2)\n" +
                    "                ELSE\n" +
                    "                    contract_performance.current_special_price\n" +
                    "            END                     AS recommended_special_price,\n" +
                    "            IFF(\n" +
                    "                GREATEST(\n" +
                    "                    typical_price_for_customer_type.avg_price_by_branch,\n" +
                    "                    typical_price_for_customer_type.avg_price_by_market,\n" +
                    "                    typical_price_for_customer_type.avg_price_by_region,\n" +
                    "                    typical_price_for_customer_type.avg_price_by_division,\n" +
                    "                    contract_performance.rate_card_price\n" +
                    "                ) > contract_performance.current_special_price,\n" +
                    "                'REVIEW OR APPLY RATE CARD',\n" +
                    "                'N/A'\n" +
                    "            )                        AS recommended_action\n" +
                    "        FROM contract_performance\n" +
                    "        LEFT JOIN typical_price_for_customer_type\n" +
                    "            ON contract_performance.product_id = typical_price_for_customer_type.product_id\n" +
                    "            AND contract_performance.customer_rate_card = typical_price_for_customer_type.customer_rate_card\n" +
                    "\n" +
                    "\n" +
                    "    )\n" +
                    "\n" +
                    "SELECT\n" +
                    "    /*\n" +
                    "     Final select statement to get all the desired columns\n" +
                    "     */\n" +
                    "    branch_id,\n" +
                    "    customer_contract_territory,\n" +
                    "    customer_id,\n" +
                    "    customer_name,\n" +
                    "    customer_rate_card,\n" +
                    "    sales_person,\n" +
                    "    price_line,\n" +
                    "    product_id,\n" +
                    "    manufacturer,\n" +
                    "    manufacturer_part_number,\n" +
                    "    product_description,\n" +
                    "    basis_name,\n" +
                    "    basis_number,\n" +
                    "    standard_cost,\n" +
                    "    cmp,\n" +
                    "    rate_card_price,\n" +
                    "    current_special_price,\n" +
                    "    customer_sales_qty,\n" +
                    "    customer_sales_value,\n" +
                    "    avg_price_by_branch,\n" +
                    "    avg_price_by_market,\n" +
                    "    avg_price_by_region,\n" +
                    "    avg_price_by_division,\n" +
                    "    typical_price,\n" +
                    "    recommended_special_price,\n" +
                    "    recommended_action\n" +
                    "FROM special_pricing;",
            nativeQuery = true)
    List<SnowflakeSpecialPrice> getAll();
}
