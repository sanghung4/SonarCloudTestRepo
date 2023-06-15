package com.reece.platform.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.CustomerSearchInputDTO;
import com.reece.platform.inventory.dto.CustomerSearchResponseDTO;
import com.reece.platform.inventory.external.eclipse.EclipseService;
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
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @Mock
    private EclipseService mockEclipseService;

    @InjectMocks
    private CustomerController customerController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(
                mockEclipseService
        );
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void getCustomerSearch_shouldReturnStatus200() throws Exception {
        when(mockEclipseService
                .getCustomerSearch(any(CustomerSearchInputDTO.class)))
                .thenReturn(new CustomerSearchResponseDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(new CustomerSearchInputDTO());
        mockMvc.perform(post("/customer/_search")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(mockEclipseService,times(1))
                .getCustomerSearch(any(CustomerSearchInputDTO.class));
    }

    @Test
    void getCustomerSearch_shouldReturnBadRequestStatus400() throws Exception {
        when(mockEclipseService
                .getCustomerSearch(any(CustomerSearchInputDTO.class)))
                .thenReturn(new CustomerSearchResponseDTO());

        mockMvc.perform(post("/customer/_search")
                //missing content to trigger Bad Request
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}