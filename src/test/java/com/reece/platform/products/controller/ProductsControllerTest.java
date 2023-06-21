package com.reece.platform.products.controller;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.model.DTO.ProductSearchRequestDTO;
import com.reece.platform.products.model.DTO.ProductSearchResponseDTO;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.service.ProductService;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductsController.class)
public class ProductsControllerTest {

    private static final ProductSearchResponseDTO TEST_SEARCH_RESPONSE = TestUtils.loadResponseJson(
        "product-search-response.json",
        ProductSearchResponseDTO.class
    );
    private static final ProductSearchResponseDTO TEST_SEARCH_INSTOCK_LOCATION_RESPONSE = TestUtils.loadResponseJson(
        "product-search-response-instock-location.json",
        ProductSearchResponseDTO.class
    );

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final UUID TEST_SHIP_TO_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {
        when(searchService.getProductsByQuery(any(ProductSearchRequestDTO.class), eq(null)))
            .thenReturn(TEST_SEARCH_RESPONSE);
        when(searchService.getProductsByQuery(any(ProductSearchRequestDTO.class), isNotNull()))
            .thenReturn(TEST_SEARCH_INSTOCK_LOCATION_RESPONSE);
    }

    @Test
    public void testSearchProducts_noParams() throws Exception {
        mockMvc
            .perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.aggregates.inStockLocation[0].count").value(0));

        val requestCaptor = ArgumentCaptor.forClass(ProductSearchRequestDTO.class);
        verify(searchService, times(1)).getProductsByQuery(requestCaptor.capture(), eq(null));

        assertEquals("", requestCaptor.getValue().getSearchTerm());
        assertEquals(1, requestCaptor.getValue().getCurrentPage());
        assertEquals(12, requestCaptor.getValue().getPageSize());
        assertNull(requestCaptor.getValue().getErpSystem());
        assertEquals(0, requestCaptor.getValue().getSelectedCategories().size());
        assertEquals(0, requestCaptor.getValue().getSelectedAttributes().size());
        assertEquals(0, requestCaptor.getValue().getCategoryLevel());
        assertNull(requestCaptor.getValue().getShipToId());
        assertNull(requestCaptor.getValue().getSelectedBranchId());
        assertNull(requestCaptor.getValue().getResultFields());
    }

