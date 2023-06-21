package com.reece.platform.notifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reece.platform.notifications.exception.SalesforceMarketingCloudException;
import com.reece.platform.notifications.model.DTO.*;
import com.reece.platform.notifications.model.DataExtensions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesforceMarketingCloudService {
    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${sfmcAuthUrl}")
    private String sfmcAuthUrl;

    @Value("${sfmcRestUrl}")
    private String sfmcRestUrl;

    @Value("${creditDepartmentEmail}")
    private String creditDepartmentEmail;

    @Value("${portalUrl}")
    private String portalUrl;

    @Value("${environment}")
    private String environmentName;

    @Value("${accountId}")
    private String accountId;

    @Value("${grantType}")
    private String grantType;

    @Value("${supportEmails}")
    private String[] supportEmails;

    private final RestTemplate restTemplate;

    // Salesforce email template definition keys
    private static final String welcomeUserDK = "Welcome_Email";
    private static final String approvedUserDK = "Approved_User";
    private static final String invitedUserDK = "Invited_User";
    private static final String verifyEmailDK = "Verify_Email";
    private static final String rejectedUserDK = "Rejected_User";
    private static final String accountDeletedDK = "Account_Deleted";
    private static final String accountDeletedAdminDK = "Account_Deleted_Notify_Admins";
    private static final String accountDeletedBranchDK = "Account_Deleted_Notify_Branch";
    private static final String accountDeletedCreditDK = "Account_Deleted_Notify_Credit";
    private static final String accountUserLoginUpdatedDK = "Account_User_Login_Updated";
    private static final String orderApprovalRequestDK = "Order_Approval_Request";
    private static final String orderRejectedDK = "Order_Rejected";
    private static final String orderApprovedDK = "Order_Approved";
    private static final String orderStatusUpdateDK = "Order_Status_Update";
    private static final String orderSubmittedDK = "Order_Submitted";
    private static final String deliveryOptionErrorDK = "Delivery_Option_Error";
    private static final String accountRequestDK = "Account_Request";
    @Autowired
    public SalesforceMarketingCloudService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendWelcomeUserEmail(UserDTO newUser) throws SalesforceMarketingCloudException, JsonProcessingException {
        ApprovedUserDE userDE = new ApprovedUserDE(newUser);
        String definitionKey = getEnvironmentSpecificDefinitionKey(welcomeUserDK);
        return sendEmail(definitionKey, Collections.singletonList(userDE), newUser.getBrand(), newUser.getDomain());
    }

    public ResponseEntity<String> sendApproveUserEmail(UserDTO approvedUser) throws SalesforceMarketingCloudException, JsonProcessingException {
        ApprovedUserDE approvedUserDE = new ApprovedUserDE(approvedUser);
        String definitionKey = getEnvironmentSpecificDefinitionKey(approvedUserDK);
        return sendEmail(definitionKey, Collections.singletonList(approvedUserDE), approvedUser.getBrand(), approvedUser.getDomain());
    }

    public ResponseEntity<String> sendInviteUserEmail(InviteUserDTO invitedUser) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(invitedUserDK);
        InvitedUserDE invitedUserDE = new InvitedUserDE(invitedUser, invitedUser.getDomain());
        return sendEmail(definitionKey, Collections.singletonList(invitedUserDE), invitedUser.getBrand(), invitedUser.getDomain());
    }

    public ResponseEntity<String> sendEmployeeActivationEmail(EmployeeVerificationDTO employeeVerification) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(verifyEmailDK);
        employeeVerification.generateInviteLink(portalUrl, employeeVerification.getDomain());
        EmployeeVerificationDE employeeVerificationDE = new EmployeeVerificationDE(employeeVerification);
        return sendEmail(definitionKey, Collections.singletonList(employeeVerificationDE), employeeVerification.getBrand(), employeeVerification.getBaseUrl(portalUrl));
    }

    public ResponseEntity<String> sendRejectedUserEmail(RejectedUserDTO rejectedUser) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(rejectedUserDK);
        RejectedUserDE rejectedUserDE = new RejectedUserDE(rejectedUser);
        return sendEmail(definitionKey, Collections.singletonList(rejectedUserDE), rejectedUser.getBrand(), rejectedUser.getDomain());
    }

    public ResponseEntity<String> sendCustomerAccountDeletedEmail(CustomerAccountDeletedDTO customerAccountDeletedDTO, Boolean userLeftCompany) throws SalesforceMarketingCloudException, JsonProcessingException {
        CustomerAccountDeletedDE customerAccountDeletedDE = new CustomerAccountDeletedDE(customerAccountDeletedDTO);
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountDeletedDK);
        String brand = customerAccountDeletedDTO.getBrand();
        String domain = customerAccountDeletedDTO.getDomain();
        if (userLeftCompany) {
            if (customerAccountDeletedDTO.getAdminEmails() != null && !customerAccountDeletedDTO.getAdminEmails().isEmpty()) {
                sendAdminNotificationEmailForDeletedCustomer(customerAccountDeletedDTO.getAdminEmails(), customerAccountDeletedDE, brand, domain);
            }
            if (customerAccountDeletedDTO.getBranchManagerEmail() != null) {
                sendBranchManagerNotificationEmailForDeletedCustomer(customerAccountDeletedDTO.getBranchManagerEmail(), customerAccountDeletedDE, brand, domain);
            }
            sendCreditDepartmentNotificationEmailForDeletedCustomer(customerAccountDeletedDE, brand, domain);
        }

        return sendEmail(definitionKey, Collections.singletonList(customerAccountDeletedDE), brand, domain);
    }

    public void sendAdminNotificationEmailForDeletedCustomer(List<String> adminEmails, CustomerAccountDeletedDE customerAccountDeletedDE, String brand, String domain) throws SalesforceMarketingCloudException, JsonProcessingException {
        List<CustomerAccountDeletedNotificationDE> adminEmailDEs =
                adminEmails
                        .stream()
                        .map((email) -> new CustomerAccountDeletedNotificationDE(email, customerAccountDeletedDE))
                        .collect(Collectors.toList());
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountDeletedAdminDK);
        sendEmail(definitionKey, adminEmailDEs, brand, domain);
    }

    public void sendBranchManagerNotificationEmailForDeletedCustomer(String branchManagerEmail, CustomerAccountDeletedDE customerAccountDeletedDE, String brand, String domain) throws SalesforceMarketingCloudException, JsonProcessingException {
        CustomerAccountDeletedNotificationDE branchManagerEmailDE = new CustomerAccountDeletedNotificationDE(branchManagerEmail, customerAccountDeletedDE);
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountDeletedBranchDK);
        sendEmail(definitionKey, Collections.singletonList(branchManagerEmailDE), brand, domain);
    }

    public void sendCreditDepartmentNotificationEmailForDeletedCustomer(CustomerAccountDeletedDE customerAccountDeletedDE, String brand, String domain) throws SalesforceMarketingCloudException, JsonProcessingException {
        CustomerAccountDeletedNotificationDE creditDepartmentEmailDE = new CustomerAccountDeletedNotificationDE(creditDepartmentEmail, customerAccountDeletedDE);
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountDeletedCreditDK);
        sendEmail(definitionKey, Collections.singletonList(creditDepartmentEmailDE), brand, domain);
    }

    public ResponseEntity<String> sendUserNotificationEmailForUpdatedLogin(AccountUserLoginUpdatedDTO accountUserLoginUpdatedDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountUserLoginUpdatedDK);
        String supportEmail = accountUserLoginUpdatedDTO.getSupportEmail();

        List<AccountUserLoginUpdatedDE>  accountUpdateDE = accountUserLoginUpdatedDTO.getEmails().stream()
                .map((email) -> new AccountUserLoginUpdatedDE(email, supportEmail, accountUserLoginUpdatedDTO.getFirstName(), accountUserLoginUpdatedDTO.getLastName() ))
                .collect(Collectors.toList());

        return sendEmail(definitionKey, accountUpdateDE, accountUserLoginUpdatedDTO.getBrand(), accountUserLoginUpdatedDTO.getDomain());
    }

    public ResponseEntity<String> sendRequestOrderApprovalEmail(SubmitOrderDTO submitOrderDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(orderApprovalRequestDK);
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();
        for (String email: submitOrderDTO.getEmails()) {
            OrderInfoDE requestOrderApprovalDE = new OrderInfoDE(submitOrderDTO, email);
            ResponseEntity<String> sendEmailResponse = sendEmail(definitionKey, Collections.singletonList(requestOrderApprovalDE), submitOrderDTO.getBrand(), submitOrderDTO.getDomain());
            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    public ResponseEntity<String> sendOrderRejectedEmail(SubmitOrderDTO submitOrderDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(orderRejectedDK);
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();
        for (String email: submitOrderDTO.getEmails()) {
            OrderInfoDE requestOrderApprovalDE = new OrderInfoDE(submitOrderDTO, email);
            ResponseEntity<String> sendEmailResponse = sendEmail(definitionKey, Collections.singletonList(requestOrderApprovalDE), submitOrderDTO.getBrand(), submitOrderDTO.getDomain());
            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    public ResponseEntity<String> sendOrderApprovedEmail(SubmitOrderDTO submitOrderDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(orderApprovedDK);
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();
        for (String email: submitOrderDTO.getEmails()) {
            OrderInfoDE requestOrderApprovalDE = new OrderInfoDE(submitOrderDTO, email);
            ResponseEntity<String> sendEmailResponse = sendEmail(definitionKey, Collections.singletonList(requestOrderApprovalDE), submitOrderDTO.getBrand(), submitOrderDTO.getDomain());
            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    public ResponseEntity<String> sendOrderStatusUpdateEmail(SubmitOrderDTO submitOrderDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(orderStatusUpdateDK);
        OrderInfoDE orderStatusUpdateDE = new OrderInfoDE(submitOrderDTO, submitOrderDTO.getEmails().get(0));
        return sendEmail(definitionKey, Collections.singletonList(orderStatusUpdateDE), submitOrderDTO.getBrand(), submitOrderDTO.getDomain());
    }

    public ResponseEntity<String> sendSubmitOrderEmail(SubmitOrderDTO submitOrderDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(orderSubmittedDK);
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();
        for (String email: submitOrderDTO.getEmails()) {
            OrderInfoDE orderStatusUpdateDE = new OrderInfoDE(submitOrderDTO, email);
            ResponseEntity<String> sendEmailResponse =  sendEmail(definitionKey, Collections.singletonList(orderStatusUpdateDE), submitOrderDTO.getBrand(), submitOrderDTO.getDomain());
            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    public ResponseEntity<String> sendDeliveryOptionMissingFromBranchEmail(
            DeliveryMethodErrorDTO deliveryMethodErrorDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException {

        String definitionKey = getEnvironmentSpecificDefinitionKey(deliveryOptionErrorDK);
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();

        for (String supportEmail: supportEmails) {

            DeliveryMethodErrorDE deliveryMethodErrorDE =
                    new DeliveryMethodErrorDE(deliveryMethodErrorDTO, supportEmail);

            ResponseEntity<String> sendEmailResponse =
                    sendEmail(
                            definitionKey,
                            Collections.singletonList(deliveryMethodErrorDE),
                            "Reece",
                            "morrisonSupply");
                            /* MAX-766: hardcoded brand/domain above since this email is only being sent to
                                        support and getting branch info would've required call to branches-service */

            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    public ResponseEntity<String> sendNewCustomerBranchManagerEmail(NewCustomerNotificationDTO newCustomerNotificationDTO) throws SalesforceMarketingCloudException, JsonProcessingException {
        String definitionKey = getEnvironmentSpecificDefinitionKey(accountRequestDK);
        AccountRequestDE accountRequestDE = new AccountRequestDE(newCustomerNotificationDTO);
        return sendEmail(definitionKey, Collections.singletonList(accountRequestDE), newCustomerNotificationDTO.getBrand(), newCustomerNotificationDTO.getDomain());
    }

    public ResponseEntity<String> sendNewUserAwaitingApprovalNotification(NewCustomerNotificationDTO newCustomerNotificationDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException {

        String definitionKey = getEnvironmentSpecificDefinitionKey(accountRequestDK);
        AccountRequestDE accountRequestDE = new AccountRequestDE(newCustomerNotificationDTO);

        sendEmail(
                definitionKey,
                Collections.singletonList(accountRequestDE),
                newCustomerNotificationDTO.getBrand(),
                newCustomerNotificationDTO.getDomain()
        );

        List<UserDTO> accountAdmins = newCustomerNotificationDTO.getAccountAdmins();
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();

        for (UserDTO userDTO : accountAdmins) {
            newCustomerNotificationDTO.setEmail(userDTO.getEmail());
            newCustomerNotificationDTO.setManagerFirstName(userDTO.getFirstName());

            ResponseEntity<String> sendEmailResponse = sendEmail(definitionKey, Collections.singletonList(
                    new AccountRequestDE(newCustomerNotificationDTO)),
                    newCustomerNotificationDTO.getBrand(),
                    newCustomerNotificationDTO.getDomain());

            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    /**
     * https://developer.salesforce.com/docs/atlas.en-us.mc-apis.meta/mc-apis/sendMessageMultipleRecipients.htm
     * TODO: Docs recommend no more than 500 recipients in a single request. Unlikely scenario but potential optimization would be to batch requests per 500 dataExtension objects
     * @param definitionKey
     * @param dataExtensions
     * @return
     * @throws SalesforceMarketingCloudException
     * @throws JsonProcessingException
     */
    private ResponseEntity<String> sendEmail(String definitionKey, List<? extends BaseDataExtension> dataExtensions, String brand, String domain) throws SalesforceMarketingCloudException, JsonProcessingException {
        HttpHeaders headers = getAuthHeaders();
        String body = buildRequestBody(definitionKey, dataExtensions, brand, domain);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> res = restTemplate.postForEntity(String.format("%s/messaging/v1/email/messages", sfmcRestUrl), request, String.class);
        if (res.getStatusCode() == HttpStatus.ACCEPTED || res.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>("Email sent successfully!", res.getStatusCode());
        } else {
            JsonNode resNode = new ObjectMapper().readTree(res.getBody());
            throw new SalesforceMarketingCloudException(resNode.get("message").asText());
        }
    }

    private HttpHeaders getAuthHeaders() throws SalesforceMarketingCloudException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode body = mapper.createObjectNode();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("account_id", accountId);
        body.put("grant_type", grantType);
        ResponseEntity<String> res = restTemplate.postForEntity(String.format("%s/v2/token", sfmcAuthUrl), body, String.class);
        if (res.getStatusCode() != HttpStatus.OK) {
            JsonNode resNode = mapper.readTree(res.getBody());
            throw new SalesforceMarketingCloudException(resNode.get("error").asText());
        }
        JsonNode responseNode = mapper.readTree(res.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(responseNode.get("access_token").asText());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     *
     * @param definitionKey Salesforce Marketing Cloud key of pre-defined Message Send Definition in Salesforce Marketing Cloud
     * @param dataExtensions list of data extensions for each email recipient
     * @return
     */
    private String buildRequestBody(String definitionKey, List<? extends BaseDataExtension> dataExtensions, String brand, String domain) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode body = mapper.createObjectNode();
        ArrayNode recipients = mapper.createArrayNode();
        ObjectNode attr = mapper.createObjectNode();
        attr.put("FromName", String.format("%s Support", brand != null ? brand : "Reece"));
        attr.put("Domain", domain);
        attr.put("Brand", brand);

        dataExtensions.forEach((dataExtension) -> {
            ObjectNode recipient = mapper.createObjectNode();
            ObjectNode attributes = mapper.createObjectNode();
            dataExtension.getAttr().forEach(attributes::put);
            recipient.put("contactKey", dataExtension.getTo());
            recipient.put("to", dataExtension.getTo());
            recipient.set("attributes", attributes);
            recipient.put("messageKey", String.valueOf(UUID.randomUUID()));
            recipients.add(recipient);
        });

        body.put("definitionKey", definitionKey);
        body.set("recipients", recipients);
        body.set("attributes", attr);
        return body.toString();
    }

    /**
     * Covert the price into comma separated digits with two decimal places. This ensures that prices is displayed as 4.10 instead of 4.1.
     * @param price
     * @return the same price as String with comma separated digits having two decimal places.
     */
    public static String checkPriceFormat(String price) {
        try{
            return  String.format(
                    "%,.2f",
                    BigDecimal.valueOf(Double.parseDouble(price)).setScale(2, RoundingMode.HALF_UP).doubleValue()
            );
        }catch(Exception e){
            return price;
        }
    }

    public ResponseEntity<String> sendNewUserRegistrationNotification(NewCustomerNotificationDTO newCustomerNotificationDTO)
            throws SalesforceMarketingCloudException, JsonProcessingException {

        String definitionKey = getEnvironmentSpecificDefinitionKey("Account_Created_Notify");
        AccountRequestDE accountRequestDE = new AccountRequestDE(newCustomerNotificationDTO);
        ResponseEntity<String> sendEmailResponse = sendEmail(
                definitionKey,
                Collections.singletonList(accountRequestDE),
                newCustomerNotificationDTO.getBrand(),
                newCustomerNotificationDTO.getDomain()
        );

        List<UserDTO> accountAdmins = newCustomerNotificationDTO.getAccountAdmins();
        List<ResponseEntity<String>> sendEmailResponses = new ArrayList<>();
        sendEmailResponses.add(sendEmailResponse);

        for (UserDTO userDTO : accountAdmins) {
            newCustomerNotificationDTO.setEmail(userDTO.getEmail());
            newCustomerNotificationDTO.setManagerFirstName(userDTO.getFirstName());
            sendEmailResponse = sendEmail(definitionKey, Collections.singletonList(
                            new AccountRequestDE(newCustomerNotificationDTO)),
                    newCustomerNotificationDTO.getBrand(),
                    newCustomerNotificationDTO.getDomain());

            sendEmailResponses.add(sendEmailResponse);
        }
        return new ResponseEntity<>("Email(s) sent successfully!", sendEmailResponses.get(0).getStatusCode());
    }

    /**
     * Appends "_dev" to definition keys for non-production environments to ensure that appropriate SFMC
     * email is triggered.
     * @param definitionKey key that identifies which email in SFMC to trigger
     * @return environment specific definition key
     */
    private String getEnvironmentSpecificDefinitionKey(String definitionKey) {
        if(environmentName.equals("prod") || environmentName.equals("sandbox")){
            return definitionKey;
        } else {
            return  definitionKey + "_dev";
        }
    }

}
