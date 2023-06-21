package com.reece.platform.products.search;

import com.reece.platform.products.constants.ElasticsearchFieldNames;
import com.reece.platform.products.external.appsearch.AppSearchClient;
import com.reece.platform.products.external.appsearch.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.SearchEngineEnum;
import com.reece.platform.products.search.mapper.ProductDTOMapper;
import com.reece.platform.products.search.mapper.SearchSuggestionResponseMapper;
import com.reece.platform.products.search.model.EnvironmentalOption;
import com.reece.platform.products.search.model.FacetName;
import com.reece.platform.products.search.model.SearchFieldName;
import com.reece.platform.products.service.AccountService;
import com.reece.platform.products.service.CustomerProductInfoService;
import com.reece.platform.products.service.ProductService;
import com.reece.platform.products.utilities.FeaturesUtil;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SearchService {

    public static final int MAX_PAGES = 100;
    private static final String ATTRIBUTE_TYPE_ENVIRONMENTAL_OPTION = "environmentalOption";
    private static final String ATTRIBUTE_TYPE_IN_STOCK_LOCATION = "inStockLocation";
    private static final String ATTRIBUTE_TYPE_TERRITORY_EXCLUSION_LIST = "territory_exclusion_list";
    private static final String ATTRIBUTE_TYPE_MINCRON_PRODUCT_NUMBER = "mincron_product_number";
    private static final String NULL_CHARACTERS_MINCRON = "\u0000";
    private final RestTemplate restTemplate;

    private static final Filter.Combination FILTER_MINCRON_MISSING_ID = Filter.none(
        Stream.of(Filter.values(ATTRIBUTE_TYPE_MINCRON_PRODUCT_NUMBER, NULL_CHARACTERS_MINCRON))
    );

    private final AppSearchClient appSearchClient;
    private final ProductService productService;
    private final ProductDTOMapper productDTOMapper;
    private final SearchSuggestionResponseMapper searchSuggestionResponseMapper;
    private final CustomerProductInfoService customerProductInfoService;
    private final String plumbingHVACEngineName;
    private final String bathAndKitchenEngineName;
    private final String waterWorksEngineName;
    private boolean isBathAndKitchenSearch = false;
    private boolean isWaterworksSearch = false;
    private final AccountService accountService;

    @Value("${postsearch.api.url:https://devpost.reece.com/api/search}")
    private String postSearchUrlTemplate;

    @Value("${postsearch.enginename:post-max}")
    private String postSearchEngineName;

    public SearchService(
        AppSearchClient appSearchClient,
        ProductService productService,
        AccountService accountService,
        ProductDTOMapper productDTOMapper,
        SearchSuggestionResponseMapper searchSuggestionResponseMapper,
        CustomerProductInfoService customerProductInfoService,
        @Value("${appsearch.phvac.enginename}") String plumbingHVACEngineName,
        @Value("${appsearch.bk.enginename}") String bathAndKitchenEngineName,
        @Value("${appsearch.waterworks.enginename}") String waterWorksEngineName,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.appSearchClient = appSearchClient;
        this.productService = productService;
        this.productDTOMapper = productDTOMapper;
        this.searchSuggestionResponseMapper = searchSuggestionResponseMapper;
        this.customerProductInfoService = customerProductInfoService;
        this.plumbingHVACEngineName = plumbingHVACEngineName;
        this.bathAndKitchenEngineName = bathAndKitchenEngineName;
        this.waterWorksEngineName = waterWorksEngineName;
        this.accountService = accountService;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<ProductDTO> getProductsByCustomerPartNumber(
        String engineName,
        Collection<String> erpPartNumbers,
        PageRequest pageRequest
    ) {
        val request = new SearchRequest();
        request.page(pageRequest);

        request.filter(Filter.all(Stream.of(Filter.values("customer_part_number", erpPartNumbers))));

        val response = appSearchClient.search(engineName, request);

        return response
            .getResults()
            .stream()
            .map(productDTOMapper::searchResultToProductDTO)
            .collect(Collectors.toList());
    }

    public ProductSearchResponseDTO getProductsByQuery(ProductSearchRequestDTO request, UUID userId) {
        if (userId != null) {
            request.setSelectedBranchId(
                productService.getSelectedCartBranch(userId, request.getShipToId()).orElse(null)
            );
        }

        normalizePaginationParameters(request);
        normalizeEnvironmentalFilters(request);
        updateInStockLocation(request);
        String engineName = plumbingHVACEngineName;
        if (request.getEngine() != null && !request.getEngine().isEmpty()) {
            if (request.getEngine().equalsIgnoreCase(String.valueOf(SearchEngineEnum.bath_kitchen))) {
                engineName = bathAndKitchenEngineName;
                isBathAndKitchenSearch = true;
                isWaterworksSearch = false;
            } else if (request.getEngine().equalsIgnoreCase(String.valueOf(SearchEngineEnum.waterworks))) {
                engineName = waterWorksEngineName;
                isWaterworksSearch = true;
                isBathAndKitchenSearch = false;
            } else {
                isBathAndKitchenSearch = false;
                isWaterworksSearch = false;
            }
        }

        if (
            FeaturesUtil.isPostEnabled(accountService.getFeatures()) &&
            request.getEngine().equalsIgnoreCase("plumbing_hvac")
        ) {
            SearchRequest searchRequest = buildPostMaxSearchRequest(request);
            log.info("Post search url: " + postSearchUrlTemplate);
            val searchResponse = restTemplate
                .postForEntity(postSearchUrlTemplate, searchRequest, SearchResponse.class)
                .getBody();

            val partialAggResults = buildAggregateSearchRequests(request)
                .entrySet()
                .parallelStream()
                .map(entry -> {
                    val filterName = entry.getKey();
                    val aggregateSearchRequest = entry.getValue();
                    val aggregateResponse = restTemplate
                        .postForEntity(postSearchUrlTemplate, aggregateSearchRequest, SearchResponse.class)
                        .getBody();
                    return new AbstractMap.SimpleEntry<>(filterName, aggregateResponse);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            return buildProductSearchResponseDTO(request, searchResponse, partialAggResults);
        } else {
            val searchEngine = engineName;
            SearchRequest searchRequest = buildSearchRequest(request);
            val searchResponse = appSearchClient.search(searchEngine, searchRequest);

            val partialAggResults = buildAggregateSearchRequests(request)
                .entrySet()
                .parallelStream()
                .map(entry -> {
                    val filterName = entry.getKey();
                    val aggregateSearchRequest = entry.getValue();
                    val aggregateResponse = appSearchClient.search(searchEngine, aggregateSearchRequest);
                    return new AbstractMap.SimpleEntry<>(filterName, aggregateResponse);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

            return buildProductSearchResponseDTO(request, searchResponse, partialAggResults);
        }
    }

    public SearchSuggestionResponseDTO getSuggestedSearch(String term, String engine, int size, String state) {
        val request = new QuerySuggestionRequest(
            term,
            size,
            Arrays.stream(SearchFieldName.values()).map(SearchFieldName::toString).collect(Collectors.toList())
        );

        String engineName = plumbingHVACEngineName;
        if (engine != null && !engine.isEmpty()) {
            if (engine.equalsIgnoreCase(String.valueOf(SearchEngineEnum.bath_kitchen))) {
                engineName = bathAndKitchenEngineName;
                isBathAndKitchenSearch = true;
                isWaterworksSearch = false;
            } else if (engine.equalsIgnoreCase(String.valueOf(SearchEngineEnum.waterworks))) {
                engineName = waterWorksEngineName;
                isWaterworksSearch = true;
                isBathAndKitchenSearch = false;
            } else {
                isBathAndKitchenSearch = false;
                isWaterworksSearch = false;
            }
        }
        val searchEngine = engineName;

        val response = appSearchClient.querySuggestion(searchEngine, request);

        val searchSuggestionResponse = searchSuggestionResponseMapper.querySuggestionResponseToSearchSuggestionResponseDTO(
            response
        );

        response
            .getResults()
            .getDocuments()
            .stream()
            .findFirst()
            .ifPresentOrElse(
                document -> {
                    val suggestion = document.getSuggestion();
                    buildSearchSuggestionRequest(searchSuggestionResponse, suggestion, state, searchEngine);
                },
                () -> {
                    buildSearchSuggestionRequest(searchSuggestionResponse, term, state, searchEngine);
                }
            );
        return searchSuggestionResponse;
    }

    private SearchSuggestionResponseDTO buildSearchSuggestionRequest(
        SearchSuggestionResponseDTO searchSuggestionResponse,
        String term,
        String state,
        String engine
    ) {
        val searchRequest = new SearchRequest()
            .query(term)
            .page(new PageRequest(3, 1))
            .resultField("thumbnail_image_url_name", new ResultField())
            .resultField("vendor_part_nbr", new ResultField())
            .resultField("mfr_full_name", new ResultField())
            .resultField("web_description", new ResultField())
            .resultField("erp_product_id", new ResultField());

        for (val facet : FacetName.values()) {
            // TODO: extract magic number

            if (isBathAndKitchenSearch) {
                if ((facet.toString().equalsIgnoreCase("tonnage") || facet.toString().equalsIgnoreCase("btu"))) {
                    continue;
                }
            }
            if (isWaterworksSearch) {
                if (
                    (
                        facet.toString().equalsIgnoreCase("wattage") ||
                        facet.toString().equalsIgnoreCase("water_sense_compliant_flag") ||
                        facet.toString().equalsIgnoreCase("mercury_free_flag") ||
                        facet.toString().equalsIgnoreCase("energy_star_flag") ||
                        facet.toString().equalsIgnoreCase("voltage")
                    )
                ) {
                    continue;
                }
            }
            searchRequest.facet(facet.toString(), new Facet().size(250));
        }

        if (!FeaturesUtil.isPostEnabled(accountService.getFeatures())) {
            for (val searchField : SearchFieldName.values()) {
                searchRequest.searchField(searchField.toString(), new SearchField());
            }
        }
        searchRequest.filter(buildExclusionFilters(state));
        SearchResponse searchResponse = null;
        if (
            FeaturesUtil.isPostEnabled(accountService.getFeatures()) && !isBathAndKitchenSearch && !isWaterworksSearch
        ) {
            log.info("search suggestion using post");
            searchResponse =
                restTemplate.postForEntity(postSearchUrlTemplate, searchRequest, SearchResponse.class).getBody();
        } else {
            log.info("search suggestion using app search");
            searchResponse = appSearchClient.search(engine, searchRequest);
        }

        if (searchResponse != null) {
            val topProducts = searchResponse
                .getResults()
                .stream()
                .map(productDTOMapper::searchResultToProductDTO)
                .collect(Collectors.toList());

            val topCategories = searchResponse
                .getFacets()
                .get(FacetName.CATEGORY_1_NAME.toString())
                .get(0)
                .getData()
                .stream()
                .map((data -> new AggregationResponseItemDTO(data.getValue(), data.getCount())))
                .sorted(Comparator.comparing(AggregationResponseItemDTO::getCount).reversed())
                .limit(2)
                .collect(Collectors.toList());

            searchSuggestionResponse.setTopProducts(topProducts);
            searchSuggestionResponse.setTopCategories(topCategories);
        } else {
            searchSuggestionResponse.setTopProducts(Collections.emptyList());
            searchSuggestionResponse.setTopCategories(Collections.emptyList());
        }
        return searchSuggestionResponse;
    }

    @CacheEvict("categories")
    public void evictCategoryCache(String engine) {}

    @Cacheable("categories")
    public CategoriesDTO getCategories(String engine) {
        val categories = new CategoriesDTO();
        val topLevelCategories = getFacetValues(List.of(FacetName.CATEGORY_1_NAME), Collections.emptyList(), engine)
            .stream()
            .map(CategoryDTO::new)
            .filter(CategoryDTO::isVisibleCategory)
            .collect(Collectors.toList());

        val predefinedTopLevelCategories = new ArrayList<String>();
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.PIPE_AND_FITTINGS);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.HEATING_AND_COOLING);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.BATH);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.KITCHEN_BAR_LAUNDRY);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.APPLIANCES);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.PLUMBING_INSTALLATION_AND_HARDWARE);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.WATER_HEATERS);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.VALVES);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.IRRIGATION_PUMPS_FILTERS);
        predefinedTopLevelCategories.add(ElasticsearchFieldNames.TOOLS_AND_SAFETY);

        List<CategoryDTO> sortedTopLevelCategories = new ArrayList<>();
        for (String categoryName : predefinedTopLevelCategories) {
            for (val category : topLevelCategories) {
                if (categoryName.equals(category.getName())) {
                    sortedTopLevelCategories.add(category);
                }
            }
        }

        for (val category : sortedTopLevelCategories) {
            val secondLevelCategories = getFacetValues(
                List.of(FacetName.CATEGORY_1_NAME, FacetName.CATEGORY_2_NAME),
                List.of(category.getName()),
                engine
            )
                .stream()
                .map(CategoryDTO::new)
                .filter(CategoryDTO::isVisibleCategory)
                .collect(Collectors.toList());

            for (val categoryTwo : secondLevelCategories) {
                val thirdLevelCategories = getFacetValues(
                    List.of(FacetName.CATEGORY_1_NAME, FacetName.CATEGORY_2_NAME, FacetName.CATEGORY_3_NAME),
                    List.of(category.getName(), categoryTwo.getName()),
                    engine
                )
                    .stream()
                    .map(CategoryDTO::new)
                    .filter(CategoryDTO::isVisibleCategory)
                    .collect(Collectors.toList());

                categoryTwo.setChildren(thirdLevelCategories);
            }

            category.setChildren(secondLevelCategories);
        }

        categories.setCategories(sortedTopLevelCategories);

        return categories;
    }

    private List<String> getFacetValues(List<FacetName> facets, List<String> filterValues, String engine) {
        // Base case, stop when the number of filters is one less than the number of facets
        val request = new SearchRequest().query("");
        PageRequest page = new PageRequest(1, 1);
        request.setPage(page);

        facets.subList(0, filterValues.size() + 1).stream().forEach(f -> request.facet(f.toString(), new Facet()));

        if (!filterValues.isEmpty()) {
            request.filter(
                Filter.all(
                    IntStream
                        .range(0, filterValues.size())
                        .mapToObj(i ->
                            Filter.value(
                                facets.get(i).toString(),
                                new ArrayList<String>(Arrays.asList(filterValues.get(i)))
                            )
                        )
                )
            );
        }

        String engineName = plumbingHVACEngineName;
        if (engine != null && !engine.isEmpty()) {
            if (engine.equalsIgnoreCase(String.valueOf(SearchEngineEnum.bath_kitchen))) {
                engineName = bathAndKitchenEngineName;
            } else if (engine.equalsIgnoreCase(String.valueOf(SearchEngineEnum.waterworks))) {
                engineName = waterWorksEngineName;
            }
        }

        // NOTE: eventually we will want to pass an enum for the product line that will be used to identify the engine
        SearchResponse response = new SearchResponse();
        if (
            FeaturesUtil.isPostEnabled(accountService.getFeatures()) &&
            engine.equalsIgnoreCase(String.valueOf(SearchEngineEnum.plumbing_hvac))
        ) {
            log.info("Getting categories from post service");
            response = restTemplate.postForEntity(postSearchUrlTemplate, request, SearchResponse.class).getBody();
        } else {
            log.info("Getting categories from app search");
            response = appSearchClient.search(engineName, request);
        }
        return response
            .getFacets()
            .get(facets.get(filterValues.size()).toString())
            .stream()
            .findFirst()
            .get()
            .getData()
            .stream()
            .map(d -> d.getValue())
            .collect(Collectors.toList());
    }

    private SearchRequest buildSearchRequest(ProductSearchRequestDTO request) {
        val searchRequest = new SearchRequest()
            .page(new PageRequest(request.getPageSize(), request.getCurrentPage()))
            .query(request.getSearchTerm());

        searchRequest.filter(
            Filter.all(
                extractAllFilters(request)
                    .entrySet()
                    .stream()
                    .map(filter -> Filter.values(filter.getKey().toString(), filter.getValue()))
            )
        );

        searchRequest.filter(buildExclusionFilters(request.getState()));

        for (val facet : FacetName.values()) {
            // TODO: extract magic number
            if (isBathAndKitchenSearch || isWaterworksSearch) {
                if ((facet.toString().equalsIgnoreCase("tonnage") || facet.toString().equalsIgnoreCase("btu"))) {
                    continue;
                }
            }
            if (isWaterworksSearch) {
                if (
                    (
                        facet.toString().equalsIgnoreCase("wattage") ||
                        facet.toString().equalsIgnoreCase("water_sense_compliant_flag") ||
                        facet.toString().equalsIgnoreCase("mercury_free_flag") ||
                        facet.toString().equalsIgnoreCase("energy_star_flag") ||
                        facet.toString().equalsIgnoreCase("voltage")
                    )
                ) {
                    continue;
                }
            }

            searchRequest.facet(facet.toString(), new Facet().size(250));
        }

        for (val searchField : SearchFieldName.values()) {
            searchRequest.searchField(searchField.toString(), new SearchField());
        }

        if (request.getResultFields() != null) {
            for (val resultField : request.getResultFields()) {
                searchRequest.resultField(resultField, new ResultField());
            }
        }

        return searchRequest;
    }

    private SearchRequest buildPostMaxSearchRequest(ProductSearchRequestDTO request) {
        val searchRequest = new SearchRequest()
            .page(new PageRequest(request.getPageSize(), request.getCurrentPage()))
            .query(request.getSearchTerm());

        searchRequest.filter(
            Filter.all(
                extractAllFilters(request)
                    .entrySet()
                    .stream()
                    .map(filter -> Filter.values(filter.getKey().toString().toUpperCase(), filter.getValue()))
            )
        );

        for (val facet : FacetName.values()) {
            // TODO: extract magic number
            searchRequest.facet(facet.toString().toUpperCase(), new Facet().size(250));
        }
        if (request.getResultFields() != null) {
            for (val resultField : request.getResultFields()) {
                searchRequest.resultField(resultField.toUpperCase(), new ResultField());
            }
        }

        return searchRequest;
    }

    /**
     * Ensure that the current page is not null, > 0 and <= MAX_PAGES
     * and the page size is not null
     */
    private void normalizePaginationParameters(ProductSearchRequestDTO productSearchRequestDTO) {
        if (productSearchRequestDTO.getCurrentPage() == null || productSearchRequestDTO.getCurrentPage() == 0) {
            productSearchRequestDTO.setCurrentPage(1);
        }

        if (productSearchRequestDTO.getCurrentPage() > MAX_PAGES) {
            productSearchRequestDTO.setCurrentPage(MAX_PAGES);
        }

        if (productSearchRequestDTO.getPageSize() == null) {
            productSearchRequestDTO.setPageSize(0);
        }
    }

    /**
     * Environmental options come in the form
     * <pre>
     * {@code
     * { "environmentalOption": "$SOME_OPTION_NAME" }
     * }
     * </pre>
     *
     * But AppSearch expects them in this form:
     * <pre>
     * {@code
     * { "$SOME_OPTION_NAME": "true" }
     * }
     * </pre>
     *
     */
    private void normalizeEnvironmentalFilters(ProductSearchRequestDTO request) {
        if (request.getSelectedAttributes() == null) {
            return;
        }

        for (val attribute : request.getSelectedAttributes()) {
            if (ATTRIBUTE_TYPE_ENVIRONMENTAL_OPTION.equals(attribute.getAttributeType())) {
                attribute.setAttributeType(attribute.getAttributeValue());
                attribute.setAttributeValue(Boolean.TRUE.toString());
            }
        }
    }

    /**
     * If present, updates the value of the {@code inStockLocation } attribute to be the selected branch id
     */
    private void updateInStockLocation(ProductSearchRequestDTO request) {
        if (request.getSelectedAttributes() == null) {
            return;
        }

        for (val attribute : request.getSelectedAttributes()) {
            if (ATTRIBUTE_TYPE_IN_STOCK_LOCATION.equals(attribute.getAttributeType())) {
                attribute.setAttributeValue(request.getSelectedBranchId());
                break;
            }
        }
    }

    /**
     * Combine `selectedCategories` and `selectedAttributes` into one collection of filters in the format
     * {@code "filter_name": [...filter_values]}
     */
    private Map<FacetName, Collection<String>> extractAllFilters(ProductSearchRequestDTO request) {
        val filtersMap = new ArrayListValuedHashMap<FacetName, String>();

        if (request.getSelectedCategories() != null) {
            for (val category : request.getSelectedCategories()) {
                val attributeType = category.getAttributeType();
                val attributeValue = category.getAttributeValue();

                val facetName = FacetName
                    .fromClientName(attributeType)
                    .orElseThrow(() -> new IllegalArgumentException("unknown facet name: '" + attributeType + "'"));

                filtersMap.put(facetName, attributeValue);
            }
        }

        if (request.getSelectedAttributes() != null) {
            for (val attribute : request.getSelectedAttributes()) {
                val attributeType = attribute.getAttributeType();
                val attributeValue = attribute.getAttributeValue();

                val facetName = FacetName
                    .fromClientName(attributeType)
                    .orElseThrow(() -> new IllegalArgumentException("unknown facet name: '" + attributeType + "'"));

                filtersMap.put(facetName, attributeValue);
            }
        }

        return filtersMap.asMap();
    }

    /**
     * Builds the exclusion filter for elastic search
     * Currently used only for excluding products based on territory
     * @param state
     * @return
     */
    private Filter.Combination buildExclusionFilters(String state) {
        val filtersMap = new ArrayListValuedHashMap<String, String>();

        if (state != null && !state.isEmpty()) {
            filtersMap.put(ATTRIBUTE_TYPE_TERRITORY_EXCLUSION_LIST, state);
        }

        return Filter.none(
            filtersMap.asMap().entrySet().stream().map(filter -> Filter.values(filter.getKey(), filter.getValue()))
        );
    }

    private Map<String, SearchRequest> buildAggregateSearchRequests(ProductSearchRequestDTO request) {
        val aggregateRequests = new HashMap<String, SearchRequest>();

        request
            .getSelectedAttributes()
            .stream()
            .map(ProductSearchFilterDTO::getAttributeType)
            .forEach(filterName -> {
                val partialRequestDTO = new ProductSearchRequestDTO(request, filterName, true);
                val searchRequest = (
                        FeaturesUtil.isPostEnabled(accountService.getFeatures()) &&
                        !isBathAndKitchenSearch &&
                        !isWaterworksSearch
                    )
                    ? buildPostMaxSearchRequest(partialRequestDTO)
                    : buildSearchRequest(partialRequestDTO);
                aggregateRequests.put(FacetName.fromClientName(filterName).orElseThrow().toString(), searchRequest);
            });

        return aggregateRequests;
    }

    private ProductSearchResponseDTO buildProductSearchResponseDTO(
        ProductSearchRequestDTO request,
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults
    ) {
        val productSearchResponse = new ProductSearchResponseDTO();
        if (request.getSelectedAttributes() != null) {
            productSearchResponse.setSelectedAttributes(new ArrayList<>(request.getSelectedAttributes()));
        }

        if (request.getSelectedCategories() != null) {
            productSearchResponse.setSelectedCategories(new ArrayList<>(request.getSelectedCategories()));
        }

        productSearchResponse.setCategoryLevel(request.getCategoryLevel());

        val totalCount = Math.min(
            searchResponse.getMeta().getPage().getTotalResults(),
            request.getPageSize() * MAX_PAGES
        );
        if (totalCount == 0) {
            productSearchResponse.setProducts(Collections.emptyList());
            return productSearchResponse;
        }

        val productDTOs = searchResponse
            .getResults()
            .stream()
            .map(productDTOMapper::searchResultToProductDTO)
            .collect(Collectors.toList());

        val paginationResponseDTO = new PaginationResponseDTO(
            totalCount,
            request.getPageSize(),
            request.getCurrentPage()
        );

        val aggregationResponseDTO = new AggregationResponseDTO();

        aggregationResponseDTO.setBrands(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.MFR_FULL_NAME)
        );
        aggregationResponseDTO.setLines(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.PRODUCT_LINE)
        );
        aggregationResponseDTO.setFlowRate(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.FLOW_RATE)
        );
        aggregationResponseDTO.setEnvironmentalOptions(
            buildEnvironmentalOptionAggregationResponseItems(searchResponse, partialAggResults)
        );
        aggregationResponseDTO.setInStockLocation(
            buildInStockLocationAggregationResponseItems(
                searchResponse,
                partialAggResults,
                request.getSelectedBranchId()
            )
        );
        aggregationResponseDTO.setMaterial(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.MATERIAL)
        );
        aggregationResponseDTO.setColorFinish(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.COLORFINISH)
        );

        aggregationResponseDTO.setSize(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.SIZE)
        );
        aggregationResponseDTO.setLength(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.LENGTH)
        );
        aggregationResponseDTO.setWidth(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.WIDTH)
        );
        aggregationResponseDTO.setHeight(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.HEIGHT)
        );
        aggregationResponseDTO.setDepth(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.DEPTH)
        );
        if (!isWaterworksSearch) {
            aggregationResponseDTO.setVoltage(
                buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.VOLTAGE)
            );
            aggregationResponseDTO.setWattage(
                buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.WATTAGE)
            );
        }
        if (!isBathAndKitchenSearch && !isWaterworksSearch) {
            aggregationResponseDTO.setTonnage(
                buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.TONNAGE)
            );
            aggregationResponseDTO.setBtu(
                buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.BTU)
            );
        }
        aggregationResponseDTO.setPressureRating(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.PRESSURE_RATING)
        );
        aggregationResponseDTO.setTemperatureRating(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.TEMPERATURE_RATING)
        );
        aggregationResponseDTO.setInletSize(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.INLET_SIZE)
        );
        aggregationResponseDTO.setCapacity(
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.CAPACITY)
        );

        final int categoryLevel = Optional.ofNullable(request.getCategoryLevel()).orElse(0);

        if (categoryLevel < 3) {
            aggregationResponseDTO.setCategory(
                buildNextCategoryAggregationResponseItems(searchResponse, partialAggResults, categoryLevel)
            );
        }

        productSearchResponse.setPagination(paginationResponseDTO);
        productSearchResponse.setProducts(productDTOs);
        productSearchResponse.setAggregates(aggregationResponseDTO);

        return productSearchResponse;
    }

    private List<AggregationResponseItemDTO> buildAggregationResponseItems(
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults,
        FacetName facetName
    ) {
        if (
            (partialAggResults == null || !partialAggResults.containsKey(facetName.toString())) &&
            searchResponse.getFacets() != null
        ) {
            String facet = searchResponse.getFacets().containsKey(facetName.toString().toUpperCase())
                ? facetName.toString().toUpperCase()
                : facetName.toString();
            if (
                searchResponse.getFacets().containsKey(facetName.toString().toUpperCase()) ||
                searchResponse.getFacets().containsKey(facetName.toString())
            ) {
                return searchResponse
                    .getFacets()
                    .get(facet)
                    .get(0)
                    .getData()
                    .stream()
                    .map(bucket -> new AggregationResponseItemDTO(bucket.getValue(), bucket.getCount()))
                    .filter(AggregationResponseItemDTO::isVisibleCategory)
                    .collect(Collectors.toList());
            } else {
                return null;
            }
        }

        String facet = partialAggResults.containsKey(facetName.toString().toUpperCase())
            ? facetName.toString().toUpperCase()
            : facetName.toString();

        if (partialAggResults.get(facet).getFacets().containsKey(facet)) {
            return partialAggResults
                .get(facet)
                .getFacets()
                .get(facet)
                .get(0)
                .getData()
                .stream()
                .map(bucket -> new AggregationResponseItemDTO(bucket.getValue(), bucket.getCount()))
                .filter(AggregationResponseItemDTO::isVisibleCategory)
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private List<AggregationResponseItemDTO> buildEnvironmentalOptionAggregationResponseItems(
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults
    ) {
        return Arrays
            .stream(EnvironmentalOption.values())
            .map(environmentalOption -> {
                if (
                    !isWaterworksSearch ||
                    (
                        !environmentalOption.getDisplayName().equalsIgnoreCase("Energy Star") &&
                        !environmentalOption.getDisplayName().equalsIgnoreCase("WaterSense compliant") &&
                        !environmentalOption.getDisplayName().equalsIgnoreCase("Mercury free")
                    )
                ) {
                    return buildEnvironmentalOptionAggregationResponseItem(
                        searchResponse,
                        partialAggResults,
                        environmentalOption
                    );
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private AggregationResponseItemDTO buildEnvironmentalOptionAggregationResponseItem(
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults,
        EnvironmentalOption environmentalOption
    ) {
        if (partialAggResults == null || !partialAggResults.containsKey(environmentalOption.getFlagKey())) {
            String flag = searchResponse.getFacets().containsKey(environmentalOption.getFlagKey().toUpperCase())
                ? environmentalOption.getFlagKey().toUpperCase()
                : environmentalOption.getFlagKey();
            if (
                searchResponse.getFacets().containsKey(environmentalOption.getFlagKey().toUpperCase()) ||
                searchResponse.getFacets().containsKey(environmentalOption.getFlagKey())
            ) {
                return searchResponse
                    .getFacets()
                    .get(flag)
                    .get(0)
                    .getData()
                    .stream()
                    .filter(facetData -> Boolean.parseBoolean(facetData.getValue()))
                    .findFirst()
                    .map(facetData ->
                        new AggregationResponseItemDTO(environmentalOption.getDisplayName(), facetData.getCount())
                    )
                    .orElse(null);
            } else {
                return null;
            }
        }
        String facet = partialAggResults.containsKey(environmentalOption.getFlagKey().toUpperCase())
            ? environmentalOption.getFlagKey().toUpperCase()
            : environmentalOption.getFlagKey();
        if (partialAggResults.get(facet).getFacets().containsKey(facet)) {
            return partialAggResults
                .get(environmentalOption.getFlagKey())
                .getFacets()
                .get(environmentalOption.getFlagKey())
                .get(0)
                .getData()
                .stream()
                .filter(facetData -> Boolean.parseBoolean(facetData.getValue()))
                .findFirst()
                .map(facetData ->
                    new AggregationResponseItemDTO(environmentalOption.getDisplayName(), facetData.getCount())
                )
                .orElse(null);
        } else {
            return null;
        }
    }

    private List<AggregationResponseItemDTO> buildInStockLocationAggregationResponseItems(
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults,
        String selectedBranchId
    ) {
        List<AggregationResponseItemDTO> aggregationResponseItemDTOS = null;

        aggregationResponseItemDTOS =
            buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.IN_STOCK_LOCATION);
        if (aggregationResponseItemDTOS != null) {
            val count = aggregationResponseItemDTOS
                .stream()
                .filter(facetData -> facetData.getValue().equals(selectedBranchId))
                .findFirst()
                .map(AggregationResponseItemDTO::getCount)
                .orElse(0);
            val aggregationResponseItemDTO = new AggregationResponseItemDTO(
                FacetName.IN_STOCK_LOCATION.toString(),
                count
            );
            return List.of(aggregationResponseItemDTO);
        } else {
            return null;
        }
    }

    private List<AggregationResponseItemDTO> buildNextCategoryAggregationResponseItems(
        SearchResponse searchResponse,
        Map<String, SearchResponse> partialAggResults,
        int categoryLevel
    ) {
        switch (categoryLevel) {
            case 0:
                return buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.CATEGORY_1_NAME);
            case 1:
                return buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.CATEGORY_2_NAME);
            case 2:
                return buildAggregationResponseItems(searchResponse, partialAggResults, FacetName.CATEGORY_3_NAME);
            default:
                throw new IllegalArgumentException("Unknown categoryLevel: " + categoryLevel);
        }
    }
}
