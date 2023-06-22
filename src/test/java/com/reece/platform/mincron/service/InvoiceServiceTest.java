package com.reece.platform.mincron.service;

import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.InvoiceDTO;
import com.reece.platform.mincron.model.OrderDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;
import com.reece.platform.mincron.utilities.MincronDataFormatting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.client.MockRestServiceServer;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RestClientTest(InvoiceService.class)
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @MockBean
    private ManagedCallFactory managedCallFactory;

    @MockBean
    private CallBuilderConfig callBuilderConfig;

    @MockBean
    private ResponseBuilderConfig responseBuilderConfig;

    @MockBean
    private CallableStatement callableStatement;

    @MockBean
    private OrderService orderService;

    private static final String ERP_ACCOUNT_ID = "12345";
    private static final int MAX_ORDER_RESULT_SIZE = 10000;
    private static final String ORDER_TYPE = "order";
    private static final String ORDER_STATUS = "ALL";
    private static final String INVOICED_STATUS = "Invoiced";
    private static final String OPEN_STATUS = "Open";
    private static final String CLOSED_STATUS = "Closed";
    private static final int STARTING_ROW = 1;
    private static final String OUT_OF_RANGE_INVOICE_DATE = "2031993";
    private static final String ZERO_BALANCE = "0.0d";

    private static final SimpleDateFormat MINCRON_ORDER_DATE_FORMAT = new SimpleDateFormat("MMddyyyy");
    private static final SimpleDateFormat MAX_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final String VALID_CLOSED_INVOICE_DATE = MINCRON_ORDER_DATE_FORMAT.format(new Date());

    private static final String TRANSACTION_NUMBER_1 = "transaction_1";
    private static final String INVOICE_MONTH_1 = "01";
    private static final String INVOICE_DAY_1 = "01";
    private static final String INVOICE_CENTURY_1 = "20";
    private static final String INVOICE_YEAR_1 = "20";
    private static final String PURCHASE_ORDER_NUMBER_1 = "PO";
    private static final String TRANSACTION_AGING_CODE_1 = "8";
    private static final String JOB_NUMBER_1 = "job number 1";
    private static final String JOB_NAME_1 = "job name 1";
    private static final String TOTAL_DUE_1 = "1234.56";
    private static final String INVOICE_DATE_1 = String.format("%s/%s/%s", INVOICE_MONTH_1, INVOICE_DAY_1, INVOICE_CENTURY_1 + INVOICE_YEAR_1);
    private static final String ORDER_TOTAL_1 = "2000.00";
    private static final String CONTRACT_NUMBER_1 = "123456";

    private static final String TRANSACTION_NUMBER_2 = "transaction_2";
    private static final String INVOICE_MONTH_2 = "02";
    private static final String INVOICE_DAY_2 = "02";
    private static final String INVOICE_CENTURY_2 = "20";
    private static final String INVOICE_YEAR_2 = "21";
    private static final String PURCHASE_ORDER_NUMBER_2 = "PO2";
    private static final String TRANSACTION_AGING_CODE_2 = "4";
    private static final String JOB_NUMBER_2 = "job number 2";
    private static final String JOB_NAME_2 = "job name 2";
    private static final String TOTAL_DUE_2 = "4321.56";
    private static final String INVOICE_DATE_2 = String.format("%s/%s/%s", INVOICE_MONTH_2, INVOICE_DAY_2, INVOICE_CENTURY_2 + INVOICE_YEAR_2);
    private static final String ORDER_TOTAL_2 = "5000.00";
    private static final String CONTRACT_NUMBER_2 = "7891011";

    private static final String TRANSACTION_NUMBER_3 = "transaction_3";
    private static final String PURCHASE_ORDER_NUMBER_3 = "PO3";
    private static final String JOB_NUMBER_3 = "job number 3";
    private static final String JOB_NAME_3 = "job name 3";

    private static final String TRANSACTION_NUMBER_4 = "transaction_4";

    private static final String TRANSACTION_NUMBER_5 = "transaction_5";

    @Test
    void getInvoices_success() throws Exception {
        when(managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_INVOICE_LIST.getProgramCallNumber(), 9, true)).thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(1)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(true, true, false);

        when(responseBuilderConfig.getResultString()).thenReturn(TRANSACTION_NUMBER_1,
                INVOICE_MONTH_1,
                INVOICE_DAY_1,
                INVOICE_CENTURY_1,
                INVOICE_YEAR_1,
                PURCHASE_ORDER_NUMBER_1,
                TRANSACTION_AGING_CODE_1,
                JOB_NUMBER_1,
                JOB_NAME_1,
                TOTAL_DUE_1,
                "",
                CONTRACT_NUMBER_1,
                ORDER_TOTAL_1,
                TRANSACTION_NUMBER_2,
                INVOICE_MONTH_2,
                INVOICE_DAY_2,
                INVOICE_CENTURY_2,
                INVOICE_YEAR_2,
                PURCHASE_ORDER_NUMBER_2,
                TRANSACTION_AGING_CODE_2,
                JOB_NUMBER_2,
                JOB_NAME_2,
                TOTAL_DUE_2,
                "",
                CONTRACT_NUMBER_2,
                ORDER_TOTAL_2);

        OrderDTO orderDTO1 = new OrderDTO();
        OrderDTO orderDTO2 = new OrderDTO();
        OrderDTO validClosedInvoice = new OrderDTO();
        OrderDTO outOfDateRangeClosedInvoice = new OrderDTO();
        OrderDTO nonInvoicedOrder = new OrderDTO();

        orderDTO1.setOrderNumber(TRANSACTION_NUMBER_1);
        orderDTO2.setOrderNumber(TRANSACTION_NUMBER_2);
        validClosedInvoice.setOrderNumber(TRANSACTION_NUMBER_3);
        validClosedInvoice.setInvoiceNumber(TRANSACTION_NUMBER_3);
        outOfDateRangeClosedInvoice.setOrderNumber(TRANSACTION_NUMBER_4);
        nonInvoicedOrder.setOrderNumber(TRANSACTION_NUMBER_5);

        validClosedInvoice.setStatus(INVOICED_STATUS);
        outOfDateRangeClosedInvoice.setStatus(INVOICED_STATUS);
        orderDTO1.setOrderNumber(TRANSACTION_NUMBER_1);
        orderDTO2.setOrderNumber(TRANSACTION_NUMBER_2);
        nonInvoicedOrder.setStatus(OPEN_STATUS);

        nonInvoicedOrder.setInvoiceDate(VALID_CLOSED_INVOICE_DATE);
        validClosedInvoice.setInvoiceDate(VALID_CLOSED_INVOICE_DATE);
        outOfDateRangeClosedInvoice.setInvoiceDate(OUT_OF_RANGE_INVOICE_DATE);

        validClosedInvoice.setJobName(JOB_NAME_3);
        validClosedInvoice.setJobNumber(JOB_NUMBER_3);
        validClosedInvoice.setPurchaseOrderNumber(PURCHASE_ORDER_NUMBER_3);

        PageDTO<OrderDTO> orderListResponse = new PageDTO<>();
        orderListResponse.setResults(Arrays.asList(orderDTO1, orderDTO2, validClosedInvoice, outOfDateRangeClosedInvoice, nonInvoicedOrder));

        when(orderService.getOrderList(eq(ERP_ACCOUNT_ID), eq(ORDER_TYPE), eq(ORDER_STATUS), eq(STARTING_ROW), eq(MAX_ORDER_RESULT_SIZE), eq(""), any(), any(), eq(""), eq(""))).thenReturn(orderListResponse);

        List<InvoiceDTO> invoiceDTOS = invoiceService.getInvoices(ERP_ACCOUNT_ID);

        assertEquals(invoiceDTOS.size(), 3, "Expected three invoices to return.");

        InvoiceDTO invoiceDTO1 = invoiceDTOS.get(2);
        InvoiceDTO invoiceDTO2 = invoiceDTOS.get(1);
        InvoiceDTO invoiceDTO3 = invoiceDTOS.get(0);

        assertEquals(invoiceDTO1.getInvoiceDate(), INVOICE_DATE_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getInvoiceNumber(), TRANSACTION_NUMBER_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getJobName(), JOB_NAME_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getJobNumber(), JOB_NUMBER_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getCustomerPo(), PURCHASE_ORDER_NUMBER_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getOpenBalance(), TOTAL_DUE_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getAge(), TRANSACTION_AGING_CODE_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getStatus(), OPEN_STATUS, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getContractNumber(), CONTRACT_NUMBER_1, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO1.getOriginalAmt(), ORDER_TOTAL_1, "Expected mocked invoice data to equal returned values.");


        assertEquals(invoiceDTO2.getInvoiceDate(), INVOICE_DATE_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getInvoiceNumber(), TRANSACTION_NUMBER_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getJobName(), JOB_NAME_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getJobNumber(), JOB_NUMBER_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getCustomerPo(), PURCHASE_ORDER_NUMBER_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getOpenBalance(), TOTAL_DUE_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getAge(), TRANSACTION_AGING_CODE_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getStatus(), OPEN_STATUS, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getContractNumber(), CONTRACT_NUMBER_2, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO2.getOriginalAmt(), ORDER_TOTAL_2, "Expected mocked invoice data to equal returned values.");

        assertEquals(invoiceDTO3.getInvoiceDate(), MAX_DATE_FORMAT.format(MINCRON_ORDER_DATE_FORMAT.parse(VALID_CLOSED_INVOICE_DATE)), "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getInvoiceNumber(), TRANSACTION_NUMBER_3, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getJobName(), JOB_NAME_3, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getJobNumber(), JOB_NUMBER_3, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getCustomerPo(), PURCHASE_ORDER_NUMBER_3, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getOpenBalance(), ZERO_BALANCE, "Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getAge(), "","Expected mocked invoice data to equal returned values.");
        assertEquals(invoiceDTO3.getStatus(), CLOSED_STATUS, "Expected mocked invoice data to equal returned values.");
    }

    @Test
    void getInvoices_noInvoices() throws Exception {
        when(managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_INVOICE_LIST.getProgramCallNumber(), 9, true)).thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(1)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(false);

        List<InvoiceDTO> invoiceDTOS = invoiceService.getInvoices(ERP_ACCOUNT_ID);

        assertTrue(invoiceDTOS.isEmpty(), "Expected empty list of invoices to return.");
        verify(orderService, times(0)).getOrderList(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void getInvoices_sqlException() throws Exception {
        when(managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_INVOICE_LIST.getProgramCallNumber(), 9, true)).thenThrow(SQLException.class);

        assertThrows(MincronException.class, () -> invoiceService.getInvoices(ERP_ACCOUNT_ID));
    }

    @Test
    void getInvoices_generalException() throws Exception {
        when(managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_INVOICE_LIST.getProgramCallNumber(), 9, true)).thenThrow(Exception.class);

        assertThrows(MincronException.class, () -> invoiceService.getInvoices(ERP_ACCOUNT_ID));
    }
}