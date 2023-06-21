package com.reece.platform.products.service;

import static com.reece.platform.products.constants.ElasticsearchFieldNames.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reece.platform.products.branches.controller.BranchesController;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.BranchServiceCoreException;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.exceptions.ProductNotFoundException;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.Categories;
import com.reece.platform.products.model.repository.ApprovalFlowStateDAO;
import com.reece.platform.products.model.repository.CartDAO;
import com.reece.platform.products.model.repository.CategoriesDAO;
import com.reece.platform.products.model.repository.ListLineItemsDAO;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import com.reece.platform.products.search.CreateIndexService;
import com.reece.platform.products.search.SearchService;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    public ProductService(
        ElasticsearchService elasticsearchService,
        ErpService erpService,
        CategoriesDAO categoriesDAO,
        CartDAO cartDAO,
        ApprovalFlowStateDAO approvalFlowStateDAO,
        DataWarehouseRepository dataWarehouseRepository
    ) {
        this.elasticsearchService = elasticsearchService;
        this.erpService = erpService;
        this.categoriesDAO = categoriesDAO;
        this.cartDAO = cartDAO;
        this.approvalFlowStateDAO = approvalFlowStateDAO;
        this.dataWarehouseRepository = dataWarehouseRepository;
    }

    private final ElasticsearchService elasticsearchService;
    private final ErpService erpService;
    private final CategoriesDAO categoriesDAO;
    private final CartDAO cartDAO;
    private final ApprovalFlowStateDAO approvalFlowStateDAO;

    private final DataWarehouseRepository dataWarehouseRepository;

    @Autowired
    private ListLineItemsDAO listLineItemsDAO;

    /**
     * Query Elasticsearch by productId and transform the results into a ProductDTO.
     *
     * @param productId product to query on
     * @return ProductDTO with data from Elasticsearch
     */
    public ProductDTO getProductById(String productId)
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        String productDataResponse = elasticsearchService.getProductById(productId);

        return buildProductDto(productDataResponse);
    }

    @SneakyThrows
    public List<ProductDTO> getProductsByNumber(List<String> productIds) throws ElasticsearchException {
        //TODO Also handle Mincron products
        return getProductsByProductNumbers(productIds);
    }

    @SneakyThrows
    public List<ProductDTO> getProductsByProductNumbers(List<String> productIds) throws ElasticsearchException {
        List<ProductDTO> productDTOS = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode productIDS = mapper.createArrayNode();
        productIds
            .stream()
            .forEach(productId -> {
                productIDS.add(String.format("MSC-%s", productId));
            });

        try {
            String productData;
            productData = elasticsearchService.getProductByEclipseNumberArray(productIDS);

            JsonNode productNode = mapper.readTree(productData);

            int totalResult = productNode.get(META).get(PAGE).get(TOTAL_RESULTS).asInt();
            if (totalResult == 0) {
                return productDTOS;
            }
            for (int i = 0; i < totalResult; i++) {
                if (productNode.get(RESULTS).get(i) != null) {
                    productDTOS.add(buildSingleProductDto(productNode.get(RESULTS).get(i)));
                }
            }
        } catch (JsonProcessingException | ElasticsearchException | BranchServiceCoreException exception) {}
        return productDTOS;
    }

    @SneakyThrows
    public List<ProductDTO> getProductsByNumber(List<String> productIds, ERPSystem erpSystem)
        throws JsonProcessingException, ElasticsearchException {
        if (erpSystem.equals(ERPSystem.ECLIPSE)) {
            return getProductsByProductNumbers(productIds);
        }
        List<ProductDTO> productDTOS = new ArrayList<>();
        productIds.forEach(productId -> {
            try {
                productDTOS.add(getProductByNumber(productId, erpSystem));
            } catch (JsonProcessingException | ElasticsearchException | BranchServiceCoreException ignored) {}
        });
        return productDTOS;
    }

    /**
     * Query Elasticsearch by partNumber and transform the results into a ProductDTO.
     *
     * @param prodNum product to query on
     * @return ProductDTO with data from Elasticsearch
     */
    public ProductDTO getProductByNumber(String prodNum)
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        var erpSystem = ERPSystem.fromProductNumber(prodNum);
        return getProductByNumber(prodNum, erpSystem);
    }

    /**
     * Query Elasticsearch by partNumber and transform the results into a ProductDTO.
     *
     * @param prodNum   product to query on
     * @param erpSystem erpSystem for this part
     * @return ProductDTO with data from Elasticsearch
     */
    public ProductDTO getProductByNumber(String prodNum, ERPSystem erpSystem)
        throws JsonProcessingException, ElasticsearchException, BranchServiceCoreException {
        switch (erpSystem) {
            case MINCRON:
                return buildProductDto(elasticsearchService.getProductByMincronNumber(prodNum));
            case ECLIPSE:
                return buildProductDto(elasticsearchService.getProductByEclipseNumber(prodNum));
            default:
                throw new IllegalStateException("Unknown ERP SYSTEM:" + erpSystem);
        }
    }

    public List<ProductDTO> getProductDetails(List<ProductDTO> productDtos) throws ElasticsearchException {
        // Right now we'll only do this for the Eclipse data
        var productIds = productDtos
            .stream()
            .filter(p -> p.getErp().equals(ECLIPSE))
            .map(ProductDTO::getPartNumber)
            .collect(Collectors.toList());

        if (productDtos.isEmpty()) {
            return new ArrayList<>();
        }

        // Get Elastic data
        var elasticData = getProductsByNumber(productIds)
            .stream()
            .filter(p -> p != null && p.getPartNumber() != null)
            .collect(Collectors.toMap(ProductDTO::getPartNumber, Function.identity()));

        return productDtos
            .stream()
            .map(p -> {
                var elasticProduct = elasticData.get(p.getPartNumber());
                return elasticProduct != null ? elasticProduct : p;
            })
            .collect(Collectors.toList());
    }

    /**
     * Fetch branch on the given user and account's cart
     *
     * @param userId   user cart is associated with
     * @param shipToId account cart is associated with
     * @return branchId
     */
    public Optional<String> getSelectedCartBranch(UUID userId, UUID shipToId) {
        List<Cart> carts = cartDAO.findAllByOwnerIdAndShipToId(userId, shipToId);
        UUID activeFlowStateId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName())
            .getId();
        Optional<Cart> activeCart = carts
            .stream()
            .filter(cart -> cart.getApprovalState().equals(activeFlowStateId))
            .findAny();

        return activeCart.map(Cart::getShippingBranchId);
    }

    /**
     * Transform JSON string from ES into ProductDTO object
     *
     * @param productData product data JSON string from ES
     * @return ProductDTO object with data from given string
     */
    private ProductDTO buildProductDto(String productData) throws JsonProcessingException, BranchServiceCoreException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode productNode = mapper.readTree(productData);

        if (productNode.get(META).get(PAGE).get(TOTAL_RESULTS).asInt() == 0) {
            return null;
        }

        return buildSingleProductDto(productNode.get(RESULTS).get(0));
    }

    /**
     * Transform JSON string of Product SOURCE from ES into ProductDTO object
     *
     * @param productDataNode product data SOURCE JSON string from ES
     * @return ProductDTO object with data from given string
     */
    private ProductDTO buildSingleProductDto(JsonNode productDataNode)
        throws BranchServiceCoreException, JsonProcessingException {
        ProductDTO productDTO = new ProductDTO();

        if (productDataValid(MINCRON_PRODUCT_NUMBER, productDataNode)) {
            productDTO.setPartNumber(getProductField(productDataNode.get(MINCRON_PRODUCT_NUMBER)).asText());
        } else if (productDataValid(ERP_PRODUCT_ID, productDataNode)) {
            String productId = getProductField(productDataNode.get(ERP_PRODUCT_ID)).asText();
            if (productId.startsWith("MSC-")) {
                productDTO.setPartNumber(productId.replace("MSC-", ""));
            }
        }

        if (
            productDTO.getProductOverview() == null && productDataValid(PRODUCT_OVERVIEW_DESCRIPTION, productDataNode)
        ) {
            productDTO.setProductOverview(getProductField(productDataNode.get(PRODUCT_OVERVIEW_DESCRIPTION)).asText());
        }

        List<String> categories = new ArrayList<>();
        if (productDataValid(CATEGORY_1_NAME, productDataNode)) {
            categories.add(getProductField(productDataNode.get(CATEGORY_1_NAME)).asText());
        }
        if (productDataValid(CATEGORY_2_NAME, productDataNode)) {
            categories.add(getProductField(productDataNode.get(CATEGORY_2_NAME)).asText());
        }
        if (productDataValid(CATEGORY_3_NAME, productDataNode)) {
            categories.add(getProductField(productDataNode.get(CATEGORY_3_NAME)).asText());
        }
        productDTO.setCategories(categories);

        if (productDataValid(MFR_FULL_NAME, productDataNode)) {
            productDTO.setManufacturerName(getProductField(productDataNode.get(MFR_FULL_NAME)).asText());
        }

        if (productDataValid(ERP_PRODUCT_ID, productDataNode)) {
            String productId = getProductField(productDataNode.get(ERP_PRODUCT_ID)).asText();
            if (productId.startsWith("MSC-")) {
                productId = productId.replace("MSC-", "");
            }
            productDTO.setPartNumber(productId);
        }

        List<TechDoc> techDocs = new ArrayList<>();
        if (productDataValid(MFR_CATALOG_DOC_FILE_NAME, productDataNode)) {
            TechDoc techDoc = new TechDoc(
                MFR_CATALOG_PAGE,
                getProductField(productDataNode.get(MFR_CATALOG_DOC_FILE_NAME)).asText()
            );
            techDocs.add(techDoc);
        }
        if (productDataValid(MFR_SPEC_TECH_DOC_FILE_NAME, productDataNode)) {
            TechDoc techDoc = new TechDoc(
                TECHNICAL_SPECIFICATION,
                getProductField(productDataNode.get(MFR_SPEC_TECH_DOC_FILE_NAME)).asText()
            );
            techDocs.add(techDoc);
        }
        if (productDataValid(MFR_MSDS_DOC_FILE_NAME, productDataNode)) {
            TechDoc techDoc = new TechDoc(
                SDS_SHEET,
                getProductField(productDataNode.get(MFR_MSDS_DOC_FILE_NAME)).asText()
            );
            techDocs.add(techDoc);
        }
        if (productDataValid(MFR_INSTALL_INSTRUCTION_DOC_FILE_NAME, productDataNode)) {
            TechDoc techDoc = new TechDoc(
                INSTALLATION_SHEET,
                getProductField(productDataNode.get(MFR_INSTALL_INSTRUCTION_DOC_FILE_NAME)).asText()
            );
            techDocs.add(techDoc);
        }
        if (productDataValid(MFR_ITEM_DATA_DOC_FILE_NAME, productDataNode)) {
            TechDoc techDoc = new TechDoc(
                MFR_ITEM_DATA,
                getProductField(productDataNode.get(MFR_ITEM_DATA_DOC_FILE_NAME)).asText()
            );
            techDocs.add(techDoc);
        }
        productDTO.setTechnicalDocuments(techDocs);

        List<String> environmentalOptions = new ArrayList<>();
        if (productDataValid(LOW_LEAD_COMPLIANT_FLAG, productDataNode)) {
            if (getProductField(productDataNode.get(LOW_LEAD_COMPLIANT_FLAG)).asBoolean()) {
                environmentalOptions.add(LOW_LEAD_COMPLIANT);
            }
        }
        if (productDataValid(MERCURY_FREE_FLAG, productDataNode)) {
            if (getProductField(productDataNode.get(MERCURY_FREE_FLAG)).asBoolean()) {
                environmentalOptions.add(MERCURY_FREE);
            }
        }
        if (productDataValid(WATER_SENSE_COMPLIANT_FLAG, productDataNode)) {
            if (getProductField(productDataNode.get(WATER_SENSE_COMPLIANT_FLAG)).asBoolean()) {
                environmentalOptions.add(WATERSENSE_COMPLIANT);
            }
        }
        if (productDataValid(ENERGY_STAR_FLAG, productDataNode)) {
            if (getProductField(productDataNode.get(ENERGY_STAR_FLAG)).asBoolean()) {
                environmentalOptions.add(ENERGY_STAR);
            }
        }
        if (productDataValid(HAZARDOUS_MATERIAL_FLAG, productDataNode)) {
            if (getProductField(productDataNode.get(HAZARDOUS_MATERIAL_FLAG)).asBoolean()) {
                environmentalOptions.add(HAZARDS_MATERIAL);
            }
        }
        productDTO.setEnvironmentalOptions(environmentalOptions);

        if (productDataValid(UPC_ID, productDataNode)) {
            productDTO.setUpc(getProductField(productDataNode.get(UPC_ID)).asText());
        }

        if (productDataValid(FEATURE_BENEFIT_LIST_TEXT, productDataNode)) {
            productDTO.setFeaturesAndBenefits(getProductField(productDataNode.get(FEATURE_BENEFIT_LIST_TEXT)).asText());
        }

        ImageUrls imageUrls = new ImageUrls();
        if (productDataValid(FULL_IMAGE_URL_NAME, productDataNode)) {
            imageUrls.setLarge(getProductField(productDataNode.get(FULL_IMAGE_URL_NAME)).asText());
        }
        if (productDataValid(THUMBNAIL_IMAGE_URL_NAME, productDataNode)) {
            imageUrls.setSmall(getProductField(productDataNode.get(THUMBNAIL_IMAGE_URL_NAME)).asText());
            imageUrls.setThumb(getProductField(productDataNode.get(THUMBNAIL_IMAGE_URL_NAME)).asText());
        }
        if (productDataValid(MEDIUM_IMAGE_URL_NAME, productDataNode)) {
            imageUrls.setMedium(getProductField(productDataNode.get(MEDIUM_IMAGE_URL_NAME)).asText());
        }
        productDTO.setImageUrls(imageUrls);

        PackageDimensions packageDimensions = new PackageDimensions();
        if (productDataValid(PACKAGE_HEIGHT_NBR, productDataNode)) {
            packageDimensions.setHeight(getProductField(productDataNode.get(PACKAGE_HEIGHT_NBR)).asDouble());
        }
        if (productDataValid(PACKAGE_LENGTH_NBR, productDataNode)) {
            packageDimensions.setLength(getProductField(productDataNode.get(PACKAGE_LENGTH_NBR)).asDouble());
        }
        if (productDataValid(PACKAGE_VOLUME_NBR, productDataNode)) {
            packageDimensions.setVolume(getProductField(productDataNode.get(PACKAGE_VOLUME_NBR)).asDouble());
        }
        if (productDataValid(PACKAGE_VOLUME_UOM_CODE, productDataNode)) {
            packageDimensions.setVolumeUnitOfMeasure(
                getProductField(productDataNode.get(PACKAGE_VOLUME_UOM_CODE)).asText()
            );
        }
        if (productDataValid(PACKAGE_WEIGHT_NBR, productDataNode)) {
            packageDimensions.setWeight(getProductField(productDataNode.get(PACKAGE_WEIGHT_NBR)).asDouble());
        }
        if (productDataValid(PACKAGE_WEIGHT_UOM_CODE, productDataNode)) {
            packageDimensions.setWeightUnitOfMeasure(
                getProductField(productDataNode.get(PACKAGE_WEIGHT_UOM_CODE)).asText()
            );
        }
        if (productDataValid(PACKAGE_WIDTH_NBR, productDataNode)) {
            packageDimensions.setWidth(getProductField(productDataNode.get(PACKAGE_WIDTH_NBR)).asDouble());
        }
        productDTO.setPackageDimensions(packageDimensions);

        if (productDataValid(MINIMUM_INCREMENT_QTY, productDataNode)) {
            productDTO.setMinIncrementQty(getProductField(productDataNode.get(MINIMUM_INCREMENT_QTY)).asInt());
        }

        if (productDataValid(LAST_UPDATE_DATE, productDataNode)) {
            productDTO.setLastUpdateDate(getProductField(productDataNode.get(LAST_UPDATE_DATE)).asText());
        }

        if (productDataValid(ERP_PRODUCT_ID, productDataNode)) {
            String productId = getProductField(productDataNode.get(ERP_PRODUCT_ID)).asText();
            productDTO.setId(productId);
        }

        if (productDataValid(PRODUCT_SOLD_COUNT, productDataNode)) {
            productDTO.setProductSoldCount(getProductField(productDataNode.get(PRODUCT_SOLD_COUNT)).asText());
        }

        if (productDataValid(UNSPC_ID, productDataNode)) {
            productDTO.setUnspsc(getProductField(productDataNode.get(UNSPC_ID)).asText());
        }

        if (productDataValid(VENDOR_PART_NBR, productDataNode)) {
            productDTO.setManufacturerNumber(getProductField(productDataNode.get(VENDOR_PART_NBR)).asText());
        }

        if (productDataValid(WEB_DESCRIPTION, productDataNode)) {
            productDTO.setName(getProductField(productDataNode.get(WEB_DESCRIPTION)).asText());
        }

        if (productDataNode.has(CUSTOMER_NUMBER)) {
            productDTO.setCustomerNumber(getProductArray(productDataNode, CUSTOMER_NUMBER));
        }

        if (productDataNode.has(CUSTOMER_PART_NUMBER)) {
            productDTO.setCustomerPartNumber(getProductArray(productDataNode, CUSTOMER_PART_NUMBER));
        }

        if (productDataNode.has(CUSTOMER_PART_NUMBERS)) {
            var customerPartNumberList = getCustomerPartNumbersList(
                getProductField(productDataNode.get(CUSTOMER_PART_NUMBERS))
            );
            productDTO.setCustomerPartNumbers(customerPartNumberList);
        }

        if (productDataNode.has(IN_STOCK_LOCATION)) {
            productDTO.setInStockLocation(getProductArray(productDataNode, IN_STOCK_LOCATION));
        }

        if (productDataNode.has(PRODUCT_BRANCH_EXCLUSION)) {
            productDTO.setProductBranchExclusion(getProductArray(productDataNode, PRODUCT_BRANCH_EXCLUSION));
        }

        if (productDataValid(SEARCH_KEYWORD_TEXT, productDataNode)) {
            productDTO.setSearchKeywordText(getProductField(productDataNode.get(SEARCH_KEYWORD_TEXT)).asText());
        }

        if (productDataValid(CMP, productDataNode)) {
            productDTO.setCmp(getProductField(productDataNode.get(CMP)).asText());
        }

        HashMap<String, String> productSearchBoostMap = new HashMap<>();
        for (String searchBoostPropertyName : SEARCH_BOOST_PROPERTY_LIST) {
            if (productDataValid(searchBoostPropertyName, productDataNode)) {
                productSearchBoostMap.put(
                    searchBoostPropertyName,
                    getProductField(productDataNode.get(searchBoostPropertyName)).asText()
                );
            }
        }
        productDTO.setProductSearchBoost(productSearchBoostMap);

        List<TechSpec> techSpecList = new ArrayList<>();

        if (productDataNode.has(TECHNICAL_SPECIFICATIONS)) {
            JsonNode techSpecNodeArr = getProductField(productDataNode.get(TECHNICAL_SPECIFICATIONS));
            if (!techSpecNodeArr.isEmpty()) {
                JsonNode techSpecNode = new ObjectMapper().readTree(techSpecNodeArr.get(0).asText());
                for (Iterator<String> it = techSpecNode.fieldNames(); it.hasNext();) {
                    String techSpecPropertyName = it.next();
                    if (productDataValid(techSpecPropertyName, techSpecNode)) {
                        TechSpec techSpec = new TechSpec();
                        String formattedValue = WordUtils.capitalize(techSpecPropertyName.replace("_", " "), ' ', '/');
                        techSpec.setName(formattedValue);
                        techSpec.setValue(techSpecNode.get(techSpecPropertyName).asText());
                        techSpecList.add(techSpec);
                    }
                }
            }
        }

        if (!techSpecList.isEmpty()) {
            productDTO.setTechSpecifications(techSpecList);
        }

        //TODO: remove/update this line when Mincron products are used
        productDTO.setErp(ECLIPSE);

        return productDTO;
    }

    private List<String> getProductArray(JsonNode productDataNode, String JsonKeyName) {
        JsonNode arrNode = getProductField(productDataNode.get(JsonKeyName));
        List<String> arr = new ArrayList<>();
        for (JsonNode node : arrNode) {
            if (node.textValue() != null && !node.textValue().isEmpty()) {
                arr.add(node.textValue());
            }
        }
        return arr;
    }

    private JsonNode getProductField(JsonNode field) {
        return field.get(RAW);
    }

    /**
     * Checks the existence and nullness of the value in the given JsonNode with the given dataKey
     *
     * @param dataKey         key to validate within productDataNode
     * @param productDataNode product data key/value pairs
     * @return True if key exists in productDataNode and value is non-null; false otherwise
     */
    private boolean productDataValid(String dataKey, JsonNode productDataNode) {
        if (!productDataNode.has(dataKey)) return false;

        if (productDataNode.get(dataKey).has(RAW)) {
            return (
                !getProductField(productDataNode.get(dataKey)).asText().equals(NULL_CHARACTERS_SNOWFLAKE) &&
                !getProductField(productDataNode.get(dataKey)).asText().equals("") &&
                !getProductField(productDataNode.get(dataKey)).asText().equals(NULL_CHARACTERS_MINCRON)
            );
        } else {
            return (
                productDataNode.has(dataKey) &&
                !productDataNode.get(dataKey).asText().equals(NULL_CHARACTERS_SNOWFLAKE) &&
                !productDataNode.get(dataKey).asText().equals("") &&
                !productDataNode.get(dataKey).asText().equals(NULL_CHARACTERS_MINCRON)
            );
        }
    }

    /**
     * Retrieve categories for the given ERP
     *
     * @param erp optional erp to find categories for
     * @return category data structure
     */
    @Deprecated(since = "0.94", forRemoval = true)
    public CategoriesDTODeprecated getCategories(String erp) {
        if (erp != null) {
            ERPSystem erpSystem = ERPSystem.valueOf(erp);
            Optional<Categories> categories = categoriesDAO.findByErp(erp);

            if (categories.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            return new CategoriesDTODeprecated(erpSystem, categories.get().getValue());
        }
        List<Categories> allCategories = categoriesDAO.findAll();
        return new CategoriesDTODeprecated(allCategories);
    }

    /**
     * Fetch All LitsId and partnumber presents In List
     * @param erpPartNumbers   list of Part Numbers
     * @param erpAccountId shiftto account id
     * @return Set of PartNumbers
     */
    public List<String[]> getAllListIdsByPartNumbersAndErpAccountId(
        List<String> erpPartNumbers,
        String erpAccountId,
        ErpEnum erp
    ) {
        return listLineItemsDAO.findAllListIdsByPartNumbersAndErpAccountId(erpPartNumbers, erpAccountId, erp);
    }

    @SneakyThrows
    public List<ProductResponseDTO> getAllProducts() {
        try {
            log.info("ENTERING getAllProducts()");
            val start = Instant.now();
            List<ProductResponseDTO> productDTOS = dataWarehouseRepository.getAllProductsData();
            log.info(
                "EXITING getAllProducts(), duration- {}ms, Total Products- {}",
                Duration.between(start, Instant.now()).toMillis(),
                productDTOS.size()
            );
            return productDTOS;
        } catch (Exception e) {
            throw new ProductNotFoundException();
        }
    }

    /**
     * Get Pricing and Availability for Products
     * Optionally include product list information for customer
     * - Will omit products that do not include pricing or availability from eclipse
     * @param requestDTO
     * @return
     */
    public ProductPricingResponseDTO getPricing(ProductPricingRequestDTO requestDTO) {
        int partitionSize = requestDTO.getProductIds().size() > 50 ? 20 : 5;
        List<CompletableFuture<ProductPricingResponseDTO>> futures = new ArrayList<>();

        // Partition Eclipse Pricing and availability calls in parallel
        for (int i = 0; i < requestDTO.getProductIds().size(); i += partitionSize) {
            var partitionedList = requestDTO
                .getProductIds()
                .subList(i, Math.min(i + partitionSize, requestDTO.getProductIds().size()));
            CompletableFuture<ProductPricingResponseDTO> partitionedProducts = erpService.getProductPricingAsync(
                requestDTO.getCustomerId(),
                requestDTO.getBranchId(),
                partitionedList
            );
            futures.add(partitionedProducts);
        }

        futures.forEach(CompletableFuture::join);

        val products = new ArrayList<ProductPricingDTO>();
        futures.forEach(future -> {
            try {
                var subproducts = future
                    .get()
                    .getProducts()
                    .stream()
                    .peek(p -> {
                        if (p.getSellPrice() == null) {
                            p.setSellPrice(BigDecimal.ZERO);
                        }
                    })
                    .toList();
                products.addAll(subproducts);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        var pricingResponse = new ProductPricingResponseDTO();

        pricingResponse.setProducts(products);
        pricingResponse.setBranch(requestDTO.getBranchId());
        pricingResponse.setCustomerId(requestDTO.getCustomerId());

        // load in list data if asked for
        if (requestDTO.isIncludeListData()) {
            var erp = ErpEnum.ECLIPSE;
            var listIdsPerProduct = listLineItemsDAO.findAllListIdsByProductAndBillToAccount(
                requestDTO.getProductIds(),
                requestDTO.getCustomerId(),
                erp
            );

            List<ProductPricingDTO> updatedProducts = products
                .stream()
                .peek(product -> {
                    //Get listIds for matched partNumber
                    var listIds = new ArrayList<String>();
                    listIdsPerProduct
                        .stream()
                        .filter(p -> p[0].equals(product.getProductId()))
                        .forEach(p -> {
                            listIds.addAll(Arrays.asList(p[1].split(",")));
                        });
                    product.setListIds(listIds);
                })
                .toList();

            pricingResponse.setProducts(updatedProducts);
        }

        return pricingResponse;
    }

    /**
     * Get Product Inventory
     * @param productId
     * @return
     */
    public ProductInventoryResponseDTO getProductInventory(String productId) {
        return erpService.getProductInventory(productId);
    }

    public List<CustomerPartNumber> getCustomerPartNumbersList(JsonNode productDataNode) {
        val customerPartNumbersList = new ArrayList<CustomerPartNumber>();
        if (productDataNode != null && productDataNode.isArray()) {
            for (val item : productDataNode) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode node = objectMapper.readTree(item.asText());
                    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                    fields.forEachRemaining(field -> {
                        CustomerPartNumber customerPartNumber = new CustomerPartNumber();
                        customerPartNumber.setCustomer(field.getKey());
                        var rawValue = field.getValue();
                        val listValue = new ArrayList<String>();
                        if (rawValue != null && rawValue.isArray()) {
                            for (val listItem : rawValue) {
                                listValue.add(listItem.textValue());
                            }
                        }
                        customerPartNumber.setPartNumbers(listValue);
                        customerPartNumbersList.add(customerPartNumber);
                    });
                } catch (Exception e) {
                    log.error("Error in parsing customer part numbers: " + e.getMessage());
                }
            }
        }

        return customerPartNumbersList;
    }
}
