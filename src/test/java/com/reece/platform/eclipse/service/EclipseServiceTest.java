package com.reece.platform.eclipse.service;

import static com.reece.platform.eclipse.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.TestUtils;
import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.MassSalesOrderResponse;
import com.reece.platform.eclipse.model.XML.ProductRequest.ProductRequest;
import com.reece.platform.eclipse.model.XML.ProductResponse.ProductResponse;
import com.reece.platform.eclipse.model.XML.ProductSearchResponse.ProductSearchResult;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.enums.OrderStatusEnum;
import com.reece.platform.eclipse.model.generated.*;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.BaseEclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.testConstants.TestConstants;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class EclipseServiceTest {

    public static final String SESSION_TOKEN = "abcdefg";
    public static final String USER_ID = "userId";

    private List<LineItemDTO> itemsInStock;
    private List<LineItemDTO> itemsOutOfStock;

    private static final String ERP_PART_NUMBER = "222";
    private static final String CUSTOMER_PART_NUMBER = "333";
    private static final String TEST_ACCOUNT_ID = "123";
    private static final String WILL_CALL_SHIP_VIA_ID = "WILL CALL";
    private static final String DELIVERY_SHIP_VIA_ID = "OT OUR TRUCK";
    private static final float SUB_TOTAL = 60.68f;
    private static final float TAX = 5.31f;
    private static final float FEDERAL_EXCISE_TAX = 0.00f;
    private static final float FREIGHT = 18.00f;
    private static final float HANDLING = 0.00f;

    private static final String WAREHOUSE_ID = "S~T00B05~S112228790.1.1.0~I";

    @Captor
    private ArgumentCaptor<String> salesOrderArgumentCaptor;

    private EclipseSessionService eclipseSessionService;

    private RestTemplate restTemplate;

    private MockRestServiceServer mockServerJSON;

    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper mapper;

    private AsyncExecutionsService asyncExecutionsService = mock(AsyncExecutionsService.class);

    @InjectMocks
    private EclipseService eclipseService;

    class XMLRequestMatcher implements RequestMatcher {

        private final String element;

        public XMLRequestMatcher(String element) {
            this.element = element;
        }

        @Override
        public void match(ClientHttpRequest clientHttpRequest) throws IOException, AssertionError {
            String body = clientHttpRequest.getBody().toString();
            assertTrue(body.contains(element));
        }
    }

    @BeforeEach
    public void setup() {
        RestTemplate rtJSON = new RestTemplate();

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        jsonConverter.getObjectMapper().setDateFormat(new SimpleDateFormat());
        rtJSON.setMessageConverters(List.of(jsonConverter));

        RestTemplate rtXML = new RestTemplate();

        Jaxb2RootElementHttpMessageConverter xmlConverter = new Jaxb2RootElementHttpMessageConverter();
        xmlConverter.setSupportDtd(true);
        rtXML.setMessageConverters(List.of(xmlConverter));

        eclipseService = new EclipseService(rtXML, rtJSON);
        eclipseSessionService = new EclipseSessionService(rtXML, rtJSON);
        eclipseService.eclipseSessionService = eclipseSessionService;
        eclipseService.asyncExecutionsService = asyncExecutionsService;

        mockServerJSON = MockRestServiceServer.bindTo(rtJSON).ignoreExpectOrder(true).build();
        mockServer = MockRestServiceServer.bindTo(rtXML).ignoreExpectOrder(true).build();

        ReflectionTestUtils.setField(eclipseService, "eclipseApiEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseService, "eclipseEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseService, "loginId", "bob");
        ReflectionTestUtils.setField(eclipseService, "password", "bobebob");
        ReflectionTestUtils.setField(eclipseSessionService, "eclipseApiEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseSessionService, "eclipseEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseSessionService, "loginId", "bob");
        ReflectionTestUtils.setField(eclipseSessionService, "password", "bobebob");
    }

    private void initializeLineItems() {
        LineItemDTO lineItemDTO1 = new LineItemDTO();
        lineItemDTO1.setQuantity(5);
        lineItemDTO1.setQtyAvailable(6);
        lineItemDTO1.setErpPartNumber(ERP_PART_NUMBER);
        lineItemDTO1.setCustomerPartNumber(CUSTOMER_PART_NUMBER);

        LineItemDTO lineItemDTO2 = new LineItemDTO();
        lineItemDTO2.setQuantity(5);
        lineItemDTO2.setQtyAvailable(6);
        lineItemDTO2.setErpPartNumber(ERP_PART_NUMBER);
        lineItemDTO2.setCustomerPartNumber(CUSTOMER_PART_NUMBER);

        itemsInStock = Arrays.asList(lineItemDTO1, lineItemDTO2);

        LineItemDTO lineItemDTO3 = new LineItemDTO();
        lineItemDTO3.setQuantity(5);
        lineItemDTO3.setQtyAvailable(6);
        lineItemDTO3.setErpPartNumber(ERP_PART_NUMBER);
        lineItemDTO3.setCustomerPartNumber(CUSTOMER_PART_NUMBER);

        LineItemDTO lineItemDTO4 = new LineItemDTO();
        lineItemDTO4.setQuantity(5);
        lineItemDTO4.setQtyAvailable(1);
        lineItemDTO4.setErpPartNumber(ERP_PART_NUMBER);
        lineItemDTO4.setCustomerPartNumber(CUSTOMER_PART_NUMBER);

        itemsOutOfStock = Arrays.asList(lineItemDTO3, lineItemDTO4);
    }

    @Test
    public void getOrderById_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseSuccess())
                );
        //  Test
        assertDoesNotThrow(() -> {
            GetOrderResponseDTO dto = eclipseService.getOrderById("123", "123", "123");
            assertEquals("S108210380", dto.getOrderNumber());
            assertEquals("I", dto.getOrderStatus());
            assertEquals("SPC MECHANICAL CORPORATION", dto.getBillToName());
            DecimalFormat df = new DecimalFormat("#.###");
            assertEquals("60.682", df.format(dto.getLineItems().get(0).getUnitPrice()));
        });
    }

    @Test
    public void getOrderById_verify_orderTotal_and_Tax() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseSuccess())
                );
        //  Test
        assertDoesNotThrow(() -> {
            GetOrderResponseDTO dto = eclipseService.getOrderById("123", "123", "123");
            assertEquals(dto.getOrderTotal(), SUB_TOTAL + TAX + FEDERAL_EXCISE_TAX + FREIGHT + HANDLING);
            assertEquals(dto.getTax(), TAX + FEDERAL_EXCISE_TAX);
        });
    }

    @Test
    public void getOrderById_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseFailure())
                );

        // Test
        try {
            eclipseService.getOrderById("error", "error", "error");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Insufficient access."));
        }
    }

    @Test
    public void getOrdersByAccountId_success() throws URISyntaxException, EclipseException, ExecutionException, InterruptedException, EclipseTokenException, TimeoutException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(Matchers.containsString("AccountHistoryInquiry")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountHistoryResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(Matchers.containsString("OpenOrderInquiry")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.OpenOrderResponseSuccess())
                );
        GetOrderResponseDTO sampleResponse = new GetOrderResponseDTO();
        sampleResponse.setOrderDate("01/14/2021");
        sampleResponse.setOrderStatus("I");
        List<GetOrderResponseDTO> sample = Arrays.asList(sampleResponse);
        CompletableFuture<List<GetOrderResponseDTO>> results = CompletableFuture.completedFuture(sample);
        when(asyncExecutionsService.getOrdersForDateRangeAsync("123", "01/14/2021","01/14/2021")).thenReturn(results);

        //  Test
        assertDoesNotThrow(() -> {
            List<GetOrderResponseDTO> dto = eclipseService.getOrdersByAccountId("123", "01/14/2021", "01/14/2021");
            assertEquals("01/14/2021", dto.get(0).getOrderDate());
            assertEquals("I", dto.get(0).getOrderStatus());
        });
    }

    @Test
    public void getOrdersByAccountId_verify_cashId_order_is_filtered() throws EclipseException, ExecutionException, InterruptedException, EclipseTokenException, TimeoutException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<AccountHistoryInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountHistoryResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<OpenOrderInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.OpenOrderResponseSuccess())
                );

        GetOrderResponseDTO sampleResponse = new GetOrderResponseDTO();
        sampleResponse.setOrderDate("01/14/2021");
        sampleResponse.setOrderStatus("I");
        List<GetOrderResponseDTO> sample = Arrays.asList(sampleResponse, sampleResponse);
        CompletableFuture<List<GetOrderResponseDTO>> results = CompletableFuture.completedFuture(sample);
        when(asyncExecutionsService.getOrdersForDateRangeAsync("123", "01/14/2021","01/14/2021")).thenReturn(results);

        //  Test
        assertDoesNotThrow(() -> {
            List<GetOrderResponseDTO> dto = eclipseService.getOrdersByAccountId("123", "01/14/2021", "01/14/2021");
            assertEquals(dto.size(), 2, "DTO list size should not contain orderId that begins with 'C'");
        });
    }

    @Test
    public void getOrdersByAccountId_failure() throws EclipseException, ExecutionException, InterruptedException, EclipseTokenException, TimeoutException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<AccountHistoryInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountHistoryResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<OpenOrderInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.OpenOrderResponseFailure())
                );
        GetOrderResponseDTO sampleResponse = new GetOrderResponseDTO();
        sampleResponse.setOrderDate("01/14/2021");
        sampleResponse.setOrderStatus("I");
        List<GetOrderResponseDTO> sample = Arrays.asList(sampleResponse);
        CompletableFuture<List<GetOrderResponseDTO>> results = new CompletableFuture<>();
        results.completeExceptionally(new Exception("EntityID could not be determined"));
        when(asyncExecutionsService.getOrdersForDateRangeAsync("123", "01/14/2021","01/14/2021")).thenReturn(results);

        // Test
        assertThrows(Exception.class, () -> {
            eclipseService.getOrdersByAccountId("error", "01/01/2023", "01/01/2023");
        });
    }

    @Test
    public void getProduct_success_no_user_info() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            ProductResponse productResponse = eclipseService.getProductById("1", new ErpUserInformationDTO(), false);
            assertEquals(productResponse.getMassProductInquiryResponse().getStatusResult().getSuccess(), "Yes");
            assertEquals(
                    productResponse
                            .getMassProductInquiryResponse()
                            .getProductList()
                            .getProducts()
                            .get(0)
                            .getPartIdentifiers()
                            .getEclipsePartNumber(),
                    "425275"
            );
        });
    }

    @Test
    public void getProduct_success_user_info() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            ProductResponse productResponse = eclipseService.getProductById("1", new ErpUserInformationDTO(), false);
            assertEquals(productResponse.getMassProductInquiryResponse().getStatusResult().getSuccess(), "Yes");
            assertEquals(
                    productResponse
                            .getMassProductInquiryResponse()
                            .getProductList()
                            .getProducts()
                            .get(0)
                            .getPartIdentifiers()
                            .getEclipsePartNumber(),
                    "425275"
            );
        });
    }

    @Test
    public void getProduct_success_admin() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            ProductResponse productResponse = eclipseService.getProductById("1", new ErpUserInformationDTO(), false);
            assertEquals(productResponse.getMassProductInquiryResponse().getStatusResult().getSuccess(), "Yes");
            assertEquals(
                    productResponse
                            .getMassProductInquiryResponse()
                            .getProductList()
                            .getProducts()
                            .get(0)
                            .getPartIdentifiers()
                            .getEclipsePartNumber(),
                    "425275"
            );
        });
    }

    @Test
    public void getProduct_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseFailure())
                );

        try {
            eclipseService.getProductById("error", new ErpUserInformationDTO(), false);
        } catch (Exception e) {
            assertEquals("Error response from Eclipse", e.getMessage());
        }
    }

    @Test
    public void getAccount_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess())
                );

        // Test
        assertDoesNotThrow(() -> {
            GetAccountResponseDTO accountResponse = eclipseService.getAccountById("123456", false, false);
            assertTrue(accountResponse.getEmail().contains("jglenn@mtbmechanical.com"));
            assertTrue(accountResponse.getEmail().contains("jtrexler@mtbmechanical.com"));
            assertEquals(accountResponse.getPoReleaseRequired(), "PO Number");
            assertEquals("MTB MECHANICAL - SHOP", accountResponse.getCompanyName());
        });
    }

    @Test
    public void getAccount_withBillTo_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess2())
                );

        // Test
        assertDoesNotThrow(() -> {
            GetAccountResponseDTO accountResponse = eclipseService.getAccountById("billto-123456", true, false);
            assertTrue(accountResponse.getEmail().contains("jglenn@mtbmechanical.com"));
            assertTrue(accountResponse.getEmail().contains("jtrexler@mtbmechanical.com"));
            assertEquals("MDFHOP", accountResponse.getCompanyName());
        });
    }

    @Test
    public void getAccount_withRetrieveShipTo_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess3())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess4())
                );

        // Test
        assertDoesNotThrow(() -> {
            GetAccountResponseDTO accountResponse = eclipseService.getAccountById("billto-123456", false, true);
            assertEquals("MTB MECHANICAL - SHOP", accountResponse.getCompanyName());
            assertEquals("asdf", accountResponse.getShipToAccounts().get(0).getCompanyName());
            assertEquals("fdsa", accountResponse.getShipToAccounts().get(1).getCompanyName());
            assertTrue(accountResponse.getShipToAccountIds().contains(ENTITY_RESPONSE_ACCOUNT_ID_1));
            assertTrue(accountResponse.getShipToAccountIds().contains(ENTITY_RESPONSE_ACCOUNT_ID_2));
        });
    }

    @Test
    public void getAccount_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        // Setup
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseFailure())
                );

        // Test
        try {
            eclipseService.getAccountById("error", false, false);
        } catch (Exception e) {
            assertEquals("Error response from Eclipse", e.getMessage());
        }
    }

    @Test
    public void getContact_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ContactInquirySuccess())
                );

        //  Test
        assertDoesNotThrow(() -> {
            GetContactResponseDTO dto = eclipseService.getContactById("123", "123");
            assertEquals("Johnz", dto.getFirstName());
            assertEquals("john_doe@activant.com", dto.getEmail().get(0));
        });
    }

    @Test
    public void getContact_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.GetContactResponseFailure())
                );

        // Test
        try {
            eclipseService.getContactById("error", "error");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Error response from Eclipse"));
        }
    }

    @Test
    public void createContact_success() {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<ContactNewSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ContactNewSubmitResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<EntityInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess4())
                );

        assertDoesNotThrow(() -> {
            CreateContactResponseDTO response = eclipseService.createContact("123", CreateContactRequest());
            assertEquals("login", response.getErpUsername());
            assertEquals("Password1", response.getErpPassword());
            assertEquals("89967", response.getContactId());
        });
    }

    @Test
    public void createContact_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(""));

        assertThrows(NoSuchElementException.class, () -> eclipseService.createContact("123", CreateContactRequest()));
    }

    @Test
    public void updateContact_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ContactInquirySuccess())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.UpdateContactResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            UpdateContactResponseDTO response = eclipseService.updateContact(
                    "123",
                    "123",
                    CreateUpdateContactRequest()
            );
            assertEquals("Johnz", response.getFirstName());
            assertEquals("Doe", response.getLastName());
            assertEquals(
                    new ArrayList<>(Arrays.asList("john_doe@activant.com", "johnd@activant.com")),
                    response.getEmail()
            );
        });
    }

    @Test
    public void updateContact_unmarshall_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(""));

        assertThrows(
                NoSuchElementException.class,
                () -> eclipseService.updateContact("123", "123", CreateUpdateContactRequest())
        );
    }

    @Test
    public void updateContact_eclipse_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), anything())
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ContactInquirySuccess())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.UpdateContactResponseFailure())
                );
        assertThrows(
                EclipseException.class,
                () -> eclipseService.updateContact("123", "123", CreateUpdateContactRequest())
        );
    }

    @Test
    public void submitOrder_willCallItemsInStock() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(false);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsInStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderResponse(OrderStatusEnum.SHIP_WHEN_SPECIFIED))
                );

        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrder(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.SHIP_WHEN_SPECIFIED.getCode(),
                "Expected order status S from sales order response"
        );
    }

    @Test
    public void submitOrder_willCallItemsOutOfStock() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(false);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );

        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrder(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                "Expected order status C from sales order response"
        );
    }

    @Test
    public void submitOrder_deliveryItemsInStock() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsInStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );
        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrder(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                "Expected order status C from sales order response"
        );
    }

    @Test
    public void submitOrder_deliveryItemsOutOfStock_splitShipment() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderResponse(OrderStatusEnum.CALL_WHEN_AVAILABLE))
                );

        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrder(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.CALL_WHEN_AVAILABLE.getCode(),
                "Expected order status L from sales order response"
        );
    }

    @Test
    public void submitOrder_deliveryItemsOutOfStock_shipFullOrder() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(true);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );

        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrder(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                "Expected order status C from sales order response"
        );
    }

    @Test
    public void submitOrderPreview_willCallItemsInStock() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(false);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsInStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.SHIP_WHEN_SPECIFIED))
                );
        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrderPreview(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.SHIP_WHEN_SPECIFIED.getCode(),
                "Expected order status S from sales order response"
        );
    }

    @Test
    public void submitOrderPreview_willCallItemsOutOfStock() throws Exception {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(false);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );

        GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrderPreview(salesOrderDTO);

        String orderStatus = getOrderResponseDTO.getOrderStatus();
        assertEquals(
                orderStatus,
                OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                "Expected order status C from sales order response"
        );
    }

    @Test
    public void submitOrderPreview_deliveryItemsInStock() throws URISyntaxException {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsInStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );

        assertDoesNotThrow(() -> {
            GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrderPreview(salesOrderDTO);

            String orderStatus = getOrderResponseDTO.getOrderStatus();
            assertEquals(
                    orderStatus,
                    OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                    "Expected order status C from sales order response"
            );
        });
    }

    @Test
    public void submitOrderPreview_deliveryItemsOutOfStock_splitShipment() throws URISyntaxException {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(false);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.CALL_WHEN_AVAILABLE))
                );

        assertDoesNotThrow(() -> {
            GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrderPreview(salesOrderDTO);

            String orderStatus = getOrderResponseDTO.getOrderStatus();
            assertEquals(
                    orderStatus,
                    OrderStatusEnum.CALL_WHEN_AVAILABLE.getCode(),
                    "Expected order status L from sales order response"
            );
        });
    }

    @Test
    public void submitOrderPreview_deliveryItemsOutOfStock_shipFullOrder() throws URISyntaxException {
        initializeLineItems();

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setIsDelivery(true);
        salesOrderDTO.setPreferredDate(new Date());
        salesOrderDTO.setShouldShipFullOrder(true);
        salesOrderDTO.setLineItems(itemsOutOfStock);

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );

        assertDoesNotThrow(() -> {
            GetOrderResponseDTO getOrderResponseDTO = eclipseService.submitOrderPreview(salesOrderDTO);

            String orderStatus = getOrderResponseDTO.getOrderStatus();
            assertEquals(
                    orderStatus,
                    OrderStatusEnum.CALL_WHEN_COMPLETE.getCode(),
                    "Expected order status C from sales order response"
            );
        });
    }

    @Test
    public void getAccountInquiry_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountInquiryResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            AccountInquiryResponseDTO response = eclipseService.getAccountInquiry("123");
            assertNotNull(response, "Expected response to be non-null");
            assertEquals(745973.49, response.getCurrentAmt(), "Expected current amt to equal mocked data");
            assertEquals(1300977.67, response.getTotalAmt(), "Expected total amt to equal mocked data");
            assertEquals(
                    1300977.67 - 745973.49,
                    response.getTotalPastDue(),
                    "Expected passed due amt to equal mocked data"
            );
            assertEquals(534601.93, response.getBucketThirty(), "Expected bucket 30 amt to equal mocked data");
            assertEquals(8027.58, response.getBucketSixty(), "Expected bucket 60 amt to equal mocked data");
            assertEquals(8801.03, response.getBucketNinety(), "Expected bucket 90 amt to equal mocked data");
            assertEquals(3061.20, response.getBucketOneTwenty(), "Expected bucket 120 amt to equal mocked data");
            assertNotNull(response.getInvoices(), "Expected response invoices to be non-null");
            assertEquals(1, response.getInvoices().size(), "Expected one invoice from mocked data.");
            AccountInquiryItemDTO accountInquiryItemDTO = response.getInvoices().get(0);
            assertEquals(
                    "S108411768.001",
                    accountInquiryItemDTO.getInvoiceNumber(),
                    "Expected invoice to equal mock data"
            );
            assertEquals("Open", accountInquiryItemDTO.getStatus(), "Expected status to equal mock data");
            assertEquals("475615", accountInquiryItemDTO.getCustomerPo(), "Expected customer po to equal mock data");
            assertEquals("Over120", accountInquiryItemDTO.getAge(), "Expected customer po to equal mock data");
            assertEquals(
                    "05/08/2020",
                    accountInquiryItemDTO.getInvoiceDate(),
                    "Expected invoice date to equal mock data"
            );
            assertEquals(-0.94, accountInquiryItemDTO.getOpenBalance(), "Expected open balance to equal mock data");
            assertEquals(1683.19, accountInquiryItemDTO.getOriginalAmt(), "Expected original amt to equal mock data");
        });
    }

    @Test
    public void getAccountInquiry_eclipseError() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountInquiryResponseFailure())
                );

        assertThrows(EclipseException.class, () -> eclipseService.getAccountInquiry("555"));
    }

    @Test
    public void getSalesOrders_success() throws URISyntaxException {
        // Setup
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.MassSalesInquiryResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            MassSalesOrderResponseDTO response = eclipseService.getSalesOrders("555", "1/1/2001", "1/3/2005", 0);
            assertNotNull(response, "Expected response to be non-null");

            assertNotNull(response.getSalesOrders(), "Expected sales orders returned to be non-null");
            assertEquals(1, response.getSalesOrders().size(), "Expected one sales order to return");
            GetOrderResponseDTO salesOrderDTO = response.getSalesOrders().get(0);
            assertEquals("S110436695", salesOrderDTO.getOrderNumber(), "Expected order number to equal mock data");
        });
    }

    @Test
    public void getSalesOrders_eclipseError() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.MassSalesInquiryResponseFailure())
                );

        assertThrows(EclipseException.class, () -> eclipseService.getSalesOrders("555", "1/1/2001", "1/3/2005", 0));
    }

    @Test
    public void getQuotebyQuoteId() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            GetOrderResponseDTO dto = eclipseService.getQuoteById(TEST_ACCOUNT_ID, "123");
            assertEquals("S108210380", dto.getOrderNumber());
            assertEquals("I", dto.getOrderStatus());
            assertEquals("SPC MECHANICAL CORPORATION", dto.getBillToName());
            DecimalFormat df = new DecimalFormat("#.###");
            assertEquals("60.682", df.format(dto.getLineItems().get(0).getUnitPrice()));
        });
    }

    @Test
    public void getQuoteById_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseFailure())
                );
        try {
            eclipseService.getQuoteById("error", "error");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Insufficient access."));
        }
    }

    @Test
    public void uploadTaxDocument_success() throws URISyntaxException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DocumentImagingFile documentImagingFile = new DocumentImagingFile();
        String mockId = "mockId";
        documentImagingFile.setId(mockId);
        documentImagingFile.setFileInformation(new DocumentImagingFileInformation());

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/DocumentImagingFiles/")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(documentImagingFile))
                );

        assertDoesNotThrow(() -> {
            DocumentImagingFileDTO dto = eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, "123");
            assertEquals(dto.getId(), mockId);
        });
    }

    @Test
    public void uploadTaxDocument_invalidId() throws URISyntaxException {
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/DocumentImagingFiles/")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("Invalid Id"));

        assertThrows(InvalidIdException.class, () -> eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, "123"));
    }

    @Test
    public void uploadTaxDocument_defaultException() throws URISyntaxException {
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/DocumentImagingFiles/")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(HttpClientErrorException.class, () -> eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, "123"));
    }

    @Test
    public void getCreditCardInfo_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.GetCreditCardList())
                );

        assertDoesNotThrow(() -> {
            CreditCardListResponseDTO cardListResponseDTO = eclipseService.getCreditCardList(TEST_ACCOUNT_ID);

            assertEquals("Other", cardListResponseDTO.getCreditCardList().getCreditCard().get(0).getCreditCardType());
        });
    }

    @Test
    public void getCreditCardInfo_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(""));

        assertThrows(NoSuchElementException.class, () -> eclipseService.getCreditCardList(TEST_ACCOUNT_ID));
    }

    @Test
    public void creditCardSetUpURL_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.CreditCardSetupURLSuccess())
                );

        assertDoesNotThrow(() -> {
            ElementAccountSetupDataDTO setupDataDTO = new ElementAccountSetupDataDTO();
            setupDataDTO.setCardHolder("Test User");
            setupDataDTO.setStreetAddress("123 Test Drive");
            setupDataDTO.setPostalCode("76227");
            setupDataDTO.setReturnUrl("http://testURL");
            ElementAccountSetupResponseDTO responseDTO = eclipseService.getCreditCardSetupUrl(
                    TEST_ACCOUNT_ID,
                    setupDataDTO
            );

            assertEquals(
                    "https://certtransaction.hostedpayments.com/?TransactionSetupID=83B5AA85-0E57-4439-A564-AFE933C1DB1F",
                    responseDTO.getElementSetupUrl()
            );
        });
    }

    @Test
    public void creditCardSetUpURL_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.CreditCardSetupURLFailure())
                );
        ElementAccountSetupDataDTO setupDataDTO = new ElementAccountSetupDataDTO();
        setupDataDTO.setCardHolder("");
        setupDataDTO.setStreetAddress("123 Test Drive");
        setupDataDTO.setPostalCode("76227");
        setupDataDTO.setReturnUrl("http://testURL");

        assertThrows(EclipseException.class, () -> eclipseService.getCreditCardSetupUrl(TEST_ACCOUNT_ID, setupDataDTO));
    }

    @Test
    public void getCreditCardElementInfoL_success() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.GetCreditCardElementInfoSuccess())
                );
        assertDoesNotThrow(() -> {
            ElementSetupQueryResponseDTO responseDTO = eclipseService.getCreditCardElementInfo(
                    TEST_ACCOUNT_ID,
                    "1234646"
            );
            assertEquals("01/31/2023", responseDTO.getCreditCard().getExpirationDate().getDate());
        });
    }

    @Test
    public void getCreditCardElementInfoL_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.GetCreditCardElementInfoFailure())
                );

        assertThrows(EclipseException.class, () -> eclipseService.getCreditCardElementInfo(TEST_ACCOUNT_ID, "1234646"));
    }

    @Test
    public void updateEntityInquiryForCreditCard_failure() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(""));

        EntityUpdateSubmitRequestDTO submitRequestDTO = mock(EntityUpdateSubmitRequestDTO.class);

        assertThrows(
                NoSuchElementException.class,
                () -> eclipseService.updateEntityInquiryForCreditCard(TEST_ACCOUNT_ID, submitRequestDTO)
        );
    }

    @Test
    public void verifySerialNumber_success() throws URISyntaxException {
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(
                                new URI(
                                        "http://ewitest.morsco.com/WarehouseTasks/SerialNumbers?warehouseId=S~T00B05~S112228790.1.1.0~I"
                                )
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.loadResponseJsonString("serial-numbers-response.json"))
                );

        assertDoesNotThrow(() -> {
            var results = eclipseService.getSerialNumbers(WAREHOUSE_ID);
            assertEquals(results.getResults().get(0).getWarehouseId(), WAREHOUSE_ID);
        });
    }

    @Test
    public void getPickingTasks_success() throws Exception {
        var mockResponse = new PickingTasksResponseDTO();
        ObjectMapper mapper = new ObjectMapper();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(
                                new URI(
                                        "http://ewitest.morsco.com/WarehouseTasks/PickTasks?branchId=1&userId=test&orderType=SaleOrder"
                                )
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );

        assertDoesNotThrow(() -> {
            eclipseService.getPickingTasks("1", "test", OrderMode.SALEORDER);
        });
    }

    @Test
    public void getPickingTasks_failure_eclipseToken() throws Exception {
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(
                EclipseTokenException.class,
                () -> eclipseService.getPickingTasks("1", "test", OrderMode.SALEORDER)
        );
    }

    @Test
    public void assignPickingTasks_success() throws Exception {
        var mockRequest = new WarehousePickTaskList();
        var mockResponse = new WarehousePickTaskList();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/PickTasks")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );

        assertDoesNotThrow(() -> {
            eclipseService.assignPickingTasks(mockRequest);
        });
    }

    @Test
    public void assignPickingTasks_failure_eclipseToken() throws URISyntaxException {
        var mockRequest = new WarehousePickTaskList();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));
        assertThrows(EclipseTokenException.class, () -> eclipseService.assignPickingTasks(mockRequest));
    }

    @Test
    public void getUserPicks_success() throws URISyntaxException, JsonProcessingException {
        var mockResponse = new WarehouseTaskUserPicksDTO();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.manyTimes(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.manyTimes(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/UserPick?branchId=test&userId=test"))
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );
        assertDoesNotThrow(() -> {
            eclipseService.getUserPicks("test", "test");
        });
    }

    @Test
    public void getUserPicks_failure_eclipseToken() throws URISyntaxException {
        mockServerJSON
                .expect(ExpectedCount.manyTimes(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));
        assertThrows(EclipseTokenException.class, () -> eclipseService.getUserPicks("test", "test"));
    }

    @Test
    public void completeUserPick_success() throws URISyntaxException, JsonProcessingException {
        var mockRequest = new WarehousePickComplete();
        var mockResponse = new WarehousePickComplete();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/UserPick/test")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );
        assertDoesNotThrow(() -> {
            eclipseService.completeUserPick("test", mockRequest);
        });
    }

    @Test
    public void verifySerialNumber_invalid() throws URISyntaxException {
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/SerialNumbers?warehouseId=test"))
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("This is not a serialized product")
                );
        assertThrows(InvalidSerializedProductException.class, () -> eclipseService.getSerialNumbers("test"));
    }

    @Test
    public void completeUserPick_failure_eclipseToken() throws URISyntaxException {
        var mockRequest = new WarehousePickComplete();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));
        assertThrows(EclipseTokenException.class, () -> eclipseService.completeUserPick("test", mockRequest));
    }

    @Test
    public void updateToteTask_success() throws URISyntaxException, JsonProcessingException {
        var mockRequest = new WarehouseToteTask();
        mockRequest.setTote("test");
        var mockResponse = new WarehouseToteTask();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/ToteTask/test")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );
        assertDoesNotThrow(() -> {
            eclipseService.updateToteTask(mockRequest);
        });
    }

    @Test
    public void updateToteTask_failure_token() throws URISyntaxException {
        var mockRequest = new WarehouseToteTask();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));
        assertThrows(EclipseTokenException.class, () -> eclipseService.updateToteTask(mockRequest));
    }

    @Test
    public void updateTotePackages_success() throws URISyntaxException, JsonProcessingException {
        var mockRequest = new WarehouseTotePackages();
        mockRequest.setTote("test");
        var mockResponse = new WarehouseTotePackages();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/TotePackages/test"))
                )
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(mockResponse))
                );
        assertDoesNotThrow(() -> {
            eclipseService.updateTotePackages(mockRequest);
        });
    }

    @Test
    public void updateTotePackages_failure_token() throws URISyntaxException {
        var mockRequest = new WarehouseTotePackages();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));
        assertThrows(EclipseTokenException.class, () -> eclipseService.updateTotePackages(mockRequest));
    }

    @Test
    public void completeUserPick_failure_invalidPick() throws URISyntaxException {
        var mockRequest = new WarehousePickComplete();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/UserPick/test")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Pick Not Found.")
                );
        assertThrows(PickNotFoundException.class, () -> eclipseService.completeUserPick("test", mockRequest));
    }

    @Test
    public void completeUserPick_failure_invalidTote() throws URISyntaxException {
        var mockRequest = new WarehousePickComplete();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/UserPick/test")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Tote"));
        assertThrows(InvalidToteException.class, () -> eclipseService.completeUserPick("test", mockRequest));
    }

    @Test
    public void updateProductSerialNumbers_success() throws URISyntaxException {
        var mockRequestBody = new WarehouseSerialNumbers();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/SerialNumbers/S~T00B05~S112228790.1.1.0~I"))
                )
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.loadResponseJsonString("serial-numbers-response.json"))
                );

        assertDoesNotThrow(() -> {
            eclipseService.updateSerialNumbers(WAREHOUSE_ID, mockRequestBody);
        });
    }

    @Test
    public void updateProductSerialNumbers_fail() throws URISyntaxException {
        var mockRequestBody = new WarehouseSerialNumbers();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/SerialNumbers/S~T00B05~S112228790.1.1.0~I"))
                )
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(Exception.class, () -> eclipseService.updateSerialNumbers(WAREHOUSE_ID, mockRequestBody));
    }

    @Test
    public void updateProductSerialNumbers_invalid() throws URISyntaxException {
        var mockRequestBody = new WarehouseSerialNumbers();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/SerialNumbers/S~T00B05~S112228790.1.1.0~I"))
                )
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("This is not a serialized product")
                );

        assertThrows(
                InvalidSerializedProductException.class,
                () -> eclipseService.updateSerialNumbers(WAREHOUSE_ID, mockRequestBody)
        );
    }

    @Test
    public void closeTask_success() throws URISyntaxException {
        var mockRequestBody = new WarehouseCloseTask();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/CloseTask")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));

        assertDoesNotThrow(() -> {
            eclipseService.closeTask(mockRequestBody);
        });
    }

    @Test
    public void closeTask_error() throws URISyntaxException {
        var mockRequestBody = new WarehouseCloseTask();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/CloseTask")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("This is not a serialized product")
                );

        assertThrows(
                Exception.class,
                () -> {
                    eclipseService.closeTask(mockRequestBody);
                }
        );
    }

    @Test
    public void getCustomerSearch_success() throws URISyntaxException {
        var mockRequestBody = new CustomerSearchRequestDTO();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(
                                new URI("http://ewitest.morsco.com/Customers?includeTotalItems=true&startIndex=1&pageSize=25")
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));

        assertDoesNotThrow(() -> {
            eclipseService.getCustomerSearch(mockRequestBody);
        });
    }

    @Test
    public void getCustomerSearch_error() throws URISyntaxException {
        var mockRequestBody = new CustomerSearchRequestDTO();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(
                                new URI("http://ewitest.morsco.com/Customers?includeTotalItems=true&startIndex=1&pageSize=25")
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(
                Exception.class,
                () -> {
                    eclipseService.getCustomerSearch(mockRequestBody);
                }
        );
    }

    @Test
    public void getCustomerById_success() throws URISyntaxException {
        var customerId = "123456";

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Customers/" + customerId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.loadResponseJsonString("get-customer-success.json"))
                );

        assertDoesNotThrow(() -> {
            eclipseService.getCustomerById(customerId);
        });
    }

    @Test
    public void getCustomerById_error() throws URISyntaxException {
        var customerId = "123456";

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Customers/" + customerId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(
                Exception.class,
                () -> {
                    eclipseService.getCustomerById(customerId);
                }
        );
    }

    @Test
    public void getTerritoryById_success() throws URISyntaxException {
        String territoryId = "MSCNTXOK";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Territories/" + territoryId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.loadResponseJsonString("get-territory-success-response.json"))
                );

        assertDoesNotThrow(() -> {
            Territory territory = eclipseService.getTerritoryById(territoryId);
            assertTrue(territory.getBranchList().size() > 0);
        });
    }

    @Test
    public void getTerritoryById_Failure() throws URISyntaxException {
        String territoryId = "MSCNTXOK";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Territories/" + territoryId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.loadResponseJsonString("get-territory-success-response.json"))
                );

        assertThrows(Exception.class, () -> eclipseService.getTerritoryById(territoryId));
    }

    @Test
    public void getTerritoryById_failure_eclipseToken() throws Exception {
        String territoryId = "MSCNTXOK";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(""));

        assertThrows(EclipseTokenException.class, () -> eclipseService.getTerritoryById(territoryId));
    }

    @Test
    public void getQuotebyByAccountId() throws Exception {
        String accountId = "123";
        String orderPostStartDate = "12/09/2022";
        String orderPostEndDate = "12/09/2022";
        Integer page = 123;
        Security security = new Security();

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        MassSalesOrderResponse massSalesOrderResponse = new MassSalesOrderResponse();
        ResponseEntity<MassSalesOrderResponse> massResponse = new ResponseEntity<>(
                massSalesOrderResponse,
                HttpStatus.ACCEPTED
        );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.MassSalesInquiryResponseSuccess())
                );
        assertDoesNotThrow(() -> {
            MassSalesOrderResponseDTO dto = eclipseService.getQuotesByAccountId(
                    accountId,
                    orderPostStartDate,
                    orderPostEndDate,
                    page
            );
            assertNotNull(dto);
        });
    }

    @Test
    public void getQuoteBy_AccountId() throws URISyntaxException {
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.MassSalesInquiryResponseFailure())
                );
        try {
            eclipseService.getQuotesByAccountId("error", "error", "error", -1);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Insufficient access."));
        }
    }

    @Test
    public void getProducts() throws URISyntaxException {
        List<String> productIds = new ArrayList<>();
        ErpUserInformationDTO erpUserInformationDTO = new ErpUserInformationDTO();

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseSuccess())
                );

        assertDoesNotThrow(() -> {
            ProductResponse productResponse = eclipseService.getProducts(productIds, erpUserInformationDTO, true);
            assertNotNull(productResponse);
        });
    }

    @Test
    public void getProducts_Failure() throws Exception {
        List<String> productIds = new ArrayList<>();
        ErpUserInformationDTO erpUserInformationDTO = new ErpUserInformationDTO();

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ProductResponseFailure())
                );

        assertThrows(EclipseException.class, () -> eclipseService.getProducts(productIds, erpUserInformationDTO, true));
    }

    @Test
    public void createContact_failure1() {
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<ContactNewSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.ContactNewSubmitResponseFailure())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<EntityInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess4())
                );

        assertThrows(EclipseException.class, () -> eclipseService.createContact("123", CreateContactRequest()));
    }

    @Test
    public void deleteContact_success() throws Exception {
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        String msg = "Successfully deleted";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));
        eclipseService.deleteContact("123");
    }

    @Test
    public void deleteContact_failure() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        String msg = "Invalid Id";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(msg))
                );

        assertThrows(InvalidIdException.class, () -> eclipseService.deleteContact("123"));
    }

    @Test
    public void deleteContact_Error() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        String msg = "Internal server error";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(msg))
                );
        assertThrows(HttpClientErrorException.class, () -> eclipseService.deleteContact("123"));
    }

    @Test
    public void getContact__success() throws Exception {
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        eclipseService.getContact("123");
    }

    @Test
    public void getContact__failure() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        String msg = "Invalid Id";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(msg))
                );
        assertThrows(InvalidIdException.class, () -> eclipseService.getContact("123"));
    }

    @Test
    public void getContact__error() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String userId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        String msg = "Internal server error";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(deleteUserUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(msg))
                );
        assertThrows(HttpClientErrorException.class, () -> eclipseService.getContact("123"));
    }

    @Test
    public void getEnitity_Search() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String accountId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String getEnitityUrl = String.format(eclipseApiEndpoint + "/EntitySearch?id=" + accountId);

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(getEnitityUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));

        eclipseService.searchEntity(accountId);
    }

    @Test
    public void updateSalesOrderInternalNotes__success() throws Exception {
        String orderId = "123";
        String notes = "Update order";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String srUrl = String.format(
                eclipseApiEndpoint + "/SalesOrders/%s/InternalNotes?generationId=1&copyToAll=true",
                orderId
        );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(srUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));
        eclipseService.updateSalesOrderInternalNotes(orderId, notes);
    }

    @Test
    public void updateSalesOrderInternalNotes__Failure() throws Exception {
        String orderId = "123";
        String notes = "Update order";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String srUrl = String.format(
                eclipseApiEndpoint + "/SalesOrders/%s/InternalNotes?generationId=1&copyToAll=true",
                orderId
        );
        String msg = "Invalid Id";
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(srUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThrows(
                HttpClientErrorException.class,
                () -> eclipseService.updateSalesOrderInternalNotes(orderId, notes)
        );
    }

    @Test
    public void rejectQuote_success() throws Exception {
        RejectQuoteDTO rejectQuoteDTO = new RejectQuoteDTO();
        rejectQuoteDTO.setQuoteId("123");
        String orderId = "123";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String srUrl = String.format(
                eclipseApiEndpoint + "/SalesOrders/%s/InternalNotes?generationId=1&copyToAll=true",
                orderId
        );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(srUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));
        eclipseService.rejectQuote(rejectQuoteDTO);
    }

    @Test
    public void getPreviouslyPurchasedProducts_Failure() throws Exception {
        List<String> productIds = new ArrayList<>();
        ErpUserInformationDTO erpUserInformationDTO = new ErpUserInformationDTO();
        String accountId = "123";
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );
        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        assertThrows(NoSuchElementException.class, () -> eclipseService.getPreviouslyPurchasedProducts("123", 1, 10));
    }

    @Test
    public void approveQuote_failure() throws Exception {
        ApproveQuoteDTO approveQuoteDTO = new ApproveQuoteDTO();
        approveQuoteDTO.setQuoteId("123");
        approveQuoteDTO.setShipToEntityId("123");
        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SalesOrderResponseSuccess())
                );

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.SubmitOrderPreviewResponse(OrderStatusEnum.CALL_WHEN_COMPLETE))
                );
        assertThrows(Exception.class, () -> eclipseService.approveQuote(approveQuoteDTO));
    }

    @Test
    void deleteCreditCard() throws Exception {
        String accountId = "2222222";
        String creditCardId = "123";
        mockServer
                .expect(ExpectedCount.manyTimes(), new XMLRequestMatcher("<LoginSubmit>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ValidLogin())
                );

        mockServer
                .expect(ExpectedCount.once(), new XMLRequestMatcher("<EntityInquiry>"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_XML)
                                .body(TestConstants.AccountResponseSuccess4())
                );
        assertThrows(Exception.class, () -> eclipseService.deleteCreditCard(accountId, creditCardId));
    }

    @Test
    public void getProductImageUrl_Sucess() throws URISyntaxException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String productId = "mockId";
        String url = "ImageUrl";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String urlTemplate = UriComponentsBuilder
                .fromHttpUrl(eclipseApiEndpoint)
                .path("Products/")
                .path(productId)
                .path("/ImageUrl")
                .queryParam("thumbnail", true)
                .queryParam("keyword", true)
                .encode()
                .toUriString();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(String.format(urlTemplate)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(url))
                );

        assertDoesNotThrow(() -> {
            var response = eclipseService.getProductImageUrl(productId);
            assertNotNull(response);
        });
    }

    @Test
    public void getProductImageUrl_Failure() throws URISyntaxException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String productId = "mockId";
        String url = "ImageUrl";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String urlTemplate = UriComponentsBuilder
                .fromHttpUrl(eclipseApiEndpoint)
                .path("Products/")
                .path(productId)
                .path("/ImageUrl")
                .queryParam("thumbnail", true)
                .queryParam("keyword", true)
                .encode()
                .toUriString();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(String.format(urlTemplate)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(ProductImageUrlNotFoundException.class, () -> eclipseService.getProductImageUrl(productId));
    }

    @Test
    public void getProductImageUrl_Error() throws URISyntaxException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String productId = "mockId";
        String url = "ImageUrl";
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String urlTemplate = UriComponentsBuilder
                .fromHttpUrl(eclipseApiEndpoint)
                .path("Products/")
                .path(productId)
                .path("/ImageUrl")
                .queryParam("thumbnail", true)
                .queryParam("keyword", true)
                .encode()
                .toUriString();

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(String.format(urlTemplate)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpServerErrorException.class, () -> eclipseService.getProductImageUrl(productId));
    }

    @Test
    void getProductSearch() throws Exception {
        ProductSearchRequestDTO request = new ProductSearchRequestDTO();
        request.setSearchTerm("20");
        request.setCurrentPage(0);
        request.setPageSize(8);
        request.setSearchInputType(1);
        String eclipseApiEndpoint = "http://ewitest.morsco.com";
        String searchUri = String.format("%s%s", eclipseApiEndpoint, "/Products");

        ObjectMapper mapper = new ObjectMapper();
        String productId = "mockId";
        String url = "ImageUrl";

        String urlTemplate = UriComponentsBuilder
                .fromHttpUrl(eclipseApiEndpoint)
                .path("Products/")
                .path(productId)
                .path("/ImageUrl")
                .queryParam("thumbnail", true)
                .queryParam("keyword", true)
                .encode()
                .toUriString();

        ProductSearchResponseDTO productSearchResponseDTO = new ProductSearchResponseDTO();
        ProductSearchResult productSearchResult = new ProductSearchResult();
        List<ProductSearchResult> listProductSearchResult = new ArrayList();
        listProductSearchResult.add(productSearchResult);
        productSearchResponseDTO.setResults(listProductSearchResult);

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(
                                new URI(
                                        "http://ewitest.morsco.com/Products?searchMode=2&keyword=20&startIndex=1&pageSize=8&includeTotalItems=true"
                                )
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(productSearchResponseDTO))
                );

        mockServerJSON
                .expect(ExpectedCount.twice(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(String.format(urlTemplate)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(url))
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/Products/0/ImageUrl?thumbnail=true&keyword=true"))
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(url))
                );

        assertDoesNotThrow(() -> {
            var response = eclipseService.getProductSearch(request);
            assertNotNull(response);
        });
    }

    @Test
    void sendRequest_success() throws URISyntaxException {
        List<String> productIds = new ArrayList<>();
        productIds.add("12345");
        productIds.add("67890");
        ProductRequest productRequest = new ProductRequest(productIds, new Security());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_XML);

        mockServer
                .expect(ExpectedCount.manyTimes(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(TestConstants.ProductResponse())
                );

        assertDoesNotThrow(() -> eclipseService.sendRequest(productRequest, ProductResponse.class));
    }

    @Test
    void sendRequest_nocontent() throws URISyntaxException {
        List<String> productIds = new ArrayList<>();
        productIds.add("12345");
        productIds.add("67890");
        ProductRequest productRequest = new ProductRequest(productIds, new Security());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_XML);
        mockServer
                .expect(ExpectedCount.manyTimes(), requestTo(new URI("http://ewitest.morsco.com")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_XML).body(""));

        assertDoesNotThrow(() -> eclipseService.sendRequest(productRequest, ProductResponse.class));
    }

    @Test
    public void updateTotePackages_failure_InvalidInvoiceException()
            throws URISyntaxException, JsonProcessingException {
        var mockRequest = new WarehouseTotePackages();
        mockRequest.setTote("test");
        var mockResponse = new WarehouseTotePackages();
        ObjectMapper mapper = new ObjectMapper();
        mockServerJSON
                .expect(ExpectedCount.once(), requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestConstants.ValidSessionToken())
                );

        mockServerJSON
                .expect(
                        ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/WarehouseTasks/TotePackages/test"))
                )
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Invoice number provided is not correct.")
                );
        assertThrows(InvalidInvoiceException.class, () -> eclipseService.updateTotePackages(mockRequest));
    }
}