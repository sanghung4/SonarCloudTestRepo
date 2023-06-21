package com.reece.platform.notifications.controller;

import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.notifications.exception.SalesforceMarketingCloudException;
import com.reece.platform.notifications.model.DTO.*;
import com.reece.platform.notifications.service.SalesforceMarketingCloudService;
import com.reece.platform.notifications.service.SimpleEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller()
@RequestMapping("/notifications")
public class NotificationsController {
    private final SimpleEmailService simpleEmailService;
    private final SalesforceMarketingCloudService salesforceMarketingCloudService;
    @Autowired
    NotificationsController(
        SimpleEmailService simpleEmailService,
        SalesforceMarketingCloudService salesforceMarketingCloudService
    ) {
        this.simpleEmailService = simpleEmailService;
        this.salesforceMarketingCloudService = salesforceMarketingCloudService;
    }

    @PostMapping("welcome-user")
    public @ResponseBody ResponseEntity<String> welcomeUser(@RequestBody UserDTO newUser) throws
            SalesforceMarketingCloudException,
            JsonProcessingException {
        return salesforceMarketingCloudService.sendWelcomeUserEmail(newUser);
    }

    @PostMapping("approve-user")
    public @ResponseBody ResponseEntity<String> approveUser(@RequestBody UserDTO approvedUser) throws
            SalesforceMarketingCloudException,
            JsonProcessingException {
        return salesforceMarketingCloudService.sendApproveUserEmail(approvedUser);
    }

    /**
     * Sends email to branch manager when user submits an account request for a new bill-to account
     * @param newCustomerNotificationDTO new customer info that will be transferred to DE
     * @return a string containing response status
     * @throws SalesforceMarketingCloudException
     * @throws JsonProcessingException
     */
    @PostMapping("new-customer-branch-manager")
    public @ResponseBody ResponseEntity<String> newCustomerBranchManager(@RequestBody NewCustomerNotificationDTO newCustomerNotificationDTO) throws
            SalesforceMarketingCloudException,
            JsonProcessingException {
        return salesforceMarketingCloudService.sendNewCustomerBranchManagerEmail(newCustomerNotificationDTO);
    }

    /**
     * Sends email to admin(s) and branch manager when user on an existing account submits an account request
     * @param newCustomerNotificationDTO new user info that will be transferred to DE
     * @return a string containing response status
     * @throws SalesforceMarketingCloudException
     * @throws JsonProcessingException
     */
    @PostMapping("new-user-awaiting-approval")
    public @ResponseBody ResponseEntity<String> newUserAwaitingApproval(
            @RequestBody NewCustomerNotificationDTO newCustomerNotificationDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException {
        return salesforceMarketingCloudService.sendNewUserAwaitingApprovalNotification(newCustomerNotificationDTO);
    }

    @PostMapping("reject-user")
    public @ResponseBody ResponseEntity<String> rejectUser(@RequestBody RejectedUserDTO rejectedUser) throws
            SalesforceMarketingCloudException,
            JsonProcessingException {
        return salesforceMarketingCloudService.sendRejectedUserEmail(rejectedUser);
    }

    @PostMapping("invite-user")
    public @ResponseBody ResponseEntity<String> inviteUser(@RequestBody InviteUserDTO inviteUserDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        return salesforceMarketingCloudService.sendInviteUserEmail(inviteUserDTO);
    }

    @PostMapping("employee-email-activate")
    public @ResponseBody ResponseEntity<String> employeeEmailActivate(@RequestBody EmployeeVerificationDTO employeeVerificationDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        return salesforceMarketingCloudService.sendEmployeeActivationEmail(employeeVerificationDTO);
    }

    @PostMapping("submit-order")
    public @ResponseBody ResponseEntity<String> submitOrder(@RequestBody SubmitOrderDTO submitOrderDTO) throws
            SalesforceMarketingCloudException,
            JsonProcessingException {
        return salesforceMarketingCloudService.sendSubmitOrderEmail(submitOrderDTO);
    }

    @PostMapping("customer-deleted")
    public @ResponseBody ResponseEntity<String> customerDeleted(@RequestBody CustomerAccountDeletedDTO customerAccountDeletedDTO, @RequestParam(name = "user-left-company", defaultValue = "false", required = false) Boolean userLeftCompany)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(customerAccountDeletedDTO, userLeftCompany);
    }

    @PostMapping("updated-login")
    public @ResponseBody ResponseEntity<String> updatedLogin(@RequestBody AccountUserLoginUpdatedDTO AccountUserLoginUpdatedDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendUserNotificationEmailForUpdatedLogin(AccountUserLoginUpdatedDTO);
    }

    @PostMapping("order-status-update")
    public @ResponseBody ResponseEntity<String> orderStatusUpdate(@RequestBody SubmitOrderDTO submitOrderDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendOrderStatusUpdateEmail(submitOrderDTO);
    }

    @PostMapping("request-order-approval")
    public @ResponseBody ResponseEntity<String> requestOrderApproval(@RequestBody SubmitOrderDTO submitOrderDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendRequestOrderApprovalEmail(submitOrderDTO);
    }

    @PostMapping("order-approved")
    public @ResponseBody ResponseEntity<String> orderApproved(@RequestBody SubmitOrderDTO submitOrderDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendOrderApprovedEmail(submitOrderDTO);
    }

    @PostMapping("order-rejected")
    public @ResponseBody ResponseEntity<String> orderRejected(@RequestBody SubmitOrderDTO submitOrderDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendOrderRejectedEmail(submitOrderDTO);
    }

    @PostMapping("delivery-option-error")
    public ResponseEntity<String> sendDeliveryOptionMissingFromBranchEmail(
            @RequestBody DeliveryMethodErrorDTO deliveryMethodErrorDTO
    )
            throws SalesforceMarketingCloudException, JsonProcessingException
    {
        return salesforceMarketingCloudService.sendDeliveryOptionMissingFromBranchEmail(deliveryMethodErrorDTO);
    }

    @PostMapping("contact-form")
    public @ResponseBody ResponseEntity<String> sendContactForm(@RequestBody ContactFormDTO contactFormDTO) throws IOException {
        return simpleEmailService.sendContactForm(contactFormDTO);
    }

    @PostMapping("new-user-registration")
    public @ResponseBody ResponseEntity<String> newUserRegistration(
            @RequestBody NewCustomerNotificationDTO newCustomerNotificationDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException {
        return salesforceMarketingCloudService.sendNewUserRegistrationNotification(newCustomerNotificationDTO);
    }

}
