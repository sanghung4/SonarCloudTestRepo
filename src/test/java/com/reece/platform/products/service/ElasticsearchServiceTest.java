package com.reece.platform.products.service;

import static com.reece.platform.products.testConstant.TestConstants.*;
import static com.reece.platform.products.testConstant.TestConstants.AVAILABLE_PRODUCT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.model.DTO.ProductSearchRequestDTO;
import com.reece.platform.products.model.QueryResult;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

class ElasticsearchServiceTest {

    private ElasticsearchService elasticsearchService;

    private static final float DEFAULT_ES_BOOST = 1.0f;

    private static final String ERROR_MESSAGE = "ERROR";

    @Mock
    private AccountService accountService;

    RestTemplate restTemplate = mock(RestTemplate.class);
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode productIDS = mapper.createArrayNode();
    ArrayNode errorProductIDS = mapper.createArrayNode();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        elasticsearchService = new ElasticsearchService(restTemplate, accountService);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("null");

        AVAILABLE_PRODUCT_NUMBERS
            .stream()
            .forEach(productId -> {
                productIDS.add(String.format("MSC-%s", productId));
                errorProductIDS.add(String.format("ERR-%s", productId));
            });

        String expectedElasticsearchBody = "{\"query\":\"\",\"filters\":{\"erp_product_id\":\"%s\"}}";
        String availableProductBody = String.format(expectedElasticsearchBody, AVAILABLE_PRODUCT_ID);

        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(availableProductBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String unknownProductBody = String.format(expectedElasticsearchBody, UNKNOWN_PRODUCT_ID, DEFAULT_ES_BOOST);
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(unknownProductBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(NO_PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String errorProductBody = String.format(expectedElasticsearchBody, ERROR_PRODUCT_ID, DEFAULT_ES_BOOST);
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(errorProductBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        String expectedElasticsearchMincronNumberBody =
            "{\"query\":\"\",\"filters\":{\"mincron_product_number\":\"%s\"}}";

        String availableProductByNumberBody = String.format(
            expectedElasticsearchMincronNumberBody,
            AVAILABLE_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(availableProductByNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String unknownProductByMincronNumberBody = String.format(
            expectedElasticsearchMincronNumberBody,
            UNKNOWN_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(unknownProductByMincronNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(NO_PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String errorProductByMincronNumberBody = String.format(
            expectedElasticsearchMincronNumberBody,
            ERROR_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(errorProductByMincronNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        String expectedElasticsearchEclipseNumberBody = "{\"query\":\"\",\"filters\":{\"id\":\"%s\"}}";
        String availableProductByEclipseNumberBody = String.format(
            expectedElasticsearchEclipseNumberBody,
            AVAILABLE_ECLIPSE_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(availableProductByEclipseNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String unknownProductByEclipseNumberBody = String.format(
            expectedElasticsearchEclipseNumberBody,
            UNKNOWN_ECLIPSE_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(unknownProductByEclipseNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(NO_PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String errorProductByEclipseNumberBody = String.format(
            expectedElasticsearchEclipseNumberBody,
            ERROR_ECLIPSE_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(errorProductByEclipseNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));

        when(restTemplate.postForEntity(contains("size=20&from=40"), any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        when(restTemplate.getForEntity(nullable(String.class), eq(String.class)))
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));
        var restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    void getProductById_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductById(AVAILABLE_PRODUCT_ID);
        assertEquals(
            productDto,
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product id from service to be OK"
        );
    }

    @Test
    void getProductById_not_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductById(UNKNOWN_PRODUCT_ID);
        assertEquals(
            productDto,
            NO_PRODUCT_FOUND_RESPONSE,
            "Expected status of unknown product id from service to be NOT_FOUND"
        );
    }

    @Test
    void getProductById_errorResponse() {
        ElasticsearchException exception = assertThrows(
            ElasticsearchException.class,
            () -> elasticsearchService.getProductById(ERROR_PRODUCT_ID)
        );

        assertEquals(
            exception.getHttpStatus(),
            HttpStatus.BAD_REQUEST,
            "Expected status code of exception to be BAD_REQUEST"
        );
    }

    @Test
    void getProductByMincronNumber_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductByMincronNumber(AVAILABLE_PRODUCT_NUMBER);
        assertEquals(
            productDto,
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product number from service to be OK"
        );
    }

    @Test
    void getProductByMincronNumber_not_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductByMincronNumber(UNKNOWN_PRODUCT_NUMBER);
        assertEquals(
            productDto,
            NO_PRODUCT_FOUND_RESPONSE,
            "Expected status of unknown product number from service to be NOT_FOUND"
        );
    }

    @Test
    void getProductByMincronNumber_errorResponse() {
        ElasticsearchException exception = assertThrows(
            ElasticsearchException.class,
            () -> elasticsearchService.getProductByMincronNumber(ERROR_PRODUCT_NUMBER)
        );

        assertEquals(
            exception.getHttpStatus(),
            HttpStatus.BAD_REQUEST,
            "Expected status code of exception to be BAD_REQUEST"
        );
    }

    @Test
    void getProductByEclipseNumber_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductByEclipseNumber(AVAILABLE_ECLIPSE_PRODUCT_NUMBER);
        assertEquals(
            productDto,
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product number from service to be OK"
        );
    }

    @Test
    void getProductByEclipseNumberArray_available() throws ElasticsearchException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("null");

        String expectedElasticsearchArrayBody =
            "{\"page\":{\"size\":2,\"current\":1},\"query\":\"\",\"filters\":{\"id\":[\"MSC-123\",\"MSC-123\"]}}";

        String availableProductByNumberBody = String.format(
            expectedElasticsearchArrayBody,
            AVAILABLE_PRODUCT_NUMBER,
            DEFAULT_ES_BOOST
        );
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(availableProductByNumberBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String productDto = elasticsearchService.getProductByEclipseNumberArray(productIDS);
        assertEquals(
            productDto,
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product number from service to be OK"
        );
    }

    @Test
    void getProductByEclipseNumber_not_available() throws ElasticsearchException {
        String productDto = elasticsearchService.getProductByEclipseNumber(UNKNOWN_ECLIPSE_PRODUCT_NUMBER);
        assertEquals(
            productDto,
            NO_PRODUCT_FOUND_RESPONSE,
            "Expected status of unknown product number from service to be NOT_FOUND"
        );
    }

    @Test
    void getProductByEclipseNumberArray_not_available() throws ElasticsearchException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("null");

        String expectedElasticsearchArrayBody =
            "{\"page\":{\"size\":2,\"current\":1},\"query\":\"\",\"filters\":{\"id\":[\"ERR-123\",\"ERR-123\"]}}";

        String unknownProductBody = String.format(expectedElasticsearchArrayBody, errorProductIDS, DEFAULT_ES_BOOST);
        when(
            restTemplate.postForEntity(
                nullable(String.class),
                eq(new HttpEntity<>(unknownProductBody, headers)),
                eq(String.class)
            )
        )
            .thenReturn(new ResponseEntity<>(NO_PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        String productDto = elasticsearchService.getProductByEclipseNumberArray(errorProductIDS);
        assertEquals(
            productDto,
            NO_PRODUCT_FOUND_RESPONSE,
            "Expected status of unknown product number from service to be NOT_FOUND"
        );
    }

    @Test
    void getProductByEclipseNumber_errorResponse() {
        ElasticsearchException exception = assertThrows(
            ElasticsearchException.class,
            () -> elasticsearchService.getProductByEclipseNumber(ERROR_ECLIPSE_PRODUCT_NUMBER)
        );

        assertEquals(
            exception.getHttpStatus(),
            HttpStatus.BAD_REQUEST,
            "Expected status code of exception to be BAD_REQUEST"
        );
    }

    @Test
    void getProductsByQuery() throws ElasticsearchException, UnsupportedEncodingException, JsonProcessingException {
        when(restTemplate.postForEntity(nullable(String.class), any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        ProductSearchRequestDTO request = new ProductSearchRequestDTO(
            "test",
            null,
            null,
            "",
            null,
            null,
            0,
            null,
            null,
            null,
            null,
            null
        );
        QueryResult productsResult = elasticsearchService.getProductsByQuery(request);
        assertEquals(
            productsResult.getFilteredResult(),
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product id from service to be OK"
        );
    }

    @Test
    void getProductsByQuery_pagination()
        throws ElasticsearchException, UnsupportedEncodingException, JsonProcessingException {
        when(restTemplate.postForEntity(nullable(String.class), any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(PRODUCT_FOUND_RESPONSE, HttpStatus.OK));

        ProductSearchRequestDTO request = new ProductSearchRequestDTO(
            "test",
            2,
            20,
            "",
            null,
            null,
            0,
            null,
            null,
            null,
            null,
            null
        );
        QueryResult productsResult = elasticsearchService.getProductsByQuery(request);
        assertEquals(
            productsResult.getFilteredResult(),
            PRODUCT_FOUND_RESPONSE,
            "Expected status of available product id from service to be OK"
        );
    }

    @Test
    void getProductsByQuery_errorResponse() {
        when(restTemplate.postForEntity(nullable(String.class), any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>(ERROR_MESSAGE, HttpStatus.BAD_REQUEST));
        ProductSearchRequestDTO request = new ProductSearchRequestDTO(
            "error",
            0,
            12,
            "",
            null,
            null,
            0,
            null,
            null,
            null,
            null,
            null
        );
        ElasticsearchException exception = assertThrows(
            ElasticsearchException.class,
            () -> elasticsearchService.getProductsByQuery(request)
        );

        assertEquals(
            exception.getHttpStatus(),
            HttpStatus.BAD_REQUEST,
            "Expected status code of exception to be BAD_REQUEST"
        );
    }
}
