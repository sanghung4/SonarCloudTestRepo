package com.reece.platform.inventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.ProductSearchRequestDTO;
import com.reece.platform.inventory.dto.ProductSearchResultDTO;
import com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.util.TestCommon;
import java.net.URLEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

class ProductsControllerTest {

    @Mock
    private EclipseService mockEclipseService;

    @InjectMocks
    private ProductsController productsController;

    @Autowired
    private MockMvc mockMvc;

    private String keywords = "BLK TAPE";
    private String displayName = null;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productsController = new ProductsController(mockEclipseService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(productsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void searchProducts_shouldReturnStatus200() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(new ProductSearchRequestDTO());
        mockMvc
            .perform(post("/products/search").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).getProductSearch(any(ProductSearchRequestDTO.class));
    }

    @Test
    void kourierSearchProducts_failure() throws Exception {
        mockMvc
            .perform(get("/products/search").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
