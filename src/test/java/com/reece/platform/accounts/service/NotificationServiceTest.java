package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.AccountRequest;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.entity.RejectionReason;
import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { NotificationService.class, RestTemplate.class, ObjectMapper.class })
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private BranchDTO branchDTO;

    private MockRestServiceServer mockRestServiceServer;

    private static final String BRAND = "brand";
    private static final String DOMAIN = "domain";
    private static final String EMAIL = "email@test.com";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    private static final String DEFAULT_DOMAIN = "default";
    private static final String EMPLOYEE_DOMAIN_NO_DNS = "domain.com";
    private static final String[] EMPLOYEE_DOMAINS_NO_DNS = new String[] { EMPLOYEE_DOMAIN_NO_DNS };

    private static final String WATERWORKS_DOMAIN = "fortiline";

    private static final String MORRISON_SUPPLY_DOMAIN = "morrisonsupply";

    private static final String MORSCO_HVAC_SUPPLY_DOMAIN = "morscohvacsupply";

    private static final String MURRAY_SUPPLY_DOMAIN = "murraysupply";

    private static final String FARNSWORTH_WHOLESALE_DOMAIN = "fwcaz";

    private static final String EXPRESS_PIPE_DOMAIN = "expresspipe";

    private static final String DEVORE_AND_JOHNSON_DOMAIN = "devoreandjohnson";

    private static final String WHOLESALE_SPECIALITIES_DOMAIN = "wholesalespecialities";

    private static final String EXPRESSIONS_HOME_GALLERY_DOMAIN = "expressionshomegallery";

    @BeforeEach
    void setUp() {
        branchDTO = new BranchDTO();
        branchDTO.setBrand(BRAND);
        branchDTO.setDomain(DOMAIN);
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

        ReflectionTestUtils.setField(
            notificationService,
            "notificationServiceUrl",
            "http://ecomm-dev-notifications-service:8080"
        );
    }

    @Test
    void sendEmployeeVerificationEmail() {
        EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO();
        employeeVerificationDTO.setEmail(EMAIL);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/employee-email-activate")
            )
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendEmployeeVerificationEmail(employeeVerificationDTO);
    }

    @Test
    void sendUserApprovedEmail() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setEmail(EMAIL);
        accountRequest.setFirstName(FIRST_NAME);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/approve-user")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com\",\"firstName\":\"first name\",\"brand\":\"brand\",\"domain\":\"domain\",\"branchInfo\":{\"phoneNumber\":null,\"supportEmail\":\"support@reecedev.us\"}}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendUserApprovedEmail(branchDTO, accountRequest);
    }

    @Test
    void sendUserLoginUpdatedEmail() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        String oldEmail = "old_email";

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/updated-login")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"emails\":[\"old_email\",\"email@test.com\"],\"firstName\":\"first name\",\"lastName\":\"last name\",\"supportEmail\":\"support@reecedev.us\",\"brand\":\"brand\",\"domain\":\"domain\"}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendUserLoginUpdatedEmail(branchDTO, user, oldEmail);
    }

    @Test
    void sendRejectUserEmail() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setFirstName(FIRST_NAME);
        accountRequest.setEmail(EMAIL);
        String customRejectionReason = "custom";
        accountRequest.setRejectionReason(RejectionReasonEnum.OTHER);
        accountRequest.setCustomRejectionReason(customRejectionReason);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/reject-user")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com\",\"firstName\":\"first name\",\"brand\":\"brand\",\"domain\":\"domain\",\"branchInfo\":{\"phoneNumber\":null,\"supportEmail\":\"support@reecedev.us\"},\"rejectionReason\":\"custom\"}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendRejectUserEmail(accountRequest, branchDTO);
    }

    @Test
    void sendUserDeletedEmail() {
        CustomerAccountDeletedDTO customerAccountDeletedDTO = new CustomerAccountDeletedDTO();
        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    "http://ecomm-dev-notifications-service:8080/notifications/customer-deleted?user-left-company=true"
                )
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"firstName\":null,\"lastName\":null,\"email\":null,\"accountNumber\":null,\"companyName\":null,\"adminEmails\":null,\"branchManagerEmail\":null,\"brand\":null,\"domain\":null}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));
        notificationService.sendUserDeletedEmail(customerAccountDeletedDTO, true);
    }

    @Test
    void sendInviteUserEmail() {
        InvitedUser invitedUser = new InvitedUser();
        invitedUser.setFirstName(FIRST_NAME);
        invitedUser.setEmail(EMAIL);
        UUID inviteId = UUID.randomUUID();
        invitedUser.setId(inviteId);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/invite-user")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        String.format(
                            "{\"email\":\"email@test.com\",\"firstName\":\"first name\",\"inviteId\":\"%s\",\"brand\":\"brand\",\"domain\":\"domain\"}",
                            inviteId
                        )
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendInviteUserEmail(branchDTO, invitedUser);
    }

    @Test
    void sendEmployeeVerificationEmail_noDnsDomain() {
        EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO();
        employeeVerificationDTO.setEmail(String.format("%s@%s", EMAIL, EMPLOYEE_DOMAIN_NO_DNS));

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/employee-email-activate")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com@domain.com\",\"firstName\":null,\"lastName\":null,\"verificationToken\":null,\"brand\":null,\"domain\":\"test.com@domain\",\"waterworksSubdomain\":false}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendEmployeeVerificationEmail(employeeVerificationDTO);
    }

    @Test
    void sendNewCustomerEmailBranchManager_success() {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setEmail(EMAIL);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/new-customer-branch-manager")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com\",\"brand\":null,\"domain\":null,\"managerFirstName\":null,\"requestFirstName\":null,\"requestLastName\":null,\"billToName\":null,\"requestEmail\":null,\"accountAdmins\":null,\"phoneNumber\":null,\"accountNumber\":null,\"existingEcommAccount\":false}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendNewCustomerEmailBranchManager(newCustomerNotificationDTO);
    }

    @Test
    void newCustomerNotificationDTO_success() throws Exception {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setEmail("email@test.com");
        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/new-user-awaiting-approval")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com\",\"brand\":null,\"domain\":null,\"managerFirstName\":null,\"requestFirstName\":null,\"requestLastName\":null,\"billToName\":null,\"requestEmail\":null,\"accountAdmins\":null,\"phoneNumber\":null,\"accountNumber\":null,\"existingEcommAccount\":false}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendNewUserAwaitingApprovalEmail(newCustomerNotificationDTO);
    }

    @Test
    void sendNewUserRegistrationEmail_success() throws Exception {
        NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO();
        newCustomerNotificationDTO.setEmail("email@test.com");
        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/new-user-registration")
            )
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .string(
                        "{\"email\":\"email@test.com\",\"brand\":null,\"domain\":null,\"managerFirstName\":null,\"requestFirstName\":null,\"requestLastName\":null,\"billToName\":null,\"requestEmail\":null,\"accountAdmins\":null,\"phoneNumber\":null,\"accountNumber\":null,\"existingEcommAccount\":false}"
                    )
            )
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendNewUserRegistrationEmail(newCustomerNotificationDTO);
    }

    @Test
    void sendUserWelcomeEmail_Success() {
        var welcomeUserDTO = new WelcomeUserDTO(new BranchDTO(), new User(), "Test@gmail.com");
        welcomeUserDTO.setEmail(EMAIL);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/welcome-user")
            )
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendUserWelcomeEmail(new BranchDTO(), new User());
    }

    @Test
    void sendEmployeeVerificationEmailNew_success() {
        EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO();
        employeeVerificationDTO.setEmail(EMAIL);
        employeeVerificationDTO.setBrand("Reece");

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-notifications-service:8080/notifications/employee-email-activate")
            )
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK));

        notificationService.sendEmployeeVerificationEmailNew(employeeVerificationDTO);
    }

    @Test
    void sendGetWaterworks_Brand_success() {
        var response = notificationService.getDomain("Fortiline Waterworks");
        assertEquals(response, WATERWORKS_DOMAIN);
    }

    @Test
    void sendGetMorrison_supply_domain_success() {
        var response = notificationService.getDomain("Morrison Supply");
        assertEquals(response, MORRISON_SUPPLY_DOMAIN);
    }

    @Test
    void sendGetmorsco_hvac_supply_domain_success() {
        var response = notificationService.getDomain("Morsco HVAC Supply");
        assertEquals(response, MORSCO_HVAC_SUPPLY_DOMAIN);
    }

    @Test
    void sendGetMurray_supply_domain_success() {
        var response = notificationService.getDomain("Murray Supply Company");
        assertEquals(response, MURRAY_SUPPLY_DOMAIN);
    }

    @Test
    void sendGetFarnsworth_wholesale_domain_success() {
        var response = notificationService.getDomain("Farnsworth Wholesale Company");
        assertEquals(response, FARNSWORTH_WHOLESALE_DOMAIN);
    }

    @Test
    void sendExpress_pipe_domain_success() {
        var response = notificationService.getDomain("Express Pipe & Supply");
        assertEquals(response, EXPRESS_PIPE_DOMAIN);
    }

    @Test
    void sendDevore_and_johnson_domain_success() {
        var response = notificationService.getDomain("DeVore & Johnson");
        assertEquals(response, DEVORE_AND_JOHNSON_DOMAIN);
    }

    @Test
    void sendGetWholesale_specialities_domain_success() {
        var response = notificationService.getDomain("Wholesale Specialties");
        assertEquals(response, WHOLESALE_SPECIALITIES_DOMAIN);
    }

    @Test
    void sendGetExpressions_home_gallery_domain_success() {
        var response = notificationService.getDomain("Expressions Home Gallery");
        assertEquals(response, EXPRESSIONS_HOME_GALLERY_DOMAIN);
    }
}
