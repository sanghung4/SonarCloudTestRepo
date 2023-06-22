package com.reece.platform.mincron.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.model.contracts.ContractDTO;
import com.reece.platform.mincron.model.BranchDTO;
import com.reece.platform.mincron.model.common.CartLineItemDTO;
import com.reece.platform.mincron.model.contracts.*;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ContractServiceTest {

    private ContractService contractService;
    private ManagedCallFactory managedCallFactory;
    private CallBuilderConfig callBuilderConfig;
    private ResponseBuilderConfig responseBuilderConfig;
    private CallableStatement callableStatement;
    private RestTemplate restTemplate;

    private static final String ERP_ACCOUNT_ID = "123456";
    public static final int START_ROW = 1;
    public static final int MAX_ROWS = 10;
    public static final String SORT_ORDER_LAST_RELEASE = "lastRelease";
    public static final String SORT_DIRECTION_DESC = "DESC";
    public static final String MINCRON_WEBSMART_HOST_URL = "http://localhost:8100/WebSmart-apiTest";
    private static final int CONTRACT_CALLS_STARTING_ROW = 2;
    private static final String DISPLAY_ONLY = "N";
    private static final String PRODUCT_NUMBER = "8T50";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final String UNIT_PRICE = "17.60";
    private static final String NET_PRICE = UNIT_PRICE;
    private static final String UOM = "FT";
    private static final String QUANTITY_ORDERED = "140";
    private static final String QUANTITY_RELEASED = QUANTITY_ORDERED;
    private static final String QUANTITY_SHIPPED = QUANTITY_ORDERED;
    private static final String EXTENDED_PRICE = "600.00";
    private static final String ORDER_LINE_ITEM_CODE = "S";
    private static final String LINE_NUMBER = "70";
    private static final String SEQUENCE_NUMBER = "7";
    private static final String CONTRACT_NUMBER = "contract number";
    private static final String MINCRON_DATE = "10212018";
    private static final String MINCRON_DATE_ZEROS = "00000000";
    private static final LocalDate MINCRON_DATE_LOCAL_DATE = LocalDate.of(2018,10,21);
    private static final String BRANCH_NUMBER = "32";
    private static final String STREET = "STREET";
    private static final String JOB_NUMBER = "job number";
    private static final String JOB_NAME = "job name";
    private static final String PO_NUMBER = "po number";
    private static final String CONTRACT_DESCRIPTION = "contractDescription";
    private  HttpEntity<HttpHeaders> request;
    private HttpHeaders httpHeaders;
    private SubmitOrderReviewRequestDTO orderRequest;
    private CreateCartRequestDTO createCartRequest;
    private ProductDetailRequestDTO productDetailRequest;

    @BeforeEach
    public void setup() throws Exception {
        restTemplate = mock(RestTemplate.class);
        contractService = mock(ContractService.class);
        managedCallFactory = mock(ManagedCallFactory.class);
        callBuilderConfig = mock(CallBuilderConfig.class);
        responseBuilderConfig = mock(com.reece.platform.mincron.callBuilder.ResponseBuilderConfig.class);
        callableStatement = mock(CallableStatement.class);
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        request = new HttpEntity<>(httpHeaders);

        orderRequest = new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO cartRequestDTO= new CreateCartRequestDTO();
        cartRequestDTO.setRePrice("Y");
        cartRequestDTO.setShoppingCartId("00000000000000001341");
        cartRequestDTO.setApplication("B2B");
        orderRequest.setCreateCartRequest(cartRequestDTO);
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
        orderRequest.setAddItemsToCart(items);

        createCartRequest= new CreateCartRequestDTO();
        createCartRequest.setApplication("B2B");
        createCartRequest.setAccountId("200010");
        createCartRequest.setJobNumber("SHOP");
        createCartRequest.setContractNumber("2805599");
        createCartRequest.setUserId("JHILLIN");
        createCartRequest.setShoppingCartId("00000000000000001341");
        createCartRequest.setJobName("Meadows@CC");
        createCartRequest.setBranchNumber("032");
        ShipmentDetailDTO ship= new ShipmentDetailDTO();
        ship.setShippingZip("29715");
        createCartRequest.setShipmentDetail(ship);

        productDetailRequest= new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");

        ItemWrapperDTO itemWrapper = new ItemWrapperDTO();
        List<LineItemRequest> itemRequestList = new ArrayList<>();
        LineItemRequest lineItemRequest = new LineItemRequest();
        lineItemRequest.setContractNumber("2805599");
        lineItemRequest.setProductNumber("8M2");
        lineItemRequest.setBranch(branch);
        itemRequestList.add(lineItemRequest);
        itemWrapper.setItems(itemRequestList);
        productDetailRequest.setItemDTO(itemWrapper);
        var restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        contractService = new ContractService(managedCallFactory, restTemplateBuilder);

        ReflectionTestUtils.setField(contractService, "mincronHostUrl", MINCRON_WEBSMART_HOST_URL);

        when(
            managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_LIST.getProgramCallNumber(), 18, true)
        )
            .thenReturn(callBuilderConfig);
    }

    @Test
    void getContractList_success() throws Exception {
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(true, false);
        when(responseBuilderConfig.getResultString()).thenReturn(CONTRACT_NUMBER, CONTRACT_DESCRIPTION, JOB_NUMBER, JOB_NAME, PO_NUMBER);
        when(responseBuilderConfig.getMincronDate()).thenReturn(MINCRON_DATE, MINCRON_DATE, MINCRON_DATE_ZEROS, MINCRON_DATE_ZEROS);

        PageDTO<ContractListDTO> contractListDTOPageDTO = contractService.getContractList(ERP_ACCOUNT_ID, START_ROW, MAX_ROWS, "", "", "", "lastRelease", "DESC");

        ContractListDTO contractListDTO = contractListDTOPageDTO.getResults().get(0);
        assertNull(contractListDTO.getFirstReleaseDate());
        assertNull(contractListDTO.getLastReleaseDate());

        verify(callBuilderConfig, times(1)).setInputInt(START_ROW);
        verify(callBuilderConfig, times(1)).setInputInt(MAX_ROWS);
        verify(callBuilderConfig, times(1)).setInputString(ERP_ACCOUNT_ID);
        verify(callBuilderConfig, times(3)).setInputString("");
        verify(callBuilderConfig, times(1)).setInputString("lastRelease");
        verify(callBuilderConfig, times(1)).setInputString("DESC");
    }

    @Test
    void getContractList_sqlException() throws Exception {
        when(
            managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_LIST.getProgramCallNumber(), 18, true)
        )
            .thenThrow(new SQLException());

        HttpStatus responseStatus = assertThrows(
            MincronException.class,
            () ->
                contractService.getContractList(
                    ERP_ACCOUNT_ID,
                    START_ROW,
                    MAX_ROWS,
                    "",
                    "",
                    "",
                    SORT_ORDER_LAST_RELEASE,
                    SORT_DIRECTION_DESC
                )
        )
            .getHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    void getContractList_genericMincronException() throws Exception {
        when(
            managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_LIST.getProgramCallNumber(), 18, true)
        )
            .thenThrow(new Exception());

        HttpStatus responseStatus = assertThrows(
            MincronException.class,
            () ->
                contractService.getContractList(
                    ERP_ACCOUNT_ID,
                    START_ROW,
                    MAX_ROWS,
                    "",
                    "",
                    "",
                    SORT_ORDER_LAST_RELEASE,
                    SORT_DIRECTION_DESC
                )
        )
            .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void getContractItemList_success() throws Exception {
        when(callBuilderConfig.getResultSet(1)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(true, true, false);

        when(responseBuilderConfig.getResultString()).thenReturn(
                "",
                DISPLAY_ONLY,
                PRODUCT_NUMBER,
                PRODUCT_DESCRIPTION,
                UNIT_PRICE,
                UOM,
                QUANTITY_ORDERED,
                QUANTITY_RELEASED,
                EXTENDED_PRICE,
                NET_PRICE,
                ORDER_LINE_ITEM_CODE,
                LINE_NUMBER,
                SEQUENCE_NUMBER,
                QUANTITY_SHIPPED);
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        List<ProductLineItemDTO> productLineItemDTOList = contractService.getContractItemList(ERP_ACCOUNT_ID, CONTRACT_NUMBER, "", START_ROW, 10000);

        verify(callBuilderConfig, times(1)).setInputInt(START_ROW);
        verify(callBuilderConfig, times(1)).setInputInt(10000);
        verify(callBuilderConfig, times(1)).setInputString(ERP_ACCOUNT_ID);
        verify(callBuilderConfig, times(1)).setInputString("");
        verify(callBuilderConfig, times(1)).setInputString(CONTRACT_NUMBER);

        ProductLineItemDTO productLineItemDTO = productLineItemDTOList.get(0);

        assertEquals(DISPLAY_ONLY, productLineItemDTO.getDisplayOnly());
        assertEquals(PRODUCT_NUMBER, productLineItemDTO.getProductNumber());
        assertEquals(PRODUCT_DESCRIPTION, productLineItemDTO.getDescription());
        assertEquals(UNIT_PRICE, productLineItemDTO.getUnitPrice());
        assertEquals(UOM, productLineItemDTO.getUom());
        assertEquals(QUANTITY_ORDERED, productLineItemDTO.getQuantityOrdered());
        assertEquals(QUANTITY_RELEASED, productLineItemDTO.getQuantityReleasedToDate());
        assertEquals(EXTENDED_PRICE, productLineItemDTO.getExtendedPrice());
        assertEquals(NET_PRICE, productLineItemDTO.getNetPrice());
        assertEquals(ORDER_LINE_ITEM_CODE, productLineItemDTO.getOrderLineItemTypeCode());
        assertEquals(LINE_NUMBER, productLineItemDTO.getLineNumber());
        assertEquals(SEQUENCE_NUMBER, productLineItemDTO.getSequenceNumber());
        assertEquals(QUANTITY_SHIPPED, productLineItemDTO.getQuantityShipped());


    }

    @Test
    void getContractItemList_genericMincronException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenThrow(new Exception());
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.getContractItemList(ERP_ACCOUNT_ID, CONTRACT_NUMBER, "", START_ROW, 10000)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void getContractItemList_sqlException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenThrow(new SQLException());
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.getContractItemList(ERP_ACCOUNT_ID, CONTRACT_NUMBER, "", START_ROW, 10000)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    @Test
    void getContractHeader_dateShouldConvertToLocalDateFormat() throws Exception {
        ContractDTO response = contractHeaderSetup();
        assertEquals(MINCRON_DATE_LOCAL_DATE, response.getContractDate(),
                "Expected date string to be converted to local date");
        assertEquals(MINCRON_DATE_LOCAL_DATE, response.getFirstReleaseDate(),
                "Expected date string to be converted to local date");
        assertEquals(MINCRON_DATE_LOCAL_DATE, response.getLastReleaseDate(),
                "Expected date string to be converted to local date");
        assertEquals(MINCRON_DATE_LOCAL_DATE, response.getFirstShipmentDate(),
                "Expected date string to be converted to local date");
        assertEquals(MINCRON_DATE_LOCAL_DATE, response.getLastShipmentDate(),
                "Expected date string to be converted to local date");
        assertEquals(BRANCH_NUMBER, response.getBranch().getBranchNumber(), "Expected branch number to be set on branch");
    }

    @Test
    void getContractHeader_dateShouldBeNullWhenZeros() throws Exception {
        ContractDTO response = contractHeaderSetup();
        assertEquals(null, response.getPromisedDate(), "Expected date to be null");
    }

    @Test
    void getContractHeader_success() throws Exception {
        ContractDTO response = contractHeaderSetup();

        verify(callBuilderConfig, times(27)).setOutputChar();
        verify(callBuilderConfig, times(6)).setOutputNumeric(2);
        verify(callBuilderConfig, times(1)).setInputString(CONTRACT_NUMBER);

        assertEquals(JOB_NUMBER, response.getJobNumber(), "Expected contract header to have job number");
        assertEquals(JOB_NAME, response.getJobName(), "Expected contract header to have job name");
        assertEquals(PO_NUMBER, response.getPurchaseOrderNumber(), "Expected contract header to have PO number");
        assertEquals(CONTRACT_DESCRIPTION, response.getContractDescription(), "Expected contract header to have contract description");
        assertEquals(BRANCH_NUMBER, response.getBranch().getBranchNumber(), "Expected branch number to be set on branch");
    }

    @Test
    void getContractHeader_addressesShouldBeSet() throws Exception {
        ContractDTO response = contractHeaderSetup();

        assertEquals(STREET, response.getBranch().getAddressDTO().getAddress1(),
                "Expected address to be set on branch");
        assertEquals(STREET, response.getShipToAddress().getAddress1(),
                "Expected ship address to be set");
    }

    @Test
    void getContractHeader_genericMincronException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_HEADER.getProgramCallNumber(),
                        41,
                        true)
        )
                .thenThrow(new Exception());
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.getContractHeader(CONTRACT_NUMBER)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    void deleteContractCart_Success(){
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", "200010")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("branchNumber", "032")
                .queryParam("shoppingCartId","00000000000000001341")
                .encode()
                .toUriString();
        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("00000000000000001341");
        when(restTemplate
                .exchange(eq(updateCartUrlTemplate), eq(HttpMethod.DELETE), eq(request), eq(ContractCreateCartReturnTableDTO.class))).thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp= contractService.deleteCartItems("B2B","200010","JHILLIN","00000000000000001341","032");
        assertEquals(dto.getReturnTable(), resp.getReturnTable());

    }

    @Test
    void deleteContractCart_Exception(){
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", "200010")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("branchNumber", "032")
                .queryParam("shoppingCartId","00000000000000001341")
                .encode()
                .toUriString();
        when(restTemplate
                .exchange(eq(updateCartUrlTemplate), eq(HttpMethod.DELETE), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenThrow(new MincronException("Delete cart failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.deleteCartItems("B2B","200010","JHILLIN","00000000000000001341","032")
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void getContractHeader_sqlException() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_HEADER.getProgramCallNumber(),
                        41,
                        true)
        )
                .thenThrow(new SQLException());
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.getContractHeader(CONTRACT_NUMBER)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
    }

    ContractDTO contractHeaderSetup() throws Exception {
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_HEADER.getProgramCallNumber(),
                        41,
                        true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getCs()).thenReturn(callableStatement);

        when(callableStatement.getString(9)).thenReturn(MINCRON_DATE_ZEROS);
        when(callableStatement.getString(10)).thenReturn(MINCRON_DATE);
        when(callableStatement.getString(12)).thenReturn(BRANCH_NUMBER);
        when(callableStatement.getString(14)).thenReturn(STREET);
        when(callableStatement.getString(20)).thenReturn(JOB_NUMBER);
        when(callableStatement.getString(21)).thenReturn(JOB_NAME);
        when(callableStatement.getString(22)).thenReturn(PO_NUMBER);
        when(callableStatement.getString(23)).thenReturn(MINCRON_DATE);
        when(callableStatement.getString(24)).thenReturn(MINCRON_DATE);
        when(callableStatement.getString(25)).thenReturn(MINCRON_DATE);
        when(callableStatement.getString(26)).thenReturn(MINCRON_DATE);
        when(callableStatement.getString(27)).thenReturn(STREET);
        when(callableStatement.getString(40)).thenReturn(CONTRACT_DESCRIPTION);

        return contractService.getContractHeader(CONTRACT_NUMBER);
    }

    @Test
    void submitContractToReleaseOrder_Success(){
        SubmitOrderRequestDTO submitOrderRequestInput = new SubmitOrderRequestDTO();
        submitOrderRequestInput.setJobNumber("shop");
        submitOrderRequestInput.setPromiseDate("08312022");

        SubmitOrderRequestDTO submitOrderRequestOutput = new SubmitOrderRequestDTO();
        submitOrderRequestOutput.setJobNumber("shop");
        submitOrderRequestOutput.setPromiseDate("20220831");

        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(submitOrderRequestOutput).length());
        HttpEntity<String> strrequest = new HttpEntity<>(new Gson().toJson(submitOrderRequestOutput),httpHeaders);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrder")
                .queryParam("application", "B2B")
                .queryParam("shoppingCartId", "00000000000000001341")
                .queryParam("userId", "JHILLIN")
                .queryParam("accountId", "200010")
                .encode()
                .toUriString();
        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("54532311");
        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), eq(strrequest), eq(ContractCreateCartReturnTableDTO.class))).thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp= contractService.submitContractToReleaseOrder(submitOrderRequestInput,"B2B","200010","JHILLIN","00000000000000001341");
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void submitContractToReleaseOrder_badPromiseDate(){
        SubmitOrderRequestDTO submitOrderRequestInput = new SubmitOrderRequestDTO();
        submitOrderRequestInput.setJobNumber("shop");
        submitOrderRequestInput.setPromiseDate("55");

        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(submitOrderRequestInput).length());
        HttpEntity<String> strrequest = new HttpEntity<>(new Gson().toJson(submitOrderRequestInput),httpHeaders);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrder")
                .queryParam("application", "B2B")
                .queryParam("shoppingCartId", "00000000000000001341")
                .queryParam("userId", "JHILLIN")
                .queryParam("accountId", "200010")
                .encode()
                .toUriString();
        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("54532311");
        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), eq(strrequest), eq(ContractCreateCartReturnTableDTO.class))).thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp= contractService.submitContractToReleaseOrder(submitOrderRequestInput,"B2B","200010","JHILLIN","00000000000000001341");
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void submitContractToReleaseOrder_Exception(){
        SubmitOrderRequestDTO submitOrderRequest= new SubmitOrderRequestDTO();
        submitOrderRequest.setJobNumber("shop");
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(submitOrderRequest).length());
        HttpEntity<String> strrequest = new HttpEntity<>(new Gson().toJson(submitOrderRequest),httpHeaders);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrder")
                .queryParam("application", "B2B")
                .queryParam("shoppingCartId", "00000000000000001341")
                .queryParam("userId", "JHILLIN")
                .queryParam("accountId", "200010")
                .encode()
                .toUriString();
        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), eq(strrequest), eq(ContractCreateCartReturnTableDTO.class)))
                .thenThrow(new MincronException("Order submission failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.submitContractToReleaseOrder(submitOrderRequest,"B2B","200010","JHILLIN","00000000000000001341")
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void createShoppingCart_Success(){
        String createCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", "200010")
                .queryParam("contractNumber", "2805599")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("jobNumber", "SHOP")
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("00000000000000001341");

        when(restTemplate
                .exchange(eq(createCartUrlTemplate), eq(HttpMethod.PUT), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp=  contractService.createShoppingCart(createCartRequest);

        assertEquals(dto.getReturnTable(), resp.getReturnTable());

    }

    @Test
    void createShoppingCart_Exception(){
        String createCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", "200010")
                .queryParam("contractNumber", "2805599")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("jobNumber", "SHOP")
                .encode()
                .toUriString();

        when(restTemplate
                .exchange(eq(createCartUrlTemplate), eq(HttpMethod.PUT), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenThrow(new MincronException("Cart creation failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.createShoppingCart(createCartRequest)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void updateShoppingCart_Success(){
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", createCartRequest.getAccountId())
                .queryParam("contractNumber", createCartRequest.getContractNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("userId", createCartRequest.getUserId())
                .queryParam("jobName", createCartRequest.getJobName())
                .queryParam("jobNumber", createCartRequest.getJobNumber())
                .queryParam("shoppingCartId",createCartRequest.getShoppingCartId())
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("Yes");

        when(restTemplate
                .exchange(eq(updateCartUrlTemplate), eq(HttpMethod.POST), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp=  contractService.updateShoppingCart(createCartRequest);

        assertEquals(dto.getReturnTable(), resp.getReturnTable());

    }

    @Test
    void updateShoppingCart_Exception(){
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", createCartRequest.getAccountId())
                .queryParam("contractNumber", createCartRequest.getContractNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("userId", createCartRequest.getUserId())
                .queryParam("jobName", createCartRequest.getJobName())
                .queryParam("jobNumber", createCartRequest.getJobNumber())
                .queryParam("shoppingCartId",createCartRequest.getShoppingCartId())
                .encode()
                .toUriString();
        when(restTemplate
                .exchange(eq(updateCartUrlTemplate), eq(HttpMethod.POST), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenThrow(new MincronException("Update shopping cart failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.updateShoppingCart(createCartRequest)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void AddItemsToCart_Success(){
        HttpHeaders httpHeaders_cart = new HttpHeaders();
        httpHeaders_cart.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders_cart.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders_cart.setContentLength(new Gson().toJson(orderRequest.getAddItemsToCart()).length());

        HttpEntity<String> request_cart = new HttpEntity<>(new Gson().toJson(orderRequest.getAddItemsToCart()),httpHeaders_cart);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartItem")
                .queryParam("application", orderRequest.getCreateCartRequest().getApplication())
                .queryParam("shoppingCartId", orderRequest.getCreateCartRequest().getShoppingCartId())
                .queryParam("rePrice", orderRequest.getCreateCartRequest().getRePrice())
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO dto= new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("Yes");

        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), eq(request_cart), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp=  contractService.addItemsToCart(orderRequest);

        assertEquals(dto.getReturnTable(), resp.getReturnTable());

    }

    @Test
    void AddItemsToCart_Exception(){
        HttpHeaders httpHeaders_cart = new HttpHeaders();
        httpHeaders_cart.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders_cart.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders_cart.setContentLength(new Gson().toJson(orderRequest.getAddItemsToCart()).length());

        HttpEntity<String> request_cart = new HttpEntity<>(new Gson().toJson(orderRequest.getAddItemsToCart()),httpHeaders_cart);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartItem")
                .queryParam("application", orderRequest.getCreateCartRequest().getApplication())
                .queryParam("shoppingCartId", orderRequest.getCreateCartRequest().getShoppingCartId())
                .queryParam("rePrice", orderRequest.getCreateCartRequest().getRePrice())
                .encode()
                .toUriString();
        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), eq(request_cart), eq(ContractCreateCartReturnTableDTO.class)))
                .thenThrow(new MincronException("Items not added to cart",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.addItemsToCart(orderRequest)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void submitOrderForReview_Success(){
        String orderReviewUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrderReview")
                .queryParam("shoppingCartId", createCartRequest.getShoppingCartId())
                .queryParam("branchNumber", createCartRequest.getBranchNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("shippingAddress1", createCartRequest.getShipmentDetail().getShippingAddress1())
                .queryParam("shippingAddress2", createCartRequest.getShipmentDetail().getShippingAddress2())
                .queryParam("shippingAddress3", createCartRequest.getShipmentDetail().getShippingAddress3())
                .queryParam("shippingCity",createCartRequest.getShipmentDetail().getShippingCity())
                .queryParam("shippingState",createCartRequest.getShipmentDetail().getShippingState())
                .queryParam("shippingZip",createCartRequest.getShipmentDetail().getShippingZip())
                .queryParam("shippingCountry",createCartRequest.getShipmentDetail().getShippingCountry())
                .queryParam("shippingTaxJurisdiction",createCartRequest.getShipmentDetail().getShippingTaxJurisdiction())
                .queryParam("shipMethod",createCartRequest.getShipmentDetail().getShipMethod())
                .encode()
                .toUriString();

        SubmitOrderReviewResponseDTO dto = new SubmitOrderReviewResponseDTO();
        SubmitOrderWrapperDTO wrapper=new SubmitOrderWrapperDTO();
        wrapper.setContractNumber("3000000");
        dto.setReturnTable(wrapper);

        when(restTemplate
                .exchange(eq(orderReviewUrlTemplate), eq(HttpMethod.GET), eq(request), eq(SubmitOrderReviewResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        SubmitOrderReviewResponseDTO resp=  contractService.submitOrderForReview(createCartRequest);

        assertEquals(dto.getReturnTable().getContractNumber(), resp.getReturnTable().getContractNumber());
    }

    @Test
    void submitOrderForReview_Exception(){
        String orderReviewUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrderReview")
                .queryParam("shoppingCartId", createCartRequest.getShoppingCartId())
                .queryParam("branchNumber", createCartRequest.getBranchNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("shippingAddress1", createCartRequest.getShipmentDetail().getShippingAddress1())
                .queryParam("shippingAddress2", createCartRequest.getShipmentDetail().getShippingAddress2())
                .queryParam("shippingAddress3", createCartRequest.getShipmentDetail().getShippingAddress3())
                .queryParam("shippingCity",createCartRequest.getShipmentDetail().getShippingCity())
                .queryParam("shippingState",createCartRequest.getShipmentDetail().getShippingState())
                .queryParam("shippingZip",createCartRequest.getShipmentDetail().getShippingZip())
                .queryParam("shippingCountry",createCartRequest.getShipmentDetail().getShippingCountry())
                .queryParam("shippingTaxJurisdiction",createCartRequest.getShipmentDetail().getShippingTaxJurisdiction())
                .queryParam("shipMethod",createCartRequest.getShipmentDetail().getShipMethod())
                .encode()
                .toUriString();

        when(restTemplate
                .exchange(eq(orderReviewUrlTemplate), eq(HttpMethod.GET), eq(request), eq(SubmitOrderReviewResponseDTO.class)))
                .thenThrow(new MincronException("Review order failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.submitOrderForReview(createCartRequest)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

    @Test
    void orderReview_Success() throws Exception {
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", createCartRequest.getAccountId())
                .queryParam("contractNumber", createCartRequest.getContractNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("userId", createCartRequest.getUserId())
                .queryParam("jobName", createCartRequest.getJobName())
                .queryParam("jobNumber", createCartRequest.getJobNumber())
                .queryParam("shoppingCartId",createCartRequest.getShoppingCartId())
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO updateCartDto = new ContractCreateCartReturnTableDTO();
        updateCartDto.setReturnTable("Yes");

        when(restTemplate
                .exchange(eq(updateCartUrlTemplate), eq(HttpMethod.POST), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(updateCartDto, HttpStatus.OK));

        HttpHeaders httpHeaders_cart = new HttpHeaders();
        httpHeaders_cart.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders_cart.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders_cart.setContentLength(new Gson().toJson(orderRequest.getAddItemsToCart()).length());

        CartLineItemDTO lineItem = orderRequest.getAddItemsToCart().getItems().get(0);
        lineItem.setLineNumber(Integer.valueOf(SEQUENCE_NUMBER));

        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartItem")
                .queryParam("application", orderRequest.getCreateCartRequest().getApplication())
                .queryParam("shoppingCartId", orderRequest.getCreateCartRequest().getShoppingCartId())
                .queryParam("rePrice", orderRequest.getCreateCartRequest().getRePrice())
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO createCartItemDto = new ContractCreateCartReturnTableDTO();
        createCartItemDto.setReturnTable("Yes");

        when(restTemplate
                .exchange(eq(addItemToCartUrl), eq(HttpMethod.PUT), any(), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(createCartItemDto, HttpStatus.OK));

        String createCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCart")
                .queryParam("accountId", "200010")
                .queryParam("contractNumber", "2805599")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("jobNumber", "SHOP")
                .encode()
                .toUriString();

        ContractCreateCartReturnTableDTO createCartDto = new ContractCreateCartReturnTableDTO();
        createCartDto.setReturnTable("00000000000000001341");

        when(restTemplate
                .exchange(eq(createCartUrlTemplate), eq(HttpMethod.PUT), eq(request), eq(ContractCreateCartReturnTableDTO.class)))
                .thenReturn(new ResponseEntity<>(createCartDto, HttpStatus.OK));

        when(callBuilderConfig.getResultSet(1)).thenReturn(responseBuilderConfig);
        when(responseBuilderConfig.hasMoreData()).thenReturn(true, true, false);

        when(responseBuilderConfig.getResultString()).thenReturn(
                "",
                DISPLAY_ONLY,
                "112VBR",
                PRODUCT_DESCRIPTION,
                UNIT_PRICE,
                UOM,
                QUANTITY_ORDERED,
                QUANTITY_RELEASED,
                EXTENDED_PRICE,
                NET_PRICE,
                ORDER_LINE_ITEM_CODE,
                LINE_NUMBER,
                SEQUENCE_NUMBER,
                QUANTITY_SHIPPED);
        when(
                managedCallFactory.makeManagedCall(ProgramCallNumberEnum.GET_CONTRACT_ITEM_LIST.getProgramCallNumber(), 14, true)
        )
                .thenReturn(callBuilderConfig);
        when(callBuilderConfig.getResultSet(CONTRACT_CALLS_STARTING_ROW)).thenReturn(responseBuilderConfig);

        String orderReviewUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/shoppingCartOrderReview")
                .queryParam("shoppingCartId", createCartRequest.getShoppingCartId())
                .queryParam("branchNumber", createCartRequest.getBranchNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("shippingAddress1", createCartRequest.getShipmentDetail().getShippingAddress1())
                .queryParam("shippingAddress2", createCartRequest.getShipmentDetail().getShippingAddress2())
                .queryParam("shippingAddress3", createCartRequest.getShipmentDetail().getShippingAddress3())
                .queryParam("shippingCity",createCartRequest.getShipmentDetail().getShippingCity())
                .queryParam("shippingState",createCartRequest.getShipmentDetail().getShippingState())
                .queryParam("shippingZip",createCartRequest.getShipmentDetail().getShippingZip())
                .queryParam("shippingCountry",createCartRequest.getShipmentDetail().getShippingCountry())
                .queryParam("shippingTaxJurisdiction",createCartRequest.getShipmentDetail().getShippingTaxJurisdiction())
                .queryParam("shipMethod",createCartRequest.getShipmentDetail().getShipMethod())
                .encode()
                .toUriString();

        SubmitOrderReviewResponseDTO dto = new SubmitOrderReviewResponseDTO();
        SubmitOrderWrapperDTO wrapper=new SubmitOrderWrapperDTO();
        wrapper.setContractNumber("3000000");
        dto.setReturnTable(wrapper);

        List<CartLineItemDTO> cartLineItemDTOS = new ArrayList<>();
        CartLineItemDTO cartLineItemDTO = new CartLineItemDTO();
        cartLineItemDTO.setProductNumber("112VBR");
        cartLineItemDTOS.add(cartLineItemDTO);
        ContractAddItemToCartRequestDTO addItemToCartRequestDTO = new ContractAddItemToCartRequestDTO();
        addItemToCartRequestDTO.setItems(cartLineItemDTOS);
        SubmitOrderReviewRequestDTO mockedOrderRequest = new SubmitOrderReviewRequestDTO();
        createCartRequest.setRePrice("Y");
        mockedOrderRequest.setCreateCartRequest(createCartRequest);
        mockedOrderRequest.setAddItemsToCart(orderRequest.getAddItemsToCart());

        when(restTemplate
                .exchange(eq(orderReviewUrlTemplate), eq(HttpMethod.GET), eq(request), eq(SubmitOrderReviewResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        SubmitOrderReviewResponseDTO resp =  contractService.orderReview(mockedOrderRequest);

        assertEquals(dto.getReturnTable().getShoppingCartId(), resp.getReturnTable().getShoppingCartId());
    }

    @Test
    void getProductDetails_Success(){
        HttpHeaders httpHeaders_item = new HttpHeaders();
        httpHeaders_item.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders_item.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders_item.setContentLength(new Gson().toJson(productDetailRequest.getItemDTO()).length());

        HttpEntity<String> request_item = new HttpEntity<>(new Gson().toJson(productDetailRequest.getItemDTO()),httpHeaders_item);
        String itemDetailUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/itemDetail")
                .queryParam("accountId", "200010")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("url", "SHOP.FORTILINE.COM")
                .build()
                .toUriString();

        ProductDetailsResponseDTO dto=new ProductDetailsResponseDTO();
        ProductDetailWrapperDTO resp = new ProductDetailWrapperDTO();
        List<LineItemResponse> respList = new ArrayList<>();
        LineItemResponse itemResp = new LineItemResponse();
        BranchDTO branch = new BranchDTO();
        branch.setBranchNumber("032");
        itemResp.setBranch(branch);
        itemResp.setQuantityAvailableHomeBranch("10");
        respList.add(itemResp);
        resp.setItems(respList);
        dto.setReturnTable(resp);

        when(restTemplate
                .exchange(eq(itemDetailUrlTemplate), eq(HttpMethod.POST), eq(request_item), eq(ProductDetailsResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ProductDetailsResponseDTO response=  contractService.getProductDetails(productDetailRequest);

        assertEquals(dto.getReturnTable(), response.getReturnTable());

    }

    @Test
    void getProductDetails_Exception(){
        HttpHeaders httpHeaders_item = new HttpHeaders();
        httpHeaders_item.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders_item.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders_item.setContentLength(new Gson().toJson(productDetailRequest.getItemDTO()).length());

        HttpEntity<String> request_item = new HttpEntity<>(new Gson().toJson(productDetailRequest.getItemDTO()),httpHeaders_item);
        String itemDetailUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(MINCRON_WEBSMART_HOST_URL)
                .path("/itemDetail")
                .queryParam("accountId", "200010")
                .queryParam("application","B2B")
                .queryParam("userId", "JHILLIN")
                .queryParam("url", "SHOP.FORTILINE.COM")
                .build()
                .toUriString();

        when(restTemplate
                .exchange(eq(itemDetailUrlTemplate), eq(HttpMethod.POST), eq(request_item), eq(ProductDetailsResponseDTO.class)))
                .thenThrow(new MincronException("Item details failed",HttpStatus.INTERNAL_SERVER_ERROR));
        HttpStatus responseStatus = assertThrows(
                MincronException.class,
                () ->
                        contractService.getProductDetails(productDetailRequest)
        )
                .getHttpStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
    }

}
