package com.reece.platform.mincron.service;

import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.OrderDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.model.enums.OrderStatusEnum;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;
import com.reece.platform.mincron.model.enums.ShipmentMethodEnum;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static com.reece.platform.mincron.controller.OrderControllerTest.ORDER_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RestClientTest(OrderService.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

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

    @Value("${mincron_host_websmart}")
    private String mincronHostUrl;

    private static final int ORDER_CALLS_STARTING_ROW = 2;

    private static final String ERP_ACCOUNT_ID = "123456";
    public static final int START_ROW = 1;
    public static final int MAX_ROWS = 10;
    public static final String MINCRON_WEBSMART_HOST_URL = "http://localhost:8100/WebSmart-apiTest";
    public static final int DEFAULT_START_ROW = 1;
    public static final int DEFAULT_MAX_ROWS = 10;
    public static final String ORDER_TYPE = "ORDER";
    private static final String ORDER_NUMBER = "NUMBER";
    private static final String SHIPPED_ORDER_STATUS = "Shipped";
    private static final String SEARCH_FILTER = "search";
    private static final String FROM_DATE = "2/2/2012";
    private static final String TO_DATE = "3/2/2012";
    private static final String SORT_ORDER = "orderTotal";
    private static final String SORT_DIRECTION = "ASC";

    private static final String ORDER_NUMBER_1 = "333";
    private static final String INVOICE_NUMBER_1 = "444";
    private static final String STATUS_1 = OrderStatusEnum.O.toString();
    private static final String ORDER_DATE_1 = "1/1/2002";
    private static final String SHIP_DATE_1 = "2/2/2002";
    private static final String TRACKING_NUMBER_1 = "32123212";
    private static final String PO_NUMBER_1 = "po333";
    private static final String JOB_NUMBER_1 = "job num 1";
    private static final String JOB_NAME_1 = "job name 1";
    private static final String ORDER_TOTAL_1 = "324.12";
    private static final String CONTRACT_NUMBER_1 = "342132";
    private static final String DISPLAY_ONLY_N = "N";
    private static final String PRODUCT_NUMBER_1 = "prod_num_1";
    private static final String PRODUCT_DESCRIPTION_1 = "prod_desc_1";
    private static final String UOM_1 = "ft";
    private static final String LINE_NUMBER_1 = "6";
    private static final String QUANTITY_ORDERED_1 = "12";
    private static final String UNIT_PRICE_1 = "10.23";
    private static final String EXTENDED_PRICE_1 = "11.23";
    private static final String QUANTITY_BACK_ORDERED_1 = "5";
    private static final String QUANTITY_SHIPPED_1 = "8";
    private static final String TYPE_CODE_1 = "G";

    private static final String ORDER_NUMBER_2 = "555";
    private static final String INVOICE_NUMBER_2 = "666";
    private static final String STATUS_2 = OrderStatusEnum.R.toString();
    private static final String ORDER_DATE_2 = "3/3/2003";
    private static final String SHIP_DATE_2 = "4/4/2002";
    private static final String TRACKING_NUMBER_2 = "52123212";
    private static final String PO_NUMBER_2 = "po555";
    private static final String JOB_NUMBER_2 = "job num 2";
    private static final String JOB_NAME_2 = "job name 2";
    private static final String ORDER_TOTAL_2 = "444.12";
    private static final String CONTRACT_NUMBER_ZEROES = "000000";
    private static final String DISPLAY_ONLY_Y = "Y";

    @Test
    public void getOrderList_success() throws Exception {
        when(managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_LIST.getProgramCallNumber(), 21, true)).thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(ORDER_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(true, true, false);

        when(responseBuilderConfig.getResultString()).thenReturn(ORDER_NUMBER_1,
            INVOICE_NUMBER_1,
            STATUS_1,
            ORDER_DATE_1,
            SHIP_DATE_1,
            "",
            TRACKING_NUMBER_1,
            PO_NUMBER_1,
            JOB_NUMBER_1,
            JOB_NAME_1,
            ORDER_TOTAL_1,
            "",
            CONTRACT_NUMBER_1,
            ORDER_NUMBER_2,
            INVOICE_NUMBER_2,
            STATUS_2,
            ORDER_DATE_2,
            SHIP_DATE_2,
                "",
            TRACKING_NUMBER_2,
            PO_NUMBER_2,
            JOB_NUMBER_2,
            JOB_NAME_2,
            ORDER_TOTAL_2,
            "",
            CONTRACT_NUMBER_ZEROES);

        PageDTO<OrderDTO> ordersList = orderService.getOrderList(ERP_ACCOUNT_ID, ORDER_TYPE, ORDER_STATUS, START_ROW, MAX_ROWS, SEARCH_FILTER, FROM_DATE, TO_DATE, SORT_ORDER, SORT_DIRECTION);

        OrderDTO orderDTO1 = ordersList.getResults().get(0);
        OrderDTO orderDTO2 = ordersList.getResults().get(1);

        assertEquals(ORDER_NUMBER_1, orderDTO1.getOrderNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(INVOICE_NUMBER_1, orderDTO1.getInvoiceNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(OrderStatusEnum.O.getOrderStatus(), orderDTO1.getStatus(), "Expected mocked program call value to equal actual value");
        assertEquals(ORDER_DATE_1, orderDTO1.getOrderDate(), "Expected mocked program call value to equal actual value");
        assertEquals(SHIP_DATE_1, orderDTO1.getShipDate(), "Expected mocked program call value to equal actual value");
        assertEquals(TRACKING_NUMBER_1, orderDTO1.getTrackingNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(PO_NUMBER_1, orderDTO1.getPurchaseOrderNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(JOB_NUMBER_1, orderDTO1.getJobNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(JOB_NAME_1, orderDTO1.getJobName(), "Expected mocked program call value to equal actual value");
        assertEquals(ORDER_TOTAL_1, orderDTO1.getOrderTotal(), "Expected mocked program call value to equal actual value");
        assertEquals(CONTRACT_NUMBER_1, orderDTO1.getContractNumber(), "Expected mocked program call value to equal actual value");

        assertEquals(ORDER_NUMBER_2, orderDTO2.getOrderNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(INVOICE_NUMBER_2, orderDTO2.getInvoiceNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(SHIPPED_ORDER_STATUS, orderDTO2.getStatus(), "Expected mocked program call value to equal actual value");
        assertEquals(ORDER_DATE_2, orderDTO2.getOrderDate(), "Expected mocked program call value to equal actual value");
        assertEquals(SHIP_DATE_2, orderDTO2.getShipDate(), "Expected mocked program call value to equal actual value");
        assertEquals(TRACKING_NUMBER_2, orderDTO2.getTrackingNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(PO_NUMBER_2, orderDTO2.getPurchaseOrderNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(JOB_NUMBER_2, orderDTO2.getJobNumber(), "Expected mocked program call value to equal actual value");
        assertEquals(JOB_NAME_2, orderDTO2.getJobName(), "Expected mocked program call value to equal actual value");
        assertEquals(ORDER_TOTAL_2, orderDTO2.getOrderTotal(), "Expected mocked program call value to equal actual value");
        assertEquals("", orderDTO2.getContractNumber(), "Expected mocked program call value to equal actual value");

        verify(callBuilderConfig, times(1)).setInputString(ERP_ACCOUNT_ID);
        verify(callBuilderConfig, times(1)).setInputString(ORDER_TYPE.toUpperCase());
        verify(callBuilderConfig, times(1)).setInputString(ORDER_STATUS);

        verify(callBuilderConfig, times(1)).setInputString(SEARCH_FILTER);
        verify(callBuilderConfig, times(1)).setInputString(FROM_DATE);
        verify(callBuilderConfig, times(1)).setInputString(TO_DATE);
        verify(callBuilderConfig, times(1)).setInputString(SORT_ORDER);
        verify(callBuilderConfig, times(1)).setInputString(SORT_DIRECTION);

        verify(callBuilderConfig, times(1)).setInputInt(START_ROW);
        verify(callBuilderConfig, times(1)).setInputInt(MAX_ROWS);

        verify(callBuilderConfig, times(2)).setOutputChar();
        verify(callBuilderConfig, times(2)).setOutputDecimal();
    }

    @Test
    void getOrderList_MincronException_SQL() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_LIST.getProgramCallNumber(), 21, true)
        ).thenThrow(SQLException.class);

        val responseStatus = assertThrows(
                MincronException.class,
                () -> orderService.getOrderList(ERP_ACCOUNT_ID, ORDER_TYPE, ORDER_STATUS, START_ROW, MAX_ROWS, SEARCH_FILTER, FROM_DATE, TO_DATE, SORT_ORDER, SORT_DIRECTION)
        )
                .getHttpStatus();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    void getOrderList_MincronException_Exception() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_LIST.getProgramCallNumber(), 21, true)
        ).thenThrow(Exception.class);

        val responseStatus = assertThrows(
                MincronException.class,
                () -> orderService.getOrderList(ERP_ACCOUNT_ID, ORDER_TYPE, ORDER_STATUS, START_ROW, MAX_ROWS, SEARCH_FILTER, FROM_DATE, TO_DATE, SORT_ORDER, SORT_DIRECTION)
        )
                .getHttpStatus();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    public void getOrderHeader_success() throws Exception {
        val testOrderNumber = "12345";
        val testOrderType = "ORDER";

        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_HEADER.getProgramCallNumber(), 78, true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getCs()).thenReturn(callableStatement);

        String jobName = "job name";
        String jobNumber = "job number";
        String contractNumber = "contractNumber";
        String terms = "terms";
        when(callableStatement.getString(43)).thenReturn(ShipmentMethodEnum.O.toString());
        when(callableStatement.getString(18)).thenReturn(OrderStatusEnum.P.toString());
        when(callableStatement.getString(39)).thenReturn(jobNumber);
        when(callableStatement.getString(40)).thenReturn(jobName);
        when(callableStatement.getString(63)).thenReturn(contractNumber);
        when(callableStatement.getString(64)).thenReturn(terms);

        String month = "01";
        String day = "01";
        String year = "2001";
        String expectedDateFormat = month + "/" + day + "/" + year;
        when(callableStatement.getString(19)).thenReturn(month); // order month
        when(callableStatement.getString(20)).thenReturn(day); // order day
        when(callableStatement.getString(22)).thenReturn(year); //order yr
        when(callableStatement.getString(23)).thenReturn(month); // ship mo
        when(callableStatement.getString(24)).thenReturn(day); // ship day
        when(callableStatement.getString(26)).thenReturn(year); // ship yr
        when(callableStatement.getString(65)).thenReturn(month); // due date month
        when(callableStatement.getString(66)).thenReturn(day); // due date day
        when(callableStatement.getString(68)).thenReturn(year); // due date yr
        when(callableStatement.getString(69)).thenReturn(month); // invoice date month
        when(callableStatement.getString(70)).thenReturn(day); // invoice date day
        when(callableStatement.getString(72)).thenReturn(year); // invoice date yr

        String purchaseOrderNum = "po num";
        String orderedBy = "ordered by";
        when(callableStatement.getString(41)).thenReturn(purchaseOrderNum);
        when(callableStatement.getString(42)).thenReturn(orderedBy);

        BigDecimal otherCharges = BigDecimal.valueOf(1.0);
        BigDecimal subTotal = BigDecimal.valueOf(10.0);
        BigDecimal taxAmount = BigDecimal.valueOf(11.0);
        BigDecimal totalAmount = BigDecimal.valueOf(12.0);

        when(callableStatement.getBigDecimal(56)).thenReturn(otherCharges);
        when(callableStatement.getBigDecimal(55)).thenReturn(subTotal);
        when(callableStatement.getBigDecimal(57)).thenReturn(taxAmount);
        when(callableStatement.getBigDecimal(59)).thenReturn(totalAmount);

        val response = orderService.getOrderHeader(testOrderNumber, testOrderType);
        verify(callBuilderConfig, times(63)).setOutputChar();
        verify(callBuilderConfig, times(2)).setOutputNumeric(0);
        verify(callBuilderConfig, times(6)).setOutputNumeric(2);
        verify(callBuilderConfig, times(1)).setInputString(testOrderNumber);
        verify(callBuilderConfig, times(1)).setInputString(testOrderType);

        assertEquals(response.getShipmentMethod(), "Our Truck", "Expected order header to have shipment method of Our Truck");
        assertEquals(expectedDateFormat, response.getOrderDate(), "Expected order header to have order date in proper format.");
        assertEquals(response.getTaxAmount(), taxAmount.floatValue(), "Expected order header to have mocked tax amount");
        assertEquals(response.getTotalAmount(), totalAmount.floatValue(), "Expected order header to have mocked total amount");
        assertEquals(response.getSubTotal(), subTotal.floatValue(), "Expected order header to have mocked subtotal amount");
        assertEquals(response.getOrderBy(), orderedBy, "Expected order header to have mocked ordered by");
        assertEquals(response.getPurchaseOrderNumber(), purchaseOrderNum, "Expected order header to have mocked purchase order number");
        assertEquals(response.getOrderStatus(), "Shipped", "Expected normalized order status to be 'Shipped' since ERP order status is 'Priced'");
        assertEquals(contractNumber, response.getContractNumber(), "Expected order header to have contract number: " + contractNumber);
        assertEquals(terms, response.getTerms(), "Expected order header to have terms: " + terms);
        assertEquals(expectedDateFormat, response.getDueDate(), "Expected due date to be in MM/DD/YYYY format.");
        assertEquals(expectedDateFormat, response.getInvoiceDate(), "Expected due date to be in MM/DD/YYYY format.");
    }

    @Test
    public void getOrderHeader_PendingOrderEmptyShipmentMethod() throws Exception {
        val testOrderNumber = "12345";
        val testOrderType = "ORDER";

        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_HEADER.getProgramCallNumber(),
                        78,
                        true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getCs()).thenReturn(callableStatement);

        String jobName = "job name";
        String jobNumber = "job number";
        String contractNumber = "contractNumber";
        String terms = "terms";
        when(callableStatement.getString(43)).thenReturn("");
        when(callableStatement.getString(18)).thenReturn(OrderStatusEnum.P.toString());
        when(callableStatement.getString(39)).thenReturn(jobNumber);
        when(callableStatement.getString(40)).thenReturn(jobName);
        when(callableStatement.getString(63)).thenReturn(contractNumber);
        when(callableStatement.getString(64)).thenReturn(terms);

        String month = "01";
        String day = "01";
        String year = "2001";
        String expectedDateFormat = month + "/" + day + "/" + year;
        when(callableStatement.getString(19)).thenReturn(month); // order month
        when(callableStatement.getString(20)).thenReturn(day); // order day
        when(callableStatement.getString(22)).thenReturn(year); //order yr
        when(callableStatement.getString(23)).thenReturn(month); // ship mo
        when(callableStatement.getString(24)).thenReturn(day); // ship day
        when(callableStatement.getString(26)).thenReturn(year); // ship yr
        when(callableStatement.getString(65)).thenReturn(month); // due date month
        when(callableStatement.getString(66)).thenReturn(day); // due date day
        when(callableStatement.getString(68)).thenReturn(year); // due date yr
        when(callableStatement.getString(69)).thenReturn(month); // invoice date month
        when(callableStatement.getString(70)).thenReturn(day); // invoice date day
        when(callableStatement.getString(72)).thenReturn(year); // invoice date yr


        String purchaseOrderNum = "po num";
        String orderedBy = "ordered by";
        when(callableStatement.getString(41)).thenReturn(purchaseOrderNum);
        when(callableStatement.getString(42)).thenReturn(orderedBy);

        BigDecimal otherCharges = BigDecimal.valueOf(1.0);
        BigDecimal subTotal = BigDecimal.valueOf(10.0);
        BigDecimal taxAmount = BigDecimal.valueOf(11.0);
        BigDecimal totalAmount = BigDecimal.valueOf(12.0);

        when(callableStatement.getBigDecimal(56)).thenReturn(otherCharges);
        when(callableStatement.getBigDecimal(55)).thenReturn(subTotal);
        when(callableStatement.getBigDecimal(57)).thenReturn(taxAmount);
        when(callableStatement.getBigDecimal(59)).thenReturn(totalAmount);

        val response = orderService.getOrderHeader(testOrderNumber, testOrderType);

        assertEquals(response.getShipmentMethod(), "N/A", "Shipment status should resolve to " +
                "'N/A' when value from mincron is an empty string");
    }

    @Test
    void getOrderHeader_MincronException_SQL() throws Exception {
        val testOrderNumber = "12345";
        val testOrderType = "ORDER";

        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_HEADER.getProgramCallNumber(),
                        78,
                        true)
        ).thenThrow(SQLException.class);

        val responseStatus = assertThrows(
                MincronException.class,
                () -> orderService.getOrderHeader(testOrderNumber, testOrderType)
        )
                .getHttpStatus();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    void getOrderHeader_MincronException_Exception() throws Exception {
        val testOrderNumber = "12345";
        val testOrderType = "ORDER";

        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_HEADER.getProgramCallNumber(),
                        72,
                        true)
        ).thenThrow(Exception.class);

        val responseStatus = assertThrows(
                MincronException.class,
                () -> orderService.getOrderHeader(testOrderNumber, testOrderType)
        )
                .getHttpStatus();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void getOrderItemList_success() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(ORDER_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        when(responseBuilderConfig.hasMoreData()).thenReturn(true, false);

        when(responseBuilderConfig.getResultString()).thenReturn(ORDER_NUMBER_1,
                DISPLAY_ONLY_N,
                PRODUCT_NUMBER_1,
                PRODUCT_DESCRIPTION_1,
                "",
                UOM_1,
                LINE_NUMBER_1,
                QUANTITY_ORDERED_1,
                UNIT_PRICE_1,
                EXTENDED_PRICE_1,
                QUANTITY_BACK_ORDERED_1,
                QUANTITY_SHIPPED_1,
                TYPE_CODE_1);

        List<ProductLineItemDTO> productLineItemDTOS = orderService.getOrderItemList(ERP_ACCOUNT_ID,
                ORDER_TYPE,
                ORDER_NUMBER,
                "Y",
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS);

        verify(callBuilderConfig, times(1)).setInputString(ERP_ACCOUNT_ID);
        verify(callBuilderConfig, times(1)).setInputString(ORDER_TYPE);
        verify(callBuilderConfig, times(1)).setInputString("Y");
        verify(callBuilderConfig, times(1)).setInputInt(START_ROW);
        verify(callBuilderConfig, times(1)).setInputInt(MAX_ROWS);

        verify(responseBuilderConfig, times(2)).hasMoreData();
        verify(responseBuilderConfig, times(14)).getResultString();

        assertEquals(1, productLineItemDTOS.size());

        ProductLineItemDTO productLineItemDTO = productLineItemDTOS.get(0);

        assertEquals(ORDER_NUMBER_1, productLineItemDTO.getOrderNumber());
        assertEquals(DISPLAY_ONLY_N, productLineItemDTO.getDisplayOnly());
        assertEquals(PRODUCT_NUMBER_1, productLineItemDTO.getProductNumber());
        assertEquals(PRODUCT_DESCRIPTION_1, productLineItemDTO.getDescription());
        assertEquals(UOM_1, productLineItemDTO.getUom());
        assertEquals(LINE_NUMBER_1, productLineItemDTO.getLineNumber());
        assertEquals(QUANTITY_ORDERED_1, productLineItemDTO.getQuantityOrdered());
        assertEquals(UNIT_PRICE_1, productLineItemDTO.getUnitPrice());
        assertEquals(EXTENDED_PRICE_1, productLineItemDTO.getExtendedPrice());
        assertEquals(QUANTITY_BACK_ORDERED_1, productLineItemDTO.getQuantityBackOrdered());
        assertEquals(QUANTITY_SHIPPED_1, productLineItemDTO.getQuantityShipped());
        assertEquals(TYPE_CODE_1, productLineItemDTO.getOrderLineItemTypeCode());
    }

    @Test
    void getOrderItemList_sqlException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenThrow(new SQLException());
        when(callBuilderConfig.getResultSet(ORDER_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        orderService.getOrderItemList(ERP_ACCOUNT_ID,
                                ORDER_TYPE,
                                ORDER_NUMBER,
                                "Y",
                                DEFAULT_START_ROW,
                                DEFAULT_MAX_ROWS)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    void getOrderItemList_genericMincronException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenThrow(new Exception());
        when(callBuilderConfig.getResultSet(ORDER_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        orderService.getOrderItemList(ERP_ACCOUNT_ID,
                                ORDER_TYPE,
                                ORDER_NUMBER,
                                "Y",
                                DEFAULT_START_ROW,
                                DEFAULT_MAX_ROWS)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    private String buildOrderHeaderHeaderRequestUrl(String orderType,
                                                    String orderNumber) {
        return UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/orderHeader")
                .query("orderType={orderType}")
                .query("orderNumber={orderNumber}")
                .buildAndExpand(orderType, orderNumber)
                .toUriString();
    }
}
