package com.reece.platform.mincron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.InvoiceDTO;
import com.reece.platform.mincron.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {InvoiceController.class, GlobalExceptionHandler.class})
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private InvoiceController invoiceController;

    private static final String ERP_ACCOUNT_ID = "123456";
    private static final String ERROR_MESSAGE = "error";

    @BeforeEach
    public void setup() {
        invoiceController =  new InvoiceController(invoiceService);
    }

    @Test
    public void getInvoiceList_success() throws Exception {
        List<InvoiceDTO> response = new ArrayList<>();

        when(invoiceService.getInvoices(ERP_ACCOUNT_ID)).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        MvcResult result =
                this.mockMvc.perform(get("/invoices")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class));
    }

    @Test
    public void getInvoiceList_failed_parseException() throws Exception {

        when(invoiceService.getInvoices(ERP_ACCOUNT_ID)).thenThrow(new ParseException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST.value()));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        this.mockMvc.perform(get("/invoices")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof ParseException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getInvoiceList_failed_mincronException() throws Exception {

        when(invoiceService.getInvoices(ERP_ACCOUNT_ID)).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        this.mockMvc.perform(get("/invoices")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }
}
