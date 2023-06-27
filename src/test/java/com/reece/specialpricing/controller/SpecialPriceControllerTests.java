package com.reece.specialpricing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.model.pojo.ProductPriceResponse;
import com.reece.specialpricing.service.EclipseService;
import com.reece.specialpricing.service.SpecialPricingService;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SpecialPriceControllerTests extends TestCommon {
    private MockMvc mockMvc;

    @Mock
    private SpecialPricingService specialPricingService;

    @InjectMocks
    private SpecialPriceController controller;

    private ObjectMapper objectMapper;

    @Mock
    private EclipseService eclipseService;

    @Before
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new CustomExceptionHandler()).build();
    }

    @BeforeEach
    public void setUp(){
        reset(specialPricingService);
        reset(eclipseService);
    }

    @Test
    public void POST_UploadSpecialPriceShould4XXWithEmptyListOfSuggestions() throws Exception {
        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(emptyChangeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void POST_UpdateSpecialPriceShouldSucceedWithValidRequest() throws Exception {
        when(specialPricingService.createAndUpdatePrices(validChangeRequest)).thenReturn(allSuccessfulUploadResponse);
        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(validChangeRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.successfulUploads.size()").value(1))
                .andExpect(jsonPath("$.successfulUploads[0].uploadedPath").value(allSuccessfulUploadResponse.getSuccessfulUploadPaths().get(0)))
                .andExpect(jsonPath("$.failedUpdateSuggestions.size()").value(0));

        verify(specialPricingService, times(1)).createAndUpdatePrices(validChangeRequest);
    }

    @Test
    public void POST_CreateSpecialPriceShouldSucceedWithValidRequest() throws Exception {
        when(specialPricingService.createAndUpdatePrices(validCreateRequest)).thenReturn(allSuccessfulUploadResponse);
        this.mockMvc.perform(post("/v1/product/specialPrice")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.successfulUploads.size()").value(1))
                .andExpect(jsonPath("$.successfulUploads[0].uploadedPath").value(allSuccessfulUploadResponse.getSuccessfulUploadPaths().get(0)))
                .andExpect(jsonPath("$.failedUpdateSuggestions.size()").value(0));

        verify(specialPricingService, times(1)).createAndUpdatePrices(validCreateRequest);

    }

    @Test
    public void POST_UpdateSpecialPriceShouldSucceedWithAMixtureOfSuccessfulAndFailedUploads() throws Exception {
        when(specialPricingService.createAndUpdatePrices(validCreateRequest)).thenReturn(someSuccessSomeFailedUploadResponse);

        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.successfulUploads.size()").value(1))
                .andExpect(jsonPath("$.successfulUploads[0].uploadedPath").value(someSuccessSomeFailedUploadResponse.getSuccessfulUploadPaths().get(0)))
                .andExpect(jsonPath("$.failedUpdateSuggestions.size()").value(1))
                .andExpect(jsonPath("$.failedUpdateSuggestions[0].productId").value(someSuccessSomeFailedUploadResponse.getFailedUpdateSuggestions().get(0).getProductId()));


        verify(specialPricingService, times(1)).createAndUpdatePrices(validCreateRequest);
    }

    @Test
    public void POST_CreateSpecialPriceShouldSucceedWithAMixtureOfSuccessfulAndFailedUploads() throws Exception {
        when(specialPricingService.createAndUpdatePrices(validCreateRequest)).thenReturn(someSuccessSomeFailedUploadResponse);
        this.mockMvc.perform(post("/v1/product/specialPrice")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.successfulUploads.size()").value(1))
                .andExpect(jsonPath("$.successfulUploads[0].uploadedPath").value(someSuccessSomeFailedUploadResponse.getSuccessfulUploadPaths().get(0)))
                .andExpect(jsonPath("$.failedUpdateSuggestions.size()").value(1))
                .andExpect(jsonPath("$.failedUpdateSuggestions[0].productId").value(someSuccessSomeFailedUploadResponse.getFailedUpdateSuggestions().get(0).getProductId()));

        verify(specialPricingService, times(1)).createAndUpdatePrices(validCreateRequest);
        
    }

    @Test
    public void POST_UpdateSpecialPriceShouldFailWithAllFailedUploads() throws Exception {
        var failureException = new CSVUploadException("failed because I said so");
        when(specialPricingService.createAndUpdatePrices(validChangeRequest)).thenThrow(failureException);
        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(validChangeRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").doesNotExist())
                .andExpect(jsonPath("$.errors[0].errorMessage").value(failureException.getMessage()))
                .andExpect(jsonPath("$.errors[0].exceptionType").value(failureException.getExceptionType()));

        verify(specialPricingService, times(1)).createAndUpdatePrices(validChangeRequest);

    }

    @Test
    public void POST_CreateSpecialPriceShouldFailWithAllFailedUploads() throws Exception {
        var failureException = new CSVUploadException("failed because I said so");
        when(specialPricingService.createAndUpdatePrices(validCreateRequest)).thenThrow(failureException);
        this.mockMvc.perform(post("/v1/product/specialPrice")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").doesNotExist())
                .andExpect(jsonPath("$.errors[0].errorMessage").value(failureException.getMessage()))
                .andExpect(jsonPath("$.errors[0].exceptionType").value(failureException.getExceptionType()));

        verify(specialPricingService, times(1)).createAndUpdatePrices(validCreateRequest);

    }

    @Test
    public void POST_UpdateSpecialPriceShouldFailWhenValidatingAnnotatedParameters() throws Exception {
        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidProductIdChangeRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(2))
                .andExpect(jsonPath("$.errors.size()").value(2))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'productId' is null or blank, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void POST_CreateSpecialPriceShouldFailWhenValidatingAnnotatedParameters() throws Exception {
        this.mockMvc.perform(post("/v1/product/specialPrice")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(invalidProductIdCreateRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'productId' is null or blank, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void POST_UpdateSpecialPriceShouldFailWhenValidatingPriceCategoryField() throws Exception {
        this.mockMvc.perform(post("/v1/product/specialPrice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidPriceCategoryChangeRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("priceChangeSuggestions[0].priceCategory"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'priceCategory' value is not in the allowed value array: ['User Defined', 'Rate Card', 'Typical Price', 'Recommended']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void GET_getSpecialPricesShouldSucceedWithJustCustomerId() throws Exception {
        var specialPrice = MOCK_PRODUCT_ID_AND_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT.get(0);
        var result = new PagedSearchResults(10, 967, 5, List.of(specialPrice));
        when(specialPricingService.getPrices(any(), any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.productId").doesNotExist())
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].customerId").value(specialPrice.getCustomerId()))
                .andExpect(jsonPath("$.results[0].prices.size()").value(7));

        verify(specialPricingService, times(1)).getPrices(eq("1"), eq(null), any(), any());
    }

    @Test
    public void GET_getSpecialPricesShouldSucceedWithJustProductId() throws Exception {
        var specialPrice = MOCK_PRODUCT_ID_AND_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT.get(0);
        var result = new PagedSearchResults(10, 967, 5, List.of(specialPrice));
        when(specialPricingService.getPrices(any(), any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.productId").value("1"))
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].customerId").value(specialPrice.getCustomerId()))
                .andExpect(jsonPath("$.results[0].prices.size()").value(7));

        verify(specialPricingService, times(1)).getPrices(eq(null), eq("1"), any(), any());
    }

    @Test
    public void GET_getSpecialPricesShouldSucceedWithBothCustomerIdAndProductId() throws Exception {
        var specialPrice = MOCK_PRODUCT_ID_AND_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT.get(0);
        var result = new PagedSearchResults(10, 967, 5, List.of(specialPrice));
        when(specialPricingService.getPrices(any(), any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1")
                        .param("productId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.productId").value("1"))
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].customerId").value(specialPrice.getCustomerId()))
                .andExpect(jsonPath("$.results[0].prices.size()").value(7));

        verify(specialPricingService, times(1)).getPrices(eq("1"), eq("1"), any(), any());
    }

    @Test
    public void GET_getSpecialPricesShouldSucceedWithPagingParams() throws Exception {
        var specialPrice = MOCK_PRODUCT_ID_AND_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT.get(0);
        var result = new PagedSearchResults(10, 967, 5, List.of(specialPrice));
        when(specialPricingService.getPrices(any(), any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1")
                        .param("productId", "1")
                        .param("page", "4")
                        .param("pageSize", "100")
                        .param("orderBy", "manufacturer")
                        .param("orderDirection", "desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(4))
                .andExpect(jsonPath("$.meta.productId").value("1"))
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].customerId").value(specialPrice.getCustomerId()))
                .andExpect(jsonPath("$.results[0].prices.size()").value(7));

        verify(specialPricingService, times(1)).getPrices(eq("1"), eq("1"), any(), any());
    }

    @Test
    public void GET_getSpecialPricesShouldFailWithInvalidJavaxParameter() throws Exception {
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "1")
                        .param("page", "0")
                        .param("pageSize", "100")
                        .param("orderBy", "something")
                        .param("orderDirection", "desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("page"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'page' is 0 or negative, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void GET_getSpecialPricesShouldFailWithInvalidManualPaginationParameter() throws Exception {
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1")
                        .param("page", "4")
                        .param("pageSize", "100")
                        .param("orderBy", "MANUFACTURER")
                        .param("orderDirection", "desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("orderBy"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'orderBy' not in value array: ['displayName', 'manufacturer', 'branch', 'customerDisplayName', 'manufacturerReferenceNumber', 'priceLine','currentPrice','standardCost','typicalPrice','rateCardPrice','recommendedPrice']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void GET_getSpecialPricesShouldFailWithMultipleInvalidManualPaginationParameter() throws Exception {
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1")
                        .param("page", "4")
                        .param("pageSize", "100")
                        .param("orderBy", "MANUFACTURER")
                        .param("orderDirection", "asc,desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(2))
                .andExpect(jsonPath("$.errors.size()").value(2))
                .andExpect(jsonPath("$.errors[0].field").value("orderBy"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'orderBy' not in value array: ['displayName', 'manufacturer', 'branch', 'customerDisplayName', 'manufacturerReferenceNumber', 'priceLine','currentPrice','standardCost','typicalPrice','rateCardPrice','recommendedPrice']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"))
                .andExpect(jsonPath("$.errors[1].field").value("orderDirection"))
                .andExpect(jsonPath("$.errors[1].errorMessage").value("Invalid parameter: 'orderDirection' not in value array: ['asc', 'desc']"))
                .andExpect(jsonPath("$.errors[1].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void GET_getSpecialPricesShouldFailWithInvalidManualQueryParameter() throws Exception {
        this.mockMvc.perform(get("/v1/product/specialPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("customerId or productId"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }

    @Test
    public void GET_getPriceLinesShouldSucceedWithJustCustomerId() throws Exception {
        var result = Set.of("MSC-PVCPIPE");
        when(specialPricingService.getPriceLines(any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice/priceLines")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray());

        verify(specialPricingService, times(1)).getPriceLines(eq("1"), eq(null));
    }

    @Test
    public void GET_getPriceLinesShouldSucceedWithBothCustomerIdAndProductId() throws Exception {
        var result = Set.of("MSC-PVCPIPE");
        when(specialPricingService.getPriceLines(any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice/priceLines")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("customerId", "1")
                        .param("productId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray());

        verify(specialPricingService, times(1)).getPriceLines(eq("1"), eq("1"));
    }

    @Test
    public void GET_getPriceLinesShouldSucceedWithJustProductId() throws Exception {
        var result = Set.of("MSC-PVCPIPE");
        when(specialPricingService.getPriceLines(any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice/priceLines")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray());

        verify(specialPricingService, times(1)).getPriceLines(eq(null), eq("1"));
    }

    @Test
    public void GET_getPriceLinesShouldFailWithInvalidManualQueryParameter() throws Exception {
        this.mockMvc.perform(get("/v1/product/specialPrice/priceLines")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("customerId or productId"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(specialPricingService);
    }


    @Test
    public void GET_GetProductPrices() throws Exception {
        String stringDate="1998/12/25";
        Date date=new SimpleDateFormat("yyyy/mm/dd").parse(stringDate);
        ProductPriceResponse result = new ProductPriceResponse();
        when(eclipseService.getProductPrice(any(), any(),any(), any(), any(), any()))
                .thenReturn(result);
        this.mockMvc.perform(get("/v1/product/specialPrice/productPrice")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "testProd1")
                        .param("branch", "1")
                        .param("customerId", "1")
                        .param("userId", "1")
                        .param("effectiveDate", stringDate)
                        .param("correlationId", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isMap());

        verify(eclipseService, times(1)).getProductPrice(eq("testProd1"), eq("1"), eq("1"), eq("1"), eq(date), eq("1"));
    }

}
