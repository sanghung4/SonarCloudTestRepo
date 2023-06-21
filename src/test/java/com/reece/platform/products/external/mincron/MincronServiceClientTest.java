package com.reece.platform.products.external.mincron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.external.mincron.model.ContractHeader;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest(MincronServiceClient.class)
public class MincronServiceClientTest {

    private static final TypeReference<List<ProductLineItem>> ITEM_LIST_TYPE_REF = new TypeReference<>() {};
    public static final String SQL_STATE_CONNECTION_FAILURE_CODE = "08S01";

    @Autowired
    private MincronServiceClient mincronServiceClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    @Test
    public void getContractHeader_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("get-contract-header-success-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "get-contract-header-success-response.json",
            ContractHeader.class
        );

        val testContractNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetContractHeaderRequestUrl(testContractNumber)))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        mincronServiceClient
            .getContractHeader(testContractNumber)
            .ifPresentOrElse(
                response -> {
                    assertEquals(expectedResponse, response);
                },
                Assertions::fail
            );
    }

    @Test
    public void getContractHeader_notFound() {
        val testContractNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetContractHeaderRequestUrl(testContractNumber)))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        val response = mincronServiceClient.getContractHeader(testContractNumber);
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void getContractHeader_exception() {
        val testContractNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetContractHeaderRequestUrl(testContractNumber)))
            .andRespond(withServerError());

        assertThrows(
            MincronException.class,
            () -> {
                mincronServiceClient.getContractHeader(testContractNumber);
            }
        );
    }

    @Test
    public void getContractItemList_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("get-contract-item-list-success-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "get-contract-item-list-success-response.json",
            ITEM_LIST_TYPE_REF
        );

        val testAccountId = "12345";
        val testContractNumber = "0987654";

        mockRestServiceServer
            .expect(requestTo(buildGetContractItemListRequestUrl(testAccountId, testContractNumber)))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = mincronServiceClient.getContractItemList(testAccountId, testContractNumber);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getContractItemList_exception() {
        val testAccountId = "12345";
        val testContractNumber = "0987654";

        mockRestServiceServer
            .expect(requestTo(buildGetContractItemListRequestUrl(testAccountId, testContractNumber)))
            .andRespond(withServerError());

        assertThrows(
            MincronException.class,
            () -> {
                mincronServiceClient.getContractItemList(testAccountId, testContractNumber);
            }
        );
    }

    @Test
    public void getOrderHeader_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("get-order-header-success-response.json");
        val expectedResponse = TestUtils.loadResponseJson("get-order-header-success-response.json", OrderHeader.class);

        val testOrderNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetOrderHeaderRequestUrl(testOrderNumber, "order")))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        mincronServiceClient
            .getOrderHeader(testOrderNumber, "order")
            .ifPresentOrElse(
                response -> {
                    assertEquals(expectedResponse.getOrderNumber(), response.getOrderNumber());
                    assertEquals(expectedResponse.getOrderStatus(), response.getOrderStatus());
                    assertEquals(expectedResponse.isDelivery(), response.isDelivery());
                },
                Assertions::fail
            );
    }

    @Test
    public void getOrderHeader_notFound() {
        val testOrderNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetOrderHeaderRequestUrl(testOrderNumber, "order")))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        val response = mincronServiceClient.getOrderHeader(testOrderNumber, "order");
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void getOrderHeader_exception() {
        val testOrderNumber = "abc123";

        mockRestServiceServer
            .expect(requestTo(buildGetOrderHeaderRequestUrl(testOrderNumber, "order")))
            .andRespond(withServerError());

        assertThrows(
            MincronException.class,
            () -> {
                mincronServiceClient.getOrderHeader(testOrderNumber, "order");
            }
        );
    }

    @Test
    public void getOrderItemList_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("get-order-item-list-success-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "get-order-item-list-success-response.json",
            ITEM_LIST_TYPE_REF
        );

        val testAccountId = "12345";
        val testOrderNumber = "0987654";

        mockRestServiceServer
            .expect(requestTo(buildGetOrderItemListRequestUrl(testAccountId, "order", testOrderNumber)))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = mincronServiceClient.getOrderItemList(testAccountId, "order", testOrderNumber);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getOrderItemList_exception() {
        val testAccountId = "12345";
        val testOrderNumber = "0987654";

        mockRestServiceServer
            .expect(requestTo(buildGetOrderItemListRequestUrl(testAccountId, "order", testOrderNumber)))
            .andRespond(withServerError());

        assertThrows(
            MincronException.class,
            () -> {
                mincronServiceClient.getOrderItemList(testAccountId, "order", testOrderNumber);
            }
        );
    }

    @Test
    public void getOrderHeader_retryUpToFourTimesWhenSQLStateConnectionFailure() throws Exception {
        val testOrderNumber = "abc123";

        mockRestServiceServer
            .expect(ExpectedCount.times(4), requestTo(buildGetOrderHeaderRequestUrl(testOrderNumber, "order")))
            .andRespond(response -> {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, SQL_STATE_CONNECTION_FAILURE_CODE);
            });

        assertThrows(MincronException.class, () -> mincronServiceClient.getOrderHeader(testOrderNumber, "order"));

        mockRestServiceServer.verify();
    }

    @Test
    public void getOrderItemList_retryUpToFourTimesWhenSQLStateConnectionFailure() throws Exception {
        val testOrderNumber = "abc123";
        val testAccountId = "12345";

        mockRestServiceServer
            .expect(
                ExpectedCount.times(4),
                requestTo(buildGetOrderItemListRequestUrl(testAccountId, testOrderNumber, "order"))
            )
            .andRespond(response -> {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, SQL_STATE_CONNECTION_FAILURE_CODE);
            });

        assertThrows(
            MincronException.class,
            () -> mincronServiceClient.getOrderItemList(testAccountId, testOrderNumber, "order")
        );

        mockRestServiceServer.verify();
    }

    private String buildGetContractHeaderRequestUrl(String contractNumber) {
        return UriComponentsBuilder
            .fromPath("/contracts/contract-header")
            .query("contractNumber={contractNumber}")
            .buildAndExpand(contractNumber)
            .toUriString();
    }

    private String buildGetContractItemListRequestUrl(String accountId, String contractNumber) {
        return UriComponentsBuilder
            .fromPath("/contracts/contract-item-list")
            .query("accountId={accountId}&contractNumber={contractNumber}&itemNumber=&startRow=1&maxRows=10000")
            .buildAndExpand(accountId, contractNumber)
            .toUriString();
    }

    private String buildGetOrderHeaderRequestUrl(String orderNumber, String orderType) {
        return UriComponentsBuilder
            .fromPath("/orders/order-header")
            .query("orderType={orderType}&orderNumber={orderNumber}")
            .buildAndExpand(orderType, orderNumber)
            .toUriString();
    }

    private String buildGetOrderItemListRequestUrl(String erpAccountId, String orderType, String orderNumber) {
        return UriComponentsBuilder
            .fromPath("/orders/order-item-list")
            .query(
                "accountId={accountId}&orderType={orderType}&orderNumber={orderNumber}&WCStdOrder=Y&startRow=1&maxRows=10000"
            )
            .buildAndExpand(erpAccountId, orderType, orderNumber)
            .toUriString();
    }
}
