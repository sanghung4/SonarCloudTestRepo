package com.reece.platform.products.service;

import com.reece.platform.products.model.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Value("${notification_service_url}")
    private String notificationServiceUrl;

    @Value("${enable_notification_calls:true}")
    private Boolean enableNotificationCalls;

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(RestTemplate rt) {
        this.restTemplate = rt;
    }

    /**
     * Send notification email for order submit
     *
     * @param salesOrderSubmitNotificationDTO order submit information for email
     */
    public void sendOrderSubmittedEmail(SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO) {
        if (enableNotificationCalls) {
            String inviteUserNotificationUrl = String.format("%s/notifications/submit-order", notificationServiceUrl);
            restTemplate.postForEntity(inviteUserNotificationUrl, salesOrderSubmitNotificationDTO, String.class);
        }
    }

    /**
     * Send notification email for order approval request
     *
     * @param orderSubmitNotificationDTO order approval request information for email
     */
    public void sendOrderApprovalRequestEmail(SalesOrderSubmitNotificationDTO orderSubmitNotificationDTO) {
        if (enableNotificationCalls) {
            String requestOrderApprovalNotificationUrl = String.format(
                "%s/notifications/request-order-approval",
                notificationServiceUrl
            );
            restTemplate.postForEntity(requestOrderApprovalNotificationUrl, orderSubmitNotificationDTO, String.class);
        }
    }

    /**
     * Send notification email for order approval request
     *
     * @param orderSubmitNotificationDTO order approve information for email
     */
    public void sendOrderApprovedEmail(SalesOrderSubmitNotificationDTO orderSubmitNotificationDTO) {
        if (enableNotificationCalls) {
            String orderApprovedNotificationUrl = String.format(
                "%s/notifications/order-approved",
                notificationServiceUrl
            );
            restTemplate.postForEntity(orderApprovedNotificationUrl, orderSubmitNotificationDTO, String.class);
        }
    }

    /**
     * Send notification email for order rejection
     *
     * @param salesOrderSubmitNotificationDTO order reject information for email
     */
    public void sendOrderRejectedEmail(SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO) {
        if (enableNotificationCalls) {
            String orderRejectedNotificationUrl = String.format(
                "%s/notifications/order-rejected",
                notificationServiceUrl
            );
            restTemplate.postForEntity(orderRejectedNotificationUrl, salesOrderSubmitNotificationDTO, String.class);
        }
    }

    /**
     * Send notification email for an order with an updated status
     *
     * @param salesOrderSubmitNotificationDTO order information for email
     */
    public void sendOrderStatusUpdateEmail(SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO) {
        if (enableNotificationCalls) {
            String orderStatusUpdateNotificationUrl = String.format(
                "%s/notifications/order-status-update",
                notificationServiceUrl
            );
            restTemplate.postForEntity(orderStatusUpdateNotificationUrl, salesOrderSubmitNotificationDTO, String.class);
        }
    }

    /**
     * Send notification email to the support team when a user is unable to checkout with
     * the the delivery or will call option
     *
     * @param deliveryMethodErrorDTO contains customer details that will be transferred to DE in notifications-service
     */
    public void sendDeliveryOptionMissingFromBranchEmail(DeliveryMethodErrorDTO deliveryMethodErrorDTO) {
        if (enableNotificationCalls) {
            String orderStatusUpdateNotificationUrl = String.format(
                "%s/notifications/delivery-option-error",
                notificationServiceUrl
            );
            restTemplate.postForEntity(orderStatusUpdateNotificationUrl, deliveryMethodErrorDTO, String.class);
        }
    }
}
