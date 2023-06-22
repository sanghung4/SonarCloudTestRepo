package com.reece.platform.mincron.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.BranchDTO;
import com.reece.platform.mincron.model.common.CartLineItemDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.model.contracts.*;
import com.reece.platform.mincron.service.ContractService;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {ContractController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class ContractControllerTest {

    public static final int DEFAULT_START_ROW = 1;
    public static final int DEFAULT_MAX_ROWS = 10;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    private ContractController contractController;

    private static final String ERP_ACCOUNT_ID = "123456";
    private static final String CONTRACT_NUMBER = "123456";
    public static final String ERROR_MESSAGE = "Error message";

    @BeforeEach
    public void setup() throws Exception {
        contractController =  new ContractController(contractService);
    }

    @Test
    public void getContractList_success() throws Exception {
        PageDTO response = new PageDTO();

        when(contractService.getContractList(
                ERP_ACCOUNT_ID,
                1,
                10,
                "",
                "",
                "",
                "lastRelease",
                "DESC")
        ).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("startRow", "1");
        requestParams.add("maxRows", "10");
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("toDate", "");
        requestParams.add("sortOrder", "lastRelease");
        requestParams.add("sortDirection", "DESC");

        MvcResult result =
                this.mockMvc.perform(get("/contracts")
                        .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), PageDTO.class));
    }

    @Test
    public void getContractList_failed() throws Exception {

        when(contractService.getContractList(
                ERP_ACCOUNT_ID,
                1,
                10,
                "",
                "",
                "",
                "lastRelease",
                "DESC")
        ).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("startRow", "1");
        requestParams.add("maxRows", "10");
        requestParams.add("searchFilter", "");
        requestParams.add("fromDate", "");
        requestParams.add("toDate", "");
        requestParams.add("sortOrder", "lastRelease");
        requestParams.add("sortDirection", "DESC");

        this.mockMvc.perform(get("/contracts")
                .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getContractHeader_success() throws Exception {
        ContractDTO response = new ContractDTO();

        when(contractService.getContractHeader(CONTRACT_NUMBER)).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("contractNumber", CONTRACT_NUMBER);

        MvcResult result =
                this.mockMvc.perform(get("/contracts/contract-header")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ContractDTO.class));
    }

    @Test
    public void getContractHeader_failed() throws Exception {

        when(contractService.getContractHeader(CONTRACT_NUMBER))
                .thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("contractNumber", CONTRACT_NUMBER);

        this.mockMvc.perform(get("/contracts/contract-header")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getContractItemList_success() throws Exception {
        List<ProductLineItemDTO> response = new ArrayList<>();

        when(contractService.getContractItemList(
                ERP_ACCOUNT_ID,
                CONTRACT_NUMBER,
                "",
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS)).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("contractNumber", CONTRACT_NUMBER);
        requestParams.add("itemNumber", "");
        requestParams.add("startRow", "");
        requestParams.add("maxRows", "");

        MvcResult result =
                this.mockMvc.perform(get("/contracts/contract-item-list")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ProductLineItemDTO[].class));
    }

    @Test
    public void getContractItemList_failed() throws Exception {

        when(contractService.getContractItemList(
                ERP_ACCOUNT_ID,
                CONTRACT_NUMBER,
                "",
                DEFAULT_START_ROW,
                DEFAULT_MAX_ROWS))
                .thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("contractNumber", CONTRACT_NUMBER);
        requestParams.add("itemNumber", "");
        requestParams.add("startRow", "");
        requestParams.add("maxRows", "");

        this.mockMvc.perform(get("/contracts/contract-item-list")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void deleteCart_success() throws Exception {
        ContractCreateCartReturnTableDTO response = new ContractCreateCartReturnTableDTO();

        when(contractService.deleteCartItems("B2B",
                ERP_ACCOUNT_ID,
                "JHILLIN",
                "00000000000000001341",
                "032")).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("application", "B2B");
        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");
        requestParams.add("branchNumber", "032");

        MvcResult result =
                this.mockMvc.perform(delete("/contracts/orders/delete-cart")
                                .params(requestParams))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ContractCreateCartReturnTableDTO.class));
    }

    @Test
    public void deleteCart_failed() throws Exception {

        when(contractService.deleteCartItems("B2B",
                ERP_ACCOUNT_ID,
                "JHILLIN",
                "00000000000000001341",
                "032"))
                .thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("application", "B2B");
        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");
        requestParams.add("branchNumber", "032");

        this.mockMvc.perform(delete("/contracts/orders/delete-cart")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void submitOrderReview_success() throws Exception {
        SubmitOrderReviewResponseDTO response = new SubmitOrderReviewResponseDTO();

        SubmitOrderReviewRequestDTO req= new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO cartRequestDTO= new CreateCartRequestDTO();
        cartRequestDTO.setRePrice("Y");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setAccountId("200010");
        cartRequestDTO.setJobNumber("SHOP");
        cartRequestDTO.setContractNumber("2805599");
        cartRequestDTO.setUserId("JHILLIN");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setJobName("Meadows@CC");
        cartRequestDTO.setBranchNumber("032");
        ShipmentDetailDTO ship= new ShipmentDetailDTO();
        ship.setShippingZip("29715");
        cartRequestDTO.setShipmentDetail(ship);
        req.setCreateCartRequest(cartRequestDTO);
        ContractAddItemToCartRequestDTO items= new ContractAddItemToCartRequestDTO();
        List<CartLineItemDTO> lineitem= new ArrayList<>();
        CartLineItemDTO product= new CartLineItemDTO();
        product.setUnitPrice("14.50");
        product.setTaxable("Y");
        product.setLineNumber(1);
        product.setProductNumber("112VBR");
        product.setQuantityOrdered("1");
        BranchDTO branch= new BranchDTO();
        branch.setBranchNumber("032");
        product.setBranch(branch);
        lineitem.add(product);
        items.setItems(lineitem);
        req.setAddItemsToCart(items);
        when(contractService.orderReview(req)).thenReturn(response);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountId", ERP_ACCOUNT_ID);
        requestParams.add("contractNumber", CONTRACT_NUMBER);
        requestParams.add("itemNumber", "");
        requestParams.add("startRow", "");
        requestParams.add("maxRows", "");

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
                this.mockMvc.perform(post("/contracts/orders/review").content(objectMapper.writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), SubmitOrderReviewResponseDTO.class));
    }

    @Test
    public void submitOrderReview_failed() throws Exception {

        SubmitOrderReviewRequestDTO req= new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO cartRequestDTO= new CreateCartRequestDTO();
        cartRequestDTO.setRePrice("Y");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setApplication("B2B");
        cartRequestDTO.setAccountId("200010");
        cartRequestDTO.setJobNumber("SHOP");
        cartRequestDTO.setContractNumber("2805599");
        cartRequestDTO.setUserId("JHILLIN");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setJobName("Meadows@CC");
        cartRequestDTO.setBranchNumber("032");
        ShipmentDetailDTO ship= new ShipmentDetailDTO();
        ship.setShippingZip("29715");
        cartRequestDTO.setShipmentDetail(ship);
        req.setCreateCartRequest(cartRequestDTO);
        ContractAddItemToCartRequestDTO items= new ContractAddItemToCartRequestDTO();
        List<CartLineItemDTO> lineitem= new ArrayList<>();
        CartLineItemDTO product= new CartLineItemDTO();
        product.setUnitPrice("14.50");
        product.setTaxable("Y");
        product.setLineNumber(1);
        product.setProductNumber("112VBR");
        product.setQuantityOrdered("1");
        BranchDTO branch= new BranchDTO();
        branch.setBranchNumber("032");
        product.setBranch(branch);
        lineitem.add(product);
        items.setItems(lineitem);
        req.setAddItemsToCart(items);
        when(contractService.orderReview(req)).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        ObjectMapper objectMapper= new ObjectMapper();
        this.mockMvc.perform(post("/contracts/orders/review").content(objectMapper.writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void submitContractToReleaseOrder_success() throws Exception {
        SubmitOrderRequestDTO submitOrderRequest= new SubmitOrderRequestDTO();
        submitOrderRequest.setJobNumber("shop");

        ContractCreateCartReturnTableDTO dto=new ContractCreateCartReturnTableDTO();

        when(contractService.submitContractToReleaseOrder(submitOrderRequest,"B2B",ERP_ACCOUNT_ID,"JHILLIN","00000000000000001341")).thenReturn(dto);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("application", "B2B");
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/orders/submit");
        request.params(requestParams);
        request.content(objectMapper.writeValueAsString(submitOrderRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        MvcResult result =
                this.mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ContractCreateCartReturnTableDTO.class));
    }

    @Test
    public void submitContractToReleaseOrder_failed() throws Exception {

        SubmitOrderRequestDTO submitOrderRequest= new SubmitOrderRequestDTO();
        submitOrderRequest.setJobNumber("shop");

        when(contractService.submitContractToReleaseOrder(submitOrderRequest,"B2B",ERP_ACCOUNT_ID,"JHILLIN","00000000000000001341")).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("application", "B2B");
        requestParams.add("accountId", ERP_ACCOUNT_ID);

        requestParams.add("userId", "JHILLIN");
        requestParams.add("shoppingCartId", "00000000000000001341");

        ObjectMapper objectMapper= new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/orders/submit");
        request.params(requestParams);
        request.content(objectMapper.writeValueAsString(submitOrderRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void getProductDetails_success() throws Exception {
        ProductDetailRequestDTO productDetailRequest= new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");

        ItemWrapperDTO itemWrapper = new ItemWrapperDTO();
        List<LineItemRequest> itemRequestList = new ArrayList<>();
        LineItemRequest lineItemRequest = new LineItemRequest();
        lineItemRequest.setContractNumber("2805599");
        lineItemRequest.setProductNumber("8M2");
        BranchDTO branch = new BranchDTO();
        branch.setBranchNumber("032");
        lineItemRequest.setBranch(branch);
        itemRequestList.add(lineItemRequest);
        itemWrapper.setItems(itemRequestList);
        productDetailRequest.setItemDTO(itemWrapper);


        ProductDetailsResponseDTO dto=new ProductDetailsResponseDTO();
        ProductDetailWrapperDTO resp = new ProductDetailWrapperDTO();
        List<LineItemResponse> respList = new ArrayList<>();
        LineItemResponse itemResp = new LineItemResponse();
        itemResp.setBranch(branch);
        itemResp.setQuantityAvailableHomeBranch("10");
        respList.add(itemResp);
        resp.setItems(respList);
        dto.setReturnTable(resp);

        when(contractService.getProductDetails(productDetailRequest)).thenReturn(dto);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/orders/product-details");

        request.content(objectMapper.writeValueAsString(productDetailRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        MvcResult result =
                this.mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), ProductDetailsResponseDTO.class));
    }

    @Test
    public void getProductDetails_failed() throws Exception {

        ProductDetailRequestDTO productDetailRequest= new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");


        when(contractService.getProductDetails(productDetailRequest)).thenThrow(new MincronException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        ObjectMapper objectMapper= new ObjectMapper();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/contracts/orders/product-details");

        request.content(objectMapper.writeValueAsString(productDetailRequest));
        request.contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(response -> assertTrue(response.getResolvedException() instanceof MincronException))
                .andExpect(response -> assertEquals(ERROR_MESSAGE, response.getResolvedException().getMessage()))
                .andReturn();
    }
}
