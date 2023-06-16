package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.dto.ProductSearchResultDTO;
import com.reece.platform.mincron.service.MincronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductsControllerTest {

    @Mock
    private MincronService mockMincronService;

    @InjectMocks
    private ProductsController productsController;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productsController = new ProductsController(mockMincronService);
        mockMvc =
                MockMvcBuilders.standaloneSetup(productsController)
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                        .build();
    }

    @Test
    void search_shouldReturnStatus200() throws Exception {
        when(mockMincronService.searchProducts(anyString(), anyString(), anyString())).thenReturn(new ProductSearchResultDTO());

        mockMvc.perform(get("/products/search?branchNum=Testing&query=Testing&lastItem=Testing"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).searchProducts(anyString(), anyString(), anyString());
    }

    @Test
    void search_shouldReturnBadRequestStatus400() throws Exception {
        when(mockMincronService.searchProducts(anyString(), anyString(), anyString())).thenReturn(new ProductSearchResultDTO());

        mockMvc.perform(get("/products/search?branchNum=Testing&Wrong param=Testing&lastItem=Testing"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
