package com.reece.platform.products.external.mincron;

import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.external.mincron.model.ContractHeader;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MincronServiceClient {

    private static final int MINCRON_MAX_ROWS_RETURNED = 10_000;
    public static final String SQL_STATE_CONNECTION_FAILURE_CODE = "08S01";
    public static final int MAX_RETRIES = 4;

    private final RestTemplate restTemplate;

    public MincronServiceClient(
        RestTemplateBuilder restTemplateBuilder,
        @Value("${mincron_service_url}") String mincronServiceUrl
    ) {
        restTemplate = restTemplateBuilder.rootUri(mincronServiceUrl).build();
    }

    public Optional<ContractHeader> getContractHeader(String contractNumber) {
        try {
            val response = restTemplate.getForEntity(
                "/contracts/contract-header?contractNumber={contractNumber}",
                ContractHeader.class,
                contractNumber
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error returned from MincronService#getContractHeader", e);
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return Optional.empty();
            }

            throw new MincronException(e.getMessage());
        }
    }

    public List<ProductLineItem> getContractItemList(String accountId, String contractNumber) {
        return getContractItemList(accountId, contractNumber, "", 1, MINCRON_MAX_ROWS_RETURNED);
    }

    public List<ProductLineItem> getContractItemList(
        String accountId,
        String contractNumber,
        String itemNumber,
        int startRow,
        int maxRows
    ) {
        val responseType = new ParameterizedTypeReference<List<ProductLineItem>>() {};
        try {
            val response = restTemplate.exchange(
                "/contracts/contract-item-list?accountId={accountId}&contractNumber={contractNumber}&itemNumber={itemNumber}&startRow={startRow}&maxRows={maxRows}",
                HttpMethod.GET,
                null,
                responseType,
                accountId,
                contractNumber,
                itemNumber,
                startRow,
                maxRows
            );

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error returned from MincronService#getContractItemList", e);
            throw new MincronException(e.getMessage());
        }
    }

    public Optional<OrderHeader> getOrderHeader(String orderNumber, String orderType) {
        Exception sqlException = null;
        ResponseEntity<OrderHeader> response = null;
        for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
            try {
                response =
                    restTemplate.getForEntity(
                        "/orders/order-header?orderType={orderType}&orderNumber={orderNumber}",
                        OrderHeader.class,
                        orderType,
                        orderNumber
                    );
                break;
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Error returned from MincronService#getOrderHeader", e);
                if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                    return Optional.empty();
                } else if (e.getMessage().contains(SQL_STATE_CONNECTION_FAILURE_CODE)) {
                    sqlException = e;
                    continue;
                } else {
                    throw new MincronException(e.getMessage());
                }
            }
        }
        if (response == null) throw new MincronException(sqlException.getMessage());
        return Optional.ofNullable(response.getBody());
    }

    public List<ProductLineItem> getOrderItemList(String accountId, String orderType, String orderNumber) {
        return getOrderItemList(accountId, orderType, orderNumber, "Y", 1, MINCRON_MAX_ROWS_RETURNED);
    }

    public List<ProductLineItem> getOrderItemList(
        String accountId,
        String orderType,
        String orderNumber,
        String wCStdOrder,
        int startRow,
        int maxRows
    ) {
        Exception sqlException = null;
        List<ProductLineItem> response = null;
        for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
            try {
                val responseType = new ParameterizedTypeReference<List<ProductLineItem>>() {};
                response =
                    restTemplate
                        .exchange(
                            "/orders/order-item-list?accountId={accountId}&orderType={orderType}&orderNumber={orderNumber}&WCStdOrder={WCStdOrder}&startRow={startRow}&maxRows={maxRows}",
                            HttpMethod.GET,
                            null,
                            responseType,
                            accountId,
                            orderType,
                            orderNumber,
                            wCStdOrder,
                            startRow,
                            maxRows
                        )
                        .getBody();
                break;
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Error returned from MincronService#getOrderItemList", e);
                if (e.getMessage().contains(SQL_STATE_CONNECTION_FAILURE_CODE)) {
                    sqlException = e;
                    continue;
                } else {
                    throw new MincronException(e.getMessage());
                }
            }
        }
        if (response == null) throw new MincronException(sqlException.getMessage());
        return response;
    }
}
