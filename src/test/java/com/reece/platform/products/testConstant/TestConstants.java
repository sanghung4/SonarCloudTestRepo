package com.reece.platform.products.testConstant;

import com.reece.platform.products.branches.model.entity.Branch;
import com.reece.platform.products.model.DTO.ProductSearchFilterDTO;
import com.reece.platform.products.model.DTO.ProductSearchRequestDTO;
import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Point;

public class TestConstants {

    public static final String AVAILABLE_PRODUCT_ID = "123";
    public static final String AVAILABLE_PRODUCT_NUMBER = "123";
    public static final String AVAILABLE_ECLIPSE_PRODUCT_NUMBER = "MSC-123";
    public static final List<String> AVAILABLE_PRODUCT_NUMBERS = Arrays.asList(
        String.valueOf(AVAILABLE_PRODUCT_NUMBER),
        String.valueOf(AVAILABLE_PRODUCT_NUMBER)
    );

    public static final int ECLIPSE_PRODUCT_ID = 321;
    public static final String UNKNOWN_PRODUCT_ID = "321";
    public static final String UNKNOWN_PRODUCT_NUMBER = "321";
    public static final String UNKNOWN_ECLIPSE_PRODUCT_NUMBER = "MSC-321";

    public static final String ERROR_PRODUCT_ID = "555";
    public static final String ERROR_PRODUCT_NUMBER = "555";
    public static final String ERROR_ECLIPSE_PRODUCT_NUMBER = "MSC-555";

