package com.reece.platform.eclipse.service.EclipseService;

import com.reece.platform.eclipse.exceptions.EclipseException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.DTO.GetOrderResponseDTO;
import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.AccountHistoryInquiryRequest;
import com.reece.platform.eclipse.model.XML.AccountHistoryResponse.AccountHistoryItem;
import com.reece.platform.eclipse.model.XML.AccountHistoryResponse.AccountHistoryResponse;
import com.reece.platform.eclipse.model.XML.OpenOrderInquiry.OpenOrderInquiryRequest;
import com.reece.platform.eclipse.model.XML.OpenOrderResponse.OpenOrderItem;
import com.reece.platform.eclipse.model.XML.OpenOrderResponse.OpenOrderResponse;
import com.reece.platform.eclipse.model.XML.common.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncExecutionsService extends BaseEclipseService {
    public static final String CASH_RECEIPT_ID_CODE = "C";
    public static final String ECLIPSE_ERROR_CODE_NO_ORDERS_PRESENT_FOR_DATE_RANGE = "(538)";

    @Autowired
    private final EclipseSessionService eclipseSessionService;

    @Async("taskExecutor")
    public CompletableFuture<List<GetOrderResponseDTO>> getOrdersForDateRangeAsync(String accountId,
                                                                                   String postOrderStartDate,
                                                                                   String postOrderEndDate)
            throws EclipseException, ExecutionException, InterruptedException, TimeoutException, EclipseTokenException {
        AccountHistoryInquiryRequest accountHistoryInquiryRequest = new AccountHistoryInquiryRequest(
                eclipseSessionService.getRequestSecurity(accountId, null, null, true),
                accountId,
                postOrderStartDate,
                postOrderEndDate
        );
        OpenOrderInquiryRequest openOrderInquiryRequest = new OpenOrderInquiryRequest(
                accountId,
                eclipseSessionService.getRequestSecurity(accountId, null, null, true),
                postOrderStartDate,
                postOrderEndDate
        );

        CompletableFuture<AccountHistoryResponse> accountHistoryFuture = CompletableFuture.supplyAsync(() ->
                sendRequest(accountHistoryInquiryRequest, AccountHistoryResponse.class).orElseThrow()
        );

        CompletableFuture<OpenOrderResponse> openOrderFuture = CompletableFuture.supplyAsync(() ->
                sendRequest(openOrderInquiryRequest, OpenOrderResponse.class).orElseThrow()
        );

        AccountHistoryResponse accountHistoryResponse = accountHistoryFuture.get(60, TimeUnit.SECONDS);
        OpenOrderResponse openOrderResponse = openOrderFuture.get(60, TimeUnit.SECONDS);
        if (
                !accountHistoryResponse.getAccountHistoryInquiryResponse().getStatusResult().getSuccess().equals("Yes") &&
                        !openOrderResponse.getOpenOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")
        ) {
            ErrorMessage errorMessage = accountHistoryResponse
                    .getAccountHistoryInquiryResponse()
                    .getStatusResult()
                    .getErrorMessageList()
                    .getErrorMessages()
                    .get(0);
            String errorDescription = errorMessage.getDescription();
            String errorCode = StringUtils.isEmpty(errorMessage.getCode()) ? "" : "(" + errorMessage.getCode() + ")";

            // Don't bail if there is no account info for this date range
            if (errorCode.equals(ECLIPSE_ERROR_CODE_NO_ORDERS_PRESENT_FOR_DATE_RANGE)){
                return CompletableFuture.completedFuture(new ArrayList<GetOrderResponseDTO>());
            }
            throw new EclipseException(
                    errorDescription + " " + errorCode,
                    accountHistoryResponse.getAccountHistoryInquiryResponse().getStatusResult().getDescription()
            );
        }

        List<GetOrderResponseDTO> orders = new ArrayList<>();
        if (accountHistoryResponse.getAccountHistoryInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            for (AccountHistoryItem accountHistoryItem : accountHistoryResponse
                    .getAccountHistoryInquiryResponse()
                    .getAccountHistoryItemList()
                    .getAccountHistoryItems()) {
                if (!accountHistoryItem.getOrderID().startsWith(CASH_RECEIPT_ID_CODE)) {
                    orders.add(new GetOrderResponseDTO(accountHistoryItem));
                }
            }
        }
        if (openOrderResponse.getOpenOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            for (OpenOrderItem openOrderItem : openOrderResponse
                    .getOpenOrderInquiryResponse()
                    .getOpenOrderItemList()
                    .getOpenOrderItems()) {
                if (!openOrderItem.getOrderID().startsWith(CASH_RECEIPT_ID_CODE)) {
                    orders.add(new GetOrderResponseDTO(openOrderItem));
                }
            }
        }

        // Remove cancelled orders and payment generations
        return CompletableFuture.completedFuture(orders
                .stream()
                .filter(o ->
                        !o.getOrderStatus().equals("$") && !o.getOrderStatus().equals("X") && !o.getOrderStatus().equals("B")
                )
                .collect(Collectors.toList()));
    }
}
