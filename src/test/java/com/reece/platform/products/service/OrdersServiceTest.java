package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.branches.exception.BranchNotFoundException;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.external.appsearch.model.PageRequest;
import com.reece.platform.products.external.mincron.MincronServiceClient;
import com.reece.platform.products.external.mincron.model.Address;
import com.reece.platform.products.external.mincron.model.ContractHeader;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.ImageUrls;
import com.reece.platform.products.model.WebStatusesEnum;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.PreviouslyPurchasedEclipseProductResponse;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.PreviouslyPurchasedProduct;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.Quantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.MassProductInquiryResponse;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.AvailabilityList;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.BranchAvailability;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.ProductList;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.User;
import com.reece.platform.products.model.repository.OrderStatusDAO;
import com.reece.platform.products.model.repository.QuoteStatusDAO;
import com.reece.platform.products.search.SearchService;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

class OrdersServiceTest {

    private static final TypeReference<List<ProductLineItem>> ITEM_LIST_TYPE_REF = new TypeReference<>() {};

    public static final String ORDER_NUMBER = "1234";
    public static final String ORDER_STATUS = "Shipped";
    public static final String ERP_ACCOUNT_ID = "123123";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String STREET = "Street";
    public static final String ZIP = "ZIP";
    public static final String URL = "URL";
    public static final String PRODUCT_NAME = "Product Name";
    public static final String PART_NUMBER = "Part Number";
    public static final String MANUFACTURER_NAME = "Manufacturer Name";
    public static final String CONTRACT_NUMBER = "Contract Number";
    public static final String CONTRACT_DESCRIPTION = "Contract Description";
    public static final String JOB_NAME = "Job Name";
    public static final String JOB_NUMBER = "Job Number";
    public static final String DATE = "date";
    public static final String ORDER_TOTAL = "123.12";
    public static final int ROWS_RETURNED = 1;
    public static final int START_ROW = 1;
    public static final int TOTAL_ROWS = 10;
    public static final String MINCRON_ORDER_DATE = "11112000";
    public static final String PO_NUMBER = "po";
    private static final int CONTRACT_ITEM_COUNT = 2;
    private static final int ORDER_ITEM_COUNT = 3;
    private static final PageRequest CONTRACT_ITEMS_PAGE_SIZE = new PageRequest(CONTRACT_ITEM_COUNT, 1);
    private static final PageRequest ORDER_ITEMS_PAGE_SIZE = new PageRequest(ORDER_ITEM_COUNT, 1);
    public static final String SQL_STATE_CONNECTION_FAILURE_CODE = "08S01";
    private static final String DISPLAY_ONLY_NAME = "DISPLAY ONLY";

    private OrdersService ordersService;
    private RestTemplate restTemplate;
    private ProductService productService;
    private ErpService erpService;

    private GetOrderResponseDTO getOrderResponseDTO;
    private ProductDTO productDTO;
    private ContractDTO contractDTO;
    private PageDTO<ContractDTO> pageDTO;
    private ProductDetailRequestDTO productDetailRequest;

    private OrderStatusDAO orderStatusDAO;
    private QuoteStatusDAO quoteStatusDAO;
    private NotificationService notificationService;
    private CartService cartService;
    private AccountService accountService;
    private MincronServiceClient mincronServiceClient;
    private SearchService searchService;
    private BranchesService branchesService;

    private static final String ECLIPSE_SERVICE_URL = "http://eclipse-service";
    private static final String MINCRON_SERVICE_URL = "http://mincron-service";
    private static final String ORDER_START_DATE = "11/11/2000";
    private static final String ORDER_END_DATE = "12/11/2000";

    @Captor
    private ArgumentCaptor<SalesOrderSubmitNotificationDTO> salesOrderSubmitNotificationDTOArgumentCaptor;

    @BeforeEach
    public void setup() {
        orderStatusDAO = mock(OrderStatusDAO.class);
        quoteStatusDAO = mock(QuoteStatusDAO.class);
        restTemplate = mock(RestTemplate.class);
        productService = mock(ProductService.class);
        erpService = mock(ErpService.class);
        notificationService = mock(NotificationService.class);
        cartService = mock(CartService.class);
        accountService = mock(AccountService.class);
        mincronServiceClient = mock(MincronServiceClient.class);
        searchService = mock(SearchService.class);
        branchesService = mock(BranchesService.class);

        var restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        ordersService =
            new OrdersService(
                restTemplate,
                productService,
                erpService,
                orderStatusDAO,
                quoteStatusDAO,
                notificationService,
                cartService,
                accountService,
                mincronServiceClient,
                searchService,
                branchesService
            );
        ReflectionTestUtils.setField(ordersService, "eclipseServiceUrl", ECLIPSE_SERVICE_URL);
        ReflectionTestUtils.setField(ordersService, "mincronServiceUrl", MINCRON_SERVICE_URL);
        var lineItems = new ArrayList<OrderLineItemResponseDTO>();
        var lineItem = new OrderLineItemResponseDTO();
        lineItem.setErpPartNumber(PART_NUMBER);
        lineItems.add(lineItem);

        getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setOrderNumber(ORDER_NUMBER);
        getOrderResponseDTO.setOrderStatus(ORDER_STATUS);
        getOrderResponseDTO.setLineItems(lineItems);
        EclipseAddressResponseDTO eclipseAddressResponseDTO = new EclipseAddressResponseDTO();
        eclipseAddressResponseDTO.setStreetLineOne(STREET);
        eclipseAddressResponseDTO.setPostalCode(ZIP);
        eclipseAddressResponseDTO.setCity(CITY);
        eclipseAddressResponseDTO.setState(STATE);
        getOrderResponseDTO.setShipAddress(eclipseAddressResponseDTO);

        productDetailRequest = new ProductDetailRequestDTO();
        productDetailRequest.setAccountId("200010");
        productDetailRequest.setUserId("JHILLIN");
        productDetailRequest.setApplication("B2B");

        ItemWrapperDTO itemWrapper = new ItemWrapperDTO();
        List<LineItemRequest> itemRequestList = new ArrayList<>();
        LineItemRequest lineItemRequest = new LineItemRequest();
        LineItemRequest.Branch branch = new LineItemRequest.Branch();
        branch.setBranchNumber("032");
        lineItemRequest.setContractNumber("2805599");
        lineItemRequest.setProductNumber("8M2");
        lineItemRequest.setBranch(branch);
        itemRequestList.add(lineItemRequest);
        itemWrapper.setItems(itemRequestList);
        productDetailRequest.setItemDTO(itemWrapper);

        ImageUrls imageUrls = new ImageUrls();
        imageUrls.setLarge(URL);
        productDTO = new ProductDTO();
        productDTO.setImageUrls(imageUrls);
        productDTO.setPartNumber(PART_NUMBER);
        productDTO.setName(PRODUCT_NAME);
        productDTO.setManufacturerName(MANUFACTURER_NAME);

        contractDTO = new ContractDTO();
        contractDTO.setContractNumber(CONTRACT_NUMBER);
        contractDTO.setDescription(CONTRACT_DESCRIPTION);
        contractDTO.setJobName(JOB_NAME);
        contractDTO.setJobNumber(JOB_NUMBER);
        contractDTO.setFirstReleaseDate(DATE);
        contractDTO.setLastReleaseDate(DATE);

        pageDTO = new PageDTO();
        pageDTO.setRowsReturned(ROWS_RETURNED);
        pageDTO.setStartRow(START_ROW);
        pageDTO.setTotalRows(TOTAL_ROWS);
        pageDTO.setResults(Arrays.asList(contractDTO));
    }

