package com.reece.platform.eclipse.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.DTO.CustomerSearchRequestDTO;
import com.reece.platform.eclipse.model.DTO.CustomerSearchResponseDTO;
import com.reece.platform.eclipse.model.generated.Customer;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EclipseServiceApplication.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class CustomersControllerTest {

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService; // Declared to circumvent @SpringBootTest issues

    @MockBean
    private KourierService kourierService;

    private CustomersController customerController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        customerController = new CustomersController(eclipseService);
    }

    @Test
    public void getCustomerSearch_success() throws Exception {
        var mockResponse = new CustomerSearchResponseDTO();
        when(eclipseService.getCustomerSearch(any(CustomerSearchRequestDTO.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result = this.mockMvc
                .perform(
                        post("/customers/_search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CustomerSearchRequestDTO()))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CustomerSearchResponseDTO>() {});
        });
    }

    @Test
    public void getCustomerSearch_failureEclipse() throws Exception {
        when(eclipseService.getCustomerSearch(any(CustomerSearchRequestDTO.class))).thenThrow(new EclipseTokenException());
        var objectMapper = new ObjectMapper();
        this.mockMvc
                .perform(
                        post("/customers/_search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CustomerSearchRequestDTO()))
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    public void getCustomerById_success() throws Exception {
        var mockResponse = new Customer();
        when(eclipseService.getCustomerById(any())).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result = this.mockMvc
                .perform(
                        get("/customers/12345")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Customer>() {});
        });
    }

    @Test
    public void getCustomerById_failureEclipse() throws Exception {
        when(eclipseService.getCustomerById(any())).thenThrow(new EclipseTokenException());
        this.mockMvc
                .perform(
                        get("/customers/12345")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();
    }
}
