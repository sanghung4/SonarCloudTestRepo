package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.model.DTO.AccountResponseDTO;
import com.reece.platform.products.model.DTO.FeatureDTO;
import com.reece.platform.products.model.FeaturesEnum;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class AccountServiceTest {

    private AccountService accountService;
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://account-service";

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(new RestTemplate());
        restTemplate = accountService.getRestTemplate();
        ReflectionTestUtils.setField(accountService, "accountServiceUrl", ACCOUNT_SERVICE_URL);
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }

    @Test
    void getAccountData_success() throws URISyntaxException {
        String accountId = "123";
        String authorization = "auth";
        String brand = "Reece";
        AccountResponseDTO mockAccountResponseDTO = new AccountResponseDTO();
        AccountResponseDTO[] mockedAccountResponseList = new AccountResponseDTO[] { mockAccountResponseDTO };

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://account-service/account/123?brand=Reece")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(TestUtils.jsonStringify(mockedAccountResponseList))
            );

        AccountResponseDTO accountResponseDTO = accountService.getAccountData(accountId, authorization, brand);
        assertEquals(accountResponseDTO, mockAccountResponseDTO);
    }

    @Test
    void getEcommShipToId_success() throws URISyntaxException {
        String accountId = "123";
        String erpName = "Eclipse";
        UUID mockedUUID = UUID.randomUUID();

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(new URI("http://account-service/account/123/find-ecomm-ship-to-id/Eclipse"))
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("\"" + mockedUUID.toString() + "\"")
            );

        val uuid = accountService.getEcommShipToId(accountId, erpName);
        assertEquals(mockedUUID, uuid);
    }

    @Test
    void getEcommBillToId_success() throws URISyntaxException {
        String accountId = "123";
        String erpName = "Eclipse";
        UUID mockedUUID = UUID.randomUUID();

        mockServer
            .expect(
                ExpectedCount.once(),
                requestTo(new URI("http://account-service/account/123/find-ecomm-bill-to-id/Eclipse"))
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("\"" + mockedUUID.toString() + "\"")
            );

        val uuid = accountService.getEcommBillToId(accountId, erpName);
        assertEquals(mockedUUID, uuid);
    }

    @Test
    void getFeatures_success() throws URISyntaxException {
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setName(FeaturesEnum.WATERWORKS.name());
        featureDTO.setIsEnabled(true);
        FeatureDTO[] mockedFeatureArray = new FeatureDTO[] { featureDTO };

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://account-service/features/")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(TestUtils.jsonStringify(mockedFeatureArray))
            );

        val actualFeatureList = accountService.getFeatures();
        assertEquals(actualFeatureList, Arrays.asList(mockedFeatureArray));
    }
}