    @Test
    public void testSearchProducts_withParams() throws Exception {
        mockMvc
            .perform(
                get("/products")
                    .queryParam("userId", String.valueOf(TEST_USER_ID))
                    .queryParam("shipToId", String.valueOf(TEST_SHIP_TO_ID))
                    .queryParam("customerNumber", String.valueOf("123"))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.aggregates.inStockLocation[0].count").value(100));

        val requestCaptor = ArgumentCaptor.forClass(ProductSearchRequestDTO.class);
        verify(searchService, times(1)).getProductsByQuery(requestCaptor.capture(), isNotNull());

        assertEquals("", requestCaptor.getValue().getSearchTerm());
        assertEquals(1, requestCaptor.getValue().getCurrentPage());
        assertEquals(12, requestCaptor.getValue().getPageSize());
        assertNull(requestCaptor.getValue().getErpSystem());
        assertEquals(0, requestCaptor.getValue().getSelectedCategories().size());
        assertEquals(0, requestCaptor.getValue().getSelectedAttributes().size());
        assertEquals(0, requestCaptor.getValue().getCategoryLevel());
        assertEquals(TEST_SHIP_TO_ID, requestCaptor.getValue().getShipToId());
        assertNull(requestCaptor.getValue().getSelectedBranchId());
        assertNull(requestCaptor.getValue().getResultFields());
    }

    @Test
    public void testSearchProducts_searchTerm() throws Exception {
        val testSearchTerm = "copper";
        mockMvc
            .perform(get("/products").queryParam("searchTerm", testSearchTerm))
            .andExpect(status().isOk())
            .andReturn();

        val requestCaptor = ArgumentCaptor.forClass(ProductSearchRequestDTO.class);
        verify(searchService, times(1)).getProductsByQuery(requestCaptor.capture(), eq(null));

        assertEquals(testSearchTerm, requestCaptor.getValue().getSearchTerm());
    }

    @Test
    public void testSearchProducts_pagination() throws Exception {
        val testPageNumber = 7;
        val testPageSize = 25;
        mockMvc
            .perform(
                get("/products")
                    .queryParam("page", String.valueOf(testPageNumber))
                    .queryParam("size", String.valueOf(testPageSize))
            )
            .andExpect(status().isOk());

        val requestCaptor = ArgumentCaptor.forClass(ProductSearchRequestDTO.class);
        verify(searchService, times(1)).getProductsByQuery(requestCaptor.capture(), eq(null));

        assertEquals(testPageNumber, requestCaptor.getValue().getCurrentPage());
        assertEquals(testPageSize, requestCaptor.getValue().getPageSize());
    }

    @Test
    public void testSearchProducts_categoriesAndFilters() throws Exception {
        val testCategoryType = "category1";
        val testCatrgoryValue = "Pipe & Fittings";
        val testFilterType = "brand";
        val testFilterValue = "ProPress XL";
        val testCategoryLevel = 1;

        mockMvc
            .perform(
                get("/products")
                    .queryParam("categories", testCategoryType + "|" + testCatrgoryValue)
                    .queryParam("filters", testFilterType + "|" + testFilterValue)
                    .queryParam("categoryLevel", String.valueOf(testCategoryLevel))
            )
            .andExpect(status().isOk());

        val requestCaptor = ArgumentCaptor.forClass(ProductSearchRequestDTO.class);
        verify(searchService, times(1)).getProductsByQuery(requestCaptor.capture(), eq(null));

        assertEquals(1, requestCaptor.getValue().getSelectedCategories().size());
        assertEquals(testCategoryType, requestCaptor.getValue().getSelectedCategories().get(0).getAttributeType());
        assertEquals(testCatrgoryValue, requestCaptor.getValue().getSelectedCategories().get(0).getAttributeValue());

        assertEquals(1, requestCaptor.getValue().getSelectedAttributes().size());
        assertEquals(testFilterType, requestCaptor.getValue().getSelectedAttributes().get(0).getAttributeType());
        assertEquals(testFilterValue, requestCaptor.getValue().getSelectedAttributes().get(0).getAttributeValue());

        assertEquals(testCategoryLevel, requestCaptor.getValue().getCategoryLevel());
    }

    @Test
    public void testSearchProducts_pageTooHigh() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("page", "101"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("searchProducts.page"))
            .andExpect(jsonPath("$.violations[0].message").value("must be less than or equal to 100"));
    }

    @Test
    public void testSearchProducts_pageTooLow() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("page", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("searchProducts.page"))
            .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 1"));
    }

    @Test
    public void testSearchProducts_sizeTooLow() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("size", "-1"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("searchProducts.size"))
            .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    public void testSearchProducts_categoryLevelTooLowl() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("categoryLevel", "-1"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("searchProducts.categoryLevel"))
            .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    public void testSearchProducts_categoryLevelTooHigh() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("categoryLevel", "4"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("searchProducts.categoryLevel"))
            .andExpect(jsonPath("$.violations[0].message").value("must be less than or equal to 3"));
    }

    @Test
    public void testSearchProducts_malformedCategories() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("categories", "category1 value"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(
                jsonPath("$.violations[0].field")
                    .value(matchesPattern("searchProducts\\.categories\\[\\d+].<list element>"))
            )
            .andExpect(jsonPath("$.violations[0].message").value("must match \"^(.*)\\|(.*)$\""));
    }

    @Test
    public void testSearchProducts_malformedFilters() throws Exception {
        mockMvc
            .perform(get("/products").queryParam("filters", "brandgeneric"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(
                jsonPath("$.violations[0].field")
                    .value(matchesPattern("searchProducts\\.filters\\[\\d+].<list element>"))
            )
            .andExpect(jsonPath("$.violations[0].message").value("must match \"^(.*)\\|(.*)$\""));
    }
}