    public static final String PRODUCT_FOUND_RESPONSE =
        "{\"meta\":{\"alerts\":[],\"warnings\":[],\"page\":{\"current\":1,\"total_pages\":10000,\"total_results\":10000,\"size\":1},\"engine\":{\"name\":\"products-beta\",\"type\":\"meta\"},\"request_id\":\"36e6802e-062b-4ab8-9631-db618e3d2ee3\"},\"results\":[{\"mfr_item_data_doc_file_name\":{\"raw\":\"SCHEMA_MODEL\"},\"package_width_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"cmp\":{\"raw\":\"123.45\"},\"mfr_spec_tech_doc_file_name\":{\"raw\":\"SCHEMA_MODEL\"},\"product_search_boost_1_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"capacity\":{\"raw\":\"SCHEMA_MODEL\"},\"product_search_boost_4_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"customer_number\":{\"raw\":[\"SCHEMA_MODEL\"]},\"product_overview_description\":{\"raw\":\"SCHEMA_MODEL\"},\"inlet_size\":{\"raw\":\"SCHEMA_MODEL\"},\"size\":{\"raw\":\"SCHEMA_MODEL\"},\"voltage\":{\"raw\":\"SCHEMA_MODEL\"},\"package_weight_uom_code\":{\"raw\":\"SCHEMA_MODEL\"},\"full_image_url_name\":{\"raw\":\"SCHEMA_MODEL\"},\"mfr_msds_doc_file_name\":{\"raw\":\"SCHEMA_MODEL\"},\"height\":{\"raw\":\"SCHEMA_MODEL\"},\"vendor_part_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"mfr_full_name\":{\"raw\":\"SCHEMA_MODEL\"},\"package_volume_uom_code\":{\"raw\":\"SCHEMA_MODEL\"},\"thumbnail_image_url_name\":{\"raw\":\"SCHEMA_MODEL\"},\"mfr_catalog_doc_file_name\":{\"raw\":\"SCHEMA_MODEL\"},\"low_lead_compliant_flag\":{\"raw\":\"SCHEMA_MODEL\"},\"temperature_rating\":{\"raw\":\"SCHEMA_MODEL\"},\"product_branch_exclusion\":{\"raw\":[\"SCHEMA_MODEL\"]},\"energy_star_flag\":{\"raw\":\"SCHEMA_MODEL\"},\"material\":{\"raw\":\"SCHEMA_MODEL\"},\"web_description\":{\"raw\":\"SCHEMA_MODEL\"},\"flow_rate\":{\"raw\":\"SCHEMA_MODEL\"},\"mercury_free_flag\":{\"raw\":\"SCHEMA_MODEL\"},\"product_search_boost_3_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"in_stock_location\":{\"raw\":[\"SCHEMA_MODEL_1\", \"SCHEMA_MODEL_2\"]},\"tonnage\":{\"raw\":\"SCHEMA_MODEL\"},\"feature_benefit_list_text\":{\"raw\":\"SCHEMA_MODEL\"},\"package_height_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"hazardous_material_flag\":{\"raw\":\"SCHEMA_MODEL\"},\"_meta\":{\"id\":\"SCHEMA_MODEL\",\"engine\":\"products-beta-1632769203463\",\"score\":19.97112},\"id\":{\"raw\":\"products-beta-1632769203463|SCHEMA_MODEL\"},\"btu\":{\"raw\":\"SCHEMA_MODEL\"},\"water_sense_compliant_flag\":{\"raw\":\"SCHEMA_MODEL\"},\"internal_item_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"last_update_date\":{\"raw\":\"SCHEMA_MODEL\"},\"product_search_boost_2_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"erp_product_id\":{\"raw\":\"MSC-" +
        ECLIPSE_PRODUCT_ID +
        "\"},\"colorfinish\":{\"raw\":\"SCHEMA_MODEL\"},\"product_sold_count\":{\"raw\":\"0\"},\"pressure_rating\":{\"raw\":\"SCHEMA_MODEL\"},\"depth\":{\"raw\":\"SCHEMA_MODEL\"},\"category_3_name\":{\"raw\":\"SCHEMA_MODEL\"},\"wattage\":{\"raw\":\"SCHEMA_MODEL\"},\"category_1_name\":{\"raw\":\"SCHEMA_MODEL\"},\"category_2_name\":{\"raw\":\"SCHEMA_MODEL\"},\"search_keyword_text\":{\"raw\":\"SCHEMA_MODEL\"},\"upc_id\":{\"raw\":\"SCHEMA_MODEL\"},\"width\":{\"raw\":\"SCHEMA_MODEL\"},\"package_volume_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"package_length_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"package_weight_nbr\":{\"raw\":\"SCHEMA_MODEL\"},\"length\":{\"raw\":\"SCHEMA_MODEL\"},\"minimum_increment_qty\":{\"raw\":\"5\"},\"medium_image_url_name\":{\"raw\":\"SCHEMA_MODEL\"},\"product_line\":{\"raw\":\"SCHEMA_MODEL\"},\"technical_specifications\":{\"raw\":[\"{\\\"applicable_standard\\\":\\\"SCHEMA_MODEL\\\",\\\"flow_rate\\\":\\\"SCHEMA_MODEL\\\",\\\"inclusions/features\\\":\\\"SCHEMA_MODEL\\\",\\\"inlet_connection\\\":\\\"SCHEMA_MODEL\\\",\\\"inlet_size\\\":\\\"SCHEMA_MODEL\\\",\\\"outlet_connection\\\":\\\"SCHEMA_MODEL\\\",\\\"type\\\":\\\"SCHEMA_MODEL\\\",\\\"used_on_item\\\":\\\"SCHEMA_MODEL\\\",\\\"used_on_model/brand\\\":\\\"SCHEMA_MODEL\\\"}\"]},\"mfr_install_instruction_doc_file_name\":{\"raw\":\"SCHEMA_MODEL\"},\"customer_part_number\":{\"raw\":[\"SCHEMA_MODEL\"]},\"unspc_id\":{\"raw\":\"SCHEMA_MODEL\"}}],\"facets\":{\"product_line\":[{\"type\":\"value\",\"data\":[{\"value\":\"KOHLER\",\"count\":1048},{\"value\":\"DELTA\",\"count\":604},{\"value\":\"XIRTEC 140\",\"count\":222},{\"value\":\"OAT\",\"count\":160},{\"value\":\"BRIZO\",\"count\":144},{\"value\":\"Comfortmaker\",\"count\":108},{\"value\":\"Chemtrol\",\"count\":98},{\"value\":\"Corzan\",\"count\":72},{\"value\":\"Chemtrol, Corzan\",\"count\":57},{\"value\":\"Corzan, Chemtrol\",\"count\":49}]}],\"mfr_full_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"Kohler\",\"count\":1075},{\"value\":\"Spears\",\"count\":642},{\"value\":\"Merit Brass\",\"count\":627},{\"value\":\"International Comfort Products\",\"count\":572},{\"value\":\"Delta\",\"count\":556},{\"value\":\"Generic\",\"count\":464},{\"value\":\"Nibco\",\"count\":344},{\"value\":\"IPEX\",\"count\":332},{\"value\":\"A.O. Smith\",\"count\":320},{\"value\":\"Moen\",\"count\":309}]}],\"category_1_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"Faucets, Fixtures & Appliances\",\"count\":3365},{\"value\":\"Pipe & Fittings\",\"count\":2793},{\"value\":\"Plumbing Installation, Tools, Hardware & Safety\",\"count\":1446},{\"value\":\"Heating & Cooling\",\"count\":1112},{\"value\":\"Hot Water, Valves, Irrigation & Pumps\",\"count\":809},{\"value\":\"TBC\",\"count\":164},{\"value\":\"PVF\",\"count\":10},{\"value\":\"Plumbing Faucets and Fixtures\",\"count\":10},{\"value\":\"Water Heaters, Filtration and Tanks\",\"count\":8},{\"value\":\"HVAC\",\"count\":6}]}],\"category_2_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"TBC\",\"count\":1291},{\"value\":\"Plumbing Installation\",\"count\":868},{\"value\":\"Metal Pipe & Fittings\",\"count\":830},{\"value\":\"Plastic Pipe & Fittings - Drainage\",\"count\":767},{\"value\":\"Plastic Pipe & Fittings - Supply\",\"count\":733},{\"value\":\"Faucets, Showers & Bathroom Accessories\",\"count\":592},{\"value\":\"Repair & Replacement Parts\",\"count\":519},{\"value\":\"Tools\",\"count\":232},{\"value\":\"Residential Equipment\",\"count\":160},{\"value\":\"Appliances\",\"count\":151}]}],\"category_3_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"TBC\",\"count\":2729},{\"value\":\"PVC Pipe & Fittings\",\"count\":752},{\"value\":\"Steel Pipe & Fittings\",\"count\":605},{\"value\":\"CPVC Pipe & Fittings\",\"count\":392},{\"value\":\"PEX Pipe & Fittings\",\"count\":341},{\"value\":\"Brass Pipe & Fittings\",\"count\":150},{\"value\":\"Spare Parts\",\"count\":149},{\"value\":\"Showers\",\"count\":143},{\"value\":\"Bathroom Accessories\",\"count\":123},{\"value\":\"Tub & Shower\",\"count\":100}]}]}}";