    @Test
    public void getOrderById_success() throws Exception {
        var productResponse = new ProductPricingResponseDTO();
        var product = new ProductPricingDTO();
        product.setProductId(productDTO.getPartNumber());
        product.setBranchAvailableQty(10);
        productResponse.setProducts(List.of(product));
        ErpUserInformation erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpSystemName(ErpEnum.ECLIPSE.name());

        when(restTemplate.getForEntity(anyString(), eq(GetOrderResponseDTO.class)))
            .thenReturn(new ResponseEntity<>(getOrderResponseDTO, HttpStatus.OK));
        when(productService.getProductsByNumber(any())).thenReturn(Collections.singletonList(productDTO));
        when(erpService.getProductPricing(any(), any(), any())).thenReturn(productResponse);

        GetOrderResponseDTO dto = ordersService.getOrderById("123456", "789", "1", erpUserInformation, null);
        assertTrue(dto.getOrderNumber().equals(ORDER_NUMBER));
        assertTrue(dto.getOrderStatus().equals(ORDER_STATUS));
        assertTrue(dto.getShipAddress().getStreetLineOne().equals(STREET));
        assertTrue(dto.getLineItems().stream().findFirst().get().getImageUrls().getLarge().equals(URL));
        assertTrue(dto.getLineItems().stream().findFirst().get().getManufacturerName().equals(MANUFACTURER_NAME));
    }

