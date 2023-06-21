package com.reece.platform.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.notifications.exception.SalesforceMarketingCloudException;
import com.reece.platform.notifications.model.DTO.*;
import com.reece.platform.notifications.notificationservice.NotificationServiceApplication;
import com.reece.platform.notifications.service.SalesforceMarketingCloudService;
import com.reece.platform.notifications.service.SimpleEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = NotificationServiceApplication.class)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
class NotificationsControllerTest {

    private NotificationsController notificationsController;

    @MockBean
    private SalesforceMarketingCloudService salesforceMarketingCloudService;

    @MockBean
    private SimpleEmailService simpleEmailService;

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_NOTIFICATION_URL = "/notifications/";
    private static final String SUCCESS_STRING = "Success";
    private static final String ERROR_STRING = "Error";
    private final ResponseEntity<String> successResponseEntity = new ResponseEntity<>(SUCCESS_STRING, HttpStatus.OK);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        notificationsController = new NotificationsController(simpleEmailService, salesforceMarketingCloudService);
    }

    @Test
    void welcomeUser_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(salesforceMarketingCloudService.sendWelcomeUserEmail(userDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "welcome-user")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void approveUser_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(salesforceMarketingCloudService.sendApproveUserEmail(userDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "approve-user")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void newUserAwaitingApproval_success() throws Exception {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        when(salesforceMarketingCloudService.sendNewUserAwaitingApprovalNotification(newCustomerNotificationDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "new-user-awaiting-approval")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newCustomerNotificationDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void newCustomerBranchManager_success() throws Exception {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        when(salesforceMarketingCloudService.sendNewCustomerBranchManagerEmail(newCustomerNotificationDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "new-customer-branch-manager")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newCustomerNotificationDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void rejectUser_success() throws Exception {
        RejectedUserDTO rejectedUserDTO = new RejectedUserDTO();
        when(salesforceMarketingCloudService.sendRejectedUserEmail(rejectedUserDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "reject-user")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rejectedUserDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void inviteUser_success() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        when(salesforceMarketingCloudService.sendInviteUserEmail(inviteUserDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "invite-user")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inviteUserDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void employeeEmailActivate_success() throws Exception {
        EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO();
        when(salesforceMarketingCloudService.sendEmployeeActivationEmail(employeeVerificationDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "employee-email-activate")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employeeVerificationDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void submitOrder_success() throws Exception {
        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        when(salesforceMarketingCloudService.sendSubmitOrderEmail(submitOrderDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "submit-order")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(submitOrderDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void customerDeleted_success() throws Exception {
        CustomerAccountDeletedDTO customerAccountDeletedDTO = new CustomerAccountDeletedDTO();
        Boolean userLeftCompany = true;
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("user-left-company", userLeftCompany.toString());
        when(salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(customerAccountDeletedDTO, userLeftCompany)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "customer-deleted").params(requestParams)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customerAccountDeletedDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void updatedLogin_success() throws Exception {
        AccountUserLoginUpdatedDTO accountUserLoginUpdatedDTO = new AccountUserLoginUpdatedDTO();
        when(salesforceMarketingCloudService.sendUserNotificationEmailForUpdatedLogin(accountUserLoginUpdatedDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "updated-login")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountUserLoginUpdatedDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void orderStatusUpdate_success() throws Exception {
        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        when(salesforceMarketingCloudService.sendOrderStatusUpdateEmail(submitOrderDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "order-status-update")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(submitOrderDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void requestOrderApproval_success() throws Exception {
        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        when(salesforceMarketingCloudService.sendRequestOrderApprovalEmail(submitOrderDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "request-order-approval")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(submitOrderDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void orderApproved_success() throws Exception {
        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        when(salesforceMarketingCloudService.sendOrderApprovedEmail(submitOrderDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "order-approved")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(submitOrderDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void orderRejected_success() throws Exception {
        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        when(salesforceMarketingCloudService.sendOrderRejectedEmail(submitOrderDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "order-rejected")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(submitOrderDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void sendDeliveryOptionMissingFromBranchEmail_success() throws Exception {
        DeliveryMethodErrorDTO deliveryMethodErrorDTO = new DeliveryMethodErrorDTO();
        when(salesforceMarketingCloudService.sendDeliveryOptionMissingFromBranchEmail(deliveryMethodErrorDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "delivery-option-error")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(deliveryMethodErrorDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void sendContactForm_success() throws Exception {
        ContactFormDTO contactFormDTO = new ContactFormDTO();
        when(simpleEmailService.sendContactForm(contactFormDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "contact-form")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contactFormDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }

    @Test
    void handleIOException() throws Exception {
        ContactFormDTO contactFormDTO = new ContactFormDTO();
        IOException ioException = new IOException(ERROR_STRING);
        when(simpleEmailService.sendContactForm(contactFormDTO)).thenThrow(ioException);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "contact-form")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contactFormDTO))).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value(), "Expected status code from service to be 400 BAD REQUEST");
        assertEquals(result.getResponse().getContentAsString(), ERROR_STRING, "Expected response body to contain error string from thrown exception");
    }

    @Test
    void handleEmailFailException() throws Exception {
        DeliveryMethodErrorDTO deliveryMethodErrorDTO = new DeliveryMethodErrorDTO();
        SalesforceMarketingCloudException salesforceMarketingCloudException = new SalesforceMarketingCloudException(ERROR_STRING);
        when(salesforceMarketingCloudService.sendDeliveryOptionMissingFromBranchEmail(deliveryMethodErrorDTO)).thenThrow(salesforceMarketingCloudException);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "delivery-option-error")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(deliveryMethodErrorDTO))).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Expected status code from service to be 500 INTERNAL SERVER ERROR");
        assertTrue(result.getResponse().getContentAsString().contains(ERROR_STRING), "Expected response body to contain error string from thrown exception");
    }

    @Test
    void newUserRegistration_success() throws Exception {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        when(salesforceMarketingCloudService.sendNewUserRegistrationNotification(newCustomerNotificationDTO)).thenReturn(successResponseEntity);
        MvcResult result = mockMvc.perform(post(BASE_NOTIFICATION_URL + "new-user-registration")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newCustomerNotificationDTO))).andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value(), "Expected status code from service to be 200 OK");
        assertEquals(result.getResponse().getContentAsString(), SUCCESS_STRING, "Expected response body to contain success string from mocked service call");
    }
}