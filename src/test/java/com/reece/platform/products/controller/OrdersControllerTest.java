package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.MincronOrderStatus;
import com.reece.platform.products.orders.OrderStatusService;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.OrdersService;
import java.util.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { OrdersController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private OrderStatusService orderStatusService;

    private OrdersController ordersController;

    private static final String ECLIPSE_ERROR_MESSAGE = "Unable to retrieve information from Eclipse";
    private static final HttpStatus ECLIPSE_STATUS_CODE = HttpStatus.NOT_FOUND;
    private static final String ERP_ACCOUNT_ID = "123456";
    public static final String ERROR_MESSAGE = "Error message";
    private static final String CONTRACT_NUMBER = "123456";

    private static final String MINCRON_ERROR_MESSAGE = "Unable to retrieve information from Mincron";
    private static final HttpStatus MINCRON_STATUS_CODE = HttpStatus.NOT_FOUND;

    private static final String ECLIPSE_ERP_NAME = "eclipse";
    private static final String MINCRON_ERP_NAME = "mincron";

    @BeforeEach
    public void setup() throws Exception {
        ordersController = new OrdersController(ordersService, authorizationService, orderStatusService);
    }

    @Test
    public void getOrder_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(ordersService.getOrderById(anyString(), anyString(), anyString(), any(ErpUserInformation.class), isNull()))
            .thenReturn(getOrderResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        requestParams.add("userId", "user");
        requestParams.add("password", "password");
        requestParams.add("erpAccountId", "12345");
        requestParams.add("name", "test");
        requestParams.add("erpSystemName", "ECLIPSE");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/orders/{orderId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void getOrder_successMincron() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(
            ordersService.getOrderById(
                anyString(),
                anyString(),
                anyString(),
                any(ErpUserInformation.class),
                eq(MincronOrderStatus.SHIPPED)
            )
        )
            .thenReturn(getOrderResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        requestParams.add("userId", "");
        requestParams.add("password", "");
        requestParams.add("erpAccountId", "");
        requestParams.add("name", "");
        requestParams.add("erpSystemName", "MINCRON");
        requestParams.add("orderStatus", "shipped");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/orders/{orderId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void getOrder_failure() throws Exception {
        when(ordersService.getOrderById(anyString(), anyString(), anyString(), any(ErpUserInformation.class), isNull()))
            .thenThrow(new EclipseException(ECLIPSE_ERROR_MESSAGE, ECLIPSE_STATUS_CODE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        requestParams.add("userId", "user");
        requestParams.add("password", "password");
        requestParams.add("erpAccountId", "12345");
        requestParams.add("name", "test");
        requestParams.add("erpSystemName", "ECLIPSE");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/orders/{orderId}", "1", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), ECLIPSE_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ECLIPSE_ERROR_MESSAGE);
    }

    @Test
    public void getOrders_success_eclipse() throws Exception {
        List<GetOrderResponseDTO> response = Arrays.asList(new GetOrderResponseDTO());
        OrdersResponseDTO ordersResponseDTO = new OrdersResponseDTO();
        ordersResponseDTO.setOrders(response);
        when(
            ordersService.getOrdersByAccountId(
                anyString(),
                anyString(),
                anyString(),
                argThat(erp -> erp.equals(ECLIPSE_ERP_NAME))
            )
        )
            .thenReturn(ordersResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("erpName", ECLIPSE_ERP_NAME);
        requestParams.add("page", "1");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{erpAccountId}/orders", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), OrdersResponseDTO.class)
        );
    }

    @Test
    public void getOrders_failure_eclipse() throws Exception {
        when(
            ordersService.getOrdersByAccountId(
                anyString(),
                anyString(),
                anyString(),
                argThat(erp -> erp.equals(ECLIPSE_ERP_NAME))
            )
        )
            .thenThrow(new EclipseException(ECLIPSE_ERROR_MESSAGE, ECLIPSE_STATUS_CODE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("erpName", ECLIPSE_ERP_NAME);
        requestParams.add("page", "1");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{erpAccountId}/orders", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), ECLIPSE_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ECLIPSE_ERROR_MESSAGE);
    }

    @Test
    public void getOrders_success_mincron() throws Exception {
        List<GetOrderResponseDTO> response = Arrays.asList(new GetOrderResponseDTO());
        OrdersResponseDTO ordersResponseDTO = new OrdersResponseDTO();
        ordersResponseDTO.setOrders(response);
        when(
            ordersService.getOrdersByAccountId(
                anyString(),
                anyString(),
                anyString(),
                argThat(erp -> erp.equals(MINCRON_ERP_NAME))
            )
        )
            .thenReturn(ordersResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("erpName", MINCRON_ERP_NAME);
        requestParams.add("page", "1");
        requestParams.add("searchTerm", "search");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{erpAccountId}/orders", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), OrdersResponseDTO.class)
        );
    }

    @Test
    public void getOrders_failure_mincron() throws Exception {
        when(
            ordersService.getOrdersByAccountId(
                anyString(),
                anyString(),
                anyString(),
                argThat(erp -> erp.equals(MINCRON_ERP_NAME))
            )
        )
            .thenThrow(new MincronException(MINCRON_ERROR_MESSAGE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("erpName", MINCRON_ERP_NAME);
        requestParams.add("page", "1");
        requestParams.add("searchTerm", "search");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{erpAccountId}/orders", "1").params(requestParams))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(result.getResponse().getContentAsString(), MINCRON_ERROR_MESSAGE);
    }

    @Test
    public void getQuote_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(ordersService.getOrderById(anyString(), anyString(), anyString(), any(ErpUserInformation.class), isNull()))
            .thenReturn(getOrderResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        requestParams.add("userId", "user");
        requestParams.add("password", "password");
        requestParams.add("erpAccountId", "12345");
        requestParams.add("name", "test");
        requestParams.add("erpSystemName", "ECLIPSE");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/quotes/{quoteId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void getQuote_failure() throws Exception {
        when(ordersService.getOrderById(anyString(), anyString(), anyString(), any(ErpUserInformation.class), isNull()))
            .thenThrow(new EclipseException(ECLIPSE_ERROR_MESSAGE, ECLIPSE_STATUS_CODE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        requestParams.add("userId", "user");
        requestParams.add("password", "password");
        requestParams.add("erpAccountId", "12345");
        requestParams.add("name", "test");
        requestParams.add("erpSystemName", "ECLIPSE");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/quotes/{quoteId}", "1", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), ECLIPSE_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ECLIPSE_ERROR_MESSAGE);
    }

    @Test
    public void getQuotes_success() throws Exception {
        List<GetOrderResponseDTO> response = Arrays.asList(new GetOrderResponseDTO());
        when(ordersService.getQuotesByAccountId(anyString(), anyString(), anyString(), anyInt())).thenReturn(response);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("page", "0");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/quotes/", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO[].class)
        );
    }

    @Test
    public void getQuotes_failure() throws Exception {
        when(ordersService.getQuotesByAccountId(anyString(), anyString(), anyString(), anyInt()))
            .thenThrow(new EclipseException(ECLIPSE_ERROR_MESSAGE, ECLIPSE_STATUS_CODE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "1");
        requestParams.add("orderPostEndDate", "1");
        requestParams.add("page", "0");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/quotes/", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), ECLIPSE_STATUS_CODE.value());
        assertEquals(result.getResponse().getContentAsString(), ECLIPSE_ERROR_MESSAGE);
    }

    @Test
    public void getContracts_success() throws Exception {
        PageDTO response = new PageDTO();

        when(ordersService.getContracts(ERP_ACCOUNT_ID, "1", "", "", "", "", "")).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("pageNumber", "1");
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("toDate", "");
        requestParams.add("sortOrder", "");
        requestParams.add("sortDirection", "");

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{erpAccountId}/contracts", ERP_ACCOUNT_ID).params(requestParams))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), PageDTO.class));
    }

    @Test
    public void getContracts_failure() throws Exception {
        when(ordersService.getContracts(ERP_ACCOUNT_ID, "1", "", "", "", "", ""))
            .thenThrow(new MincronException(ERROR_MESSAGE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("pageNumber", "1");
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("toDate", "");
        requestParams.add("sortOrder", "");
        requestParams.add("sortDirection", "");

        this.mockMvc.perform(get("/accounts/{erpAccountId}/contracts", ERP_ACCOUNT_ID).params(requestParams))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
            .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
            .andReturn();
    }

    @Test
    public void getContractHeader_success() throws Exception {
        val testContractNumber = "12345";
        val testContract = new ContractHeaderDTO();
        testContract.setContractNumber(testContractNumber);
        when(ordersService.getContract(eq(ERP_ACCOUNT_ID), eq(testContractNumber)))
            .thenReturn(Optional.of(testContract));

        mockMvc
            .perform(get("/accounts/{erpAccountId}/contracts/{contractNumber}", ERP_ACCOUNT_ID, testContractNumber))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.contractNumber").value(testContractNumber));
    }

    @Test
    public void getContractHeader_notFound() throws Exception {
        val testContractNumber = "12345";
        when(ordersService.getContract(eq(ERP_ACCOUNT_ID), eq(testContractNumber))).thenReturn(Optional.empty());

        mockMvc
            .perform(get("/accounts/{erpAccountId}/contracts/{contractNumber}", ERP_ACCOUNT_ID, testContractNumber))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getContractHeader_badRequest() throws Exception {
        val testContractNumber = "12345";
        val errorMessage = "Boom!";
        when(ordersService.getContract(eq(ERP_ACCOUNT_ID), eq(testContractNumber)))
            .thenThrow(new MincronException(errorMessage));

        val response = mockMvc
            .perform(get("/accounts/{erpAccountId}/contracts/{contractNumber}", ERP_ACCOUNT_ID, testContractNumber))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertEquals(errorMessage, response.getResponse().getContentAsString());
    }

    @Test
    public void deleteCart_success() throws Exception {
        ContractCreateCartReturnTableDTO response = new ContractCreateCartReturnTableDTO();
        response.setReturnTable("00000000000000001341");
        when(ordersService.deleteCartItems("B2B", ERP_ACCOUNT_ID, "JHILLIN", "00000000000000001341", "032"))
            .thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("application", "B2B");
        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");
        requestParams.add("branchNumber", "032");

        MvcResult result =
            this.mockMvc.perform(delete("/contracts/delete-cart").params(requestParams))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(result.getResponse().getContentAsString(), response.getReturnTable());
    }

    @Test
    public void deleteCart_failed() throws Exception {
        when(ordersService.deleteCartItems("B2B", ERP_ACCOUNT_ID, "JHILLIN", "00000000000000001341", "032"))
            .thenThrow(new MincronException(ERROR_MESSAGE));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("application", "B2B");
        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");
        requestParams.add("branchNumber", "032");

        this.mockMvc.perform(delete("/contracts/delete-cart").params(requestParams))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
            .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
            .andReturn();
    }

    @Test
    public void submitOrderReview_success() throws Exception {
        SubmitOrderReviewResponseDTO response = new SubmitOrderReviewResponseDTO();
        SubmitOrderWrapperDTO dto = new SubmitOrderWrapperDTO();
        dto.setContractNumber("123456");
        response.setReturnTable(dto);

        SubmitOrderReviewRequestDTO req = new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO cartRequestDTO = new CreateCartRequestDTO();
        cartRequestDTO.setRePrice("Y");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setAccountId("123456");
        cartRequestDTO.setJobNumber("SHOP");
        cartRequestDTO.setContractNumber("123456");
        cartRequestDTO.setUserId("JHILLIN");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setJobName("Meadows@CC");
        cartRequestDTO.setBranchNumber("032");
        ShipmentDetailDTO ship = new ShipmentDetailDTO();
        ship.setShippingZip("29715");
        cartRequestDTO.setShipmentDetail(ship);
        req.setCreateCartRequest(cartRequestDTO);
        ContractAddItemToCartRequestDTO items = new ContractAddItemToCartRequestDTO();
        List<CartLineItemDTO> lineitem = new ArrayList<>();
        CartLineItemDTO product = new CartLineItemDTO();
        product.setUnitPrice("14.50");
        product.setTaxable("Y");
        product.setLineNumber(1);
        product.setProductNumber("112VBR");
        product.setQuantityOrdered("1");

        lineitem.add(product);
        items.setItems(lineitem);
        req.setAddItemsToCart(items);
        when(ordersService.orderReview(req)).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("contractNumber", CONTRACT_NUMBER);
        requestParams.add("itemNumber", "");
        requestParams.add("startRow", "");
        requestParams.add("maxRows", "");

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/contracts/orderReview")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), SubmitOrderWrapperDTO.class)
        );
    }

    @Test
    public void submitOrderReview_failed() throws Exception {
        SubmitOrderReviewRequestDTO req = new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO cartRequestDTO = new CreateCartRequestDTO();
        cartRequestDTO.setRePrice("Y");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setAccountId("123456");
        cartRequestDTO.setJobNumber("SHOP");
        cartRequestDTO.setContractNumber("123456");
        cartRequestDTO.setUserId("JHILLIN");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setJobName("Meadows@CC");
        cartRequestDTO.setBranchNumber("032");
        ShipmentDetailDTO ship = new ShipmentDetailDTO();
        ship.setShippingZip("29715");
        cartRequestDTO.setShipmentDetail(ship);
        req.setCreateCartRequest(cartRequestDTO);
        ContractAddItemToCartRequestDTO items = new ContractAddItemToCartRequestDTO();
        List<CartLineItemDTO> lineitem = new ArrayList<>();
        CartLineItemDTO product = new CartLineItemDTO();
        product.setUnitPrice("14.50");
        product.setTaxable("Y");
        product.setLineNumber(1);
        product.setProductNumber("112VBR");
        product.setQuantityOrdered("1");

        lineitem.add(product);
        items.setItems(lineitem);
        req.setAddItemsToCart(items);
        when(ordersService.orderReview(req)).thenThrow(new MincronException(ERROR_MESSAGE));

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                post("/contracts/orderReview")
                    .content(objectMapper.writeValueAsString(req))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
            .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
            .andReturn();
    }

    @Test
    public void submitContractToReleaseOrder_success() throws Exception {
        UUID shipToId = UUID.randomUUID();
        SubmitOrderRequestDTO submitOrderRequest = new SubmitOrderRequestDTO();
        submitOrderRequest.setApplication("B2B");
        submitOrderRequest.setAccountId(ERP_ACCOUNT_ID);
        submitOrderRequest.setShoppingCartId("00000000000000001341");
        submitOrderRequest.setUserId("JHILLIN");
        List<OrderLineItemResponseDTO> lineItems = new ArrayList();
        OrderLineItemResponseDTO orderLineItemResponseDTO1 = new OrderLineItemResponseDTO();
        orderLineItemResponseDTO1.setLineNumber("123");
        orderLineItemResponseDTO1.setUnitPrice(1.2f);

        OrderLineItemResponseDTO orderLineItemResponseDTO2 = new OrderLineItemResponseDTO();
        orderLineItemResponseDTO2.setLineNumber("123");
        orderLineItemResponseDTO2.setUnitPrice(1.2f);
        lineItems.add(orderLineItemResponseDTO1);
        lineItems.add(orderLineItemResponseDTO2);
        submitOrderRequest.setLineItems(lineItems);
        submitOrderRequest.setShipBranchNumber("123");
        submitOrderRequest.setShipToId(shipToId);
        submitOrderRequest.setDeliveryMethod("WILLCALL");
        submitOrderRequest.setOrderTotal("100");
        submitOrderRequest.setTaxAmount("10");
        submitOrderRequest.setSubTotal("20");

        ContractCreateCartReturnTableDTO dto = new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("success");

        when(ordersService.submitContractToReleaseOrder(any())).thenReturn(dto);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("application", "B2B");
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/contract-order/submit");
        request.params(requestParams);
        request.content(objectMapper.writeValueAsString(submitOrderRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        request.header("Authorization", "1234");
        MvcResult result = this.mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), dto.getReturnTable());
    }

    @Test
    public void submitContractToReleaseOrder_failed() throws Exception {
        SubmitOrderRequestDTO submitOrderRequest = new SubmitOrderRequestDTO();
        submitOrderRequest.setAccountId(ERP_ACCOUNT_ID);
        submitOrderRequest.setApplication("B2B");
        submitOrderRequest.setShoppingCartId("00000000000000001341");
        submitOrderRequest.setUserId("JHILLIN");

        when(ordersService.submitContractToReleaseOrder(submitOrderRequest))
            .thenThrow(new MincronException(ERROR_MESSAGE));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("application", "B2B");
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/contract-order/submit");
        request.params(requestParams);
        request.content(objectMapper.writeValueAsString(submitOrderRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void getPreviouslyPurchasedProduct_success() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setPartNumber("14333");
        product.setName("Copper Pipe");
        PreviouslyPurchasedProductDTO previouslyPurchasedProductDTO = new PreviouslyPurchasedProductDTO();
        PreviouslyPurchasedProductResponseDTO response = new PreviouslyPurchasedProductResponseDTO();
        previouslyPurchasedProductDTO.setProduct(product);
        response.setProducts(List.of(previouslyPurchasedProductDTO));

        when(ordersService.getPreviouslyPurchasedProducts(any(), any(), any())).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("currentPage", "1");
        requestParams.add("pageSize", "10");
        requestParams.add("userId", "test123");
        requestParams.add("password", "password");
        requestParams.add("erpAccountId", ERP_ACCOUNT_ID);
        requestParams.add("name", "TEST TEST");
        requestParams.add("erpSystemName", ECLIPSE_ERP_NAME);
        requestParams.add("customerNumber", "123");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/accounts/previously-purchased-products");
        request.params(requestParams);
        request.contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void getProductDetails_success() throws Exception {
        ProductDetailRequestDTO productDetailRequest = new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");

        ItemWrapperDTO itemWrapper = new ItemWrapperDTO();
        List<LineItemRequest> itemRequestList = new ArrayList<>();
        LineItemRequest lineItemRequest = new LineItemRequest();
        lineItemRequest.setContractNumber("2805599");
        lineItemRequest.setProductNumber("8M2");
        LineItemRequest.Branch branch = new LineItemRequest.Branch();
        branch.setBranchNumber("032");
        lineItemRequest.setBranch(branch);
        itemRequestList.add(lineItemRequest);
        itemWrapper.setItems(itemRequestList);
        productDetailRequest.setItemDTO(itemWrapper);

        ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();
        ProductDetailWrapperDTO resp = new ProductDetailWrapperDTO();
        List<LineItemResponse> respList = new ArrayList<>();
        LineItemResponse.Branch responseBranch = new LineItemResponse.Branch();
        responseBranch.setBranchNumber("032");
        LineItemResponse itemResp = new LineItemResponse();
        itemResp.setBranch(responseBranch);
        itemResp.setQuantityAvailableHomeBranch("10");
        respList.add(itemResp);
        resp.setItems(respList);
        dto.setReturnTable(resp);

        when(ordersService.getProductDetails(productDetailRequest)).thenReturn(dto);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/productDetail");

        request.content(objectMapper.writeValueAsString(productDetailRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), ProductDetailWrapperDTO.class)
        );
    }

    @Test
    public void getProductDetails_failed() throws Exception {
        ProductDetailRequestDTO productDetailRequest = new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");

        when(ordersService.getProductDetails(productDetailRequest)).thenThrow(new MincronException(ERROR_MESSAGE));

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/productDetail");

        request.content(objectMapper.writeValueAsString(productDetailRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
            .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
            .andReturn();
    }
}
