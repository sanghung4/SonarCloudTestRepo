package com.reece.platform.notifications.notificationservice.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reece.platform.notifications.exception.SalesforceMarketingCloudException;
import com.reece.platform.notifications.model.DTO.*;
import com.reece.platform.notifications.model.WebStatusesEnum;
import com.reece.platform.notifications.service.SalesforceMarketingCloudService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SalesforceMarketingCloudServiceTest {

    private SalesforceMarketingCloudService salesforceMarketingCloudService;
    private RestTemplate restTemplate;

    private SubmitOrderDTO submitOrderDTO;
    private OrderNotificationDTO orderNotificationDTO;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        restTemplate = mock(RestTemplate.class);
        salesforceMarketingCloudService = new SalesforceMarketingCloudService(restTemplate);
        submitOrderDTO = new SubmitOrderDTO();
        submitOrderDTO.setBranchDTO(new BranchAddressDTO());
        submitOrderDTO.setAddress(new BaseAddressDTO());
        submitOrderDTO.setProductDTOs(new ArrayList<>());
        submitOrderDTO.setTax("1.20");
        submitOrderDTO.setSubTotal("20.10");
        submitOrderDTO.setTotal("1,432.10");
        submitOrderDTO.setEmails(Arrays.asList("email"));
        submitOrderDTO.setErpSystemName("MINCRON");
        submitOrderDTO.setRejectionReason("Failed");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setUnitPrice("1.5");
        productDTO.setProductTotal("1.5");
        List<ProductDTO> listProductDTO= new ArrayList<>();
        listProductDTO.add(productDTO);
        submitOrderDTO.setProductDTOs(listProductDTO);
        submitOrderDTO.setWebStatus(WebStatusesEnum.READY_FOR_PICKUP.name());

        orderNotificationDTO = new OrderNotificationDTO();
        orderNotificationDTO.setBranchDTO(new BranchAddressDTO());
        orderNotificationDTO.setAddress(new BaseAddressDTO());
        orderNotificationDTO.setProductDTOs(new ArrayList<>());
        orderNotificationDTO.setTax(1.2f);
        orderNotificationDTO.setSubTotal(20.1f);
        orderNotificationDTO.setTotal(32.1f);
        ReflectionTestUtils.setField(salesforceMarketingCloudService, "environmentName", "dev");

        ReflectionTestUtils.setField(
                salesforceMarketingCloudService,
                "supportEmails",
                new String[]{"test@test.com", "test2@test.com"});
    }

    @Test
    void sendWelcomeUserEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        UserDTO newUser = new UserDTO();
        BranchContactInformationDTO branchContactInformationDTO = new BranchContactInformationDTO();
        branchContactInformationDTO.setSupportEmail("email");
        branchContactInformationDTO.setPhoneNumber("123123123");
        newUser.setBranchInfo(branchContactInformationDTO);
        newUser.setEmail("email");
        newUser.setFirstName("Brian");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendWelcomeUserEmail(newUser);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendWelcomeUserEmail_authFailed() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"error\":\" unauthorized\"}", HttpStatus.UNAUTHORIZED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        UserDTO newUser = new UserDTO();
        BranchContactInformationDTO branchContactInformationDTO = new BranchContactInformationDTO();
        branchContactInformationDTO.setSupportEmail("email");
        branchContactInformationDTO.setPhoneNumber("123123123");
        newUser.setBranchInfo(branchContactInformationDTO);
        newUser.setEmail("email");
        newUser.setFirstName("Brian");
        assertThrows(SalesforceMarketingCloudException.class, () -> salesforceMarketingCloudService.sendWelcomeUserEmail(newUser));
    }

    @Test
    void sendWelcomeUserEmail_emailFailed() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("{\"message\":\" Definition Key not found\"}", HttpStatus.NOT_FOUND);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        UserDTO newUser = new UserDTO();
        BranchContactInformationDTO branchContactInformationDTO = new BranchContactInformationDTO();
        branchContactInformationDTO.setSupportEmail("email");
        branchContactInformationDTO.setPhoneNumber("123123123");
        newUser.setBranchInfo(branchContactInformationDTO);
        newUser.setEmail("email");
        newUser.setFirstName("Brian");
        assertThrows(SalesforceMarketingCloudException.class, () -> salesforceMarketingCloudService.sendWelcomeUserEmail(newUser));
    }

    @Test
    void sendApprovedUserEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        UserDTO approvedUser = new UserDTO();
        BranchContactInformationDTO branchContactInformationDTO = new BranchContactInformationDTO();
        branchContactInformationDTO.setSupportEmail("email");
        branchContactInformationDTO.setPhoneNumber("123123123");
        approvedUser.setBranchInfo(branchContactInformationDTO);
        approvedUser.setEmail("email");
        approvedUser.setFirstName("Brian");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendApproveUserEmail(approvedUser);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendRejectedUserEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        RejectedUserDTO rejectedUser = new RejectedUserDTO();
        BranchContactInformationDTO branchContactInformationDTO = new BranchContactInformationDTO();
        branchContactInformationDTO.setSupportEmail("email");
        branchContactInformationDTO.setPhoneNumber("123123123");
        rejectedUser.setBranchInfo(branchContactInformationDTO);
        rejectedUser.setEmail("email");
        rejectedUser.setFirstName("Brian");
        rejectedUser.setRejectionReason("unapproved");

        ResponseEntity<String> res = salesforceMarketingCloudService.sendRejectedUserEmail(rejectedUser);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendInvitedUserEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        InviteUserDTO inviteUser = new InviteUserDTO();
        inviteUser.setEmail("email");
        inviteUser.setFirstName("Brian");
        inviteUser.setInviteId(UUID.randomUUID());

        ResponseEntity<String> res = salesforceMarketingCloudService.sendInviteUserEmail(inviteUser);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendCustomerAccountDeletedEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(new CustomerAccountDeletedDTO(), false);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendCustomerAccountDeletedEmail_authFailed() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"error\":\" unauthorized\"}", HttpStatus.UNAUTHORIZED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        assertThrows(SalesforceMarketingCloudException.class, () -> salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(new CustomerAccountDeletedDTO(), false));
    }

    @Test
    void sendCustomerAccountDeletedEmail_emailFailed() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("{\"message\":\" Definition Key not found\"}", HttpStatus.NOT_FOUND);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        assertThrows(SalesforceMarketingCloudException.class, () -> salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(new CustomerAccountDeletedDTO(), false));
    }

    @Test
    void notifyAdminsOnAccountDeletionEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        CustomerAccountDeletedDTO dto = new CustomerAccountDeletedDTO();
        dto.setAdminEmails(List.of("admin1@email.com", "admin2@email.com"));
        dto.setEmail("deleteduser@email.com");
        dto.setAccountNumber("123123");
        dto.setFirstName("Delete");
        dto.setLastName("Me");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(dto, true);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
        // should email each admin in list as well as to deleted customer and credit dept
        verify(restTemplate, times(3)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void notifyBranchOnAccountDeletionEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        CustomerAccountDeletedDTO dto = new CustomerAccountDeletedDTO();
        dto.setBranchManagerEmail("branchManager@email.com");
        dto.setEmail("deleteduser@email.com");
        dto.setAccountNumber("123123");
        dto.setFirstName("Delete");
        dto.setLastName("Me");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(dto, true);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
        // should email branch manager as well as deleted customer and credit dept
        verify(restTemplate, times(3)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void notifyCreditDeptOnAccountDeletionEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        CustomerAccountDeletedDTO dto = new CustomerAccountDeletedDTO();
        dto.setEmail("deleteduser@email.com");
        dto.setAccountNumber("123123");
        dto.setFirstName("Delete");
        dto.setLastName("Me");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendCustomerAccountDeletedEmail(dto, true);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
        // should email credit dept as well as to deleted customer
        verify(restTemplate, times(2)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void sendRequestOrderApprovalEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendRequestOrderApprovalEmail(submitOrderDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendOrderApprovedEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendOrderApprovedEmail(submitOrderDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendOrderRejectedEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendOrderRejectedEmail(submitOrderDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendNewCustomerBranchManagerEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setEmail("email");
        newCustomerNotificationDTO.setBrand("Reece");
        newCustomerNotificationDTO.setDomain("Morrison Supply");
        newCustomerNotificationDTO.setRequestEmail("request email");
        newCustomerNotificationDTO.setManagerFirstName("manager first");
        newCustomerNotificationDTO.setBillToName("bill to");
        newCustomerNotificationDTO.setRequestFirstName("request first");
        newCustomerNotificationDTO.setRequestLastName("request last");
        ResponseEntity<String> res = salesforceMarketingCloudService.sendNewCustomerBranchManagerEmail(newCustomerNotificationDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendOrderStatusUpdateEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendOrderStatusUpdateEmail(submitOrderDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email sent successfully!");
    }

    @Test
    void sendSubmitOrderEmail_success() throws Exception {
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendSubmitOrderEmail(submitOrderDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendDeliveryOptionMissingFromBranchEmail_success() throws Exception {
        DeliveryMethodErrorDTO deliveryMethodErrorDTO = new DeliveryMethodErrorDTO();
        deliveryMethodErrorDTO.setIsDelivery(true);

        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendDeliveryOptionMissingFromBranchEmail(deliveryMethodErrorDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendUserNotificationEmailForUpdatedLogin_success() throws Exception {
        AccountUserLoginUpdatedDTO aulu = new AccountUserLoginUpdatedDTO();
        aulu.setSupportEmail("support@testreece.com");
        aulu.setEmails(List.of("test1@reece.com", "test2@reece.com"));
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);
        ResponseEntity<String> res = salesforceMarketingCloudService.sendUserNotificationEmailForUpdatedLogin(aulu);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals("Email sent successfully!", res.getBody());
    }

    @Test
    void sendNewUserAwaitingApprovalNotification_success() throws Exception {

        UserDTO userDTO_1 = new UserDTO();
        UserDTO userDTO_2 = new UserDTO();
        userDTO_1.setEmail("email_1");
        userDTO_1.setFirstName("name");
        userDTO_2.setEmail("email_2");
        userDTO_2.setFirstName("name");

        List<UserDTO> accountAdmins = Arrays.asList(userDTO_1, userDTO_2);

        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setAccountAdmins(accountAdmins);

        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);

        ResponseEntity<String> res = salesforceMarketingCloudService.sendNewUserAwaitingApprovalNotification(newCustomerNotificationDTO);

        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

    @Test
    void sendEmployeeActivationEmail_success() throws Exception {
        EmployeeVerificationDTO ev = new EmployeeVerificationDTO();
        ev.setDomain("test");
        ev.setVerificationToken(UUID.randomUUID());
        ev.generateInviteLink("reecetest.com", ev.getDomain());
        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);

        ResponseEntity<String> res = salesforceMarketingCloudService.sendEmployeeActivationEmail(ev);

        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals("Email sent successfully!", res.getBody());
    }

    @Test
    void checkPriceFormat_withDoubleDigits() throws Exception {
        String price = "54.32";
        String expectedPrice = "54.32";
        String res = SalesforceMarketingCloudService.checkPriceFormat(price);
        assertEquals(expectedPrice, res);
    }

    @Test
    void checkPriceFormat_withoutDoubleDigits() throws Exception {
        String price = "54.3";
        String expectedPrice = "54.30";
        String res = SalesforceMarketingCloudService.checkPriceFormat(price);
        assertEquals(expectedPrice, res);
    }

    @Test
    void getEnvironmentSpecificDefinitionKey_asDev() throws Exception {
        String inputEnv = "aa";
        String expected = "aa_dev";
        String res = (String) getEnvironmentSpecificDefinitionKeyMethod().invoke(salesforceMarketingCloudService, inputEnv);
        assertEquals(expected, res);
    }

    @Test
    void getEnvironmentSpecificDefinitionKey_asProd() throws Exception {
        String inputEnv = "aa";
        String expected = "aa";
        ReflectionTestUtils.setField(salesforceMarketingCloudService, "environmentName", "prod");
        String res = (String) getEnvironmentSpecificDefinitionKeyMethod().invoke(salesforceMarketingCloudService, inputEnv);
        assertEquals(expected, res);
    }

    @Test
    void getEnvironmentSpecificDefinitionKey_asSandbox() throws Exception {
        String inputEnv = "aa";
        String expected = "aa";
        ReflectionTestUtils.setField(salesforceMarketingCloudService, "environmentName", "sandbox");
        String res = (String) getEnvironmentSpecificDefinitionKeyMethod().invoke(salesforceMarketingCloudService, inputEnv);
        assertEquals(expected, res);
    }

    private Method getEnvironmentSpecificDefinitionKeyMethod() throws NoSuchMethodException {
        Method method = SalesforceMarketingCloudService.class.getDeclaredMethod("getEnvironmentSpecificDefinitionKey", String.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void sendNewUserRegistrationNotification_success() throws Exception {
        UserDTO userDTO_1 = new UserDTO();
        UserDTO userDTO_2 = new UserDTO();
        userDTO_1.setEmail("email_1");
        userDTO_1.setFirstName("name");
        userDTO_2.setEmail("email_2");
        userDTO_2.setFirstName("name");

        List<UserDTO> accountAdmins = Arrays.asList(userDTO_1, userDTO_2);

        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setAccountAdmins(accountAdmins);

        ResponseEntity<String> authRes = new ResponseEntity<>("{\"access_token\":\" token\"}", HttpStatus.OK);
        ResponseEntity<String> emailRes = new ResponseEntity<>("nice", HttpStatus.ACCEPTED);
        when(restTemplate.postForEntity(anyString(), any(ObjectNode.class), eq(String.class))).thenReturn(authRes);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(emailRes);

        ResponseEntity<String> res = salesforceMarketingCloudService.sendNewUserRegistrationNotification(newCustomerNotificationDTO);
        assertEquals(res.getStatusCode(), HttpStatus.ACCEPTED);
        assertEquals(res.getBody(), "Email(s) sent successfully!");
    }

}