    @Test
    public void getOrderById_failure() {
        ErpUserInformation erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpSystemName(ErpEnum.ECLIPSE.name());
        when(restTemplate.getForEntity(anyString(), eq(GetOrderResponseDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(
            EclipseException.class,
            () -> ordersService.getOrderById("error", "error", "error", erpUserInformation, null)
        );
    }

    @Test
    public void getOrdersByAccountId_success_eclipse() throws Exception {
        when(
            restTemplate.getForEntity(
                eq(
                    ECLIPSE_SERVICE_URL +
                    "/accounts/" +
                    ERP_ACCOUNT_ID +
                    "/orders/?orderPostStartDate=" +
                    ORDER_START_DATE +
                    "&orderPostEndDate=" +
                    ORDER_END_DATE
                ),
                eq(GetOrderResponseDTO[].class)
            )
        )
            .thenReturn(new ResponseEntity<>(new GetOrderResponseDTO[] { getOrderResponseDTO }, HttpStatus.OK));

        var dto = ordersService
            .getOrdersByAccountId(ERP_ACCOUNT_ID, ORDER_START_DATE, ORDER_END_DATE, ErpEnum.ECLIPSE.name())
            .getOrders()
            .stream()
            .findFirst()
            .get();
        assertEquals(ORDER_NUMBER, dto.getOrderNumber());
        assertEquals(ORDER_STATUS, dto.getOrderStatus());
        assertEquals(STREET, dto.getShipAddress().getStreetLineOne());
    }

    @Test
    public void getOrdersByAccountId_failure_eclipse() {
        when(
            restTemplate.getForEntity(
                eq(
                    ECLIPSE_SERVICE_URL +
                    "/accounts/" +
                    ERP_ACCOUNT_ID +
                    "/orders/?orderPostStartDate=" +
                    ORDER_START_DATE +
                    "&orderPostEndDate=" +
                    ORDER_END_DATE
                ),
                eq(GetOrderResponseDTO[].class)
            )
        )
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(
            EclipseException.class,
            () ->
                ordersService.getOrdersByAccountId(
                    ERP_ACCOUNT_ID,
                    ORDER_START_DATE,
                    ORDER_END_DATE,
                    ErpEnum.ECLIPSE.name()
                )
        );
    }

    @Test
    public void getOrdersByAccountId_failure_mincron() throws Exception {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat waterworksDateFormat = new SimpleDateFormat("yyyyMMdd");
        when(
            restTemplate.exchange(
                eq(
                    MINCRON_SERVICE_URL +
                    "/orders?orderType=order&orderStatus=ALL&fromDate=" +
                    waterworksDateFormat.format(displayDateFormat.parse(ORDER_START_DATE)) +
                    "&toDate=" +
                    waterworksDateFormat.format(displayDateFormat.parse(ORDER_END_DATE)) +
                    "&accountId=" +
                    ERP_ACCOUNT_ID +
                    "&maxRows=15000&startRow=1"
                ),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
            )
        )
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setName(FeaturesEnum.WATERWORKS.name());
        featureDTO.setIsEnabled(true);
        when(accountService.getFeatures()).thenReturn(Collections.singletonList(featureDTO));
        assertThrows(
            MincronException.class,
            () ->
                ordersService.getOrdersByAccountId(
                    ERP_ACCOUNT_ID,
                    ORDER_START_DATE,
                    ORDER_END_DATE,
                    ErpEnum.MINCRON.name()
                )
        );
    }

    @Test
    public void getOrdersByAccountId_success_mincron() throws Exception {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat waterworksDateFormat = new SimpleDateFormat("yyyyMMdd");
        PageDTO<GetOrderResponseMincronDTO> getOrderResponseMincronDTOPageDTO = new PageDTO<>();
        getOrderResponseMincronDTOPageDTO.setTotalRows(TOTAL_ROWS);
        getOrderResponseMincronDTOPageDTO.setResults(generateTestMincronOrderDTOs(TOTAL_ROWS));

        String mincronOrderListUrl =
            MINCRON_SERVICE_URL +
            "/orders?orderType=order&orderStatus=ALL&fromDate=" +
            waterworksDateFormat.format(displayDateFormat.parse(ORDER_START_DATE)) +
            "&toDate=" +
            waterworksDateFormat.format(displayDateFormat.parse(ORDER_END_DATE)) +
            "&accountId=" +
            ERP_ACCOUNT_ID +
            "&maxRows=15000&startRow=1";
        ResponseEntity<PageDTO<GetOrderResponseMincronDTO>> orderResponseMincron = new ResponseEntity<>(
            getOrderResponseMincronDTOPageDTO,
            HttpStatus.OK
        );
        when(
            restTemplate.exchange(
                eq(mincronOrderListUrl),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
            )
        )
            .thenReturn(orderResponseMincron);
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setName(FeaturesEnum.WATERWORKS.name());
        featureDTO.setIsEnabled(true);
        when(accountService.getFeatures()).thenReturn(Collections.singletonList(featureDTO));

        OrdersResponseDTO actualOrderResponseMincron = ordersService.getOrdersByAccountId(
            ERP_ACCOUNT_ID,
            ORDER_START_DATE,
            ORDER_END_DATE,
            ErpEnum.MINCRON.name()
        );
        assertEquals(
            TOTAL_ROWS,
            actualOrderResponseMincron.getPagination().getTotalItemCount(),
            "Expected order response total to equal mocked order response total"
        );
        for (GetOrderResponseDTO ordersResponseDTO : actualOrderResponseMincron.getOrders()) {
            assertEquals(
                ordersResponseDTO.getOrderNumber(),
                ORDER_NUMBER,
                "Expected actual order data to equal mocked order data."
            );
            assertEquals(
                ordersResponseDTO.getOrderDate(),
                ORDER_START_DATE,
                "Expected actual order data to equal mocked order data."
            );
            assertEquals(
                ordersResponseDTO.getShipToName(),
                JOB_NAME,
                "Expected actual order data to equal mocked order data."
            );
            assertEquals(
                ordersResponseDTO.getWebStatus(),
                WebStatusesEnum.SUBMITTED.name(),
                "Expected actual order data to equal mocked order data."
            );
            assertEquals(
                ordersResponseDTO.getCustomerPO(),
                PO_NUMBER,
                "Expected actual order data to equal mocked order data."
            );
        }

        verify(restTemplate, times(1))
            .exchange(eq(mincronOrderListUrl), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    public void getOrdersByAccountId_retryUpToFourTimesWhenSQLStateConnectionFailure() throws Exception {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat waterworksDateFormat = new SimpleDateFormat("yyyyMMdd");

        String mincronOrderListUrl =
            MINCRON_SERVICE_URL +
            "/orders?orderType=order&orderStatus=ALL&fromDate=" +
            waterworksDateFormat.format(displayDateFormat.parse(ORDER_START_DATE)) +
            "&toDate=" +
            waterworksDateFormat.format(displayDateFormat.parse(ORDER_END_DATE)) +
            "&accountId=" +
            ERP_ACCOUNT_ID +
            "&maxRows=15000&startRow=1";
        when(
            restTemplate.exchange(
                eq(mincronOrderListUrl),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
            )
        )
            .thenThrow(new MincronException(SQL_STATE_CONNECTION_FAILURE_CODE));
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setName(FeaturesEnum.WATERWORKS.name());
        featureDTO.setIsEnabled(true);
        when(accountService.getFeatures()).thenReturn(Collections.singletonList(featureDTO));

        assertThrows(
            MincronException.class,
            () ->
                ordersService.getOrdersByAccountId(
                    ERP_ACCOUNT_ID,
                    ORDER_START_DATE,
                    ORDER_END_DATE,
                    ErpEnum.MINCRON.name()
                )
        );

        verify(restTemplate, times(4))
            .exchange(eq(mincronOrderListUrl), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    public void getOrdersByAccountId_waterworks_disabled() throws Exception {
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setName(FeaturesEnum.WATERWORKS.name());
        featureDTO.setIsEnabled(false);
        when(accountService.getFeatures()).thenReturn(Collections.singletonList(featureDTO));

        List<GetOrderResponseDTO> dtos = ordersService
            .getOrdersByAccountId(ERP_ACCOUNT_ID, ORDER_START_DATE, ORDER_END_DATE, ErpEnum.MINCRON.name())
            .getOrders();
        assertNull(dtos);
    }

    @Test
    public void getQuotesByAccountId_success() throws Exception {
        when(restTemplate.getForEntity(anyString(), eq(GetOrderResponseDTO[].class)))
            .thenReturn(new ResponseEntity<>(new GetOrderResponseDTO[] { getOrderResponseDTO }, HttpStatus.OK));

        var dto = ordersService.getQuotesByAccountId("123456", null, null, 0).stream().findFirst().get();
        assertEquals(ORDER_NUMBER, dto.getOrderNumber());
        assertEquals(ORDER_STATUS, dto.getOrderStatus());
        assertEquals(STREET, dto.getShipAddress().getStreetLineOne());
    }

    @Test
    public void getQuotesByAccountId_failure() throws Exception {
        when(restTemplate.getForEntity(anyString(), eq(GetOrderResponseDTO[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(EclipseException.class, () -> ordersService.getQuotesByAccountId("error", "error", "error", 0));
    }

    @Test
    public void getContracts_success() throws Exception {
        String url =
            MINCRON_SERVICE_URL +
            "/contracts?accountId=123123&startRow=1&maxRows=10&searchFilter=&fromDate=&toDate=&sortOrder=&sortDirection=";
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
            .thenReturn(new ResponseEntity<>(pageDTO, HttpStatus.OK));
        PageDTO<ContractDTO> page = ordersService.getContracts(ERP_ACCOUNT_ID, "1", "", "", "", "", "");

        assertEquals(TOTAL_ROWS, (int) page.getTotalRows());
        assertEquals(ROWS_RETURNED, (int) page.getRowsReturned());
        assertEquals(START_ROW, (int) page.getStartRow());

        ContractDTO contract = page.getResults().get(0);
        assertEquals(CONTRACT_DESCRIPTION, contract.getDescription());
        assertEquals(CONTRACT_NUMBER, contract.getContractNumber());
        assertEquals(JOB_NAME, contract.getJobName());
        assertEquals(JOB_NUMBER, contract.getJobNumber());
        assertEquals(DATE, contract.getFirstReleaseDate());
        assertEquals(DATE, contract.getLastReleaseDate());

        verify(restTemplate, times(1))
            .exchange(eq(url), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    public void getContracts_retryUpToFourTimesWhenSQLStateConnectionFailure() {
        String url =
            MINCRON_SERVICE_URL +
            "/contracts?accountId=123123&startRow=1&maxRows=10&searchFilter=&fromDate=&toDate=&sortOrder=&sortDirection=";
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
            .thenThrow(new MincronException(SQL_STATE_CONNECTION_FAILURE_CODE));

        assertThrows(MincronException.class, () -> ordersService.getContracts(ERP_ACCOUNT_ID, "1", "", "", "", "", ""));
        verify(restTemplate, times(4))
            .exchange(eq(url), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    public void getContracts_failed() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assertThrows(MincronException.class, () -> ordersService.getContracts(ERP_ACCOUNT_ID, "1", "", "", "", "", ""));
    }

    @Test
    public void getContract_success() {
        val testAccountId = "9875";
        val testContractNumber = "12345";
        val testContractHeader = TestUtils.loadResponseJson(
            "get-contract-header-success-response.json",
            ContractHeader.class
        );

        val testBranchNum = testContractHeader.getBranch().getBranchNumber();
        val testBranch = buildTestBranch(testBranchNum, "60");

        val testLineItems = TestUtils.loadResponseJson(
            "get-contract-item-list-success-response.json",
            ITEM_LIST_TYPE_REF
        );

        val testProducts = buildTestProducts(testLineItems);

        when(mincronServiceClient.getContractHeader(eq(testContractNumber)))
            .thenReturn(Optional.of(testContractHeader));
        when(branchesService.getBranch(eq(testBranch.getBranchId()))).thenReturn(testBranch);
        when(mincronServiceClient.getContractItemList(eq(testAccountId), eq(testContractNumber)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(testLineItems.stream().map(ProductLineItem::getProductNumber).collect(Collectors.toList())),
                eq(CONTRACT_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(testProducts);

        ordersService
            .getContract(testAccountId, testContractNumber)
            .ifPresentOrElse(
                response -> {
                    assertEquals(testBranch.getAddress1(), response.getAccountInformation().getBranch().getAddress1());
                    assertEquals(
                        testLineItems.get(0).getProductNumber(),
                        response.getContractProducts().get(0).getPartNumber()
                    );
                    assertEquals(testProducts.get(0).getId(), response.getContractProducts().get(0).getId());
                    assertTrue(true);
                    assertEquals(
                        response.getContractProducts().get(1).isDisplayOnly(),
                        true,
                        "Should convert 'Y' to true"
                    );
                },
                Assertions::fail
            );
    }

    @Test
    public void getContract_productNotInAppsearch() {
        val testAccountId = "9875";
        val testContractNumber = "12345";
        val testContractHeader = TestUtils.loadResponseJson(
            "get-contract-header-success-response.json",
            ContractHeader.class
        );

        val testBranchNum = testContractHeader.getBranch().getBranchNumber();
        val testBranch = buildTestBranch(testBranchNum, "60");

        val testLineItems = TestUtils.loadResponseJson(
            "get-contract-item-list-success-response.json",
            ITEM_LIST_TYPE_REF
        );

        when(mincronServiceClient.getContractHeader(eq(testContractNumber)))
            .thenReturn(Optional.of(testContractHeader));
        when(branchesService.getBranch(eq(testBranch.getBranchId()))).thenReturn(testBranch);
        when(mincronServiceClient.getContractItemList(eq(testAccountId), eq(testContractNumber)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(testLineItems.stream().map(ProductLineItem::getProductNumber).collect(Collectors.toList())),
                eq(CONTRACT_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(List.of());

        ordersService
            .getContract(testAccountId, testContractNumber)
            .ifPresentOrElse(
                response -> {
                    assertEquals(testBranch.getAddress1(), response.getAccountInformation().getBranch().getAddress1());
                    assertEquals(
                        testLineItems.get(0).getProductNumber(),
                        response.getContractProducts().get(0).getPartNumber()
                    );
                    assertEquals(
                        testLineItems.get(0).getDescription(),
                        response.getContractProducts().get(0).getName()
                    );
                    assertTrue(response.getContractProducts().get(1).getId().contains("null-product"));
                    assertTrue(true);
                },
                Assertions::fail
            );
    }

    @Test
    public void getContract_missingBranch() {
        val testAccountId = "9875";
        val testContractNumber = "12345";
        val testContractHeader = TestUtils.loadResponseJson(
            "get-contract-header-success-response.json",
            ContractHeader.class
        );

        val testBranchNum = testContractHeader.getBranch().getBranchNumber();
        val testBranch = buildTestBranch(testBranchNum, "60");

        val testLineItems = TestUtils.loadResponseJson(
            "get-contract-item-list-success-response.json",
            ITEM_LIST_TYPE_REF
        );

        val testProducts = buildTestProducts(testLineItems);

        when(mincronServiceClient.getContractHeader(eq(testContractNumber)))
            .thenReturn(Optional.of(testContractHeader));
        when(branchesService.getBranch(eq(testBranch.getBranchId())))
            .thenThrow(new BranchNotFoundException(testBranch.getBranchId()));
        when(mincronServiceClient.getContractItemList(eq(testAccountId), eq(testContractNumber)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(testLineItems.stream().map(ProductLineItem::getProductNumber).collect(Collectors.toList())),
                eq(CONTRACT_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(testProducts);

        ordersService
            .getContract(testAccountId, testContractNumber)
            .ifPresentOrElse(
                response -> {
                    assertEquals(null, response.getAccountInformation().getBranch().getAddress1());
                    assertEquals(null, response.getAccountInformation().getBranch().getCity());
                    assertTrue(true);
                },
                Assertions::fail
            );
    }

    @Test
    void getOrderById_mincronSuccess() throws Exception {
        val testAccountId = "9875";
        val testOrderId = "12345";
        val testOrderHeader = TestUtils.loadResponseJson("get-order-header-success-response.json", OrderHeader.class);

        val testLineItems = TestUtils.loadResponseJson("get-order-item-list-success-response.json", ITEM_LIST_TYPE_REF);
        val testBranch = buildTestBranch(String.valueOf(testOrderHeader.getBranchNumber()), "600");

        ErpUserInformation mincronUser = new ErpUserInformation();
        mincronUser.setErpSystemName(ErpEnum.MINCRON.name());
        mincronUser.setErpAccountId(testAccountId);

        val testProducts = buildTestProducts(testLineItems);

        when(branchesService.getBranch(eq(testBranch.getBranchId()))).thenReturn(testBranch);
        when(mincronServiceClient.getOrderHeader(eq(testOrderId), eq("order")))
            .thenReturn(Optional.of(testOrderHeader));
        when(mincronServiceClient.getOrderItemList(eq(testAccountId), eq("order"), eq(testOrderId)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(
                    testLineItems
                        .stream()
                        .filter((productLineItem -> productLineItem.getDisplayOnly().equals("N")))
                        .map(ProductLineItem::getProductNumber)
                        .collect(Collectors.toList())
                ),
                eq(ORDER_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(testProducts);

        GetOrderResponseDTO orderResponseDTO = ordersService.getOrderById(
            testAccountId,
            testOrderId,
            "",
            mincronUser,
            MincronOrderStatus.OPEN
        );

        assertEquals(3, orderResponseDTO.getLineItems().size());

        assertEquals(
            0,
            orderResponseDTO
                .getLineItems()
                .stream()
                .filter(
                    (orderLineItemResponseDTO -> orderLineItemResponseDTO.getProductName().contains(DISPLAY_ONLY_NAME))
                )
                .count(),
            "Expected product with displayOnly = Y to not be returned in item list"
        );

        OrderLineItemResponseDTO lineItem = orderResponseDTO.getLineItems().get(0);
        assertEquals(testLineItems.get(0).getProductNumber(), lineItem.getErpPartNumber());
        assertEquals(testProducts.get(0).getId(), lineItem.getProductId());
        assertEquals(PRODUCT_NAME, lineItem.getProductName());
    }

    @Test
    void getOrderById_mincronSuccessInvoice() throws Exception {
        val testAccountId = "9875";
        val testOrderId = "12345";
        val testOrderHeader = TestUtils.loadResponseJson("get-order-header-success-response.json", OrderHeader.class);

        val testLineItems = TestUtils.loadResponseJson("get-order-item-list-success-response.json", ITEM_LIST_TYPE_REF);
        val testBranch = buildTestBranch(String.valueOf(testOrderHeader.getBranchNumber()), "600");

        ErpUserInformation mincronUser = new ErpUserInformation();
        mincronUser.setErpSystemName(ErpEnum.MINCRON.name());
        mincronUser.setErpAccountId(testAccountId);

        val testProducts = buildTestProducts(testLineItems);

        when(branchesService.getBranch(eq(testBranch.getBranchId()))).thenReturn(testBranch);
        when(mincronServiceClient.getOrderHeader(eq(testOrderId), eq("invoice")))
            .thenReturn(Optional.of(testOrderHeader));
        when(mincronServiceClient.getOrderItemList(eq(testAccountId), eq("invoice"), eq(testOrderId)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(
                    testLineItems
                        .stream()
                        .filter((productLineItem -> productLineItem.getDisplayOnly().equals("N")))
                        .map(ProductLineItem::getProductNumber)
                        .collect(Collectors.toList())
                ),
                eq(ORDER_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(testProducts);

        OrderLineItemResponseDTO lineItem = ordersService
            .getOrderById(testAccountId, testOrderId, "", mincronUser, MincronOrderStatus.INVOICED)
            .getLineItems()
            .stream()
            .findFirst()
            .get();

        assertEquals(testLineItems.get(0).getProductNumber(), lineItem.getErpPartNumber());
        assertEquals(testProducts.get(0).getId(), lineItem.getProductId());
    }

    @Test
    void getOrderById_branchNotFound() throws Exception {
        val testAccountId = "9875";
        val testOrderId = "12345";
        val testOrderHeader = TestUtils.loadResponseJson("get-order-header-success-response.json", OrderHeader.class);
        val testLineItems = TestUtils.loadResponseJson("get-order-item-list-success-response.json", ITEM_LIST_TYPE_REF);

        ErpUserInformation mincronUser = new ErpUserInformation();
        mincronUser.setErpSystemName(ErpEnum.MINCRON.name());
        mincronUser.setErpAccountId(testAccountId);

        val testBranch = buildTestBranch(String.valueOf(testOrderHeader.getBranchNumber()), "600");

        when(branchesService.getBranch(eq(testBranch.getBranchId())))
            .thenThrow(new BranchNotFoundException(testBranch.getBranchId()));
        when(mincronServiceClient.getOrderHeader(eq(testOrderId), anyString()))
            .thenReturn(Optional.of(testOrderHeader));
        when(mincronServiceClient.getOrderItemList(eq(testAccountId), anyString(), eq(testOrderId)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(testLineItems.stream().map(ProductLineItem::getProductNumber).collect(Collectors.toList())),
                eq(ORDER_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(List.of());

        GetOrderResponseDTO getOrderResponseDTO = ordersService.getOrderById(
            testAccountId,
            testOrderId,
            "",
            mincronUser,
            MincronOrderStatus.OPEN
        );
        assertNull(getOrderResponseDTO.getShipAddress().getStreetLineOne());
        assertNull(getOrderResponseDTO.getShipAddress().getCity());
    }

    @Test
    void getOrderById_mincronProductsNotInAppSearch() throws Exception {
        val testAccountId = "9875";
        val testOrderId = "12345";
        val testOrderHeader = TestUtils.loadResponseJson("get-order-header-success-response.json", OrderHeader.class);

        val testLineItems = TestUtils.loadResponseJson("get-order-item-list-success-response.json", ITEM_LIST_TYPE_REF);

        ErpUserInformation mincronUser = new ErpUserInformation();
        mincronUser.setErpSystemName(ErpEnum.MINCRON.name());
        mincronUser.setErpAccountId(testAccountId);

        val testBranch = buildTestBranch(String.valueOf(testOrderHeader.getBranchNumber()), "600");

        when(branchesService.getBranch(eq(testBranch.getBranchId()))).thenReturn(testBranch);
        when(mincronServiceClient.getOrderHeader(eq(testOrderId), anyString()))
            .thenReturn(Optional.of(testOrderHeader));
        when(mincronServiceClient.getOrderItemList(eq(testAccountId), anyString(), eq(testOrderId)))
            .thenReturn(testLineItems);
        when(
            searchService.getProductsByCustomerPartNumber(
                eq("mincron-ecomm-products"),
                eq(testLineItems.stream().map(ProductLineItem::getProductNumber).collect(Collectors.toList())),
                eq(ORDER_ITEMS_PAGE_SIZE)
            )
        )
            .thenReturn(List.of());

        OrderLineItemResponseDTO lineItem = ordersService
            .getOrderById(testAccountId, testOrderId, "", mincronUser, MincronOrderStatus.OPEN)
            .getLineItems()
            .stream()
            .findFirst()
            .get();

        assertEquals(testLineItems.get(0).getProductNumber(), lineItem.getErpPartNumber());
        assertTrue(lineItem.getProductId() == null);
    }

    public static BranchResponseDTO buildTestBranch(String branchNum, String branchPrefix) {
        val branch = new BranchResponseDTO();
        branch.setBranchId(branchPrefix + branchNum.trim());
        branch.setAddress1("123 Main St");
        branch.setCity("Dallas");
        branch.setState("TX");
        branch.setZip("75201");
        return branch;
    }

    public static List<ProductDTO> buildTestProducts(Collection<ProductLineItem> lineItems) {
        return lineItems
            .stream()
            .map(lineItem -> {
                val product = new ProductDTO();
                product.setId(UUID.randomUUID().toString());
                product.setCustomerPartNumber(List.of(lineItem.getProductNumber()));
                product.setImageUrls(new ImageUrls());
                product.setName(PRODUCT_NAME);
                return product;
            })
            .collect(Collectors.toList());
    }

    public static List<GetOrderResponseMincronDTO> generateTestMincronOrderDTOs(int numberOfOrders) {
        List<GetOrderResponseMincronDTO> getOrderResponseMincronDTOS = new ArrayList<>();
        for (int i = 0; i < numberOfOrders; ++i) {
            GetOrderResponseMincronDTO getOrderResponseMincronDTO = new GetOrderResponseMincronDTO();
            getOrderResponseMincronDTO.setOrderDate(MINCRON_ORDER_DATE);
            getOrderResponseMincronDTO.setStatus(WebStatusesEnum.SUBMITTED.name());
            getOrderResponseMincronDTO.setOrderNumber(ORDER_NUMBER);
            getOrderResponseMincronDTO.setPurchaseOrderNumber(PO_NUMBER);
            getOrderResponseMincronDTO.setJobName(JOB_NAME);
            getOrderResponseMincronDTO.setShipDate(MINCRON_ORDER_DATE);
            getOrderResponseMincronDTO.setOrderTotal(ORDER_TOTAL);
            getOrderResponseMincronDTO.setContractNumber(CONTRACT_NUMBER);
            getOrderResponseMincronDTOS.add(getOrderResponseMincronDTO);
        }
        return getOrderResponseMincronDTOS;
    }

    @Test
    public void getPreviouslyPurchasedProducts_success() throws Exception {
        PreviouslyPurchasedEclipseProductResponse response = new PreviouslyPurchasedEclipseProductResponse();
        PreviouslyPurchasedProduct previouslyPurchasedProductObj = new PreviouslyPurchasedProduct();

        Product product = new Product();
        PartIdentifiers partIdentifiers = new PartIdentifiers();
        partIdentifiers.setEclipsePartNumber("1223");
        product.setPartIdentifiers(partIdentifiers);

        AvailabilityList availabilityList = new AvailabilityList();
        availabilityList.setBranchAvailabilityList(new ArrayList<BranchAvailability>());

        Quantity quantity = new Quantity();
        quantity.setQuantity("13");

        previouslyPurchasedProductObj.setQuantity(quantity);
        previouslyPurchasedProductObj.setProduct(product);

        var listItems = new ArrayList<PreviouslyPurchasedProduct>();
        listItems.add(previouslyPurchasedProductObj);
        response.setProducts(listItems);

        ErpUserInformation erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpAccountId(ERP_ACCOUNT_ID);

        boolean isEmployee = true;

        when(erpService.getPreviouslyPurchasedProductsFromEclipse(ERP_ACCOUNT_ID, 1, 10)).thenReturn(response);

        PreviouslyPurchasedProductResponseDTO productsResponse = ordersService.getPreviouslyPurchasedProducts(
            erpUserInformation,
            1,
            10
        );
        assertNotNull(productsResponse.getProducts());
    }

    @Test
    void deleteContractCart_Success() {
        String updateCartUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/delete-cart")
            .queryParam("application", "B2B")
            .queryParam("accountId", "200010")
            .queryParam("userId", "JHILLIN")
            .queryParam("shoppingCartId", "00000000000000001341")
            .queryParam("branchNumber", "032")
            .encode()
            .toUriString();
        ContractCreateCartReturnTableDTO dto = new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("00000000000000001341");
        when(
            restTemplate.exchange(
                updateCartUrlTemplate,
                HttpMethod.DELETE,
                null,
                ContractCreateCartReturnTableDTO.class
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp = ordersService.deleteCartItems(
            "B2B",
            "200010",
            "JHILLIN",
            "00000000000000001341",
            "032"
        );
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void deleteContractCart_Exception() {
        String updateCartUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/delete-cart")
            .queryParam("application", "B2B")
            .queryParam("accountId", "200010")
            .queryParam("userId", "JHILLIN")
            .queryParam("shoppingCartId", "00000000000000001341")
            .queryParam("branchNumber", "032")
            .encode()
            .toUriString();
        when(
            restTemplate.exchange(
                updateCartUrlTemplate,
                HttpMethod.DELETE,
                null,
                ContractCreateCartReturnTableDTO.class
            )
        )
            .thenThrow(new MincronException("Delete cart failed"));
        String responseStatus = assertThrows(
            MincronException.class,
            () -> ordersService.deleteCartItems("B2B", "200010", "JHILLIN", "00000000000000001341", "032")
        )
            .getMessage();
        assertEquals("Delete cart failed", responseStatus);
    }

    @Test
    void submitContractToReleaseOrder_Success() throws Exception {
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
        dto.setReturnTable("54532311");

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setDomain("FrontLine");
        branchDTO.setBrand("Mincron");

        UserDTO user = new UserDTO();
        user.setLastName("First");
        BranchResponseDTO branchResponse = new BranchResponseDTO();
        when(branchesService.getBranchByEntityId("123", ErpEnum.MINCRON.name())).thenReturn(branchResponse);
        when(accountService.getHomeBranch("123")).thenReturn(branchDTO);
        when(accountService.getUser(submitOrderRequest.getUserId(), eq(any()))).thenReturn(user);

        String urlTemplate =
            MINCRON_SERVICE_URL +
            "/contracts/orders/submit?application=B2B&accountId=" +
            ERP_ACCOUNT_ID +
            "&userId=JHILLIN&shoppingCartId=00000000000000001341";
        when(
            restTemplate.postForEntity(
                eq(urlTemplate),
                eq(submitOrderRequest),
                eq(ContractCreateCartReturnTableDTO.class)
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp = ordersService.submitContractToReleaseOrder(submitOrderRequest);
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void submitContractToReleaseOrder_success() throws Exception {
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
        submitOrderRequest.setShipBranchNumber(null);
        submitOrderRequest.setShipToId(shipToId);
        submitOrderRequest.setDeliveryMethod("WILLCALL");
        submitOrderRequest.setOrderTotal("100");
        submitOrderRequest.setTaxAmount("10");
        submitOrderRequest.setSubTotal("20");
        ContractCreateCartReturnTableDTO dto = new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("54532311");

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setDomain("FrontLine");
        branchDTO.setBrand("Mincron");

        UserDTO user = new UserDTO();
        user.setLastName("First");
        BranchResponseDTO branchResponse = new BranchResponseDTO();
        when(branchesService.getBranchByEntityId("123", ErpEnum.MINCRON.name())).thenReturn(branchResponse);
        when(accountService.getHomeBranch(shipToId.toString())).thenReturn(branchDTO);
        when(accountService.getUser(submitOrderRequest.getUserId(), eq(any()))).thenReturn(user);

        String urlTemplate =
            MINCRON_SERVICE_URL +
            "/contracts/orders/submit?application=B2B&accountId=" +
            ERP_ACCOUNT_ID +
            "&userId=JHILLIN&shoppingCartId=00000000000000001341";
        when(
            restTemplate.postForEntity(
                eq(urlTemplate),
                eq(submitOrderRequest),
                eq(ContractCreateCartReturnTableDTO.class)
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp = ordersService.submitContractToReleaseOrder(submitOrderRequest);
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void submitContractToReleaseOrder_success2() throws Exception {
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
        submitOrderRequest.setShipBranchNumber(null);
        submitOrderRequest.setShipToId(shipToId);
        submitOrderRequest.setDeliveryMethod("DELIVERY");
        submitOrderRequest.setOrderTotal("100");
        submitOrderRequest.setTaxAmount("10");
        submitOrderRequest.setSubTotal("20");

        AddressDTO address = new AddressDTO();
        submitOrderRequest.setShipToAddress(address);
        ContractCreateCartReturnTableDTO dto = new ContractCreateCartReturnTableDTO();
        dto.setReturnTable("54532311");

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setDomain("FrontLine");
        branchDTO.setBrand("Mincron");

        UserDTO user = new UserDTO();
        user.setLastName("First");
        BranchResponseDTO branchResponse = new BranchResponseDTO();
        when(branchesService.getBranchByEntityId("123", ErpEnum.MINCRON.name())).thenReturn(branchResponse);
        when(accountService.getHomeBranch(shipToId.toString())).thenReturn(branchDTO);
        when(accountService.getUser(submitOrderRequest.getUserId(), eq(any()))).thenReturn(user);

        String urlTemplate =
            MINCRON_SERVICE_URL +
            "/contracts/orders/submit?application=B2B&accountId=" +
            ERP_ACCOUNT_ID +
            "&userId=JHILLIN&shoppingCartId=00000000000000001341";
        when(
            restTemplate.postForEntity(
                eq(urlTemplate),
                eq(submitOrderRequest),
                eq(ContractCreateCartReturnTableDTO.class)
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ContractCreateCartReturnTableDTO resp = ordersService.submitContractToReleaseOrder(submitOrderRequest);
        assertEquals(dto.getReturnTable(), resp.getReturnTable());
    }

    @Test
    void submitContractToReleaseOrder_Exception() {
        SubmitOrderRequestDTO submitOrderRequest = new SubmitOrderRequestDTO();
        submitOrderRequest.setApplication("B2B");
        submitOrderRequest.setAccountId(ERP_ACCOUNT_ID);
        submitOrderRequest.setShoppingCartId("00000000000000001341");
        submitOrderRequest.setUserId("JHILLIN");
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/submit")
            .queryParam("application", "B2B")
            .queryParam("accountId", ERP_ACCOUNT_ID)
            .queryParam("userId", "JHILLIN")
            .queryParam("shoppingCartId", "00000000000000001341")
            .encode()
            .toUriString();
        when(restTemplate.postForEntity(urlTemplate, submitOrderRequest, ContractCreateCartReturnTableDTO.class))
            .thenThrow(new MincronException("Order submission failed"));
        String responseStatus = assertThrows(
            MincronException.class,
            () -> ordersService.submitContractToReleaseOrder(submitOrderRequest)
        )
            .getMessage();
        assertEquals("Order submission failed", responseStatus);
    }

    @Test
    void submitOrderForReview_Success() {
        SubmitOrderReviewRequestDTO submitOrderReviewRequestDTO = new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO createCartRequest = new CreateCartRequestDTO();
        createCartRequest.setApplication("B2B");
        submitOrderReviewRequestDTO.setCreateCartRequest(createCartRequest);
        String orderReviewUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/review")
            .encode()
            .toUriString();

        SubmitOrderReviewResponseDTO dto = new SubmitOrderReviewResponseDTO();
        SubmitOrderWrapperDTO wrapper = new SubmitOrderWrapperDTO();
        wrapper.setContractNumber("3000000");
        dto.setReturnTable(wrapper);

        val requestEntity = new HttpEntity<>(submitOrderReviewRequestDTO);
        when(
            restTemplate.exchange(
                eq(orderReviewUrlTemplate),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(SubmitOrderReviewResponseDTO.class)
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        SubmitOrderReviewResponseDTO resp = ordersService.orderReview(submitOrderReviewRequestDTO);

        assertEquals(wrapper.getContractNumber(), resp.getReturnTable().getContractNumber());
    }

    @Test
    void submitOrderForReview_Exception() {
        SubmitOrderReviewRequestDTO submitOrderReviewRequestDTO = new SubmitOrderReviewRequestDTO();
        CreateCartRequestDTO createCartRequest = new CreateCartRequestDTO();
        createCartRequest.setApplication("B2B");
        submitOrderReviewRequestDTO.setCreateCartRequest(createCartRequest);
        String orderReviewUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/review")
            .encode()
            .toUriString();

        val requestEntity = new HttpEntity<>(submitOrderReviewRequestDTO);
        when(
            restTemplate.exchange(
                eq(orderReviewUrlTemplate),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(SubmitOrderReviewResponseDTO.class)
            )
        )
            .thenThrow(new MincronException("Review order failed"));
        String responseStatus = assertThrows(
            MincronException.class,
            () -> ordersService.orderReview(submitOrderReviewRequestDTO)
        )
            .getMessage();
        assertEquals("Review order failed", responseStatus);
    }

    @Test
    void getProductDetails_Success() {
        val requestEntity = new HttpEntity<>(productDetailRequest);
        String itemDetailUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/product-details")
            .build()
            .toUriString();

        ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();
        ProductDetailWrapperDTO resp = new ProductDetailWrapperDTO();
        List<LineItemResponse> respList = new ArrayList<>();
        LineItemResponse itemResp = new LineItemResponse();
        LineItemResponse.Branch branch = new LineItemResponse.Branch();
        branch.setBranchNumber("032");
        itemResp.setBranch(branch);
        itemResp.setQuantityAvailableHomeBranch("10");
        respList.add(itemResp);
        resp.setItems(respList);
        dto.setReturnTable(resp);

        when(
            restTemplate.exchange(
                eq(itemDetailUrlTemplate),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(ProductDetailsResponseDTO.class)
            )
        )
            .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));
        ProductDetailsResponseDTO response = ordersService.getProductDetails(productDetailRequest);

        assertEquals(dto.getReturnTable(), response.getReturnTable());
    }

    @Test
    void getProductDetails_Exception() {
        val requestEntity = new HttpEntity<>(productDetailRequest);
        String itemDetailUrlTemplate = UriComponentsBuilder
            .fromHttpUrl(MINCRON_SERVICE_URL)
            .path("/contracts/orders/product-details")
            .build()
            .toUriString();

        when(
            restTemplate.exchange(
                eq(itemDetailUrlTemplate),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(ProductDetailsResponseDTO.class)
            )
        )
            .thenThrow(new MincronException("Item details failed"));
        String responseStatus = assertThrows(
            MincronException.class,
            () -> ordersService.getProductDetails(productDetailRequest)
        )
            .getMessage();
        assertEquals("Item details failed", responseStatus);
    }
}
