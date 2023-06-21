package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.exceptions.OrderNotFoundException;
import com.reece.platform.products.model.DTO.GetInvoiceResponseDTO;
import com.reece.platform.products.model.DTO.MincronSingleInvoiceDTO;
import com.reece.platform.products.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { InvoiceController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private static final String AUTH_TOKEN =
        "Bearer eyJraWQiOiItYlVObXhLMndvLUR4OFg5elRlb0drTU1DZWFlMW8wRnVjYzdNVWV1QkZ3IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjVhNzNyOEtQRHJXLWJZMkJQenlKWUJQRnFucVZqdlJEcEdMU1k0c1lYYkkiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjE1ODE5NzMwLCJleHAiOjE2MTU5MDYxMzAsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MmQzMXBpejVLS0Y2c2I0eDciLCJzY3AiOlsiZW1haWwiLCJvcGVuaWQiLCJwcm9maWxlIl0sInN1YiI6InNkZmtqYnJhc250QHRlc3QuY29tIiwiZWNvbW1Vc2VySWQiOiI5ZmQxNjJiOS1iMzU0LTRkMDctYWM1My0yMDkxYjFmMmJlYzIiLCJlY29tbVBlcm1pc3Npb25zIjpbImVkaXRfcHJvZmlsZSIsImVkaXRfbGlzdCIsInN1Ym1pdF9xdW90ZV9vcmRlciIsImludml0ZV91c2VyIiwidmlld19pbnZvaWNlIiwiYXBwcm92ZV9jYXJ0IiwibWFuYWdlX3BheW1lbnRfbWV0aG9kcyIsImFwcHJvdmVfYWNjb3VudF91c2VyIiwibWFuYWdlX3JvbGVzIiwic3VibWl0X2NhcnRfd2l0aG91dF9hcHByb3ZhbCJdfQ.Ew3owigdqaPNQDUiG1kOxqkSZFFRHxFB2qAlzPiAIkT4ruXTqad9-cGZGBkhRYLjxhuuIJ2tdilb1nwuxNKEcZbxOBTrXkrfubh-_FrDkHjVVa59mvcXbgd1gCcADd3bs_wZCzpyJDBF_LQ3h5cYOtgvwubMQ-71Hi127fP-SgVwYmb38bMVY4IKTkja-vCeoixKGjk8p_1YRCvdpfnGxJaP9vgUqGP1-ebnN5EzO0WlkC7IHtnjXcHZdUHtIKA6NCkvlCI127_Rh8gCU6mRVoQdHjKqya8zvSkNuKAz8NijsJAk3gLSJo4Fxv_7qedBDW5XbmBaaHhw-pBezn0JSQ";

    private static final String AUTH_TOKEN_UNAUTHORIZED =
        "Bearer eyJraWQiOiJ6NjFVY0duNkRtUnhYVDM1b1JhN3h6QjJjamJiUE9nVVFpSHhET3JlbndnIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULnZEMU5hcWpoN2ZyN0xJM0ZXbTlrUDV6WDdkX01kRVJUNGx5bzFuQVFJTlUiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjE5NDY2NjA4LCJleHAiOjE2MTk1NTMwMDgsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1Mnludmg0ZEswcXRRWjE0eDciLCJzY3AiOlsib3BlbmlkIiwicHJvZmlsZSIsImVtYWlsIl0sInN1YiI6ImJyYW50X3Rlc3RfcmVlY2VfNUBnbWFpbC5jb20iLCJlY29tbVVzZXJJZCI6IjViZTJmNjllLTY4YmItNDRiMy1hYmZkLTA2NmQ4ZDIwZmIyZiIsImVjb21tUGVybWlzc2lvbnMiOlsiYXBwcm92ZV9hbGxfdXNlcnMiLCJzdWJtaXRfY2FydF93aXRob3V0X2FwcHJvdmFsIiwiYXBwcm92ZV9jYXJ0IiwiZWRpdF9saXN0IiwiZWRpdF9wcm9maWxlIiwibWFuYWdlX3JvbGVzIiwiaW52aXRlX3VzZXIiLCJhcHByb3ZlX2FjY291bnRfdXNlciIsIm1hbmFnZV9wYXltZW50X21ldGhvZHMiLCJzdWJtaXRfcXVvdGVfb3JkZXIiXX0.QvNHqf9U7jf8Hr590iadOZoGmQPQNtYsDbRIHusdw4mHMUQsbdlp3pulH1ijNOBKDfSUX3t0fy1ze8wJ8oT5Hmb5sGTxrnfD6wPtY-HR1IrkSam2AJ3xxgSGWGuS5xFn46EXSfS1CGgLC-hm_otv8hFQlTOffLgOBK5Ee00HmnkjIFwYNronkclq-z5aaj3qqF9jYnit0puQLUh5RQFFvm4xjowil-RgWQ5pr2s_rMWQ9s6XrwWrOmW0PfMcWJ2Fi9Ra1NUrcks_HtxZQindGmvfFIDXWzPbv-PlF7-LRGTJ23tQr2pIXrqwhMnpokYIRSvn4RXp033UdQywCRNoUA";

    private static final String ACCOUNT_ID = "accountId";
    private static final String INVOICE_NUMBER = "invoiceNumber";

    @BeforeEach
    void setUp() {}

    @Test
    void getInvoice_authorized() throws Exception {
        String accountId = "555";
        String erpName = "ECLIPSE";
        when(invoiceService.getInvoices(accountId, erpName, null, null, null, null))
            .thenReturn(new GetInvoiceResponseDTO());
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices", accountId)
                        .header("Authorization", AUTH_TOKEN)
                        .param("erpName", erpName)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetInvoiceResponseDTO.class)
        );
    }

    @Test
    void getInvoice_unauthorized() throws Exception {
        String accountId = "555";
        String erpName = "ECLIPSE";
        when(invoiceService.getInvoices(accountId, erpName, null, null, null, null))
            .thenReturn(new GetInvoiceResponseDTO());
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices", accountId)
                        .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                        .param("erpName", erpName)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertThrows(
            JsonProcessingException.class,
            () -> objectMapper.readValue(result.getResponse().getContentAsString(), GetInvoiceResponseDTO.class)
        );
    }

    @Test
    void getInvoicesPdf_success() throws Exception {
        when(invoiceService.getInvoicesPdfUrl(any(), any())).thenReturn("test.com");
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices-pdf?invoiceNumbers=1,2,3", "555").header("Authorization", AUTH_TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "test.com");
    }

    @Test
    void getInvoicesPdf_unauthorized() throws Exception {
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices-pdf?invoiceNumbers=1,2,3", "555")
                        .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void getMincronInvoice_success() throws Exception {
        when(invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER)).thenReturn(new MincronSingleInvoiceDTO());
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices/{invoiceNumber}", ACCOUNT_ID, INVOICE_NUMBER)
                        .header("Authorization", AUTH_TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), MincronSingleInvoiceDTO.class)
        );
    }

    @Test
    void getMincronInvoice_unauthorized() throws Exception {
        MvcResult result =
            this.mockMvc.perform(
                    get("/{accountId}/invoices/{invoiceNumber}", ACCOUNT_ID, INVOICE_NUMBER)
                        .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertThrows(
            JsonProcessingException.class,
            () -> objectMapper.readValue(result.getResponse().getContentAsString(), MincronSingleInvoiceDTO.class)
        );
    }

    @Test
    void getMincronInvoice_throwsOrderNotFound() throws Exception {
        when(invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER))
            .thenThrow(new OrderNotFoundException(ACCOUNT_ID, INVOICE_NUMBER));

        this.mockMvc.perform(
                get("/{accountId}/invoices/{invoiceNumber}", ACCOUNT_ID, INVOICE_NUMBER)
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof OrderNotFoundException));
    }
}
