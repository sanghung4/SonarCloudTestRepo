package com.reece.platform.products.service;

import static com.reece.platform.products.constants.ElasticsearchFieldNames.*;
import static com.reece.platform.products.testConstant.TestConstants.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.reece.platform.products.branches.model.DTO.BranchWithDistanceResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.BranchServiceCoreException;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.exceptions.ProductNotFoundException;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.MassProductInquiryResponse;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.AvailabilityList;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.BranchAvailability;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.NowQuantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Pricing.Pricing;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Pricing.PricingBranch;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.ProductList;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.eclipse.common.Branch;
import com.reece.platform.products.model.entity.ApprovalFlowState;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.repository.ApprovalFlowStateDAO;
import com.reece.platform.products.model.repository.CartDAO;
import com.reece.platform.products.model.repository.CategoriesDAO;
import com.reece.platform.products.model.repository.ListLineItemsDAO;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import java.util.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private ProductService productService;

    private ErpUserInformation erpUserInformation;

    private CategoriesDAO categoriesDAO;
    private CartDAO cartDAO;
    private ApprovalFlowStateDAO approvalFlowStateDAO;
    private ListLineItemsDAO listLineItemsDAO;

    private DataWarehouseRepository dataWarehouseRepository;

    private static final UUID ACTIVE_APPROVAL_STATE = UUID.randomUUID();
    private static final String BRANCH_ID = "123";
    private static final ArrayNode ARRAY_NODE = new ArrayNode(JsonNodeFactory.instance);

    @BeforeEach
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode productIDS = mapper.createArrayNode();
        AVAILABLE_PRODUCT_NUMBERS
            .stream()
            .forEach(productId -> {
                productIDS.add(String.format("MSC-%s", productId));
            });

        ElasticsearchService elasticsearchService = mock(ElasticsearchService.class);
        BranchesService branchesService = mock(BranchesService.class);
        ErpService erpService = mock(ErpService.class);
        categoriesDAO = mock(CategoriesDAO.class);
        cartDAO = mock(CartDAO.class);
        approvalFlowStateDAO = mock(ApprovalFlowStateDAO.class);
        listLineItemsDAO = mock(ListLineItemsDAO.class);
        dataWarehouseRepository = mock(DataWarehouseRepository.class);
        when(elasticsearchService.getProductById(String.valueOf(AVAILABLE_PRODUCT_ID)))
            .thenReturn(PRODUCT_FOUND_RESPONSE);
        when(elasticsearchService.getProductByMincronNumber(String.valueOf(AVAILABLE_PRODUCT_NUMBER)))
            .thenReturn(PRODUCT_FOUND_RESPONSE);
        when(elasticsearchService.getProductByEclipseNumber(String.valueOf(AVAILABLE_ECLIPSE_PRODUCT_NUMBER)))
            .thenReturn(PRODUCT_FOUND_RESPONSE);
        when(elasticsearchService.getProductByEclipseNumberArray(productIDS)).thenReturn(PRODUCT_FOUND_RESPONSE);

        when(elasticsearchService.getProductById(String.valueOf(UNKNOWN_PRODUCT_ID)))
            .thenReturn(NO_PRODUCT_FOUND_RESPONSE);
        when(elasticsearchService.getProductByMincronNumber(String.valueOf(UNKNOWN_PRODUCT_NUMBER)))
            .thenReturn(NO_PRODUCT_FOUND_RESPONSE);
        when(elasticsearchService.getProductByEclipseNumber(String.valueOf(UNKNOWN_ECLIPSE_PRODUCT_NUMBER)))
            .thenReturn(NO_PRODUCT_FOUND_RESPONSE);

        erpUserInformation = new ErpUserInformation();
        erpUserInformation.setPassword("pass");
        erpUserInformation.setUserId("user");
        erpUserInformation.setName("Grace");
        erpUserInformation.setErpAccountId("888");
        erpUserInformation.setErpSystemName(ERPSystem.ECLIPSE.name());

        ProductResponse productResponse = new ProductResponse();
        MassProductInquiryResponse massProductInquiryResponse = new MassProductInquiryResponse();
        ProductList productList = new ProductList();
        Product product = new Product();
        Pricing pricing = new Pricing();
        PricingBranch pricingBranch = new PricingBranch();
        Branch homeBranch = new Branch();
        homeBranch.setBranchName("HOME_BRANCH_NAME");
        homeBranch.setBranchId(BRANCH_ID);
        pricingBranch.setBranch(homeBranch);
        pricing.setPricingBranch(pricingBranch);
        pricing.setCustomerPrice("10.0");
        product.setPricing(pricing);
        PartIdentifiers partIdentifiers = new PartIdentifiers();
        partIdentifiers.setEclipsePartNumber(String.valueOf(ECLIPSE_PRODUCT_ID));
        product.setPartIdentifiers(partIdentifiers);
        productList.setProducts(asList(product));
        AvailabilityList availabilityList = new AvailabilityList();
        List<BranchAvailability> branchAvailabilityList = new ArrayList<>();
        BranchAvailability homeBranchAvailability = new BranchAvailability();
        BranchAvailability otherBranchAvailability = new BranchAvailability();
        homeBranchAvailability.setBranch(homeBranch);
        NowQuantity nowQuantity = new NowQuantity();
        Quantity quantity = new Quantity();
        quantity.setQuantity(10);
        nowQuantity.setQuantity(quantity);

        homeBranchAvailability.setNowQuantity(nowQuantity);

        Branch otherBranch = new Branch();
        otherBranch.setBranchId("OTHER_BRANCH_ID");
        otherBranch.setBranchName("OTHER_BRANCH_NAME");
        otherBranchAvailability.setBranch(otherBranch);
        NowQuantity nowQuantityOther = new NowQuantity();
        Quantity quantityOther = new Quantity();
        quantityOther.setQuantity(11);
        nowQuantityOther.setQuantity(quantityOther);

        otherBranchAvailability.setNowQuantity(nowQuantityOther);

        branchAvailabilityList.add(homeBranchAvailability);
        branchAvailabilityList.add(otherBranchAvailability);
        availabilityList.setBranchAvailabilityList(branchAvailabilityList);
        product.setAvailabilityList(availabilityList);

        massProductInquiryResponse.setProductList(productList);
        productResponse.setMassProductInquiryResponse(massProductInquiryResponse);
        when(erpService.getEclipseProductData(String.valueOf(ECLIPSE_PRODUCT_ID), erpUserInformation, false))
            .thenReturn(productResponse);
        when(erpService.getEclipseProductData(asList(String.valueOf(ECLIPSE_PRODUCT_ID)), erpUserInformation, false))
            .thenReturn(productResponse);

        var branchWithDistance = new BranchWithDistanceResponseDTO();
        branchWithDistance.setBranchId(BRANCH_ID);
        branchWithDistance.setDistance(5f);

        QueryResult productFoundQueryResult = new QueryResult();
        productFoundQueryResult.setFilteredResult(PRODUCT_FOUND_RESPONSE);
        HashMap<String, JsonNode> aggregateResultsMap = new HashMap<>();

        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
        objectNode.set("type", new TextNode("value"));
        ArrayNode arrayNode1 = new ArrayNode(JsonNodeFactory.instance);
        ObjectNode objectNode1 = new ObjectNode(JsonNodeFactory.instance);
        objectNode1.set("value", new TextNode("test"));
        objectNode1.set("count", new IntNode(1));
        arrayNode1.add(objectNode1);
        objectNode.set("data", arrayNode1);
        arrayNode.add(objectNode);

        aggregateResultsMap.put(TONNAGE, arrayNode);
        aggregateResultsMap.put(MFR_FULL_NAME, arrayNode);
        aggregateResultsMap.put(PRODUCT_LINE, arrayNode);
        aggregateResultsMap.put(FLOW_RATE, arrayNode);
        aggregateResultsMap.put(ENERGY_STAR_FLAG, arrayNode);
        aggregateResultsMap.put(HAZARDOUS_MATERIAL_FLAG, arrayNode);
        aggregateResultsMap.put(LOW_LEAD_COMPLIANT_FLAG, arrayNode);
        aggregateResultsMap.put(MERCURY_FREE_FLAG, arrayNode);
        aggregateResultsMap.put(WATER_SENSE_COMPLIANT_FLAG, arrayNode);
        aggregateResultsMap.put(IN_STOCK_LOCATION, arrayNode);
        aggregateResultsMap.put(MATERIAL, arrayNode);
        aggregateResultsMap.put(COLORFINISH, arrayNode);
        aggregateResultsMap.put(SIZE, arrayNode);
        aggregateResultsMap.put(LENGTH, arrayNode);
        aggregateResultsMap.put(WIDTH, arrayNode);
        aggregateResultsMap.put(HEIGHT, arrayNode);
        aggregateResultsMap.put(DEPTH, arrayNode);
        aggregateResultsMap.put(VOLTAGE, arrayNode);
        aggregateResultsMap.put(BTU, arrayNode);
        aggregateResultsMap.put(PRESSURE_RATING, arrayNode);
        aggregateResultsMap.put(TEMPERATURE_RATING, arrayNode);
        aggregateResultsMap.put(INLET_SIZE, arrayNode);
        aggregateResultsMap.put(CAPACITY, arrayNode);
        aggregateResultsMap.put(WATTAGE, arrayNode);
        aggregateResultsMap.put(CMP, arrayNode);
        productFoundQueryResult.setPartialAggResults(aggregateResultsMap);

        QueryResult noProductFoundQueryResult = new QueryResult();
        noProductFoundQueryResult.setFilteredResult(NO_PRODUCT_FOUND_RESPONSE);
        noProductFoundQueryResult.setPartialAggResults(new HashMap<>());

        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 1, 12, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);
        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 1, 12, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);
        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 2, 20, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);

        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO(null, 1, 12, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(noProductFoundQueryResult);
        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 1, 12, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);
        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 1, 12, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);
        when(
            elasticsearchService.getProductsByQuery(
                new ProductSearchRequestDTO("test", 2, 20, "", null, null, 0, null, null, null, null, null)
            )
        )
            .thenReturn(productFoundQueryResult);

        SuggestedSearchResponseDTO suggestedSearchResponseSuccess = new SuggestedSearchResponseDTO();
        suggestedSearchResponseSuccess.setSuggestedProductSearchResponse(
            "{\"meta\":{\"alerts\":[],\"warnings\":[],\"page\":{\"current\":1,\"total_pages\":10000,\"total_results\":10000,\"size\":1},\"engine\":{\"name\":\"products-beta\",\"type\":\"meta\"},\"request_id\":\"4c0d1fb7-198e-48de-8cba-40253057f661\"},\"results\":[{\"_meta\":{\"id\":\"MSC-299154\",\"engine\":\"products-beta-1632769203463\",\"score\":8.297622},\"id\":{\"raw\":\"products-beta-1632769203463|MSC-299154\"},\"erp_product_id\":{\"raw\":\"MSC-5007420\"},\"mfr_full_name\":{\"raw\":\"Sakrete\"},\"thumbnail_image_url_name\":{\"raw\":\"http://images.tradeservice.com/ProductImages/DIR100133/SAKRTE_65200390_SML.jpg\"},\"product_overview_description\":{\"raw\":\"High Strength Concrete Mix; Item Concrete Mix; Container Capacity 60 Lb; Container Type Bag; Water Content 2.5 Quart; Physical State Solid; Color Gray; Pressure Rating 4000 PSI; Temperature Rating 40 to 80 Deg F; Application Driveway, Slab, Patios, Walkway, Curb, Stairs, Ramp, Setting Fence Post, Foundation Wall and Footing, Structural Application Requiring a Small Volume of Concrete; Applicable Standard ASTM C387/C39\"},\"mfr_catalog_doc_file_name\":{\"raw\":\"http://images.tradeservice.com/ATTACHMENTS/DIR100174/SAKRTEE00001_1.pdf\"}}],\"facets\":{\"product_line\":[{\"type\":\"value\",\"data\":[{\"value\":\"BECK\",\"count\":552},{\"value\":\"XIRTEC 140\",\"count\":223},{\"value\":\"Dura-Power\",\"count\":140},{\"value\":\"OAT\",\"count\":140},{\"value\":\"DELTA\",\"count\":122},{\"value\":\"KOHLER\",\"count\":121},{\"value\":\"FlowGuard Gold\",\"count\":112},{\"value\":\"Chemtrol\",\"count\":102},{\"value\":\"Gruvlok\",\"count\":102},{\"value\":\"EverTUFF\",\"count\":90}]}],\"mfr_full_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"Generic\",\"count\":2618},{\"value\":\"Spears\",\"count\":1215},{\"value\":\"Merit Brass\",\"count\":767},{\"value\":\"Anvil International\",\"count\":658},{\"value\":\"Charlotte Pipe\",\"count\":569},{\"value\":\"Nibco\",\"count\":548},{\"value\":\"IPEX\",\"count\":393},{\"value\":\"TRUaire\",\"count\":330},{\"value\":\"Sioux Chief\",\"count\":294},{\"value\":\"A.O. Smith\",\"count\":269}]}],\"category_1_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"Pipe & Fittings\",\"count\":6763},{\"value\":\"Plumbing Installation, Tools, Hardware & Safety\",\"count\":2880},{\"value\":\"Faucets, Fixtures & Appliances\",\"count\":1532},{\"value\":\"Heating & Cooling\",\"count\":1348},{\"value\":\"Hot Water, Valves, Irrigation & Pumps\",\"count\":895},{\"value\":\"TBC\",\"count\":620},{\"value\":\"PVF\",\"count\":27},{\"value\":\"Tools and Safety\",\"count\":16},{\"value\":\"Rough Plumbing\",\"count\":14},{\"value\":\"HVAC\",\"count\":12}]}],\"category_2_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"Plastic Pipe & Fittings - Drainage\",\"count\":1989},{\"value\":\"Plumbing Installation\",\"count\":1978},{\"value\":\"Metal Pipe & Fittings\",\"count\":1777},{\"value\":\"TBC\",\"count\":1644},{\"value\":\"Plastic Pipe & Fittings - Supply\",\"count\":1101},{\"value\":\"Air Distribution\",\"count\":449},{\"value\":\"Tools\",\"count\":313},{\"value\":\"Hardware\",\"count\":177},{\"value\":\"Repair & Replacement Parts\",\"count\":138},{\"value\":\"Faucets, Showers & Bathroom Accessories\",\"count\":128}]}],\"category_3_name\":[{\"type\":\"value\",\"data\":[{\"value\":\"TBC\",\"count\":3555},{\"value\":\"PVC Pipe & Fittings\",\"count\":1943},{\"value\":\"Steel Pipe & Fittings\",\"count\":1512},{\"value\":\"CPVC Pipe & Fittings\",\"count\":824},{\"value\":\"Grills, Registers & Diffusers\",\"count\":284},{\"value\":\"PEX Pipe & Fittings\",\"count\":277},{\"value\":\"Copper Tube & Fittings\",\"count\":143},{\"value\":\"Sheet Metal Pipe & Fittings\",\"count\":130},{\"value\":\"Cast Iron Pipe & Fittings\",\"count\":80},{\"value\":\"Commercial Packaged Systems\",\"count\":71}]}]}}"
        );
        suggestedSearchResponseSuccess.setSuggestions(
            asList(
                "sc",
                "sc-3",
                "sc-3 renewable",
                "sc-3 renewable seat",
                "sc-3 renewable seat w/gaskets",
                "sc-6",
                "scp2"
            )
        );

        Cart cart = new Cart();
        cart.setShippingBranchId(BRANCH_ID);
        cart.setApprovalState(ACTIVE_APPROVAL_STATE);
        when(cartDAO.findAllByOwnerIdAndShipToId(any(), any())).thenReturn(Collections.singletonList(cart));
        ApprovalFlowState approvalFlowState = new ApprovalFlowState();
        approvalFlowState.setId(ACTIVE_APPROVAL_STATE);
        when(approvalFlowStateDAO.findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName()))
            .thenReturn(approvalFlowState);

        productService =
            new ProductService(
                elasticsearchService,
                erpService,
                categoriesDAO,
                cartDAO,
                approvalFlowStateDAO,
                dataWarehouseRepository
            );
    }

    @Test
    void getProductById_available_no_user_info()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductById(AVAILABLE_PRODUCT_ID);
        testProductResponse(productDTO, true);
    }

    @Test
    void getProductById_available_with_user_info()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductById(AVAILABLE_PRODUCT_ID);
        testProductResponse(productDTO, false);
    }

    @Test
    void getProductByNumber_Mincron_available()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductByNumber(AVAILABLE_PRODUCT_NUMBER);
        testProductResponse(productDTO, true);
    }

    @Test
    void getProductByNumber_Eclipse_available()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductByNumber(AVAILABLE_ECLIPSE_PRODUCT_NUMBER);
        testProductResponse(productDTO, true);
    }

    @Test
    void getProductsByNumber_available() throws ElasticsearchException {
        List<ProductDTO> productDTOs = productService.getProductsByNumber(AVAILABLE_PRODUCT_NUMBERS);
        for (ProductDTO productDTO : productDTOs) {
            testProductResponse(productDTO, true);
        }
    }

    @Test
    void getProductById_not_available()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductById(UNKNOWN_PRODUCT_ID);
        assertNull(productDTO, "Expected productDTO from service to be null with non-existent product ID");
    }

    @Test
    void getProductByNumber_Mincron_not_available()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductByNumber(UNKNOWN_PRODUCT_NUMBER);
        assertNull(productDTO, "Expected productDTO from service to be null with non-existent product number");
    }

    @Test
    void getProductByNumber_Eclipse_not_available()
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductByNumber(UNKNOWN_ECLIPSE_PRODUCT_NUMBER);
        assertNull(productDTO, "Expected productDTO from service to be null with non-existent product number");
    }

    void testProductResponse(ProductDTO productDTO, boolean stockNull) {
        assertNotNull(productDTO, "Expected productDTO from service to be non-null with valid product ID");
        assertNotEquals(productDTO.getId(), 0, "Expected productDTO id to be mapped to non-zero value");
        assertNotNull(productDTO.getPartNumber(), "Expected productDTO product number to be mapped to non-zero value");
        assertNotNull(productDTO.getName(), "Expected product name to be non-null");
        assertNotNull(productDTO.getManufacturerName(), "Expected manufacturer name to be mapped to non-null value");
        assertNotNull(
            productDTO.getManufacturerNumber(),
            "Expected manufacturer number to be mapped to non-null value"
        );
        assertNotNull(productDTO.getTechnicalDocuments(), "Expected tech doc list to be non-null");
        assertFalse(productDTO.getTechnicalDocuments().isEmpty(), "Expected technical document list to not be empty");
        for (TechDoc techDoc : productDTO.getTechnicalDocuments()) {
            assertNotNull(techDoc.getName(), "Expected tech doc name to be a non-null value");
            assertNotNull(techDoc.getUrl(), "Expected tech doc url name to be a non-null value");
        }
        assertNotNull(productDTO.getUpc(), "Expected UPC to be mapped to a non-null value");
        assertNotNull(productDTO.getUnspsc(), "Expected UNSPSC to be mapped to a non-null value");
        assertNotNull(productDTO.getProductOverview(), "Expected Product overview to be mapped to a non-null value");
        assertNotNull(
            productDTO.getFeaturesAndBenefits(),
            "Expected Features and benefits to be mapped to a non-null value"
        );
        assertNotNull(productDTO.getTechSpecifications(), "Expected technical specifications list to be non-null");
        assertFalse(
            productDTO.getTechSpecifications().isEmpty(),
            "Expected technical specifications list to not be empty"
        );
        for (TechSpec techSpec : productDTO.getTechSpecifications()) {
            assertNotNull(techSpec.getName(), "Expected tech spec name to be a non-null value");
            assertNotNull(techSpec.getValue(), "Expected tech spec value name to be a non-null value");
        }
        assertNotNull(productDTO.getImageUrls(), "Expected image urls to be mapped to a non-null value");
        assertNotNull(
            productDTO.getImageUrls().getLarge(),
            "Expected image urls large to be mapped to a non-null value"
        );
        assertNotNull(
            productDTO.getImageUrls().getMedium(),
            "Expected image urls medium to be mapped to a non-null value"
        );
        assertNotNull(
            productDTO.getImageUrls().getSmall(),
            "Expected image urls small to be mapped to a non-null value"
        );
        assertNotNull(
            productDTO.getImageUrls().getThumb(),
            "Expected image urls thumb to be mapped to a non-null value"
        );
        assertNotNull(
            productDTO.getPackageDimensions(),
            "Expected package dimensions to be mapped to a non-null value"
        );
        assertNotEquals(productDTO.getMinIncrementQty(), 0, "Expected min increment qty to be non-zero");
        assertNotNull(productDTO.getInStockLocation(), "Expected in stock location to be mapped to a non-null value");
        assertNotNull(productDTO.getCmp(), "Expected cmp to be non-null value");
        for (String inStockLocation : productDTO.getInStockLocation()) {
            assertNotNull(inStockLocation);
            assertFalse(inStockLocation.isEmpty());
        }
        assertNotNull(
            productDTO.getProductSearchBoost(),
            "Expected product search boost to be mapped to a non-null value"
        );
        productDTO
            .getProductSearchBoost()
            .entrySet()
            .stream()
            .forEach(e -> {
                assertNotNull(e.getValue());
                assertFalse(e.getValue().isEmpty());
            });
    }

    @Test
    void getPartNumbersAvailableInList() {
        val testAccountNo = "811824";

        List<String[]> listExpecteds = new ArrayList<String[]>();
        String[] listExpected = new String[2];
        listExpected[0] = "102137";
        listExpected[1] = "f17539a9-d85c-4bcb-8000-2317ba135f44,858d6931-4359-4f25-96cd-0e63c896e0ec";
        listExpecteds.add(listExpected);

        List<String[]> listsActuals = new ArrayList<String[]>();
        String[] listActual = new String[2];
        listActual[0] = "102137";
        listActual[1] = "f17539a9-d85c-4bcb-8000-2317ba135f44,858d6931-4359-4f25-96cd-0e63c896e0ec";
        listsActuals.add(listActual);
        Set<String> testPartNumbersRequest = Set.of("84051", "84043", "45094");
        when(
            listLineItemsDAO.findAllListIdsByPartNumbersAndErpAccountId(
                testPartNumbersRequest,
                testAccountNo,
                ErpEnum.ECLIPSE
            )
        )
            .thenReturn(listsActuals);
        assertEquals(listExpecteds.size(), listsActuals.size());
    }

    @Test
    void getAllProducts_success() {
        List<ProductResponseDTO> productDTOsExpected = new ArrayList<>();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName("RHEEM PILOT ASSEMBLY W/ BRACKET SP20058");
        ProductResponseDTO productResponseDTO1 = new ProductResponseDTO();
        productResponseDTO1.setName("MORTEX 10KW AIR HANDLER E30B4D010ABB");
        productDTOsExpected.add(productResponseDTO);
        productDTOsExpected.add(productResponseDTO1);

        when(dataWarehouseRepository.getAllProductsData()).thenReturn(productDTOsExpected);

        List<ProductResponseDTO> productDTOsActual = productService.getAllProducts();
        assertEquals(productDTOsExpected.size(), productDTOsActual.size());
        verify(dataWarehouseRepository, times(1)).getAllProductsData();
    }

    @Test
    void getAllProducts_Failure() {
        when(dataWarehouseRepository.getAllProductsData()).thenThrow(NullPointerException.class);

        assertThrows(
            ProductNotFoundException.class,
            () -> {
                productService.getAllProducts();
            }
        );
    }

    @Test
    void getCustomerPartNumbersList_Success() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode productDataNode = mapper.readTree(
            "[\"{\\\"29295\\\":[\\\"PH05420\\\"]}\",\"{\\\"303566\\\":[\\\"CCG02\\\"]}\"]"
        );
        assertNotNull(productService.getCustomerPartNumbersList(productDataNode));
    }

    @Test
    void getCustomerPartNumbersList_failure() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode productDataNode = mapper.readTree("[\"{\\\"29295\\\":null}\",\"{\\\"303566\\\":[\\\"CCG02\\\"]}\"]");
        assertEquals(productService.getCustomerPartNumbersList(productDataNode).get(0).getPartNumbers().size(), 0);
    }
}
