package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.PriceAndAvailability;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.CustomerProductInfoService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerProductInfoController.class)
public class CustomerProductInfoControllerTest {

    private static final TypeReference<ArrayList<PriceAndAvailability>> RESPONSE_TYPE_REF = new TypeReference<>() {};

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerProductInfoService customerProductInfoService;

    @MockBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    private String testAuthHeader;
    private UUID testUserId;
    private boolean testIsEmployee;
    private String testCurrentShipTo;
    private String testErpUserId;
    private String testErpPassword;
    private String testErpAccountId;
    private String testErpName;
    private String testErpSystemName;
    private List<String> testProductNumbers;
    private ArrayList<PriceAndAvailability> testResponse;

    @BeforeEach
    public void setUp() {
        testAuthHeader = TestUtils.randomAuthHeader();
        testUserId = UUID.randomUUID();
        testIsEmployee = TestUtils.randomBoolean();
        testCurrentShipTo = UUID.randomUUID().toString();

        val testErpUserInfo = TestUtils.randomErpUserInformation();

        testErpUserId = testErpUserInfo.getUserId();
        testErpPassword = testErpUserInfo.getPassword();
        testErpAccountId = testErpUserInfo.getErpAccountId();
        testErpName = testErpUserInfo.getName();
        testErpSystemName = testErpUserInfo.getErpSystemName();

        testProductNumbers = List.of("32058", "32049");
        testResponse = TestUtils.loadResponseJson("pricing-and-availability-response.json", RESPONSE_TYPE_REF);
    }

    @Test
    public void testGetPricingAndAvailability() throws Exception {
        when(authorizationService.getUserIdFromToken(testAuthHeader)).thenReturn(testUserId);
        when(authorizationService.userIsEmployee(testAuthHeader)).thenReturn(testIsEmployee);

        when(
            customerProductInfoService.getPriceAndAvailability(
                eq(testProductNumbers),
                any(ErpUserInformation.class),
                eq(testIsEmployee),
                eq(UUID.fromString(testCurrentShipTo)),
                eq(testUserId)
            )
        )
            .thenReturn(testResponse);

        val result = mockMvc
            .perform(
                get("/customer-product-info")
                    .queryParam("partNumbers", String.join(",", testProductNumbers))
                    .queryParam("shipToId", testCurrentShipTo)
                    .header("Authorization", testAuthHeader)
                    .header("X-Erp-User-Id", testErpUserId)
                    .header("X-Erp-Password", testErpPassword)
                    .header("X-Erp-Account-Id", testErpAccountId)
                    .header("X-Erp-User-Name", testErpName)
                    .header("X-Erp-System-Name", testErpSystemName)
            )
            .andExpect(status().isOk())
            .andReturn();

        val responseString = result.getResponse().getContentAsString();
        val response = objectMapper.readValue(responseString, RESPONSE_TYPE_REF);

        assertEquals(testResponse, response);
    }

    @Test
    public void testGetPricingAndAvailability_unauthorized() throws Exception {
        mockMvc
            .perform(
                get("/customer-product-info")
                    .queryParam("partNumbers", String.join(",", testProductNumbers))
                    .queryParam("shipToId", testCurrentShipTo)
                    .header("X-Erp-User-Id", testErpUserId)
                    .header("X-Erp-Password", testErpPassword)
                    .header("X-Erp-Account-Id", testErpAccountId)
                    .header("X-Erp-User-Name", testErpName)
                    .header("X-Erp-System-Name", testErpSystemName)
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetPricingAndAvailability_missingHeaders() throws Exception {
        when(authorizationService.getUserIdFromToken(testAuthHeader)).thenReturn(testUserId);
        when(authorizationService.userIsEmployee(testAuthHeader)).thenReturn(testIsEmployee);

        mockMvc
            .perform(
                get("/customer-product-info")
                    .queryParam("partNumbers", String.join(",", testProductNumbers))
                    .header("Authorization", testAuthHeader)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPricingAndAvailability_notFound() throws Exception {
        when(authorizationService.getUserIdFromToken(testAuthHeader)).thenReturn(testUserId);
        when(authorizationService.userIsEmployee(testAuthHeader)).thenReturn(testIsEmployee);

        when(
            customerProductInfoService.getPriceAndAvailability(
                eq(testProductNumbers),
                any(ErpUserInformation.class),
                eq(testIsEmployee),
                eq(UUID.fromString(testCurrentShipTo)),
                eq(testUserId)
            )
        )
            .thenThrow(
                new EclipseException("Error response from Eclipse: Part Not Found: 32058.", HttpStatus.NOT_FOUND)
            );

        mockMvc
            .perform(
                get("/customer-product-info")
                    .queryParam("partNumbers", String.join(",", testProductNumbers))
                    .queryParam("shipToId", testCurrentShipTo)
                    .header("Authorization", testAuthHeader)
                    .header("X-Erp-User-Id", testErpUserId)
                    .header("X-Erp-Password", testErpPassword)
                    .header("X-Erp-Account-Id", testErpAccountId)
                    .header("X-Erp-User-Name", testErpName)
                    .header("X-Erp-System-Name", testErpSystemName)
            )
            .andExpect(status().isNotFound());
    }
}
