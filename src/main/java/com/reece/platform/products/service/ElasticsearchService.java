package com.reece.platform.products.service;

import static com.reece.platform.products.constants.ElasticsearchFieldNames.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reece.platform.products.constants.ElasticsearchFieldNames;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.model.DTO.ProductSearchFilterDTO;
import com.reece.platform.products.model.DTO.ProductSearchRequestDTO;
import com.reece.platform.products.model.QueryResult;
import com.reece.platform.products.utilities.FeaturesUtil;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    public ElasticsearchService(RestTemplate rt, AccountService accountService) {
        this.restTemplate = rt;
        this.accountService = accountService;
    }

    @Value("${elasticsearch_endpoint}")
    private String elasticsearchEndpoint;

    @Value("${elasticsearch_api_key}")
    private String elasticsearchApiKey;

    @Value("${postsearch.api.url:https://devpost.reece.com/api/search}")
    private String postSearchUrlTemplate;

    private final RestTemplate restTemplate;

    private final AccountService accountService;

    /**
     * Retrieve an individual product by id from Elasticsearch
     *
     * @param productId productId to query Elasticsearch on
     * @return string containing product data
     * @throws ElasticsearchException thrown when an error response is returned from Elasticsearch
     */
    public String getProductById(String productId) throws ElasticsearchException {
        ResponseEntity<String> productDataResponse = null;
        if (!FeaturesUtil.isPostEnabled(accountService.getFeatures())) {
            log.info("Get product from app search");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setBearerAuth(elasticsearchApiKey);
            HttpEntity<String> request = new HttpEntity<>(buildGetProductBody(productId), requestHeaders);
            productDataResponse = restTemplate.postForEntity(elasticsearchEndpoint, request, String.class);
        } else {
            log.info("Get product from post");
            productDataResponse = restTemplate.getForEntity(postSearchUrlTemplate + "/" + productId, String.class);
        }

        HttpStatus responseCode = productDataResponse.getStatusCode();

        if (responseCode != HttpStatus.OK) {
            throw new ElasticsearchException("Error response from Elasticsearch", responseCode);
        }

        return productDataResponse.getBody();
    }

    /**
     * Retrieve an individual product by mincron product number (MINCRON_PRODUCT_NUMBER) from Elasticsearch
     *
     * @param prodNum product number to query Elasticsearch on
     * @return string containing product data
     * @throws ElasticsearchException thrown when an error response is returned from Elasticsearch
     */
    public String getProductByMincronNumber(String prodNum) throws ElasticsearchException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(elasticsearchApiKey);
        HttpEntity<String> request = new HttpEntity<>(buildProductBodyByMincronNumber(prodNum), requestHeaders);
        ResponseEntity<String> productDataResponse = restTemplate.postForEntity(
            elasticsearchEndpoint,
            request,
            String.class
        );
        HttpStatus responseCode = productDataResponse.getStatusCode();

        if (responseCode != HttpStatus.OK) {
            throw new ElasticsearchException("Error response from Elasticsearch", responseCode);
        }

        return productDataResponse.getBody();
    }

    /**
     * Retrieve an individual product by eclipse product number (PRODUCT_ID) from Elasticsearch
     *
     * @param prodNum product number to query Elasticsearch on
     * @return string containing product data
     * @throws ElasticsearchException thrown when an error response is returned from Elasticsearch
     */
    public String getProductByEclipseNumber(String prodNum) throws ElasticsearchException {
        ResponseEntity<String> productDataResponse = null;
        if (!FeaturesUtil.isPostEnabled(accountService.getFeatures())) {
            log.info("Get product from app search");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setBearerAuth(elasticsearchApiKey);
            HttpEntity<String> request = new HttpEntity<>(buildProductBodyByEclipseNumber(prodNum), requestHeaders);
            productDataResponse = restTemplate.postForEntity(elasticsearchEndpoint, request, String.class);
        } else {
            log.info("Get product from post");
            productDataResponse = restTemplate.getForEntity(postSearchUrlTemplate + "/" + prodNum, String.class);
        }
        HttpStatus responseCode = productDataResponse.getStatusCode();

        if (responseCode != HttpStatus.OK) {
            throw new ElasticsearchException("Error response from Elasticsearch", responseCode);
        }

        return productDataResponse.getBody();
    }

    /**
     * Retrieve an individual product by eclipse product number (PRODUCT_ID) from Elasticsearch
     *
     * @param prodNum product number to query Elasticsearch on
     * @return string containing product data
     * @throws ElasticsearchException thrown when an error response is returned from Elasticsearch
     */
    public String getProductByEclipseNumberArray(ArrayNode prodNum) throws ElasticsearchException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(elasticsearchApiKey);
        HttpEntity<String> request = new HttpEntity<>(buildProductBodyByEclipseNumberArray(prodNum), requestHeaders);
        ResponseEntity<String> productDataResponse = restTemplate.postForEntity(
            elasticsearchEndpoint,
            request,
            String.class
        );
        HttpStatus responseCode = productDataResponse.getStatusCode();

        if (responseCode != HttpStatus.OK) {
            throw new ElasticsearchException("Error response from Elasticsearch", responseCode);
        }

        return productDataResponse.getBody();
    }

    /*
    FILTERING DESCRIPTION:

    Aggregation results for a given filter type should reflect what's available from the pool of products defined by
    all OTHER filters the user has selected. So, we require a separate search for each filter type just to get the
    summary results.

    STEPS:

    1. Get a list of all available filter types
    2a. For each filter type, run a new aggregate-only search. The aggregate-only search should include all selected
        filters EXCEPT for the current filter type.
    2b. Insert aggregate results for this filter type into the return search aggregate results.
    2c. Repeat for each filter type
     */

    /**
     * Retrieve products by query from Elasticsearch
     *
     * @param request Product search request
     * @return string containing product result data
     * @throws ElasticsearchException thrown when an error response is returned from Elasticsearch
     */
    @SneakyThrows
    public QueryResult getProductsByQuery(ProductSearchRequestDTO request) throws ElasticsearchException {
        // This should not happen, but just in case. Pages in App Search are 1-indexed
        if (request.getCurrentPage() == null || request.getCurrentPage() == 0) request.setCurrentPage(1);

        if (request.getCurrentPage() > MAX_PAGES) request.setCurrentPage(MAX_PAGES);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(elasticsearchApiKey);

        String searchUrl = String.format("%s?", elasticsearchEndpoint);

        HttpEntity<String> filteredRequest = new HttpEntity<>(buildSearchProductsBody(request), requestHeaders);

        CompletableFuture<HashMap<String, JsonNode>> facetNodesFuture = CompletableFuture.supplyAsync(() ->
            getFacets(request, requestHeaders, searchUrl)
        );
        CompletableFuture<ResponseEntity<String>> productDataResponseFuture = CompletableFuture.supplyAsync(() ->
            getQuery(searchUrl, filteredRequest)
        );

        var facetNodes = facetNodesFuture.get();
        var productDataResponse = productDataResponseFuture.get();

        HttpStatus responseCode = productDataResponse.getStatusCode();

        if (responseCode != HttpStatus.OK) {
            throw new ElasticsearchException("Error response from Elasticsearch", responseCode);
        }

        return new QueryResult(productDataResponse.getBody(), facetNodes);
    }

    private ResponseEntity<String> getQuery(String searchUrl, HttpEntity<String> filteredRequest) {
        return restTemplate.postForEntity(searchUrl, filteredRequest, String.class);
    }

    private HashMap<String, JsonNode> getFacets(
        ProductSearchRequestDTO request,
        HttpHeaders requestHeaders,
        String searchUrl
    ) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, JsonNode> aggregateNodes = new HashMap<>();

        // See FILTERING DESCRIPTION note above for logic notes
        getFacetTypes()
            .parallelStream()
            .forEach(filterName -> {
                try {
                    ProductSearchRequestDTO partialFilterSearchRequestDTO = new ProductSearchRequestDTO(
                        request,
                        filterName
                    );
                    HttpEntity<String> partialAggRequest = new HttpEntity<>(
                        buildSearchProductsBody(partialFilterSearchRequestDTO),
                        requestHeaders
                    );
                    ResponseEntity<String> partialAggResponse = restTemplate.postForEntity(
                        searchUrl,
                        partialAggRequest,
                        String.class
                    );
                    JsonNode partialAggNode = mapper.readTree(partialAggResponse.getBody());
                    JsonNode partialAggResult = partialAggNode.get(FACETS).get(filterName);
                    aggregateNodes.put(filterName, partialAggResult);
                } catch (JsonProcessingException ignored) {}
            });
        return aggregateNodes;
    }

    /**
     * Builds a body for the POST call to ES in the following format:
     * {
     *   "query": "[query]",
     *   "page": {
     *     "current": x,
     *     "size": y
     *   },
     *   "facets": {
     *     "product_type": {
     *       "type": "value"
     *     }
     *     "ts_mfr_fullname": {
     *       "type": "value"
     *     }
     *   },
     *   "search_filters": {
     *       "vendor_part_nbr": {},
     *       "web_description": {},
     *       "customer_part_number": {},
     *       "mfr_full_name": {},
     *   }
     * }
     * @return body ObjectNode
     */
    private String buildSearchProductsBody(ProductSearchRequestDTO request) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode body = mapper.createObjectNode();
        Integer size = request.getPageSize() != null ? request.getPageSize() : 0;

        body.set(PAGE, buildPagination(size, request.getCurrentPage()));
        body.put(QUERY, request.getSearchTerm());
        body.set(FILTERS, buildProductSearchFilters(request, mapper));
        body.set(FACETS, buildFacets(mapper));
        body.set(SEARCH_FIELDS, buildSearchFields(mapper));
        if (request.getResultFields() != null) {
            body.set(RESULT_FIELDS, buildResultFields(request.getResultFields(), mapper));
        }

        return body.toString();
    }

    private JsonNode buildPagination(Integer size, Integer currentPage) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode pagination = mapper.createObjectNode();

        pagination.put(SIZE, size);
        if (currentPage != null) {
            pagination.put(CURRENT, currentPage);
        }
        return pagination;
    }

    private ObjectNode buildProductSearchFilters(ProductSearchRequestDTO request, ObjectMapper mapper) {
        if (request.getSelectedAttributes() != null) {
            for (Object attribute : request.getSelectedAttributes()) {
                ProductSearchFilterDTO productSearchFilterDTO = (ProductSearchFilterDTO) attribute;
                if (productSearchFilterDTO.getAttributeType().equals("environmentalOption")) {
                    productSearchFilterDTO.setAttributeType((String) productSearchFilterDTO.getAttributeValue());
                    productSearchFilterDTO.setAttributeValue("true");
                }
                if (productSearchFilterDTO.getAttributeType().equals("inStockLocation")) {
                    productSearchFilterDTO.setAttributeValue(request.getSelectedBranchId());
                }
            }
        }

        List<ProductSearchFilterDTO> selectedCategories = request.getSelectedCategories() != null
            ? request.getSelectedCategories()
            : Collections.emptyList();
        List<ProductSearchFilterDTO> selectedAttributes = request.getSelectedAttributes() != null
            ? request.getSelectedAttributes()
            : Collections.emptyList();
        List<ProductSearchFilterDTO> filters = Stream
            .concat(selectedCategories.stream(), selectedAttributes.stream())
            .toList();

        ObjectNode queryFilters = mapper.createObjectNode();
        ArrayNode all = mapper.createArrayNode();

        if (!filters.isEmpty()) {
            Map<String, List<ProductSearchFilterDTO>> filtersMap = filters
                .stream()
                .collect(Collectors.groupingBy(ProductSearchFilterDTO::getAttributeType));

            for (Map.Entry<String, List<ProductSearchFilterDTO>> entry : filtersMap.entrySet()) {
                List<String> filterSelections = entry
                    .getValue()
                    .stream()
                    .map(filterValue -> (String) filterValue.getAttributeValue())
                    .toList();
                ArrayNode filtersNode = mapper.createArrayNode();
                filterSelections.forEach(filtersNode::add);
                all.add(
                    mapper
                        .createObjectNode()
                        .set(new ElasticsearchFieldNames().AttributeTypeNames.get(entry.getKey()), filtersNode)
                );
            }
        }

        queryFilters.set(ALL, all);

        return queryFilters;
    }

    private ObjectNode buildFacets(ObjectMapper mapper) {
        ObjectNode facets = mapper.createObjectNode();

        /* TODO - refactor some of this so that the aggregation names and DTO names are more similar, or more
        obviously mapped to each other in code. */
        getFacetTypes().forEach(filterType -> facets.set(filterType, buildValueFacet(mapper)));

        return facets;
    }

    private ObjectNode buildSearchFields(ObjectMapper mapper) {
        val searchFields = mapper.createObjectNode();

        for (val searchField : SEARCH_FIELD_NAMES) {
            searchFields.set(searchField, mapper.createObjectNode());
        }

        return searchFields;
    }

    private List<String> getFacetTypes() {
        return Arrays.asList(
            MFR_FULL_NAME,
            FLOW_RATE,
            PRODUCT_LINE,
            CATEGORY_1_NAME,
            CATEGORY_2_NAME,
            CATEGORY_3_NAME,
            LOW_LEAD_COMPLIANT_FLAG,
            HAZARDOUS_MATERIAL_FLAG,
            MERCURY_FREE_FLAG,
            WATER_SENSE_COMPLIANT_FLAG,
            ENERGY_STAR_FLAG,
            MATERIAL,
            COLORFINISH,
            SIZE,
            LENGTH,
            WIDTH,
            HEIGHT,
            DEPTH,
            VOLTAGE,
            TONNAGE,
            BTU,
            PRESSURE_RATING,
            TEMPERATURE_RATING,
            INLET_SIZE,
            CAPACITY,
            WATTAGE,
            IN_STOCK_LOCATION
        );
    }

    /**
     * Builds a facet for a search request, which at this point is a generic "type": "value"
     * @param  mapper an ObjectMapper
     * @return facet ObjectNode
     */
    private ObjectNode buildValueFacet(ObjectMapper mapper) {
        ObjectNode facet = mapper.createObjectNode();
        facet.put(TYPE, VALUE);
        facet.put(SIZE, 250);

        return facet;
    }

    private ObjectNode buildResultFields(List<String> resultFieldList, ObjectMapper mapper) {
        ObjectNode resultFields = mapper.createObjectNode();
        ObjectNode raw = mapper.createObjectNode();
        ObjectNode emptyObj = mapper.createObjectNode();
        raw.set(RAW, emptyObj);
        resultFieldList.forEach(resultField -> resultFields.set(resultField, raw));
        return resultFields;
    }

    /**
     * Builds a body for the GET call to ES
     * @param productId product to search on
     * @return body ObjectNode
     */
    private static String buildGetProductBody(String productId) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filters = mapper.createObjectNode();
        filters.put(ERP_PRODUCT_ID, productId);

        ObjectNode body = mapper.createObjectNode();
        body.put(QUERY, "");
        body.set(FILTERS, filters);

        return body.toString();
    }

    /**
     * Builds a body for the GET call to ES in the following format:
     * @param prodNum product to search on
     * @return body ObjectNode
     */
    private static String buildProductBodyByMincronNumber(String prodNum) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filters = mapper.createObjectNode();
        filters.put(MINCRON_PRODUCT_NUMBER, prodNum);

        ObjectNode body = mapper.createObjectNode();
        body.put(QUERY, "");
        body.set(FILTERS, filters);

        return body.toString();
    }

    /**
     * Builds a body for the GET call to ES
     * @param prodNum product to search on
     * @return body ObjectNode
     */
    private static String buildProductBodyByEclipseNumber(String prodNum) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filters = mapper.createObjectNode();
        filters.put(ID, prodNum);

        ObjectNode body = mapper.createObjectNode();
        body.put(QUERY, "");
        body.set(FILTERS, filters);
        return body.toString();
    }

    /**
     * Builds a body for the GET call to ES
     * @param prodNum products ArrayNode to search on
     * @return body ObjectNode
     */
    private String buildProductBodyByEclipseNumberArray(ArrayNode prodNum) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filters = mapper.createObjectNode();
        filters.set(ID, prodNum);

        ObjectNode body = mapper.createObjectNode();
        body.set(PAGE, buildPagination(prodNum.size(), 1));
        body.put(QUERY, "");
        body.set(FILTERS, filters);
        return body.toString();
    }
}
