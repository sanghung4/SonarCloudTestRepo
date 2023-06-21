package com.reece.platform.products.controller;

import static com.reece.platform.products.testConstant.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.branches.model.DTO.BranchProximityResponseDTO;
import com.reece.platform.products.branches.model.DTO.BranchWithDistanceResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.Stock;
import com.reece.platform.products.model.StoreStock;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.BranchAvailability;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.NowQuantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import com.reece.platform.products.model.eclipse.common.Branch;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.ElasticsearchService;
import com.reece.platform.products.service.ErpService;
import com.reece.platform.products.service.ProductService;
import java.util.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { ProductController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ErpService erpService;

    @MockBean
    private ElasticsearchService elasticsearchService;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private BranchesService branchesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String ES_ERROR_MESSAGE = "ELASTIC SEARCH IS DOWN";
    private static final HttpStatus ES_STATUS_CODE = HttpStatus.BAD_GATEWAY;
    private static final String AUTH_TOKEN = "MOCK_AUTH";

    private static final String ERP_ACCOUNT_ID = "123";
    private static final String ERP_USER_PASSWORD = "333";
    private static final String ERP_USER_LOGIN = "444";
    private static final float PRODUCT_PRICE = 10.0f;
    private static final String HOME_BRANCH_NAME = "home branch name";
    private static final String HOME_BRANCH_ID = "123";
    private static final String HOME_BRANCH_ADDRESS = "123 home";
    private static final int HOME_BRANCH_AVAILABILITY = 2;
    private static final String OTHER_BRANCH_NAME = "other branch name";
    private static final String OTHER_BRANCH_ADDRESS = "321 home";
    private static final int OTHER_BRANCH_AVAILABILITY = 3;
    private static final Float DISTANCE_TO_BRANCH = Float.parseFloat("1234433.33342");
    private static final String ERP_SYSTEM = "ECLIPSE";
    private static final Float USER_INPUT_LONGITUDE = Float.parseFloat("-96.83857886668984");
    private static final Float USER_INPUT_LATITUDE = Float.parseFloat("32.751815573783645");
    private static final UUID SHIP_TO_ID = UUID.randomUUID();
    private static final UUID TOKEN_USER_ID = UUID.fromString("9fd162b9-b354-4d07-ac53-2091b1f2bec2");

    private ProductSearchRequestDTO productSearchRequestDTO = CreateProductSearchRequestDTO();
    private ProductSearchRequestDTO errorProductSearchRequestDTO = CreateProductSearchRequestDTOWithException();
    private ErpUserInformation erpUserInformation;
    private Stock stock;

    @BeforeEach
    public void setup() throws Exception {
        ProductDTO availableProductDTO = new ProductDTO();
        availableProductDTO.setId(String.valueOf(AVAILABLE_PRODUCT_ID));
        List<String> branchIds = new ArrayList<String>();

        branchIds.add(HOME_BRANCH_ID);
        branchIds.add("345");

        ProductDTO availableProductByNumberDTO = new ProductDTO();
        var branchWithDistance = new BranchWithDistanceResponseDTO();
        branchWithDistance.setAddress1("123 Main St");
        branchWithDistance.setAddress2("Apt 5");
        branchWithDistance.setCity("Dallas");
        branchWithDistance.setState("TX");
        branchWithDistance.setPhone("44499955566");
        branchWithDistance.setZip("6665555");
        branchWithDistance.setDistanceToBranch(DISTANCE_TO_BRANCH);
        BranchAvailability branchAvailability = new BranchAvailability();
        var branchWithDistanceList = new BranchProximityResponseDTO();
        List<BranchWithDistanceResponseDTO> branchList = new ArrayList<>();
        branchList.add(branchWithDistance);
        branchWithDistanceList.setBranchList(branchList);
        Branch homeBranch = new Branch();
        homeBranch.setBranchName(HOME_BRANCH_NAME);
        homeBranch.setBranchId(HOME_BRANCH_ID);
        branchAvailability.setBranch(homeBranch);
        NowQuantity nowQuantity = new NowQuantity();
        Quantity quantity = new Quantity();
        quantity.setQuantity(10);
        nowQuantity.setQuantity(quantity);

        branchAvailability.setNowQuantity(nowQuantity);

        StoreStock homeBranchStock = new StoreStock(branchAvailability, branchWithDistance);
        StoreStock otherBranch = new StoreStock(branchAvailability, branchWithDistance);
        stock = new Stock();
        stock.setHomeBranch(homeBranchStock);
        stock.setOtherBranches(Collections.singletonList(otherBranch));

        ProductDTO availableProductStockDTO = new ProductDTO();
        availableProductStockDTO.setId(String.valueOf(AVAILABLE_PRODUCT_ID));
        availableProductStockDTO.setPrice(PRODUCT_PRICE);

        erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpAccountId(ERP_ACCOUNT_ID);
        //        erpUserInformation.setName("Branch");
        erpUserInformation.setPassword(ERP_USER_PASSWORD);
        erpUserInformation.setErpAccountId(ERP_USER_LOGIN);
        erpUserInformation.setErpSystemName(ERP_SYSTEM);

        ProductSearchResponseDTO productSearchResponseDTO = new ProductSearchResponseDTO();
        productSearchResponseDTO.setProducts(Arrays.asList(availableProductDTO, availableProductDTO));

        when(productService.getProductById(AVAILABLE_PRODUCT_ID)).thenReturn(availableProductDTO);
        when(productService.getProductById(eq(AVAILABLE_PRODUCT_ID))).thenReturn(availableProductStockDTO);

        when(productService.getProductById(UNKNOWN_PRODUCT_ID)).thenReturn(null);
        when(productService.getProductById(ERROR_PRODUCT_ID))
            .thenThrow(new ElasticsearchException(ES_ERROR_MESSAGE, ES_STATUS_CODE));

        when(
            branchesService.getBranchListByProximity(
                branchIds,
                AVAILABLE_PRODUCT_ID,
                USER_INPUT_LONGITUDE,
                USER_INPUT_LATITUDE
            )
        )
            .thenReturn(branchWithDistanceList);

        when(searchService.getProductsByQuery(any(), any())).thenReturn(productSearchResponseDTO);
        when(productService.getProductByNumber(AVAILABLE_PRODUCT_NUMBER)).thenReturn(availableProductByNumberDTO);

        when(productService.getProductByNumber(UNKNOWN_PRODUCT_NUMBER)).thenReturn(null);
        when(productService.getProductByNumber(ERROR_PRODUCT_NUMBER))
            .thenThrow(new ElasticsearchException(ES_ERROR_MESSAGE, ES_STATUS_CODE));

        when(authorizationService.getUserIdFromToken(AUTH_TOKEN)).thenReturn(TOKEN_USER_ID);
    }

    @Test
    public void shouldReturnProductData_success() throws Exception {
        MvcResult resultActions =
            this.mockMvc.perform(
                    get("/product")
                            .queryParam("productId", String.valueOf(AVAILABLE_PRODUCT_ID))
                            .queryParam("customerNumber", "")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> mapper.readValue(resultActions.getResponse().getContentAsString(), ProductDTO.class));
    }

    @Test
    public void shouldReturnNotFoundOnUnknownProduct() throws Exception {
        MvcResult resultActions =
            this.mockMvc.perform(
                    get("/product/{productId}", UNKNOWN_PRODUCT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(
            JsonProcessingException.class,
            () -> mapper.readValue(resultActions.getResponse().getContentAsString(), ProductDTO.class)
        );
    }

    @Test
    public void shouldReturnProductDataValidProductNumber() throws Exception {
        MvcResult resultActions =
            this.mockMvc.perform(post("/product/number/{prodNum}", AVAILABLE_PRODUCT_NUMBER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> mapper.readValue(resultActions.getResponse().getContentAsString(), ProductDTO.class));
    }

    @Test
    public void shouldReturnNotFoundOnUnknownProductByNumber() throws Exception {
        MvcResult resultActions =
            this.mockMvc.perform(post("/product/number/{prodNum}", UNKNOWN_PRODUCT_NUMBER))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(
            JsonProcessingException.class,
            () -> mapper.readValue(resultActions.getResponse().getContentAsString(), ProductDTO.class)
        );
    }

    @Test
    public void shouldReturnErrorOnElasticsearchFailureProductByNumber() throws Exception {
        MvcResult resultActions =
            this.mockMvc.perform(post("/product/number/{prodNum}", ERROR_PRODUCT_NUMBER)).andDo(print()).andReturn();
        assertEquals(resultActions.getResponse().getStatus(), ES_STATUS_CODE.value());
        assertEquals(resultActions.getResponse().getContentAsString(), ES_ERROR_MESSAGE);
    }

    @Test
    public void shouldReturnProductSearchData() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(productSearchRequestDTO);
        MvcResult resultActions =
            this.mockMvc.perform(
                    post("/product")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", AUTH_TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(
                resultActions.getResponse().getContentAsString(),
                new TypeReference<ProductSearchResponseDTO>() {}
            )
        );
    }

    @Test
    public void shouldReturnProductSearchData_paginated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(productSearchRequestDTO);
        MvcResult result =
            this.mockMvc.perform(
                    post("/product")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", AUTH_TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), ProductSearchResponseDTO.class)
        );
    }

    @Test
    public void shouldHandleElasticsearchSearchException() throws Exception {
        when(
            searchService.getProductsByQuery(
                new ProductSearchRequestDTO("error", 0, 12, "", any(), null, 0, null, null, null, null, null),
                any()
            )
        )
            .thenThrow(new ElasticsearchException(ES_ERROR_MESSAGE, ES_STATUS_CODE));
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(errorProductSearchRequestDTO);
        MvcResult result =
            this.mockMvc.perform(
                    post("/product")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", AUTH_TOKEN)
                )
                .andReturn();

        assertEquals(result.getResponse().getStatus(), ES_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ES_ERROR_MESSAGE);
    }

    @Test
    public void suggestedSearch_success() throws Exception {
        SuggestedSearchResponseDTO elasticsearchResponse = new SuggestedSearchResponseDTO();
        elasticsearchResponse.setSuggestions(List.of("pip-ga_bb", "pipe"));
        SearchSuggestionResponseDTO expectedResponse = new SearchSuggestionResponseDTO(elasticsearchResponse);

        when(searchService.getSuggestedSearch(any(), any(), anyInt(), any())).thenReturn(expectedResponse);

        SearchSuggestionRequestDTO searchSuggestionRequestDTO = new SearchSuggestionRequestDTO();
        searchSuggestionRequestDTO.setTerm("pip");
        val requestBody = objectMapper.writeValueAsString(searchSuggestionRequestDTO);

        MvcResult result =
            this.mockMvc.perform(
                    post("/product/search-suggestions")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        val response = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            SearchSuggestionResponseDTO.class
        );
        assertEquals(2, response.getSuggestions().size());
        assertEquals(expectedResponse.getSuggestions().get(0), response.getSuggestions().get(0));
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    public void suggestedSearch_elasticsearchException() throws Exception {
        when(searchService.getSuggestedSearch(any(), any(), anyInt(), any()))
            .thenThrow(new ElasticsearchException(ES_ERROR_MESSAGE, ES_STATUS_CODE));
        MvcResult result =
            this.mockMvc.perform(
                    post("/product/search-suggestions")
                        .content(new ObjectMapper().writeValueAsString(new SearchSuggestionRequestDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        assertEquals(result.getResponse().getStatus(), ES_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ES_ERROR_MESSAGE);
    }

    @Test
    void getCategories_success() throws Exception {
        when(searchService.getCategories(anyString()))
            .thenReturn(TestUtils.loadResponseJson("categories-response.json", CategoriesDTO.class));

        mockMvc.perform(get("/product/categories").param("engine", anyString()));

        verify(searchService, times(1)).getCategories(anyString());
    }

    @Test
    void getAllProducts_success() throws Exception {
        List<ProductResponseDTO> productDTOs = new ArrayList<>();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName("RHEEM PILOT ASSEMBLY W/ BRACKET SP20058");
        ProductResponseDTO productResponseDTO1 = new ProductResponseDTO();
        productResponseDTO1.setName("MORTEX 10KW AIR HANDLER E30B4D010ABB");
        productDTOs.add(productResponseDTO);
        productDTOs.add(productResponseDTO1);

        when(productService.getAllProducts()).thenReturn(productDTOs);

        mockMvc.perform(post("/product/all-products"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getAllProducts_failure() throws Exception {
        when(productService.getAllProducts()).thenReturn(null);
        MvcResult result =
            this.mockMvc.perform(post("/product/all-products"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "{\"error\":\"Products not found.\"}");
    }
}