    public static final String NO_PRODUCT_FOUND_RESPONSE =
        "{\n" +
        "  \"meta\": {\n" +
        "    \"alerts\": [],\n" +
        "    \"warnings\": [],\n" +
        "    \"page\": {\n" +
        "      \"current\": 1,\n" +
        "      \"total_pages\": 0,\n" +
        "      \"total_results\": 0,\n" +
        "      \"size\": 1\n" +
        "    },\n" +
        "    \"engine\": {\n" +
        "      \"name\": \"ecomm-products\",\n" +
        "      \"type\": \"meta\"\n" +
        "    },\n" +
        "    \"request_id\": \"ab6f8bb9-9315-4fad-bbde-8cf9b6e2254e\"\n" +
        "  },\n" +
        "  \"results\": [],\n" +
        "  \"facets\": {\n" +
        "    \"product_type\": [\n" +
        "      {\n" +
        "        \"type\": \"value\",\n" +
        "        \"data\": []\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}";

    public static ProductSearchRequestDTO CreateProductSearchRequestDTO() {
        List<ProductSearchFilterDTO> filter = Arrays.asList(
            new ProductSearchFilterDTO("productType", "test"),
            new ProductSearchFilterDTO("productType", "Test 2")
        );
        ProductSearchRequestDTO requestDTO = new ProductSearchRequestDTO(
            "test",
            0,
            1,
            "",
            filter,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        return requestDTO;
    }

    //
    public static ProductSearchRequestDTO CreateProductSearchRequestDTOWithException() {
        ProductSearchRequestDTO requestDTO = new ProductSearchRequestDTO(
            "error",
            0,
            12,
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        return requestDTO;
    }

    public static final String BRANCH_ID_ONE = "1004";
    public static final String BRANCH_NAME_ONE = "Fortiline Waterworks";
    public static final String BRANCH_ADDRESS_ONE = "123 Test Ln";
    public static final String BRANCH_CITY_ONE = "Dallas";
    public static final String BRANCH_STATE_ONE = "TX";
    public static final String BRANCH_ZIP_ONE = "75201";
    public static final String BRANCH_BUSINESS_HOURS_ONE = "M-F: 8-5pm";
    public static final String BRANCH_MANAGER_EMAIL_ONE = "branchmanager@reece.com";
    public static final Boolean BRANCH_IS_WATERWORKS_ONE = true;
    public static final Boolean BRANCH_IS_PLUMBING_ONE = false;

    public static final String BRANCH_ID_TWO = "1005";
    public static final String BRANCH_NAME_TWO = "Eclipse Branch";
    public static final String BRANCH_ADDRESS_TWO = "123 Test Ln";
    public static final String BRANCH_CITY_TWO = "Dallas";
    public static final String BRANCH_STATE_TWO = "TX";
    public static final String BRANCH_ZIP_TWO = "75201";
    public static final String BRANCH_BUSINESS_HOURS_TWO = "M-F: 8-5pm";
    public static final String BRANCH_MANAGER_EMAIL_TWO = "branchmanager@reece.com";
    public static final Boolean BRANCH_IS_WATERWORKS_TWO = false;
    public static final Boolean BRANCH_IS_PLUMBING_TWO = true;

    public static final String BRANCH_ID_THREE = "1006";
    public static final String BRANCH_NAME_THREE = "Todd Pipe - ANAHEIM";
    public static final String BRANCH_ADDRESS_THREE = "123 Test Ln";
    public static final String BRANCH_CITY_THREE = "Addison";
    public static final String BRANCH_STATE_THREE = "TX";
    public static final String BRANCH_ZIP_THREE = "75001";
    public static final String BRANCH_BUSINESS_HOURS_THREE = "M-F: 8-5pm";
    public static final String BRANCH_MANAGER_EMAIL_THREE = "branchmanager@reece.com";
    public static final Boolean BRANCH_IS_WATERWORKS_THREE = false;
    public static final Boolean BRANCH_IS_PLUMBING_THREE = true;

    public static List<Branch> CreatePDWBranches() {
        var branchOne = new Branch(
            BRANCH_ID_ONE,
            BRANCH_NAME_ONE,
            "entityId",
            BRANCH_ADDRESS_ONE,
            "",
            BRANCH_CITY_ONE,
            BRANCH_STATE_ONE,
            BRANCH_ZIP_ONE,
            "phone",
            "zip",
            "wordayId",
            "workdayLocation",
            "rvp",
            BRANCH_IS_PLUMBING_ONE,
            BRANCH_IS_WATERWORKS_ONE,
            false,
            false,
            "branchManager",
            "branchManagerPhone",
            BRANCH_MANAGER_EMAIL_ONE,
            BRANCH_BUSINESS_HOURS_ONE
        );

        var branchTwo = new Branch(
            BRANCH_ID_TWO,
            BRANCH_NAME_TWO,
            "entityId",
            BRANCH_ADDRESS_TWO,
            "",
            BRANCH_CITY_ONE,
            BRANCH_STATE_TWO,
            BRANCH_ZIP_TWO,
            "phone",
            "zip",
            "wordayId",
            "workdayLocation",
            "rvp",
            BRANCH_IS_PLUMBING_TWO,
            BRANCH_IS_WATERWORKS_TWO,
            false,
            false,
            "branchManager",
            "branchManagerPhone",
            BRANCH_MANAGER_EMAIL_TWO,
            BRANCH_BUSINESS_HOURS_TWO
        );

        var branchThree = new Branch(
            BRANCH_ID_THREE,
            BRANCH_NAME_THREE,
            "entityId",
            BRANCH_ADDRESS_THREE,
            "",
            BRANCH_CITY_THREE,
            BRANCH_STATE_THREE,
            BRANCH_ZIP_THREE,
            "phone",
            "zip",
            "wordayId",
            "workdayLocation",
            "rvp",
            BRANCH_IS_PLUMBING_THREE,
            BRANCH_IS_WATERWORKS_THREE,
            false,
            false,
            "branchManager",
            "branchManagerPhone",
            BRANCH_MANAGER_EMAIL_THREE,
            BRANCH_BUSINESS_HOURS_THREE
        );

        return List.of(branchOne, branchTwo, branchThree);
    }

    public static List<Branch> CreateBranches(Point location) {
        var branchOne = new Branch(
            BRANCH_ID_ONE,
            BRANCH_NAME_ONE,
            BRANCH_ADDRESS_ONE,
            BRANCH_CITY_ONE,
            BRANCH_STATE_ONE,
            BRANCH_ZIP_ONE,
            BRANCH_BUSINESS_HOURS_ONE,
            BRANCH_MANAGER_EMAIL_ONE,
            location
        );

        var branchTwo = new Branch(
            BRANCH_ID_TWO,
            BRANCH_NAME_TWO,
            BRANCH_ADDRESS_TWO,
            BRANCH_CITY_TWO,
            BRANCH_STATE_TWO,
            BRANCH_ZIP_TWO,
            BRANCH_BUSINESS_HOURS_TWO,
            BRANCH_MANAGER_EMAIL_TWO,
            location
        );

        var branchThree = new Branch(
            BRANCH_ID_THREE,
            BRANCH_NAME_THREE,
            BRANCH_ADDRESS_THREE,
            BRANCH_CITY_THREE,
            BRANCH_STATE_THREE,
            BRANCH_ZIP_THREE,
            BRANCH_BUSINESS_HOURS_THREE,
            BRANCH_MANAGER_EMAIL_THREE,
            location
        );

        return List.of(branchOne, branchTwo, branchThree);
    }
}
