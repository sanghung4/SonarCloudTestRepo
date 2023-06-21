package com.reece.platform.products.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.constants.ElasticsearchFieldNames;
import com.reece.platform.products.external.appsearch.AppSearchClient;
import com.reece.platform.products.external.appsearch.model.*;
import com.reece.platform.products.model.DTO.ProductDTO;
import com.reece.platform.products.model.DTO.ProductSearchFilterDTO;
import com.reece.platform.products.model.DTO.ProductSearchRequestDTO;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.SearchEngineEnum;
import com.reece.platform.products.search.mapper.ProductDTOMapper;
import com.reece.platform.products.search.mapper.SearchSuggestionResponseMapper;
import com.reece.platform.products.service.AccountService;
import com.reece.platform.products.service.CustomerProductInfoService;
import com.reece.platform.products.service.ProductService;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class SearchServiceTest {

    public static final PageRequest PAGE_REQUEST = new PageRequest(1000, 1);
    private SearchService searchService;

    @Mock
    private AppSearchClient appSearchClient;

    @Mock
    private ProductService productService;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerProductInfoService customerProductInfoService;

    @Captor
    ArgumentCaptor<SearchRequest> searchRequestArgumentCaptor;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductDTOMapper productDTOMapper = new ProductDTOMapper(objectMapper);
    private final SearchSuggestionResponseMapper searchSuggestionResponseMapper = new SearchSuggestionResponseMapper();
    private final String testEngineName = "testEngine";
    private final String testbathAndKitchenEngineName = String.valueOf(SearchEngineEnum.bath_kitchen);
    private final String testWaterWorksEngineName = "testEngine";
    private final double PACKAGE_LENGTH_NUM = 12.1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        var restTemplateBuilder = mock(RestTemplateBuilder.class);
        searchService =
            new SearchService(
                appSearchClient,
                productService,
                accountService,
                productDTOMapper,
                searchSuggestionResponseMapper,
                customerProductInfoService,
                testEngineName,
                testbathAndKitchenEngineName,
                testWaterWorksEngineName,
                restTemplateBuilder
            );
    }

    /**
     * Verifies that when one filter is selected in the search request:
     * - the filter name is properly mapped to it's elasticsearch name
     * - appsearchClient.search is called twice
     *     - once for the main search
     *     - once excluding the filter value, to get all values for that facet
     * @throws IOException
     */
    @Test
    public void getProductsByQuery_filterSelected() throws ExecutionException, InterruptedException {
        when(appSearchClient.search(eq(testEngineName), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter.json", SearchResponse.class))
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter-agg1.json", SearchResponse.class));

        val testFilterEcommName = "brand";
        val testFilterElasticsearchName = "mfr_full_name";
        val testFilterValue = "generic";

        val request = buildTestFilterSearchRequest(testFilterEcommName, testFilterValue);

        val response = searchService.getProductsByQuery(request, null);

        val searchArgument = ArgumentCaptor.forClass(SearchRequest.class);
        verify(appSearchClient, times(2)).search(eq(testEngineName), searchArgument.capture());

        val primaryRequest = searchArgument.getAllValues().get(0);
        val aggRequest = searchArgument.getAllValues().get(1);

        assertEquals(List.of(testFilterValue), extractFilterValues(primaryRequest, testFilterElasticsearchName));
        assertNull(extractFilterValues(aggRequest, testFilterElasticsearchName));
    }

    @Test
    public void getProductsByQuery_searchFieldNames() throws ExecutionException, InterruptedException {
        when(appSearchClient.search(eq(testEngineName), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter.json", SearchResponse.class))
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter-agg1.json", SearchResponse.class));

        val testFilterEcommName = "brand";
        val testFilterValue = "generic";

        val request = buildTestFilterSearchRequest(testFilterEcommName, testFilterValue);

        val response = searchService.getProductsByQuery(request, null);

        val searchArgument = ArgumentCaptor.forClass(SearchRequest.class);
        verify(appSearchClient, times(2)).search(eq(testEngineName), searchArgument.capture());

        val primaryRequest = searchArgument.getAllValues().get(0);

        val fieldNames = primaryRequest.getSearchFields().keySet();

        assert (fieldNames.contains("erp_product_id"));
        assert (fieldNames.contains("vendor_part_nbr"));
        assert (fieldNames.contains("web_description"));
        assert (fieldNames.contains("clean_web_description"));
        assert (fieldNames.contains("clean_product_brand"));
        assert (fieldNames.contains("clean_vendor_part_nbr"));
        assert (fieldNames.contains("customer_part_number"));
        assert (fieldNames.contains("mfr_full_name"));
    }

    /**
     * Verifies that search handles when all three categories are developed
     * @throws IOException
     */
    @Test
    public void getProductsByQuery_threeCategoriesSelected() throws ExecutionException, InterruptedException {
        when(appSearchClient.search(eq(testEngineName), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("search-response-category-level-3.json", SearchResponse.class));

        val request = buildtestCategoriesSearchRequest(
            "Pipe & Fittings",
            "Metal Pipe & Fittings",
            "Brass Pipe & Fittings"
        );

        val response = searchService.getProductsByQuery(request, null);

        val searchArgument = ArgumentCaptor.forClass(SearchRequest.class);
        verify(appSearchClient, times(1)).search(eq(testEngineName), searchArgument.capture());

        assertEquals(response.getCategoryLevel(), 3);
        assertEquals(response.getSelectedCategories().size(), 3);
        assertEquals(response.getAggregates().getCategory(), null);
    }

    @Test
    public void getProductsByQuery_returnsInStockLocations() throws ExecutionException, InterruptedException {
        val testSearchResponse = TestUtils.loadResponseJson("search-response-brand-filter.json", SearchResponse.class);
        val testInStockLocation = testSearchResponse.getFacets().get("in_stock_location").get(0).getData().get(0);
        val testBranchNumber = testInStockLocation.getValue();
        val testBranchCount = testInStockLocation.getCount();
        val testUserId = UUID.randomUUID();

        when(productService.getSelectedCartBranch(eq(testUserId), any())).thenReturn(Optional.of(testBranchNumber));

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(testSearchResponse)
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter-agg1.json", SearchResponse.class));

        val testFilterEcommName = "brand";
        val testFilterValue = "generic";

        val request = buildTestFilterSearchRequest(testFilterEcommName, testFilterValue, testUserId.toString());

        val response = searchService.getProductsByQuery(request, testUserId);

        val inStockLocation = response.getAggregates().getInStockLocation();
        assertEquals(1, inStockLocation.size());
        assertEquals(testBranchCount, inStockLocation.get(0).getCount());
    }

    private ProductSearchRequestDTO buildtestCategoriesSearchRequest(
        String categoryOne,
        String categoryTwo,
        String categoryThree
    ) {
        val request = new ProductSearchRequestDTO();
        request.setCategoryLevel(3);
        request.setCurrentPage(1);
        request.setPageSize(12);
        request.setSearchTerm("");
        request.setSelectedAttributes(Collections.emptyList());

        val categories = List.of(
            new ProductSearchFilterDTO("category1", categoryOne),
            new ProductSearchFilterDTO("category2", categoryTwo),
            new ProductSearchFilterDTO("category3", categoryThree)
        );
        request.setSelectedCategories(categories);

        return request;
    }

    @Test
    public void getSuggestedSearch_success() {
        when(appSearchClient.querySuggestion(anyString(), any(QuerySuggestionRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("suggested-search-response.json", QuerySuggestonResponse.class));

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("suggested-search-top-results.json", SearchResponse.class));

        val result = searchService.getSuggestedSearch("sc", "plumbing_hvac", 7, "tx");

        assertEquals(List.of("sc", "sc-3", "sc-1", "scp2", "sc-6", "sc1", "schier"), result.getSuggestions());

        assertEquals(2, result.getTopCategories().size());

        assertEquals("Pipe & Fittings", result.getTopCategories().get(0).getValue());
        assertEquals(2749, result.getTopCategories().get(0).getCount());

        assertEquals("Plumbing Installation, Tools, Hardware & Safety", result.getTopCategories().get(1).getValue());
        assertEquals(712, result.getTopCategories().get(1).getCount());

        assertEquals(3, result.getTopProducts().size());
        assertEquals("MSC-299154", result.getTopProducts().get(0).getId());
    }

    @Test
    public void getSuggestedSearchByVendor_part_nbr_success() {
        when(appSearchClient.querySuggestion(anyString(), any(QuerySuggestionRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson(
                    "suggested-search-vendor-part-nbr-response.json",
                    QuerySuggestonResponse.class
                )
            );

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("suggested-search-top-mfr-results.json", SearchResponse.class));

        val result = searchService.getSuggestedSearch("hw8x12zt", "plumbing_hvac", 7, "tx");

        assertEquals(List.of("hw8x1/2zt", "hw8x1zt", "hw8x2zt", "hw8x12zw"), result.getSuggestions());

        assertEquals(3, result.getTopProducts().size());
        assertEquals("hw8x1/2zt", result.getTopProducts().get(0).getManufacturerNumber());
    }

    @Test
    public void getSearchProductByMeasurement_success() {
        when(appSearchClient.querySuggestion(anyString(), any(QuerySuggestionRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson(
                    "suggested-search-vendor-part-nbr-response.json",
                    QuerySuggestonResponse.class
                )
            );

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson("search-with-measurement-field-response.json", SearchResponse.class)
            );

        val result = searchService.getSuggestedSearch("1 1/2\" type K soft copper", "plumbing_hvac", 7, "tx");

        assertEquals(3, result.getTopProducts().size());
        assertEquals("1-1/2\" x 60' Soft Type K Copper Tube", result.getTopProducts().get(0).getName());
    }

    @Test
    public void getSearchProductAndBrand_success() {
        when(appSearchClient.querySuggestion(anyString(), any(QuerySuggestionRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson("suggested-search-product-brand-response.json", QuerySuggestonResponse.class)
            );

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("search-with-product-brand-response.json", SearchResponse.class));

        val result = searchService.getSuggestedSearch(
            "Southwark Metal 5FT JT 4 30GA SEALED PIPE",
            "plumbing_hvac",
            7,
            "tx"
        );
        val resultBrandProduct = searchService.getSuggestedSearch(
            "Metal 5FT JT 4 30GA SEALED PIPE Southwark",
            "plumbing_hvac",
            7,
            "tx"
        );
        assertEquals(3, result.getTopProducts().size());
        assertEquals("Southwark Metal 5FT JT 4 30GA SEALED PIPE", result.getTopProducts().get(0).getName());
        assertEquals("Southwark Metal 5FT JT 4 30GA SEALED PIPE", resultBrandProduct.getTopProducts().get(0).getName());
    }

    @Test
    public void getSuggestedSearch_BathAndKitchen() {
        when(appSearchClient.querySuggestion(eq(testbathAndKitchenEngineName), any(QuerySuggestionRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("suggested-search-response.json", QuerySuggestonResponse.class));

        when(appSearchClient.search(eq(testbathAndKitchenEngineName), any(SearchRequest.class)))
            .thenReturn(TestUtils.loadResponseJson("suggested-search-top-results.json", SearchResponse.class));

        val result = searchService.getSuggestedSearch("sc", testbathAndKitchenEngineName, 7, "tx");

        assertEquals(List.of("sc", "sc-3", "sc-1", "scp2", "sc-6", "sc1", "schier"), result.getSuggestions());

        assertEquals(2, result.getTopCategories().size());

        assertEquals("Pipe & Fittings", result.getTopCategories().get(0).getValue());
        assertEquals(2749, result.getTopCategories().get(0).getCount());

        assertEquals("Plumbing Installation, Tools, Hardware & Safety", result.getTopCategories().get(1).getValue());
        assertEquals(712, result.getTopCategories().get(1).getCount());

        assertEquals(3, result.getTopProducts().size());
        assertEquals("MSC-299154", result.getTopProducts().get(0).getId());
    }

    private ProductSearchRequestDTO buildTestFilterSearchRequest(String filterName, String filterValue) {
        return buildTestFilterSearchRequest(filterName, filterValue, null);
    }

    private ProductSearchRequestDTO buildTestFilterSearchRequest(String filterName, String filterValue, String userId) {
        val request = new ProductSearchRequestDTO();
        request.setCategoryLevel(0);
        request.setCurrentPage(1);
        request.setErpSystem("ECLIPSE");
        request.setPageSize(12);
        request.setSearchTerm("copper pipe");
        request.setSelectedAttributes(List.of(new ProductSearchFilterDTO(filterName, filterValue)));
        request.setSelectedCategories(Collections.emptyList());

        return request;
    }

    private List<String> extractFilterValues(SearchRequest searchRequest, String filterName) {
        val filters = ((List<Map<String, List<String>>>) searchRequest.getFilters().get("all"));

        return filters
            .stream()
            .filter(map -> map.containsKey(filterName))
            .findFirst()
            .map(map -> map.get(filterName))
            .orElse(null);
    }

    @Test
    public void getSuggestedSearch_emptyList() {
        when(appSearchClient.querySuggestion(anyString(), any(QuerySuggestionRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson("suggested-search-empty-documents.json", QuerySuggestonResponse.class)
            );

        val result = searchService.getSuggestedSearch("499461", "plumbing_hvac", 7, "tx");

        assertEquals(Collections.emptyList(), result.getTopCategories());
        assertEquals(Collections.emptyList(), result.getTopProducts());
    }

    @Test
    public void getCategories_plumbingHVACSuccess() {
        val searchEngine = SearchEngineEnum.plumbing_hvac.toString();
        when(appSearchClient.search(anyString(), any()))
            .thenReturn(TestUtils.loadResponseJson("query-categories-level-one-response.json", SearchResponse.class))
            .thenReturn(
                TestUtils.loadResponseJson("query-categories-faucets-and-fixtures-response.json", SearchResponse.class)
            )
            .thenReturn(
                TestUtils.loadResponseJson(
                    "query-categories-faucets-and-fixtures-facets-showers-response.json",
                    SearchResponse.class
                )
            )
            .thenReturn(
                TestUtils.loadResponseJson(
                    "query-categories-faucets-and-fixtures-appliances-response.json",
                    SearchResponse.class
                )
            )
            .thenReturn(
                TestUtils.loadResponseJson("query-categories-plumbing-tools-hardware-safety.json", SearchResponse.class)
            )
            .thenReturn(
                TestUtils.loadResponseJson(
                    "query-categories-plumbing-tools-hardware-safety-tools.json",
                    SearchResponse.class
                )
            );

        var result = searchService.getCategories(searchEngine);

        var ffa = result
            .getCategories()
            .stream()
            .filter(c -> c.getName().equals(ElasticsearchFieldNames.PIPE_AND_FITTINGS))
            .findFirst();

        assertEquals(2, result.getCategories().size());
        assertTrue(ffa.isPresent());
        // Should filter out the hidden categories
        assertEquals(2, ffa.get().getChildren().size());
        assertEquals(
            8,
            ffa
                .get()
                .getChildren()
                .stream()
                .filter(c -> c.getName().equals("Faucets, Showers & Bathroom Accessories"))
                .findFirst()
                .get()
                .getChildren()
                .size()
        );
    }

    @Test
    public void getProductsByCustomerPartNumber_success() {
        val testEngineName = "fake-engine";
        val testCustomerPartNumber = "ABC123";
        val testRequest = new SearchRequest();
        testRequest.page(PAGE_REQUEST);

        testRequest.filter(Filter.all(Stream.of(Filter.values("customer_part_number", testCustomerPartNumber))));

        when(appSearchClient.search(eq(testEngineName), any(SearchRequest.class)))
            .thenReturn(
                TestUtils.loadResponseJson("get-products-by-customer-number-response.json", SearchResponse.class)
            );

        val response = searchService.getProductsByCustomerPartNumber(
            testEngineName,
            List.of(testCustomerPartNumber),
            PAGE_REQUEST
        );

        assertFalse(response.isEmpty(), "Expected result to contain mocked products");
        ProductDTO actualProduct = response.get(0);
        assertEquals(
            PACKAGE_LENGTH_NUM,
            actualProduct.getPackageDimensions().getLength(),
            "Expected package length to equal expected numerical value"
        );

        verify(appSearchClient, times(1)).search(eq(testEngineName), eq(testRequest));
    }

    @Test
    public void getProductsByQueryFilters_success() throws ExecutionException, InterruptedException {
        val testSearchResponse = TestUtils.loadResponseJson("search-response-brand-filter.json", SearchResponse.class);
        val testInStockLocation = testSearchResponse.getFacets().get("in_stock_location").get(0).getData().get(0);
        val testBranchNumber = testInStockLocation.getValue();
        val testUserId = UUID.randomUUID();

        when(productService.getSelectedCartBranch(eq(testUserId), any())).thenReturn(Optional.of(testBranchNumber));

        when(appSearchClient.search(anyString(), any(SearchRequest.class)))
            .thenReturn(testSearchResponse)
            .thenReturn(TestUtils.loadResponseJson("search-response-brand-filter-agg1.json", SearchResponse.class));

        val testFilterEcommName = "brand";
        val testFilterValue = "generic";

        val request = buildTestFilterSearchRequest(testFilterEcommName, testFilterValue, null);
        request.setState("tx");

        val testRequest = new SearchRequest();
        testRequest.filter(Filter.all(Stream.of(Filter.values("mfr_full_name", testFilterValue))));
        testRequest.filter(Filter.none(Stream.of(Filter.values("territory_exclusion_list", "tx"))));

        searchService.getProductsByQuery(request, testUserId);

        verify(appSearchClient, times(2)).search(eq(testEngineName), searchRequestArgumentCaptor.capture());

        val requests = searchRequestArgumentCaptor.getAllValues();
        assertEquals(requests.get(0).getFilters(), testRequest.getFilters());
    }
}
