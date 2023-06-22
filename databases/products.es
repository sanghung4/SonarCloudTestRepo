GET products/_mapping
DELETE products
PUT products
PUT products/_mapping
{
  "dynamic_templates": [
          {
            "text_as_keywords": {
              "match_mapping_type": "string",
              "match": "*text*",
              "mapping": {
                "type": "keyword"
              }
            }
          },
          {
            "correct_text_as_keywords": {
              "match_mapping_type": "string",
              "match": "*correct_text*",
              "mapping": {
                "type": "keyword"
              }
            }
          },
          {
            "other_info_value_as_float": {
              "match_mapping_type": "long",
              "match": "*other_info.value*",
              "mapping": {
                "type": "double"
              }
            }
          }
        ],
      "properties" : {
        "INTERNAL_ITEM_NBR" : { "type": "keyword"},
        "ID" : { "type": "keyword"},
        "ERP_PRODUCT_ID" : { "type": "keyword"},
        "VENDOR_PART_NBR" : { "type": "keyword"},
        "UPC_ID" : { "type": "keyword"},
        "UNSPC_ID" : { "type": "keyword"},
        "CMP": { "type": "float"},
        "SEARCH_KEYWORD_TEXT" : { "type": "text"},
        "CATEGORY_1_NAME" : { "type": "keyword"},
        "CATEGORY_2_NAME" : { "type": "keyword"},
        "CATEGORY_3_NAME" : { "type": "keyword"},
        "BUYLINEID" : { "type": "keyword"},
        "WEB_DESCRIPTION" : { "type": "text"},
        "PRODUCT_OVERVIEW_DESCRIPTION" : { "type": "text"},
        "MFR_FULL_NAME" : { "type": "keyword"},
        "FEATURE_BENEFIT_LIST_TEXT" : { "type": "keyword"},
        "THUMBNAIL_IMAGE_URL_NAME" : { "type": "keyword"},
        "MEDIUM_IMAGE_URL_NAME" : { "type": "keyword"},
        "FULL_IMAGE_URL_NAME" : { "type": "keyword"},
        "MFR_CATALOG_DOC_FILE_NAME" : { "type": "keyword"},
        "MFR_SPEC_TECH_DOC_FILE_NAME" : { "type": "keyword"},
        "MFR_MSDS_DOC_FILE_NAME" : { "type": "keyword"},
        "MFR_INSTALL_INSTRUCTION_DOC_FILE_NAME" : { "type": "keyword"},
        "MFR_ITEM_DATA_DOC_FILE_NAME" : { "type": "keyword"},
        "LOW_LEAD_COMPLIANT_FLAG" : { "type": "boolean"},
        "MERCURY_FREE_FLAG" : { "type": "boolean"},
        "WATER_SENSE_COMPLIANT_FLAG" : { "type": "boolean"},
        "ENERGY_STAR_FLAG" : { "type": "boolean"},
        "HAZARDOUS_MATERIAL_FLAG" : { "type": "boolean"},
        "PACKAGE_WEIGHT_NBR" : { "type": "keyword"},
        "PACKAGE_WIDTH_NBR" : { "type": "keyword"},
        "PACKAGE_LENGTH_NBR" : { "type": "keyword"},
        "PACKAGE_HEIGHT_NBR" : { "type": "keyword"},
        "PACKAGE_VOLUME_NBR" : { "type": "keyword"},
        "PACKAGE_VOLUME_UOM_CODE" : { "type": "keyword"},
        "PACKAGE_WEIGHT_UOM_CODE" : { "type": "keyword"},
        "MINIMUM_INCREMENT_QTY" : { "type": "text"},
        "PRODUCT_SEARCH_BOOST_1_NBR" : { "type": "text"},
        "PRODUCT_SEARCH_BOOST_2_NBR" : { "type": "text"},
        "PRODUCT_SEARCH_BOOST_3_NBR" : { "type": "text"},
        "PRODUCT_SEARCH_BOOST_4_NBR" : { "type": "text"},
        "PRODUCT_LINE" : { "type": "keyword"},
        "MATERIAL" : { "type": "keyword"},
        "COLOR_FINISH" : { "type": "keyword"},
        "SIZE" : { "type": "text"},
        "LENGTH" : { "type": "text"},
        "WIDTH" : { "type": "text"},
        "HEIGHT" : { "type": "text"},
        "DEPTH" : { "type": "text"},
        "VOLTAGE" : { "type": "text"},
        "TONNAGE" : { "type": "text"},
        "BTU" : { "type": "text"},
        "PRESSURE_RATING" : { "type": "text"},
        "TEMPERATURE_RATING" : { "type": "text"},
        "INLET_SIZE" : { "type": "text"},
        "FLOW_RATE" : { "type": "text"},
        "CAPACITY" : { "type": "text"},
        "WATTAGE" : { "type": "text"},
        "PRODUCT_SOLD_COUNT" : { "type": "integer"},
        "CUSTOMER_NUMBER_LIST" : { "type": "text"},
        "CUSTOMER_PART_NUMBERS": { "type": "keyword"},
        "CUSTOMER_PART_NUMBER_LIST" : { "type": "text"},
        "TECH_SPECS_LIST" : { "type": "flattened"},
        "IN_STOCK_LOCATION_LIST" : { "type": "keyword"},
        "PRODUCT_BRANCH_EXCLUSION" : { "type": "text"},
        "TERRITORY_EXCLUSION_LIST" : { "type": "text"},
        "LAST_UPDATE_DATE" : { "type": "date"},
        "_ANGLE" : { "type": "nested"},
        "_BEND" : { "type": "flattened"},
        "_CAPACITANCE" : { "type": "nested"},
        "_COLOR" : { "type": "flattened"},
        "_CONNECTION" : { "type": "flattened"},
        "_CURRENT" : { "type": "nested"},
        "_DIMENSIONS" : { "type": "nested"},
        "_FEATURE_COUNT" : { "type": "nested"},
        "_FLOW" : { "type": "nested"},
        "_FREQUENCY" : { "type": "nested"},
        "_GAUGE" : { "type": "nested"},
        "_HEAT" : { "type": "nested"},
        "_INFO" : { "type": "nested"},
        "_MANUFACTURER" : { "type": "flattened"},
        "_MATERIAL" : { "type": "flattened"},
        "_MODEL" : { "type": "flattened"},
        "_OTHER" : { "type": "flattened"},
        "_PART" : { "type": "flattened"},
        "_POWER" : { "type": "nested"},
        "_PRESSURE_WGT" : { "type": "nested"},
        "_PROCESS" : { "type": "flattened"},
        "_QUANTITY" : { "type": "nested"},
        "_SCHEDULE" : { "type": "flattened"},
        "_TEMPERATURE" : { "type": "nested"},
        "_THREAD" : { "type": "flattened"},
        "_TIME" : { "type": "flattened"},
        "_TYPE" : { "type": "flattened"},
        "_VOLTAGE" : { "type": "nested"},
        "_VOLUME" : { "type": "nested"},
        "_MANUFACTURER_SCORES" : { "type": "flattened"},
        "_MATERIAL_SCORES" : { "type": "flattened"},
        "_PART_SCORES" : { "properties":
          {
            "scores": { "type": "flattened"},
            "scores_normalized": { "type": "flattened"},
            "probable_value": { "type": "nested"}
          }
        },
        "record_created" : { "type": "date"},
        "application_version" : { "type": "keyword"},
        "sales_info": {"properties": {
            "product_sales": { "type": "integer"},
            "category_sales": { "type": "integer"},
            "product_qty": { "type": "integer"},
            "category_qty": { "type": "integer"},
            "product_sales_pctrank": { "type": "float"},
            "category_sales_pctrank": { "type": "float"},
            "product_sales_pctlog": { "type": "float"},
            "category_sales_pctlog": { "type": "float"},
            "product_qty_pctrank": { "type": "float"},
            "category_qty_pctrank": { "type": "float"},
            "product_qty_pctlog": { "type": "float"},
            "category_qty_pctlog": { "type": "float"},
            "product_invoiced_pctrank": { "type": "float"},
            "category_invoiced_pctrank": { "type": "float"},
            "product_invoiced_pctlog": { "type": "float"},
            "category_invoiced_pctlog": { "type": "float"}
          }
        }
      }
    }
}
PUT products/_settings
{
  "index": {
    "max_result_window": 10000000
  }
}
PUT /products/_settings
{
  "settings": {
    "index.blocks.write": true
  }
}
POST /products/_clone/products_bak
