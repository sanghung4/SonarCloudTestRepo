package com.reece.platform.eclipse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.model.DTO.AccountInquiryResponseDTO;
import com.reece.platform.eclipse.model.DTO.InvoiceSummaryDTO;
import com.reece.platform.eclipse.model.DTO.InvoiceWrapperDTO;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import com.reece.platform.eclipse.testConstants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EclipseServiceApplication.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class InvoiceControllerTest {

    @MockBean
    private KourierService kourierService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService;

    private InvoiceController invoiceController;

    @Autowired
    private MockMvc mockMvc;

    private final static String TEST_ACCOUNT_ID = "555";
    private final static String TEST_SHIP_TO = "238348";
    private final static Date TEST_DATE = new Date("12/10/2022");
    private final static String TEST_INVOICE_STATUS = "Closed";

    @BeforeEach
    public void setup() throws Exception {
        invoiceController = new InvoiceController(kourierService);
    }

    @Test
    public void getAccountInquiry_success() throws Exception {
        AccountInquiryResponseDTO accountInquiryResponseDTO = TestConstants.GetAccountInquirySuccess();
        when(kourierService.getInvoices(
                TEST_ACCOUNT_ID,
                TEST_SHIP_TO,
                TEST_DATE,
                TEST_DATE,
                TEST_INVOICE_STATUS
        )).thenReturn(accountInquiryResponseDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(get("/accounts/{accountId}/invoices", TEST_ACCOUNT_ID)
                        .param("shipTo", TEST_SHIP_TO)
                        .param("startDate", "12/10/2022")
                        .param("endDate", "12/10/2022")
                        .param("invoiceStatus", TEST_INVOICE_STATUS)

                ).andExpect(status().isOk()).
                        andReturn();
        String x = result.getResponse().getContentAsString();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), AccountInquiryResponseDTO.class));
    }
}
