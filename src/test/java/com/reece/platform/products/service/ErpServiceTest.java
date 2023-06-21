package com.reece.platform.products.service;

import static com.reece.platform.products.external.mincron.MincronServiceClient.MAX_RETRIES;
import static com.reece.platform.products.external.mincron.MincronServiceClientTest.SQL_STATE_CONNECTION_FAILURE_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.configuration.AppHttpConfig;
import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.model.DTO.GetTerritoryResponseDTO;
import com.reece.platform.products.model.DTO.InvoiceDTO;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest(ErpService.class)
@Import(AppHttpConfig.class)
public class ErpServiceTest {

    @Autowired
    private ErpService erpService;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${eclipse_service_url}")
    private String eclipseServiceUrl;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    private ErpUserInformation testErpUserInformation;
    private String testErpUserInformationJson;
    private boolean testIsEmployee;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        testErpUserInformation = TestUtils.randomErpUserInformation();
        testErpUserInformationJson = objectMapper.writeValueAsString(testErpUserInformation);
        testIsEmployee = TestUtils.randomBoolean();
    }

    @Test
    public void testGetEclipseProductData_success() {
        val testProductNumbers = List.of("32058", "32049");
        val expectedResponseJson = TestUtils.loadResponseJsonString("eclipse-product-data-response.json");

        mockRestServiceServer
            .expect(requestTo(buildRequestUrl(testProductNumbers, testIsEmployee)))
            .andExpect(content().json(testErpUserInformationJson))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val result = erpService.getEclipseProductData(testProductNumbers, testErpUserInformation, testIsEmployee);

        val expectedResponse = TestUtils.loadResponseJson("eclipse-product-data-response.json", ProductResponse.class);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void testGetEclipseProductData_notFound() {
        val testProductNumbers = List.of("32058", "32049");

        mockRestServiceServer
            .expect(requestTo(buildRequestUrl(testProductNumbers, testIsEmployee)))
            .andExpect(content().json(testErpUserInformationJson))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).body("Error response from Eclipse: Part Not Found: 1184765."));

        assertThrows(
            EclipseException.class,
            () -> {
                erpService.getEclipseProductData(testProductNumbers, testErpUserInformation, testIsEmployee);
            }
        );
    }

    @Test
    public void testGetEclipseProductData_serverError() {
        val testProductNumbers = List.of("32058", "32049");

        mockRestServiceServer
            .expect(requestTo(buildRequestUrl(testProductNumbers, testIsEmployee)))
            .andExpect(content().json(testErpUserInformationJson))
            .andRespond(withServerError());

        assertThrows(
            EclipseException.class,
            () -> {
                erpService.getEclipseProductData(testProductNumbers, testErpUserInformation, testIsEmployee);
            }
        );
    }

    @Test
    public void testGetTerritoryById_success() {
        val territoryId = "MSCNTXOK";
        val expectedResponseJson = TestUtils.loadResponseJsonString("get-territory-success-response.json");

        mockRestServiceServer
            .expect(requestTo(buildEclipseBaseRequestUrl() + "/territories/" + territoryId))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val result = erpService.getTerritoryById(territoryId);

        val expectedResponse = TestUtils.loadResponseJson(
            "get-territory-success-response.json",
            GetTerritoryResponseDTO.class
        );
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getEclipseInvoices_success() {
        val accountId = "54878";
        val expectedResponseJson = TestUtils.loadResponseJsonString("eclipse-invoices-response.json");

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    buildEclipseBaseRequestUrl() + "/accounts/" + accountId + "/invoices" + "?accountId=" + accountId
                )
            )
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val result = erpService.getEclipseInvoices(accountId, null, null, null, null);

        assertEquals(10, result.getInvoices().size());
    }

    @Test
    public void getMincronInvoices_success() {
        val accountId = "123";
        val expectedResponseJson = TestUtils.loadResponseJsonString("mincron-invoices-response.json");

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(buildMincronBaseRequestUrl() + "/invoices/" + "?accountId=" + accountId)
            )
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val result = erpService.getMincronInvoices(accountId);

        assertEquals(89, result.size());
    }

    @Test
    public void getMincronInvoices_sqlFailure() {
        val accountId = "123";

        IOException exception = new IOException(SQL_STATE_CONNECTION_FAILURE_CODE);

        mockRestServiceServer
            .expect(
                ExpectedCount.times(MAX_RETRIES),
                requestTo(buildMincronBaseRequestUrl() + "/invoices/" + "?accountId=" + accountId)
            )
            .andRespond(withException(exception));

        assertThrows(MincronException.class, () -> erpService.getMincronInvoices(accountId));
    }

    private String buildEclipseBaseRequestUrl() {
        return UriComponentsBuilder.fromHttpUrl(eclipseServiceUrl).toUriString();
    }

    private String buildMincronBaseRequestUrl() {
        return UriComponentsBuilder.fromHttpUrl(mincronServiceUrl).toUriString();
    }

    private String buildRequestUrl(List<String> productIds, boolean isEmployee) {
        return UriComponentsBuilder
            .fromHttpUrl(eclipseServiceUrl)
            .path("/products")
            .query("productIds={productIds}&isEmployee={isEmployee}")
            .buildAndExpand(String.join(",", productIds), isEmployee)
            .toUriString();
    }
}
