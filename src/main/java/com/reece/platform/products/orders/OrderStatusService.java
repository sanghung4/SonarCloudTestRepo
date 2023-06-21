package com.reece.platform.products.orders;

import com.reece.platform.products.exceptions.BranchNotFoundPricingAndAvailabilityException;
import com.reece.platform.products.external.eclipse.EclipseServiceClient;
import com.reece.platform.products.model.DTO.GetOrderResponseDTO;
import com.reece.platform.products.model.DTO.OrderLineItemResponseDTO;
import com.reece.platform.products.model.DTO.SalesOrderSubmitNotificationDTO;
import com.reece.platform.products.model.entity.OrderStatus;
import com.reece.platform.products.model.repository.OrderStatusDAO;
import com.reece.platform.products.orders.model.WebStatus;
import com.reece.platform.products.service.CartService;
import com.reece.platform.products.service.NotificationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatusService {

    private final EclipseServiceClient eclipseServiceClient;
    private final OrderStatusDAO orderStatusDAO;
    private final CartService cartService;
    private final NotificationService notificationService;

    public void updateAllOrderStatuses() {
        val start = Instant.now();
        log.info("Entering OrderStatusService#updateAllOrderStatuses");
        val orderStatuses = orderStatusDAO.findAll();
        val count = orderStatuses.size();
        var index = 0;

        for (val orderStatus : orderStatuses) {
            log.info("Updating {}, {} of {}", orderStatus, ++index, count);
            try {
                updateOrderStatus(orderStatus);
            } catch (Throwable e) {
                log.error("Error caught updating status for order {}", orderStatus.getOrderId(), e);
                orderStatus.setErrorMessage(e.getMessage());
                orderStatusDAO.save(orderStatus);
            }
        }

        log.info(
            "Exiting OrderStatusService#updateAllOrderStatuses, {}ms",
            Duration.between(start, Instant.now()).toMillis()
        );
    }

    /**
     * Calls Eclipse to get the details for the given {@link OrderStatus}. If the status of the order is different
     * from what we have in the {@link OrderStatus} object, and if the new status {@code isNotifyOnChange} then we
     * generate and send a notification, update the status in the object, and save that object in the database.
     *
     * If the new status is {@link WebStatus#INVOICED} then we delete the OrderStatus object from the database.
     * @param orderStatus the status record to update
     */
    @Transactional
    public void updateOrderStatus(OrderStatus orderStatus) {
        eclipseServiceClient
            .getOrder(orderStatus.getErpAccountId(), orderStatus.getOrderId(), null)
            .ifPresentOrElse(
                order -> {
                    val newOrderStatus = WebStatus.valueOf(order.getWebStatus());

                    if (newOrderStatus != orderStatus.getWebStatus() && newOrderStatus.isNotifyOnChange()) {
                        val notification = buildNotification(orderStatus.getShipToId(), order);
                        notificationService.sendOrderStatusUpdateEmail(notification);
                        orderStatus.setWebStatus(newOrderStatus);
                        orderStatus.setErrorMessage(null);
                        orderStatusDAO.save(orderStatus);
                    } else if (WebStatus.INVOICED == newOrderStatus) {
                        orderStatusDAO.delete(orderStatus);
                    } else if (orderStatus.getErrorMessage() != null) {
                        // If there is no change in status and no error, and there was an error previously, null it out.
                        orderStatus.setErrorMessage(null);
                        orderStatusDAO.save(orderStatus);
                    }
                },
                () -> {
                    log.error(
                        "No order returned from Eclipse for entity {}, order {}",
                        orderStatus.getErpAccountId(),
                        orderStatus.getOrderId()
                    );
                    orderStatus.setErrorMessage("Not found in Eclipse");
                    orderStatusDAO.save(orderStatus);
                }
            );
    }

    private SalesOrderSubmitNotificationDTO buildNotification(UUID shipToAccountId, GetOrderResponseDTO order) {
        try {
            order.setLineItems(roundPricesInLineItems(order.getLineItems()));
            return cartService.buildOrderNotificationDTO(shipToAccountId, order, null);
        } catch (BranchNotFoundPricingAndAvailabilityException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OrderLineItemResponseDTO> roundPricesInLineItems(List<OrderLineItemResponseDTO> lineItems) {
        return lineItems
            .stream()
            .map(lineItem ->
                new OrderLineItemResponseDTO(
                    roundPriceToNearestCent(lineItem.getUnitPrice()),
                    lineItem.getErpPartNumber(),
                    lineItem.getOrderQuantity(),
                    lineItem.getBackOrderedQuantity(),
                    lineItem.getShipQuantity(),
                    lineItem.getAvailableQuantity(),
                    lineItem.getUom(),
                    roundPriceToNearestCent(lineItem.getProductOrderTotal()),
                    lineItem.getImageUrls(),
                    lineItem.getManufacturerName(),
                    lineItem.getManufacturerNumber(),
                    lineItem.getProductName(),
                    lineItem.getProductId(),
                    lineItem.getStatus(),
                    lineItem.getSequenceNumber(),
                    null,
                    null,
                    null
                )
            )
            .collect(Collectors.toList());
    }

    private float roundPriceToNearestCent(float price) {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
