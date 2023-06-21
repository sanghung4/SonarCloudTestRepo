package com.reece.platform.eclipse.service.Kourier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.DTO.kourier.ProductInventoryDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductInventoryRequestDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductInventoryResponseDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductPricingAndAvailabilityRequestDTO;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class KourierServiceTest {

    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EclipseService eclipseService;

    @InjectMocks
    private KourierService kourierService;

    private String mockUrl = "http://test.com";
    private String keywords = "BLK TAPE";
    private String displayName = null;
    Date effectiveDate;

    private SplitQuantityRequestDTO splitQuantityRequest;
    private static final String TEST_SQ_REQUEST =
        "{\n" +
        "    \"product\": {\n" +
        "        \"productId\": \"497043\",\n" +
        "        \"description\": \"\",\n" +
        "        \"quantity\": \"\",\n" +
        "        \"uom\": \"\",\n" +
        "        \"locationType\": \"\",\n" +
        "        \"location\": \"A150E20\",\n" +
        "        \"lot\": \"\",\n" +
        "        \"splitId\": \"\",\n" +
        "        \"orderId\": \"S113644026.001\",\n" +
        "        \"generationId\": \"\",\n" +
        "        \"lineId\": \"\",\n" +
        "        \"shipVia\": \"\",\n" +
        "        \"tote\": \"STACEY-TEST-TOTE\",\n" +
        "        \"userId\": \"SF08466\",\n" +
        "        \"branchId\": \"\",\n" +
        "        \"cutDetail\": \"\",\n" +
        "        \"cutGroup\": \"\",\n" +
        "        \"isParallelCut\": false,\n" +
        "        \"warehouseId\": \"S~G11C05~S113004619.1.1.0~I\",\n" +
        "        \"isLot\": false,\n" +
        "        \"isSerial\": false,\n" +
        "        \"pickGroup\": \"\",\n" +
        "        \"isOverrideProduct\": false,\n" +
        "        \"startPickTime\": \"\",\n" +
        "        \"ignoreLockToteCheck\": \"\"\n" +
        "    },\n" +
        "    \"pickedItemsCount\": 1\n" +
        "}";

    private static final String TEST_SQ_RESPONSE =
        "{\"trans\":[{\"orderId\":\"S113644026.001\",\"successStatus\":true,\"isSplit\":false,\"invalidSerialNums\":\"\",\"productId\":\"497043\",\"location\":\"A150E20\",\"errorMessage\":\"\",\"errorCode\":\"\"}]}";
    private CloseOrderRequestDTO closeOrderRequestDTO;
    private static final String TEST_CO_RESPONSE =
        "{\n" +
        "    \"status\": true,\n" +
        "    \"orderId\": \"S113643890.001\",\n" +
        "    \"pickerId\": \"SF08466\",\n" +
        "    \"errorCode\": \"\",\n" +
        "    \"errorMessage\": \"\",\n" +
        "    \"orderLocked\": false,\n" +
        "    \"moreToPick\": false,\n" +
        "    \"stillPicking\": false\n" +
        "}";
    private static final String TEST_CO_REQUEST =
        "{\n" + "    \"orderId\": \"S113643890.001\",\n" + "    \"pickerId\": \"SF08466\"\n" + "}";

    /* @BeforeAll
    public void init() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        splitQuantityRequest = om.readValue(TEST_SQ_REQUEST,SplitQuantityRequestDTO.class);
    }*/
    @BeforeEach
    public void setup() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        splitQuantityRequest = om.readValue(TEST_SQ_REQUEST, SplitQuantityRequestDTO.class);
        MockitoAnnotations.initMocks(this);
        eclipseService = mock(EclipseService.class);
        RestTemplate rt = new RestTemplate();
        mockServer = MockRestServiceServer.bindTo(rt).ignoreExpectOrder(true).build();
        kourierService = new KourierService(eclipseService, rt);
        ReflectionTestUtils.setField(kourierService, "kourierApiEndpoint", mockUrl);
        ReflectionTestUtils.setField(kourierService, "kourierUsername", "testUser");
        ReflectionTestUtils.setField(kourierService, "kourierPassword", "testPassword");
    }

    @Test
    public void getVarianceSummary_success() throws URISyntaxException, JsonProcessingException {
        VarianceSummaryDTO mockResponse = new VarianceSummaryDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/PHYSICAL/VARSUM?erpCountID=Test")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );
        assertDoesNotThrow(() -> kourierService.getVarianceSummary("Test"));
    }

    @Test
    public void getVarianceSummary_badRequest() throws URISyntaxException, JsonProcessingException {
        VarianceSummaryDTO mockResponse = new VarianceSummaryDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/PHYSICAL/VARSUM?erpCountID=Test")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> kourierService.getVarianceSummary("Test"));
    }

    @Test
    public void getVarianceDetails_success() throws URISyntaxException, JsonProcessingException {
        VarianceDetailsDTO mockResponse = new VarianceDetailsDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/PHYSICAL/VARIANCE?erpCountID=Test")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );
        assertDoesNotThrow(() -> kourierService.getVarianceDetails("Test"));
    }

    @Test
    public void getVarianceDetails_badRequest() throws URISyntaxException, JsonProcessingException {
        VarianceSummaryDTO mockResponse = new VarianceSummaryDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/PHYSICAL/VARIANCE?erpCountID=Test")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> kourierService.getVarianceDetails("Test"));
    }

    @Test
    public void getProductPrice_success() throws URISyntaxException, JsonProcessingException {
        ProductPriceResponseDTO mockResponse = new ProductPriceResponseDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setBasicAuth("RCEBIA", "bC4ZE7eUe3g4BVaW");
        responseHeaders.set("X-Connection", "ECLIPSE");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(
                ExpectedCount.twice(),
                requestTo(
                    new URI(
                        mockUrl +
                        "/ARS/PRODUCT/PRODPRICE?productId=testProductId&erpBranchNum=testBranch&customerId=123&userId=123&effectiveDate=&correlationId=123"
                    )
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );
        assertDoesNotThrow(() ->
            kourierService.getProductPrice("testProductId", "testBranch", "123", "123", effectiveDate, "123")
        );
    }

    @Test
    public void getInvoices_success() throws URISyntaxException, JsonProcessingException {
        InvoiceSummaryDTO mockResponse = new InvoiceSummaryDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setBasicAuth("RCEBIA", "bC4ZE7eUe3g4BVaW");
        responseHeaders.set("X-Connection", "ECLIPSE");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI(mockUrl + "/ARS/MAX/INVHIST?customerId=123456")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );
        assertDoesNotThrow(() -> kourierService.getInvoices("123456", null, null, null, null));
    }

    @Test
    void splitQuantity()
        throws URISyntaxException, SplitQuantityException, KourierException, InvalidSerializedProductException, EclipseTokenException, JsonProcessingException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/WHS/SPLIT")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).headers(responseHeaders).body(TEST_SQ_RESPONSE));
        assertDoesNotThrow(() -> kourierService.splitQuantity(splitQuantityRequest));
        SplitQuantityResponseDTO splitQuantityResponse = kourierService.splitQuantity(splitQuantityRequest);
        ObjectMapper om = new ObjectMapper();
        SplitQuantityRequestDTO splitQuantityRequestDTO = om.readValue(TEST_SQ_REQUEST, SplitQuantityRequestDTO.class);
        assertEquals(splitQuantityRequestDTO.getProduct().getProductId(), splitQuantityResponse.getProductId());
        assertEquals(splitQuantityRequestDTO.getProduct().getOrderId(), splitQuantityResponse.getOrderId());
        assertEquals(false, splitQuantityResponse.getIsSplit());
        assertEquals(true, splitQuantityResponse.getSuccessStatus());
    }

    @Test
    void closeOrder() throws URISyntaxException, KourierException, JsonProcessingException, CloseOrderException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/WHS/CLOSE")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).headers(responseHeaders).body(TEST_CO_RESPONSE));
        assertDoesNotThrow(() -> kourierService.closeOrder(closeOrderRequestDTO));
        CloseOrderResponseDTO closeOrderResponse = kourierService.closeOrder(closeOrderRequestDTO);
        ObjectMapper om = new ObjectMapper();
        CloseOrderResponseDTO closeOrderResponseDTO = om.readValue(TEST_CO_REQUEST, CloseOrderResponseDTO.class);
        assertEquals(closeOrderResponseDTO.getOrderId(), closeOrderResponse.getOrderId());
        assertEquals(closeOrderResponseDTO.getPickerId(), closeOrderResponse.getPickerId());
        assertEquals(true, closeOrderResponse.isStatus());
        assertEquals(false, closeOrderResponse.isOrderLocked());
        assertEquals(false, closeOrderResponse.isMoreToPick());
        assertEquals(false, closeOrderResponse.isStillPicking());
    }

    @Test
    public void kourierProductSearch_success() throws URISyntaxException, JsonProcessingException {
        ProductSearchResponseDTO mockResponse = new ProductSearchResponseDTO();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(new URI(mockUrl + "/ARS/PRODUCT/SEARCH?keywords=BLK&displayName=BLK"))
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );

        assertDoesNotThrow(() -> kourierService.getProductSearch("BLK", "BLK"));
    }

    @Test
    public void getProductInventory_success() throws URISyntaxException, JsonProcessingException {
        ProductInventoryResponseDTO mockResponse = new ProductInventoryResponseDTO();
        ProductInventoryDTO productInventoryDTO = new ProductInventoryDTO();

        List<ProductInventoryDTO> record = new ArrayList<ProductInventoryDTO>();
        record.add(productInventoryDTO);
        mockResponse.setRecord(record);

        ProductInventoryRequestDTO requestDTO = new ProductInventoryRequestDTO();
        requestDTO.setProductId("111");
        requestDTO.setUseCache(true);

        String WithInventory = "Y";

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(new URI(mockUrl + "/KAPI/PRODUCTS/ONHAND?ProductID=111&WithInventory=Y&UseOHCache=Y"))
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );

        assertDoesNotThrow(() -> kourierService.getProductInventory(requestDTO));
    }

    @Test
    public void getProductInventory_failure()
        throws URISyntaxException, JsonProcessingException, UnsupportedEncodingException {
        ProductInventoryResponseDTO mockResponse = new ProductInventoryResponseDTO();
        ProductInventoryDTO productInventoryDTO = new ProductInventoryDTO();

        List<ProductInventoryDTO> record = new ArrayList<ProductInventoryDTO>();
        record.add(productInventoryDTO);
        mockResponse.setRecord(record);

        ProductInventoryRequestDTO requestDTO = new ProductInventoryRequestDTO();
        requestDTO.setProductId("111");
        requestDTO.setUseCache(true);

        String WithInventory = "Y";

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(new URI(mockUrl + "/KAPI/PRODUCTS/ONHAND?ProductID=111&WithInventory=Y&UseOHCache=Y"))
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        assertThrows(Exception.class, () -> kourierService.getProductInventory(requestDTO));
    }

    @Test
    public void getKourierProductPrice_success() throws URISyntaxException, JsonProcessingException {
        com.reece.platform.eclipse.model.DTO.kourier.ProductPriceResponseDTO mockResponse = new com.reece.platform.eclipse.model.DTO.kourier.ProductPriceResponseDTO();
        ProductPricingAndAvailabilityRequestDTO productPricingAndAvailabilityRequestDTO = new ProductPricingAndAvailabilityRequestDTO();

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setBasicAuth("RCEBIA", "bC4ZE7eUe3g4BVaW");
        responseHeaders.set("X-Connection", "ECLIPSE");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> productIds = new ArrayList<>();
        productIds.add("111");
        productIds.add("121");

        productPricingAndAvailabilityRequestDTO.setProductIds(productIds);
        productPricingAndAvailabilityRequestDTO.setBranchId("123");
        productPricingAndAvailabilityRequestDTO.setCustomerId("111");
        productPricingAndAvailabilityRequestDTO.setUseCache(true);

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    new URI(
                        "http://test.com/KAPI/PRODUCTS/PRICE?CustomerID=111&Branch=123&ProductID=111,121&IncludeInventory=Y&UseOHCache=Y&Fields=CustomerId,Branch,ProductID,CatalogId,SellPrice,OrderUOM,OrderPerQty,TotalAvailableQty,BranchAvailableQty"
                    )
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK).headers(responseHeaders).body(mapper.writeValueAsString(mockResponse))
            );
        assertDoesNotThrow(() -> kourierService.getProductPrice(productPricingAndAvailabilityRequestDTO));
    }

    @Test
    void closeOrder_CloseOrderException()
        throws URISyntaxException, KourierException, JsonProcessingException, CloseOrderException {
        CloseOrderRequestDTO closeOrderRequest = new CloseOrderRequestDTO();
        closeOrderRequest.setOrderId("");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/WHS/CLOSE")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).headers(responseHeaders).body(TEST_CO_RESPONSE));
        assertThrows(CloseOrderException.class, () -> kourierService.closeOrder(closeOrderRequest));
    }

    @Test
    void CloseOrderException_pickingError()
        throws URISyntaxException, KourierException, JsonProcessingException, CloseOrderException {
        CloseOrderRequestDTO closeOrderRequest = new CloseOrderRequestDTO();
        closeOrderRequest.setOrderId("123");
        closeOrderRequest.setPickerId("");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/WHS/CLOSE")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).headers(responseHeaders).body(TEST_CO_RESPONSE));
        assertThrows(CloseOrderException.class, () -> kourierService.closeOrder(closeOrderRequest));
    }

    @Test
    void closeOrder_failure()
        throws URISyntaxException, KourierException, JsonProcessingException, CloseOrderException {
        HttpHeaders responseHeaders = new HttpHeaders();
        CloseOrderRequestDTO closeOrderRequestDTO = new CloseOrderRequestDTO();
        closeOrderRequestDTO.setOrderId("123");
        closeOrderRequestDTO.setPickerId("123");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI(mockUrl + "/ARS/WHS/CLOSE")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).headers(responseHeaders).body(TEST_CO_RESPONSE));
        assertThrows(KourierException.class, () -> kourierService.closeOrder(closeOrderRequestDTO));
    }

    @Test
    public void getProductPrice_failure() throws URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setBasicAuth("RCEBIA", "bC4ZE7eUe3g4BVaW");
        responseHeaders.set("X-Connection", "ECLIPSE");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    new URI(
                        mockUrl +
                        "/ARS/PRODUCT/PRODPRICE?productId=testProductId&erpBranchNum=testBranch&customerId=123&userId=123&effectiveDate=&correlationId=123"
                    )
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        var rep = kourierService.getProductPrice("testProductId", "testBranch", "123", "123", effectiveDate, "123");
        assertNull(rep);
    }
}
