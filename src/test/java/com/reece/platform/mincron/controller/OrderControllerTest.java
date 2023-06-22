package com.reece.platform.mincron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.OrderHeaderDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.service.ContractService;
import com.reece.platform.mincron.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {OrderController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderController orderController;

    private static final String ERP_ACCOUNT_ID = "123456";
    private static final String ORDER_NUMBER = "123456";
    public static final String ERROR_MESSAGE = "Error message";
    public static final int DEFAULT_START_ROW = 1;
    public static final int DEFAULT_MAX_ROWS = 10;
    public static final String ORDER_TYPE = "ORDER";
    public static final String ORDER_STATUS = "ALL";

    @BeforeEach
    public void setup() throws Exception {
        orderController =  new OrderController(orderService);
    }

    @Test
    public void getOrderItemList_success() throws Exception {
        List<ProductLineItemDTO> response = new ArrayList<>();

        when(orderService.getOrderItemList(
                ERP_ACCOUNT_ID,
                ORDER_TYPE,
                ORDER_NUMBER,
                "Y",
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS)
        ).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderNumber", ORDER_NUMBER);
        requestParams.add("WCStdOrder", "Y");
        requestParams.add("startRow", String.valueOf(DEFAULT_START_ROW));
        requestParams.add("maxRows", String.valueOf(DEFAULT_MAX_ROWS));

        MvcResult result =
                this.mockMvc.perform(get("/orders/order-item-list")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ProductLineItemDTO[].class));
    }

    @Test
    public void getOrderItemList_failed() throws Exception {
        when(orderService.getOrderItemList(
                ERP_ACCOUNT_ID,
                ORDER_TYPE,
                ORDER_NUMBER,
                "Y",
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS)
        ).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderNumber", ORDER_NUMBER);
        requestParams.add("WCStdOrder", "Y");
        requestParams.add("startRow", String.valueOf(DEFAULT_START_ROW));
        requestParams.add("maxRows", String.valueOf(DEFAULT_MAX_ROWS));

        this.mockMvc.perform(get("/orders/order-item-list")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getOrders_failed() throws Exception {
        when(orderService.getOrderList(
                ERP_ACCOUNT_ID,
                ORDER_TYPE,
                ORDER_STATUS,
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS,
                "",
                "",
                "",
                "",
                ""
                )
        ).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));


        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderStatus", ORDER_STATUS);
        requestParams.add("startRow", String.valueOf(DEFAULT_START_ROW));
        requestParams.add("maxRows", String.valueOf(DEFAULT_MAX_ROWS));
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("sortOrder", "");
        requestParams.add("sortDirection", "");

        this.mockMvc.perform(get("/orders")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getOrders_success() throws Exception {
        PageDTO response = new PageDTO();

        when(orderService.getOrderList(
                        ERP_ACCOUNT_ID,
                        ORDER_TYPE,
                        ORDER_STATUS,
                        DEFAULT_START_ROW,
                        DEFAULT_MAX_ROWS,
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        ).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderStatus", ORDER_STATUS);
        requestParams.add("startRow", String.valueOf(DEFAULT_START_ROW));
        requestParams.add("maxRows", String.valueOf(DEFAULT_MAX_ROWS));
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("sortOrder", "");
        requestParams.add("sortDirection", "");

        MvcResult result =
                this.mockMvc.perform(get("/orders")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), PageDTO.class));
    }

    @Test
    public void getOrderHeader_success() throws Exception {
        OrderHeaderDTO response = new OrderHeaderDTO();

        when(orderService.getOrderHeader(
                        ORDER_TYPE,
                        ORDER_NUMBER)
        ).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderNumber", ORDER_NUMBER);

        MvcResult result =
                this.mockMvc.perform(get("/orders/order-header")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), OrderHeaderDTO.class));
    }

    @Test
    public void getOrderHeader_failed() throws Exception {
        when(orderService.getOrderHeader(
                ORDER_TYPE,
                ORDER_NUMBER)
        ).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderType", ORDER_TYPE);
        requestParams.add("orderNumber", ORDER_NUMBER);

        this.mockMvc.perform(get("/orders/order-header")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }
}
