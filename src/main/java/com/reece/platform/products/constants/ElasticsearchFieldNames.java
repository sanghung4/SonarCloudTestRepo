package com.reece.platform.products.constants;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class ElasticsearchFieldNames {

    public Hashtable<String, String> AttributeTypeNames;

    // Categories
    public static final String CATEGORY_1_NAME = "category_1_name";
    public static final String CATEGORY_2_NAME = "category_2_name";
    public static final String CATEGORY_3_NAME = "category_3_name";

    // Technical documents
    public static final String MFR_CATALOG_DOC_FILE_NAME = "mfr_catalog_doc_file_name";
    public static final String MFR_SPEC_TECH_DOC_FILE_NAME = "mfr_spec_tech_doc_file_name";
    public static final String MFR_MSDS_DOC_FILE_NAME = "mfr_msds_doc_file_name";
    public static final String MFR_INSTALL_INSTRUCTION_DOC_FILE_NAME = "mfr_install_instruction_doc_file_name";
    public static final String MFR_ITEM_DATA_DOC_FILE_NAME = "mfr_item_data_doc_file_name";
    public static final String MFR_CATALOG_PAGE = "MFR Catalog Page";
    public static final String TECHNICAL_SPECIFICATION = "Technical Specification";
    public static final String SDS_SHEET = "SDS_SHEET";
    public static final String INSTALLATION_SHEET = "Installation Sheet";
    public static final String MFR_ITEM_DATA = "MFR Item Data";

    // Environmental options
    public static final String LOW_LEAD_COMPLIANT_FLAG = "low_lead_compliant_flag";
    public static final String LOW_LEAD_COMPLIANT = "Low lead compliant";
    public static final String MERCURY_FREE_FLAG = "mercury_free_flag";
    public static final String MERCURY_FREE = "Mercury free";
    public static final String WATER_SENSE_COMPLIANT_FLAG = "water_sense_compliant_flag";
    public static final String WATERSENSE_COMPLIANT = "WaterSense compliant";
    public static final String ENERGY_STAR_FLAG = "energy_star_flag";
    public static final String ENERGY_STAR = "Energy Star";
    public static final String HAZARDOUS_MATERIAL_FLAG = "hazardous_material_flag";
    public static final String HAZARDS_MATERIAL = "Hazardous material";

    // Package dimensions
    public static final String PACKAGE_WEIGHT_NBR = "package_weight_nbr";
    public static final String PACKAGE_WIDTH_NBR = "package_width_nbr";
    public static final String PACKAGE_LENGTH_NBR = "package_length_nbr";
    public static final String PACKAGE_HEIGHT_NBR = "package_length_nbr";
    public static final String PACKAGE_VOLUME_NBR = "package_volume_nbr";
    public static final String PACKAGE_VOLUME_UOM_CODE = "package_volume_uom_code";
    public static final String PACKAGE_WEIGHT_UOM_CODE = "package_weight_uom_code";

    // Product details
    public static final String PRODUCT_OVERVIEW_DESCRIPTION = "product_overview_description";
    public static final String UPC_ID = "upc_id";
    public static final String FEATURE_BENEFIT_LIST_TEXT = "feature_benefit_list_text";
    public static final String MINCRON_PRODUCT_NUMBER = "mincron_product_number";
    public static final String MINIMUM_INCREMENT_QTY = "minimum_increment_qty";
    public static final String MFR_FULL_NAME = "mfr_full_name";
    public static final String ERP_PRODUCT_ID = "erp_product_id";
    public static final String INTERNAL_ITEM_NBR = "internal_item_nbr";
    public static final String LAST_UPDATE_DATE = "last_update_date";
    public static final String PRODUCT_LINE = "product_line";
    public static final String PRODUCT_SOLD_COUNT = "product_sold_count";
    public static final String UNSPC_ID = "unspc_id";
    public static final String VENDOR_PART_NBR = "vendor_part_nbr";
    public static final String WEB_DESCRIPTION = "web_description";
    public static final String CUSTOMER_NUMBER = "customer_number";
    public static final String CUSTOMER_PART_NUMBER = "customer_part_number";
    public static final String IN_STOCK_LOCATION = "in_stock_location";
    public static final String PRODUCT_BRANCH_EXCLUSION = "product_branch_exclusion";
    public static final String SEARCH_KEYWORD_TEXT = "search_keyword_text";
    public static final String BRAND = "brand";
    public static final String TERRITORY_EXCLUSION_LIST = "territory_exclusion_list";
    public static final String CMP = "cmp";

    // Technical Specifications
    public static final String TECHNICAL_SPECIFICATIONS = "technical_specifications";
    public static final String FLOW_RATE = "flow_rate";
    public static final String BTU = "btu";
    public static final String CAPACITY = "capacity";
    public static final String COLORFINISH = "colorfinish";
    public static final String CLEAN_WEB_DESCRIPTION = "clean_web_description";
    public static final String CLEAN_VENDOR_PART_NBR = "clean_vendor_part_nbr";
    public static final String CLEAN_PRODUCT_BRAND = "clean_product_brand";
    public static final String DEPTH = "depth";
    public static final String HEIGHT = "height";
    public static final String INLET_SIZE = "inlet_size";
    public static final String LENGTH = "length";
    public static final String MATERIAL = "material";
    public static final String PRESSURE_RATING = "pressure_rating";
    public static final String SIZE = "size";
    public static final String TEMPERATURE_RATING = "temperature_rating";
    public static final String TONNAGE = "tonnage";
    public static final String VOLTAGE = "voltage";
    public static final String WATTAGE = "wattage";
    public static final String WIDTH = "width";
    public static final List<String> TECHNICAL_SPECIFICATIONS_NAMES = Arrays.asList(
        BTU,
        CAPACITY,
        COLORFINISH,
        DEPTH,
        FLOW_RATE,
        HEIGHT,
        INLET_SIZE,
        LENGTH,
        MATERIAL,
        PRESSURE_RATING,
        SIZE,
        TEMPERATURE_RATING,
        VOLTAGE,
        WATTAGE,
        WIDTH,
        TONNAGE
    );

    // Search boost
    public static final String PRODUCT_SEARCH_BOOST_1_NBR = "product_search_boost_1_nbr";
    public static final String PRODUCT_SEARCH_BOOST_2_NBR = "product_search_boost_2_nbr";
    public static final String PRODUCT_SEARCH_BOOST_3_NBR = "product_search_boost_3_nbr";
    public static final String PRODUCT_SEARCH_BOOST_4_NBR = "product_search_boost_4_nbr";
    public static final List<String> SEARCH_BOOST_PROPERTY_LIST = Arrays.asList(
        PRODUCT_SEARCH_BOOST_1_NBR,
        PRODUCT_SEARCH_BOOST_2_NBR,
        PRODUCT_SEARCH_BOOST_3_NBR,
        PRODUCT_SEARCH_BOOST_4_NBR
    );

    // Image urls
    public static final String FULL_IMAGE_URL_NAME = "full_image_url_name";
    public static final String MEDIUM_IMAGE_URL_NAME = "medium_image_url_name";
    public static final String THUMBNAIL_IMAGE_URL_NAME = "thumbnail_image_url_name";

    // Elasticsearch constants
    public static final String VALUE = "value";
    public static final int MAX_PAGES = 100;

    // App search constants
    public static final String META = "meta";
    public static final String PAGE = "page";
    public static final String TOTAL_RESULTS = "total_results";
    public static final String RESULTS = "results";
    public static final String RAW = "raw";
    public static final String COUNT = "count";
    public static final String DATA = "data";
    public static final String QUERY = "query";
    public static final String FILTERS = "filters";
    public static final String ID = "id";
    public static final String DOCUMENTS = "documents";
    public static final String TYPES = "types";
    public static final String TYPE = "type";
    public static final String FIELDS = "fields";
    public static final String FACETS = "facets";
    public static final String RESULT_FIELDS = "result_fields";
    public static final String CURRENT = "current";
    public static final String ALL = "all";
    public static final String NONE = "none";
    public static final String MATCH = "match";
    public static final String SHOULD = "should";
    public static final String BOOL = "bool";
    public static final String SUGGESTION = "suggestion";
    public static final String SEARCH_FIELDS = "search_fields";

    public static final Iterable<String> SEARCH_FIELD_NAMES = List.of(
        CLEAN_WEB_DESCRIPTION,
        CLEAN_PRODUCT_BRAND,
        CLEAN_VENDOR_PART_NBR,
        VENDOR_PART_NBR,
        WEB_DESCRIPTION,
        CUSTOMER_PART_NUMBER,
        MFR_FULL_NAME
    );

    // product data quality checking
    public static final String NULL_CHARACTERS_SNOWFLAKE = "\\\\N";
    public static final String NULL_CHARACTERS_MINCRON = "\u0000";

    public static final String ECLIPSE = "ECLIPSE";
    public static final String MINCRON = "MINCRON";

    public ElasticsearchFieldNames() {
        this.AttributeTypeNames = new Hashtable<>();
        this.AttributeTypeNames.put("brand", MFR_FULL_NAME);
        this.AttributeTypeNames.put("line", PRODUCT_LINE);
        this.AttributeTypeNames.put("category1", CATEGORY_1_NAME);
        this.AttributeTypeNames.put("category2", CATEGORY_2_NAME);
        this.AttributeTypeNames.put("category3", CATEGORY_3_NAME);
        this.AttributeTypeNames.put("flowRate", FLOW_RATE);
        this.AttributeTypeNames.put(ENERGY_STAR, ENERGY_STAR_FLAG);
        this.AttributeTypeNames.put(HAZARDS_MATERIAL, HAZARDOUS_MATERIAL_FLAG);
        this.AttributeTypeNames.put(MERCURY_FREE, MERCURY_FREE_FLAG);
        this.AttributeTypeNames.put(LOW_LEAD_COMPLIANT, LOW_LEAD_COMPLIANT_FLAG);
        this.AttributeTypeNames.put(WATERSENSE_COMPLIANT, WATER_SENSE_COMPLIANT_FLAG);
        this.AttributeTypeNames.put("inStockLocation", IN_STOCK_LOCATION);
        this.AttributeTypeNames.put("material", MATERIAL);
        this.AttributeTypeNames.put("colorFinish", COLORFINISH);
        this.AttributeTypeNames.put("size", SIZE);
        this.AttributeTypeNames.put("length", LENGTH);
        this.AttributeTypeNames.put("width", WIDTH);
        this.AttributeTypeNames.put("height", HEIGHT);
        this.AttributeTypeNames.put("depth", DEPTH);
        this.AttributeTypeNames.put("voltage", VOLTAGE);
        this.AttributeTypeNames.put("tonnage", TONNAGE);
        this.AttributeTypeNames.put("btu", BTU);
        this.AttributeTypeNames.put("pressureRating", PRESSURE_RATING);
        this.AttributeTypeNames.put("temperatureRating", TEMPERATURE_RATING);
        this.AttributeTypeNames.put("inletSize", INLET_SIZE);
        this.AttributeTypeNames.put("capacity", CAPACITY);
        this.AttributeTypeNames.put("wattage", WATTAGE);
        this.AttributeTypeNames.put("territoryExclusionList", TERRITORY_EXCLUSION_LIST);
    }

    //Product Categories order defined by business
    public static final String PIPE_AND_FITTINGS = "Pipe & Fittings";
    public static final String HEATING_AND_COOLING = "Heating & Cooling";
    public static final String BATH = "Bath";
    public static final String KITCHEN_BAR_LAUNDRY = "Kitchen, Bar & Laundry";
    public static final String APPLIANCES = "Appliances";
    public static final String PLUMBING_INSTALLATION_AND_HARDWARE = "Plumbing Installation & Hardware";
    public static final String WATER_HEATERS = "Water Heaters";
    public static final String VALVES = "Valves";
    public static final String IRRIGATION_PUMPS_FILTERS = "Irrigation, Pumps & Filters";
    public static final String TOOLS_AND_SAFETY = "Tools & Safety";

    public static final String CUSTOMER_PART_NUMBERS = "customer_part_numbers";
}
