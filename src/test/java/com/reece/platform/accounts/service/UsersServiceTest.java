package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNull;

import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.ErpUserInformationDTO;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.*;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.model.seed.PermissionType;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import com.reece.platform.accounts.utilities.DecodedToken;
import java.util.*;
import javax.swing.text.html.Option;
import lombok.val;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = { UsersService.class })
public class UsersServiceTest {

    @MockBean
    private InvitedUserDAO invitedUserDAO;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRequestDAO accountRequestDAO;

    @MockBean
    private AccountDAO accountDAO;

    @MockBean
    private ErpService erpService;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private ErpsUsersDAO erpsUsersDAO;

    @MockBean
    private RoleDAO roleDAO;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UsersBillToAccountsDAO usersBillToAccountsDAO;

    @MockBean
    private BranchService branchService;

    @Autowired
    private UsersService usersService;

    @MockBean
    private FeaturesService featuresService;

    private final DecodedToken token = new DecodedToken();

    private static final UUID ERP_UUID_NOT_FOUND = UUID.randomUUID();
    private static final String ERP_ACCOUNT_ID = "123";
    private static final String ERP_USER_LOGIN = "user";
    private static final String ERP_USER_PASSWORD = "pass";

    private static final String DOMAIN = "test";
    private static final String EMAIL = "test@" + DOMAIN + ".com";
    private static final String FIRST_NAME = "user";
    private static final String LAST_NAME = "pass";
    private static final String TEST_MOBILE_NO = "+918787676545";

    private static final String TEST_EMAIL_EXISTS = "test@test.com";
    private static final String TEST_EMAIL_ALREADY_APPROVED = "approved@test.com";
    private static final String TEST_EMAIL_NOT_EXISTS = "test1@test.com";
    private static final String TEST_EMPLOYEE_EMAIL = "test@reece.com";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final UUID UUID_ALREADY_APPROVED = UUID.randomUUID();
    private static final UUID BAD_UUID = UUID.randomUUID();
    private static final UUID USER_NOT_FOUND_UUID = UUID.randomUUID();
    private static final UUID ROLE_NOT_FOUND = UUID.randomUUID();
    private static final UUID VALID_ROLE = UUID.randomUUID();
    private static final UUID VALID_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID INVALID_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID ACCOUNT_ID_NOT_FOUND = UUID.randomUUID();
    private static final UUID MINCRON_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID ECLIPSE_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID VALID_USER_ID = UUID.randomUUID();
    private static final UUID INVALID_USER_ID = UUID.randomUUID();
    private static final UUID ECLIPSE_ERP_ID = UUID.randomUUID();
    private static final UUID UNKNOWN_ERP_ID = UUID.randomUUID();
    private static final UUID MINCRON_ERP_ID = UUID.randomUUID();
    public static final String SHIP_TO_ERP_ACCOUNT_ID = "6000";
    public static final String BILL_TO_ERP_ACCOUNT_ID = "6000";
    private static final String VALID_ROLE_NAME = "user";
    private static final PhoneTypeEnum MOBILE_PHONE_TYPE = PhoneTypeEnum.MOBILE;

    private static final String BILL_TO_ID = "111";
    private static final String BILL_TO_NAME = "bill_to_name";
    private static final String SHIP_TO_ID_1 = "222";
    private static final String SHIP_TO_NAME_1 = "ship_to_name_1";
    private static final String SHIP_TO_ID_2 = "333";
    private static final String SHIP_TO_NAME_2 = "ship_to_name_2";
    private static final String CONTACT_ID = "444";
    private static final String ERP_PASSWORD = "erp_password";
    private static final String ERP_USERNAME = "erp_username";
    private static final String TEST_ACCOUNT_NUMBER = "123";
    private static final String TEST_BRANCH_ID = "123";
    private static final String SHIP_TO_BRANCH_ID = "1";
    private static final String BILL_TO_BRANCH_ID = "2";
    private static final String ACCOUNT_NAME = "Account Name";
    private static final String ACTING_BRANCH_MANAGER_EMAIL = "branch@test.com";
    private static final String ACTING_BRANCH_MANAGER_NAME = "Branch Manager";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(usersService, "enableNotificationCalls", true);
    }

    @Test
    void validateUserEmail_success() {
        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> usersService.validateUserEmail(TEST_EMAIL_NOT_EXISTS));
    }

    @Test
    void validateUserEmail_validEmployeeEmail() {
        when(userDAO.findByEmail(TEST_EMPLOYEE_EMAIL)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> usersService.validateUserEmail(TEST_EMPLOYEE_EMAIL));
    }

    @Test
    void validateUserEmail_userAlreadyExists() {
        val user = new User();
        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> usersService.validateUserEmail(TEST_EMAIL_NOT_EXISTS));
    }

    // CREATE USER TESTS

    @Test
    void createUser_termsNotAccepted() {
        var user = new UserRegistrationDTO();
        assertThrows(TermsNotAcceptedException.class, () -> usersService.createUser(user, null));
    }

    @Test
    void createUser_userAlreadyExists() {
        var dbUser = new User();
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_EXISTS);

        when(userDAO.findByEmail(TEST_EMAIL_EXISTS)).thenReturn(Optional.of(dbUser));
        when(invitedUserDAO.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistsException.class, () -> usersService.createUser(user, null));
    }

    @Test
    void createUser_accountNotValid() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("54321");

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);

        assertThrows(VerifyAccountException.class, () -> usersService.createUser(user, null));
    }

    @Test
    void createUser_invalidInvite() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidInviteException.class, () -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_inviteAlreadyCompleted() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");

        var invite = new InvitedUser();
        invite.setCompleted(true);

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(invite));

        assertThrows(InvalidInviteException.class, () -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_inviteDoesNotMatchUserEmail() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");

        var invite = new InvitedUser();
        invite.setEmail(TEST_EMAIL_ALREADY_APPROVED);
        invite.setCompleted(false);

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(invite));

        assertThrows(InvalidInviteException.class, () -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_inviteRoleDoesNotExist() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");

        var invite = new InvitedUser();
        invite.setEmail(TEST_EMAIL_NOT_EXISTS);
        invite.setCompleted(true);
        invite.setUserRoleId(UUID.randomUUID());

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(invite));
        when(invitedUserDAO.save(any())).thenReturn(invite);
        when(roleDAO.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidInviteException.class, () -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_withInvite_success() throws AccountNotFoundException, BranchNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");
        user.setAccountNumber("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setErpAccountId("12345");
        erpAccountInfo.setBranchId("123");

        var invite = new InvitedUser();
        invite.setEmail(TEST_EMAIL_NOT_EXISTS);
        invite.setCompleted(false);
        invite.setUserRoleId(UUID.randomUUID());

        var role = new Role();
        com.okta.sdk.resource.user.User oktaUser = mock(com.okta.sdk.resource.user.User.class);

        var erpContact = new ErpContactCreationResponse();

        var account = new Account();
        account.setId(UUID.randomUUID());

        var newUser = new User();
        newUser.setRole(role);

        BranchDTO branch = new BranchDTO();
        branch.setBranchId("123");
        branch.setBrand("Reece");
        branch.setDomain("reece.com");
        account.setBranchId("123");

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(invite));
        when(invitedUserDAO.save(any())).thenReturn(invite);
        when(roleDAO.findById(any())).thenReturn(Optional.of(role));
        when(authenticationService.createNewUser(any(), any())).thenReturn(oktaUser);
        when(oktaUser.getId()).thenReturn("");
        when(userDAO.save(any())).thenReturn(newUser);
        doNothing().when(authenticationService).updateNewUserPermissions(any());
        when(erpService.createErpContact(any(), any(), any())).thenReturn(erpContact);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(account));
        when(accountService.findOrCreateAccount(any(), any(), any())).thenReturn(account);
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(Collections.emptyList());
        when(usersBillToAccountsDAO.save(any())).thenReturn(new UsersBillToAccounts());
        when(branchService.getBranch("123")).thenReturn(branch);
        when(erpsUsersDAO.save(any())).thenReturn(new ErpsUsers());
        doNothing().when(notificationService).sendNewUserRegistrationEmail(any());

        assertDoesNotThrow(() -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_legacyUser_success() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");
        user.setAccountNumber("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setErpAccountId("12345");

        var invite = new InvitedUser();
        invite.setEmail(TEST_EMAIL_EXISTS);
        invite.setCompleted(false);
        invite.setUserRoleId(UUID.randomUUID());

        var role = new Role();
        com.okta.sdk.resource.user.User oktaUser = mock(com.okta.sdk.resource.user.User.class);

        var erpContact = new ErpContactCreationResponse();

        var account = new Account();
        account.setId(UUID.randomUUID());

        var existingUser = new User();
        existingUser.setRole(role);

        when(userDAO.findByEmail(TEST_EMAIL_EXISTS)).thenReturn(Optional.of(existingUser));
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(invite));
        when(invitedUserDAO.save(any())).thenReturn(invite);
        when(roleDAO.findById(any())).thenReturn(Optional.of(role));
        when(authenticationService.createNewUser(any(), any())).thenReturn(oktaUser);
        when(oktaUser.getId()).thenReturn("");
        when(userDAO.save(any())).thenReturn(existingUser);
        doNothing().when(authenticationService).updateNewUserPermissions(any());
        when(erpService.createErpContact(any(), any(), any())).thenReturn(erpContact);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(account));
        when(accountService.findOrCreateAccount(any(), any(), any())).thenReturn(account);
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(Collections.emptyList());
        when(usersBillToAccountsDAO.save(any())).thenReturn(new UsersBillToAccounts());
        when(erpsUsersDAO.save(any())).thenReturn(new ErpsUsers());

        assertDoesNotThrow(() -> usersService.createUser(user, UUID.randomUUID()));
    }

    @Test
    void createUser_noInvite_success() throws AccountNotFoundException {
        var user = new UserRegistrationDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMAIL_NOT_EXISTS);
        user.setBrand("Reece");
        user.setZipcode("12345");
        user.setAccountNumber("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setErpAccountId("12345");

        var role = new Role();
        com.okta.sdk.resource.user.User oktaUser = mock(com.okta.sdk.resource.user.User.class);

        var erpContact = new ErpContactCreationResponse();
        var account = new Account();
        account.setId(UUID.randomUUID());

        var existingUser = new User();
        existingUser.setRole(role);
        existingUser.setAuthId(null);

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpBillToAccount(any(), any())).thenReturn(erpAccountInfo);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.empty());
        when(roleDAO.findByName(any())).thenReturn(role);
        when(authenticationService.createNewUser(any(), any())).thenReturn(oktaUser);
        when(oktaUser.getId()).thenReturn("");
        when(userDAO.save(any())).thenReturn(existingUser);
        doNothing().when(authenticationService).updateNewUserPermissions(any());
        when(erpService.createErpContact(any(), any(), any())).thenReturn(erpContact);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(account));
        when(accountService.findOrCreateAccount(any(), any(), any())).thenReturn(account);
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(Collections.emptyList());
        when(usersBillToAccountsDAO.save(any())).thenReturn(new UsersBillToAccounts());
        when(erpsUsersDAO.save(any())).thenReturn(new ErpsUsers());

        assertDoesNotThrow(() -> usersService.createUser(user, null));
    }

    // Registration for Employees
    @Test
    void createEmployee_notValidEmail() {
        var user = new CreateEmployeeDTO();
        user.setEmail(TEST_EMAIL_EXISTS);
        assertThrows(UserNotEmployeeException.class, () -> usersService.createEmployeeUser(user));
    }

    @Test
    void createEmployee_termsNotAccepted() {
        var user = new CreateEmployeeDTO();
        user.setEmail(TEST_EMPLOYEE_EMAIL);
        assertThrows(TermsNotAcceptedException.class, () -> usersService.createEmployeeUser(user));
    }

    @Test
    void createEmployee_userAlreadyExists() {
        var dbUser = new User();
        var user = new CreateEmployeeDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMPLOYEE_EMAIL);

        when(userDAO.findByEmail(TEST_EMPLOYEE_EMAIL)).thenReturn(Optional.of(dbUser));

        assertThrows(UserAlreadyExistsException.class, () -> usersService.createEmployeeUser(user));
    }

    @Test
    void createEmployee_success() {
        var user = new CreateEmployeeDTO();
        user.setPpAccepted(true);
        user.setTosAccepted(true);
        user.setEmail(TEST_EMPLOYEE_EMAIL);

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("54321");

        var role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);

        var dbUser = new User();
        dbUser.setRole(role);
        dbUser.setAuthId(null);
        dbUser.setEmail(user.getEmail());

        com.okta.sdk.resource.user.User oktaUser = mock(com.okta.sdk.resource.user.User.class);

        when(userDAO.findByEmail(TEST_EMPLOYEE_EMAIL)).thenReturn(Optional.empty());
        when(roleDAO.findByName(any())).thenReturn(role);
        when(authenticationService.createEmployeeUser(any())).thenReturn(oktaUser);
        when(userDAO.save(any())).thenReturn(dbUser);
        doNothing().when(notificationService).sendEmployeeVerificationEmail(any());

        assertDoesNotThrow(() -> usersService.createEmployeeUser(user));
    }

    // Verify Employee Account

    @Test
    public void verifyEmployee_userNotFound() {
        var userId = UUID.randomUUID();
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.verifyEmployee(userId));
    }

    @Test
    public void verifyEmployee_userAlreadyVerified() {
        var userId = UUID.randomUUID();
        var erpUser = new ErpsUsers();
        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));
        when(erpsUsersDAO.findByUserId(any())).thenReturn(Optional.of(erpUser));
        assertThrows(VerificationTokenNotValidException.class, () -> usersService.verifyEmployee(userId));
    }

    @Test
    public void verifyEmployee_success() {
        var userId = UUID.randomUUID();
        var erpUser = new ErpsUsers();
        var erpContact = new ErpContactCreationResponse();
        var billTo = new ErpAccountInfo();
        billTo.setErpAccountId("12345");

        var account = new Account();

        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));
        when(erpsUsersDAO.findByUserId(any())).thenReturn(Optional.empty());
        when(erpService.createErpContact(any(), any(), any())).thenReturn(erpContact);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(account));
        when(erpsUsersDAO.save(any())).thenReturn(erpUser);
        doNothing().when(authenticationService).updateNewUserPermissions(any());
        assertDoesNotThrow(() -> usersService.verifyEmployee(userId));
    }

    // resend employee verification email

    @Test
    public void resendVerification_userNotFound() {
        when(userDAO.findById(any())).thenReturn(Optional.empty());

        assertThrows(
            UserNotFoundException.class,
            () -> usersService.resendEmployeeVerificationEmail(UUID.randomUUID())
        );
    }

    @Test
    public void resendVerification_success() {
        var user = new User();
        user.setEmail(TEST_EMPLOYEE_EMAIL);
        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        doNothing().when(notificationService).sendEmployeeVerificationEmail(any());

        assertDoesNotThrow(() -> usersService.resendEmployeeVerificationEmail(UUID.randomUUID()));
    }

    @Test
    public void getUser_success() {
        val user = new User();
        user.setErpsUsers(Set.of());
        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() ->
            assertEquals(usersService.getUser(UUID.randomUUID().toString(), false), new ApiUserResponseDTO(user))
        );
    }

    @Test
    public void getUser_notFound() {
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getUser(UUID.randomUUID().toString(), false));
    }

    @Test
    public void getUser_contactExists() {
        String lastModifiedByName = "test test";
        Date lastModifiedDate = new Date();
        User user = new User();
        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setLastModifiedBy(lastModifiedByName);
        erpsUsers.setLastModifiedDate(lastModifiedDate);
        user.setErpsUsers(Set.of(erpsUsers));
        when(userDAO.findById(any())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> {
            ApiUserResponseDTO apiUserResponseDTO = usersService.getUser(UUID.randomUUID().toString(), false);

            assertEquals(
                apiUserResponseDTO.getContactUpdatedAt(),
                lastModifiedDate,
                "Expected contact updated at value to equal erp users value"
            );
            assertEquals(
                apiUserResponseDTO.getContactUpdatedBy(),
                lastModifiedByName,
                "Expected contact updated by value to equal erp users value"
            );
        });
    }

    @Test
    public void getUser_contactDoesNotExist() {
        User user = new User();
        user.setErpsUsers(Set.of());
        when(userDAO.findById(any())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> {
            ApiUserResponseDTO apiUserResponseDTO = usersService.getUser(UUID.randomUUID().toString(), false);
            assertNull(
                "Expected contact updated at value to equal erp users value",
                apiUserResponseDTO.getContactUpdatedAt()
            );
            assertNull(
                "Expected contact updated by value to equal erp users value",
                apiUserResponseDTO.getContactUpdatedBy()
            );
        });
    }

    @Test
    public void getUserApprover_success() {
        var expectedResponse = new ContactInfoDTO();
        expectedResponse.setIsBranchInfo(true);
        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(new AccountRequest());
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));
        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));
        assertDoesNotThrow(() -> assertEquals(usersService.getContactInfo(UUID.randomUUID()), expectedResponse));
    }

    @Test
    public void getErpUserInformation_userNotFound() {
        UUID userid = UUID.randomUUID();
        when(erpsUsersDAO.findByUserId(userid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getErpUserInformation(userid, UUID.randomUUID()));
    }

    @Test
    public void getErpUserInformation_accountNotFound() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(accountDAO.findById(accountId)).thenReturn(Optional.empty());
        when(erpsUsersDAO.findFirstByUserIdOrderByLastModifiedDateDesc(userId)).thenReturn(Optional.of(new ErpsUsers()));
        assertThrows(AccountNotFoundException.class, () -> usersService.getErpUserInformation(userId, accountId));
    }

    @Test
    public void getErpUserInformation_success() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Account account = new Account();
        account.setErpAccountId(ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setErpUserLogin(ERP_USER_LOGIN);
        erpsUsers.setErpUserPassword(ERP_USER_PASSWORD);
        when(accountDAO.findById(accountId)).thenReturn(Optional.of(account));
        when(erpsUsersDAO.findFirstByUserIdOrderByLastModifiedDateDesc(userId)).thenReturn(Optional.of(erpsUsers));
        when(userDAO.getOne(userId)).thenReturn(new User());
        ErpUserInformationDTO erpUserInformationDTO = usersService.getErpUserInformation(userId, accountId);
        assertEquals(
            erpUserInformationDTO.getErpAccountId(),
            String.valueOf(ERP_ACCOUNT_ID),
            "Expected erp account id in dto to be mocked value from DAO"
        );
        assertEquals(
            erpUserInformationDTO.getPassword(),
            ERP_USER_PASSWORD,
            "Expected erp user password in dto to be mocked value from DAO"
        );
        assertEquals(
            erpUserInformationDTO.getUserId(),
            ERP_USER_LOGIN,
            "Expected erp user login in dto to be mocked value from DAO"
        );
    }

    @Test
    public void getUserAccounts_morsco() throws BranchNotFoundException{
        User customerUser = new User();

        HashSet<Permission> permissions = new HashSet<>();
        Permission morscoPermission = new Permission();
        morscoPermission.setName(PermissionType.approve_all_users);
        permissions.add(morscoPermission);
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(permissions);
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        customerUser.setRole(role);

        UUID userId = UUID.randomUUID();
        when(userDAO.findById(userId)).thenReturn(Optional.of(customerUser));

        Account account = new Account();
        account.setId(UUID.randomUUID());
        Account shipTo = new Account();
        List<Account> shipTos = new ArrayList<>();
        shipTos.add(shipTo);
        account.setShipToAccounts(shipTos);
        ArrayList<Account> accounts = new ArrayList<>();
        account.setErpAccountId(ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        accounts.add(account);

        BillToAccountDTO billToAccountDTO = new BillToAccountDTO(account, null, ErpEnum.ECLIPSE);
        billToAccountDTO.setDivisionEnum(DivisionEnum.PHVAC);
        billToAccountDTO.setBranchId(null);
        billToAccountDTO.setBranchAddress("null null, null null");
        List<BillToAccountDTO> expectedBillTos = new ArrayList<>();
        expectedBillTos.add(billToAccountDTO);
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(null);
        List<BranchDTO> branchesList = new ArrayList<>();
        branchesList.add(branchDTO);


        when(accountDAO.findAllByParentAccountId(null)).thenReturn(accounts);
        when(branchService.getAllBranches()).thenReturn(branchesList);

        assertDoesNotThrow(() -> {
            List<BillToAccountDTO> billToAccountDTOS = usersService.getUserAccounts(userId);
            assertEquals(billToAccountDTOS, expectedBillTos);
        });
    }

    @Test
    public void getUserAccounts_customer() {
        User customerUser = new User();
        Role userRole = new Role();
        userRole.setName(RoleEnum.STANDARD_ACCESS.label);
        customerUser.setRole(userRole);
        UUID userId = UUID.randomUUID();
        when(userDAO.findById(userId)).thenReturn(Optional.of(customerUser));

        Account account = new Account();
        account.setId(UUID.randomUUID());
        Account shipTo = new Account();
        List<Account> shipTos = new ArrayList<>();
        shipTos.add(shipTo);
        account.setShipToAccounts(shipTos);
        HashSet<Account> accounts = new HashSet<>();
        account.setErpAccountId(ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        accounts.add(account);
        customerUser.setBillToAccounts(accounts);

        BillToAccountDTO billToAccountDTO = new BillToAccountDTO(account, null, ErpEnum.ECLIPSE);
        List<BillToAccountDTO> expectedBillTos = new ArrayList<>();
        expectedBillTos.add(billToAccountDTO);

        assertDoesNotThrow(() -> {
            List<BillToAccountDTO> billToAccountDTOS = usersService.getUserAccounts(userId);
            assertEquals(billToAccountDTOS, expectedBillTos);
        });
    }

    @Test
    public void getUserAccounts_mincron() {
        User customerUser = new User();
        Role userRole = new Role();
        userRole.setName(RoleEnum.STANDARD_ACCESS.label);
        customerUser.setRole(userRole);
        UUID userId = UUID.randomUUID();
        when(userDAO.findById(userId)).thenReturn(Optional.of(customerUser));

        Account account = new Account();
        account.setId(UUID.randomUUID());
        Account shipTo = new Account();
        List<Account> shipTos = new ArrayList<>();
        shipTos.add(shipTo);
        account.setShipToAccounts(shipTos);
        HashSet<Account> accounts = new HashSet<>();
        account.setErpAccountId("1234");
        account.setErp(ErpEnum.MINCRON);
        accounts.add(account);
        customerUser.setBillToAccounts(accounts);

        BillToAccountDTO billToAccountDTO = new BillToAccountDTO(account, null, ErpEnum.MINCRON);
        List<BillToAccountDTO> expectedBillTos = new ArrayList<>();
        expectedBillTos.add(billToAccountDTO);

        assertDoesNotThrow(() -> {
            List<BillToAccountDTO> billToAccountDTOS = usersService.getUserAccounts(userId);
            assertEquals(billToAccountDTOS, expectedBillTos);
        });
    }

    @Test
    public void getUserAccounts_returns_erpAccountIds() throws BranchNotFoundException{
        User customerUser = new User();

        HashSet<Permission> permissions = new HashSet<>();
        Permission morscoPermission = new Permission();
        morscoPermission.setName(PermissionType.approve_all_users);
        permissions.add(morscoPermission);
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(permissions);
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        customerUser.setRole(role);

        UUID userId = UUID.randomUUID();
        when(userDAO.findById(userId)).thenReturn(Optional.of(customerUser));

        String shipToCompanyName = "ship to name";
        String shipToAddress = "123 Street, City State";

        String billToCompanyName = "bill to name";
        String billToAddress = "321 Street, City2 State2";

        Account account = new Account();
        account.setErpAccountId(ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        account.setId(UUID.randomUUID());
        account.setAddress(billToAddress);
        account.setName(billToCompanyName);
        Account shipTo = new Account();
        shipTo.setErpAccountId(ERP_ACCOUNT_ID);
        shipTo.setErp(ErpEnum.ECLIPSE);
        shipTo.setParentAccountId(account.getId());
        shipTo.setName(shipToCompanyName);
        shipTo.setAddress(shipToAddress);
        List<Account> shipTos = new ArrayList<>();
        shipTos.add(shipTo);
        account.setShipToAccounts(shipTos);
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(account);
        account.setBranchId(null);
        BranchDTO branchDTO = new BranchDTO();

        when(accountDAO.findAllByParentAccountId(null)).thenReturn(accounts);
        when(accountDAO.findAllByParentAccountIdNotNull()).thenReturn(shipTos);
        when(branchService.getBranch(account.getBranchId())).thenReturn(branchDTO);

        assertDoesNotThrow(() -> {
            List<BillToAccountDTO> billToAccountDTOS = usersService.getUserAccounts(userId);

            assertEquals(
                billToAccountDTOS.get(0).getErpAccountId(),
                ERP_ACCOUNT_ID,
                "BillToAccountDTO does not correctly set erpAccountId on DTO"
            );
            assertEquals(
                billToAccountDTOS.get(0).getShipTos().get(0).getErpAccountId(),
                ERP_ACCOUNT_ID,
                "ShipToAccountDTO does not correctly set erpAccountId on DTO"
            );
            assertEquals(
                billToAccountDTOS.get(0).getName(),
                AccountDataFormatting.formatAccountName(
                    ERP_ACCOUNT_ID,
                    billToCompanyName,
                    billToAddress,
                    ErpEnum.ECLIPSE
                ),
                "Expected bill to account name on DTO to match account name format"
            );
            assertEquals(
                billToAccountDTOS.get(0).getShipTos().get(0).getName(),
                AccountDataFormatting.formatAccountName(
                    ERP_ACCOUNT_ID,
                    shipToCompanyName,
                    shipToAddress,
                    ErpEnum.ECLIPSE
                ),
                "ShipToAccountDTO does not correctly set erpAccountId on DTO"
            );
        });
    }

    @Test
    public void updateUserPassword_UserNotFound() {
        UpdateUserPasswordDTO updateUserPasswordDTO = new UpdateUserPasswordDTO();
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(
            UserNotFoundException.class,
            () -> usersService.updateUserPassword(new UUID(0, 0), updateUserPasswordDTO)
        );
    }

    @Test
    public void updateUserPassword_userExists() throws Exception {
        UpdateUserPasswordDTO updateUserPasswordDTO = new UpdateUserPasswordDTO();
        when(userDAO.findById(any())).thenReturn(Optional.of(new com.reece.platform.accounts.model.entity.User()));
        usersService.updateUserPassword(new UUID(0, 0), updateUserPasswordDTO);
        verify(notificationService, times(1)).sendUserLoginUpdatedEmail(eq(null), any(User.class), eq(null));
        verify(authenticationService, times(1)).updateUserPassword(any(), any());
    }

    @Test
    public void verifyUserToken_userNotFound() {
        when(accountRequestDAO.findByVerificationToken(any())).thenReturn(Optional.of(new AccountRequest()));
        when(userDAO.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.verifyUserToken(UUID.randomUUID()));
    }

    @Test
    public void resendVerificationEmail_userNotFound() {
        UUID userId = UUID.randomUUID();
        when(userDAO.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.resendVerificationEmail(userId, false));
    }

    @Test
    public void resendVerificationEmail_success() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        UUID verificationToken = UUID.randomUUID();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setVerificationToken(verificationToken);

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(EMAIL)).thenReturn(accountRequest);
        usersService.resendVerificationEmail(userId, false);
        verify(notificationService)
            .sendEmployeeVerificationEmail(
                argThat(value ->
                    value.getDomain().equals(DOMAIN) &&
                    value.getEmail().equals(EMAIL) &&
                    value.getFirstName().equals(FIRST_NAME) &&
                    value.getLastName().equals(LAST_NAME) &&
                    value.getVerificationToken().equals(verificationToken)
                )
            );
    }

    @Test
    void syncUserProfileToAuthProvider_success() {
        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));
        usersService.syncUserProfileToAuthProvider(UUID.randomUUID());
        verify(authenticationService, times(1)).updateUserPermissions(any());
    }

    @Test
    void syncUserProfileToAuthProvider_notFound() {
        assertThrows(NoSuchElementException.class, () -> usersService.syncUserProfileToAuthProvider(UUID.randomUUID()));
    }

    @Test
    void approveUser_validMultipleShipTo() throws Exception {
        var billTo = new Account();
        billTo.setErpAccountId(BILL_TO_ID);
        billTo.setName(BILL_TO_NAME);

        ErpAccountInfo billToErp = new ErpAccountInfo();
        billToErp.setErpAccountId(BILL_TO_ID);

        ErpAccountInfo shipTo1 = new ErpAccountInfo();
        shipTo1.setErpAccountId(SHIP_TO_ID_1);
        shipTo1.setCompanyName(SHIP_TO_NAME_1);
        ErpAccountInfo shipTo2 = new ErpAccountInfo();
        shipTo2.setErpAccountId(SHIP_TO_ID_2);
        shipTo2.setCompanyName(SHIP_TO_NAME_2);

        ErpContactCreationResponse eclipseResponse = new ErpContactCreationResponse();
        eclipseResponse.setContactId(CONTACT_ID);
        eclipseResponse.setErpPassword(ERP_PASSWORD);
        eclipseResponse.setErpUsername(ERP_USERNAME);

        BranchDTO branch = new BranchDTO();
        branch.setBrand("Reece");
        branch.setDomain("reece.com");

        when(accountDAO.findById(any())).thenReturn(Optional.of(billTo));
        when(erpService.getEclipseAccount(any(), eq(false), eq(false))).thenReturn(billToErp);
        when(accountService.getHomeBranch(any())).thenReturn(branch);

        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setId(TEST_UUID);

        when(userDAO.findById(TEST_UUID)).thenReturn(Optional.of(user));
        when(roleDAO.findById(any())).thenReturn(Optional.of(new Role()));

        ApproveUser approveUser = new ApproveUser();
        approveUser.setUserId(TEST_UUID.toString());
        approveUser.setUserRoleId(VALID_ROLE.toString());

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCompleted(false);
        accountRequest.setEmail(TEST_EMAIL_EXISTS);
        accountRequest.setErp(ErpEnum.ECLIPSE);
        when(erpService.createErpAccount(accountRequest)).thenReturn(eclipseResponse);
        when(accountRequestDAO.findMostRecentRequestByEmail(TEST_EMAIL_EXISTS)).thenReturn(accountRequest);
        when(accountService.createEcommAccount(any(), any(), any())).thenReturn(Optional.of(new Account()));
        when(accountService.findOrCreateAccount(any(), any(), any())).thenReturn(new Account());

        usersService.approveUser(approveUser);

        //verify(accountDAO, times(3)).save(any(Account.class));
        verify(erpsUsersDAO, times(1)).save(any(ErpsUsers.class));
        verify(accountRequestDAO, times(1)).save(any(AccountRequest.class));
        verify(usersBillToAccountsDAO, times(1)).save(any(UsersBillToAccounts.class));
        verify(notificationService, times(1)).sendUserApprovedEmail(any(BranchDTO.class), any(AccountRequest.class));
    }

    @Test
    void approveUser_userNotFound() {
        when(userDAO.findById(USER_NOT_FOUND_UUID)).thenReturn(Optional.empty());

        ApproveUser approveUser = new ApproveUser();
        approveUser.setUserId(USER_NOT_FOUND_UUID.toString());
        assertThrows(UserNotFoundException.class, () -> usersService.approveUser(approveUser));
    }

    @Test
    void approveUser_roleNotFound() {
        when(accountRequestDAO.findById(any())).thenReturn(Optional.of(new AccountRequest()));
        when(userDAO.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(roleDAO.findById(ROLE_NOT_FOUND)).thenReturn(Optional.empty());

        ApproveUser approveUser = new ApproveUser();
        approveUser.setUserId(TEST_UUID.toString());
        approveUser.setUserRoleId(ROLE_NOT_FOUND.toString());
        assertThrows(UserRoleNotFoundException.class, () -> usersService.approveUser(approveUser));
    }

    @Test
    void rejectUser_success() throws Exception {
        var accountRequest = new AccountRequest();
        accountRequest.setId(TEST_UUID);
        accountRequest.setEmail(TEST_EMAIL_EXISTS);
        accountRequest.setErp(ErpEnum.ECLIPSE);

        var accounts = new HashSet<Account>();
        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setBillToAccounts(accounts);

        var branch = new BranchDTO();
        branch.setBrand("Reece");
        branch.setDomain("reece.com");

        var account = new Account();
        account.setErp(ErpEnum.ECLIPSE);

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId("1000");

        when(accountDAO.findById(any())).thenReturn(Optional.of(account));
        when(erpService.getEclipseAccount(any(), eq(false), eq(false))).thenReturn(erpAccountInfo);
        when(branchService.getBranch(any())).thenReturn(branch);
        when(accountRequestDAO.findById(TEST_UUID)).thenReturn(Optional.of(accountRequest));
        when(userDAO.findByEmail(TEST_EMAIL_EXISTS)).thenReturn(Optional.of(user));

        usersService.rejectUser(TEST_UUID, RejectionReasonEnum.NOT_A_COMPANY_MEMBER, null, token);

        verify(accountRequestDAO, times(1)).findById(any());
        verify(userDAO, times(1)).findByEmail(any());
        verify(accountRequestDAO, times(1)).save(any());
        verify(authenticationService, times(1)).deleteUser(any());
        verify(notificationService, times(1)).sendRejectUserEmail(any(AccountRequest.class), any(BranchDTO.class));
    }

    @Test
    void rejectUser_useProvidedBranchIdWhenNoAccountId() throws Exception {
        var accountRequest = new AccountRequest();
        accountRequest.setId(null);
        accountRequest.setEmail(TEST_EMAIL_EXISTS);
        accountRequest.setErp(ErpEnum.ECLIPSE);

        var accounts = new HashSet<Account>();
        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setBillToAccounts(accounts);

        var branch = new BranchDTO();
        branch.setBrand("Reece");
        branch.setDomain("reece.com");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId("1000");

        when(erpService.getEclipseAccount(any(), eq(false), eq(false))).thenReturn(erpAccountInfo);
        when(branchService.getBranch(any())).thenReturn(branch);
        when(accountRequestDAO.findById(TEST_UUID)).thenReturn(Optional.of(accountRequest));
        when(userDAO.findByEmail(TEST_EMAIL_EXISTS)).thenReturn(Optional.of(user));

        usersService.rejectUser(TEST_UUID, RejectionReasonEnum.NOT_A_COMPANY_MEMBER, null, token);

        verify(branchService, times(1)).getBranch(accountRequest.getBranchId());
    }

    @Test
    void rejectUser_userNotFound() {
        when(accountRequestDAO.findById(any())).thenReturn(Optional.of(new AccountRequest()));
        assertThrows(
            UserNotFoundException.class,
            () -> usersService.rejectUser(UUID.randomUUID(), RejectionReasonEnum.OTHER, null, token)
        );
    }

    @Test
    void approveUser_userAlreadyApproved() {
        when(roleDAO.findById(any())).thenReturn(Optional.of(new Role()));
        when(authenticationService.userCanManageAccountRequests(token, UUID_ALREADY_APPROVED)).thenReturn(true);
        com.reece.platform.accounts.model.entity.User alreadyApprovedUser = new com.reece.platform.accounts.model.entity.User();
        alreadyApprovedUser.setId(UUID_ALREADY_APPROVED);
        alreadyApprovedUser.setEmail(TEST_EMAIL_ALREADY_APPROVED);
        when(userDAO.findById(UUID_ALREADY_APPROVED)).thenReturn(Optional.of(alreadyApprovedUser));

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCompleted(true);
        when(accountRequestDAO.findMostRecentRequestByEmail(TEST_EMAIL_ALREADY_APPROVED)).thenReturn(accountRequest);

        ApproveUser approveUser = new ApproveUser();
        approveUser.setUserId(UUID_ALREADY_APPROVED.toString());
        approveUser.setUserRoleId(VALID_ROLE.toString());
        assertThrows(UserAlreadyApprovedException.class, () -> usersService.approveUser(approveUser));
    }

    @Test
    void updateUser_allFieldsUpdated() throws Exception {
        String oldEmail = "oldEmail";
        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setRole(new Role());
        user.setEmail(oldEmail);

        UUID existingUserId = UUID.randomUUID();

        AccountRequest request = new AccountRequest();
        when(accountRequestDAO.findByEmailAndRejectionReasonIsNull(oldEmail)).thenReturn(Optional.of(request));

        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(true);
        when(userDAO.save(any())).thenReturn(user);
        when(userDAO.findById(existingUserId)).thenReturn(Optional.of(user));

        String erpAccountId = "erp account id";
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setErpAccountId(erpAccountId);
        account.setErp(ErpEnum.ECLIPSE);
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        String branchId = "branch id";
        erpAccountInfo.setBranchId(branchId);
        when(erpService.getEclipseAccount(erpAccountId, false, false)).thenReturn(erpAccountInfo);
        when(branchService.getBranch(branchId)).thenReturn(new BranchDTO());
        when(accountDAO.findById(accountId)).thenReturn(Optional.of(account));

        String newEmail = "newEmail";
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newPhoneNumber = "newPhoneNumber";
        UUID newApproverId = UUID.randomUUID();

        PhoneTypeEnum newPhoneType = PhoneTypeEnum.MOBILE;

        com.reece.platform.accounts.model.entity.User newApproverUser = new com.reece.platform.accounts.model.entity.User();
        when(userDAO.findById(newApproverId)).thenReturn(Optional.of(newApproverUser));

        UserDTO updatedUserDto = new UserDTO();
        updatedUserDto.setEmail(newEmail);
        updatedUserDto.setFirstName(newFirstName);
        updatedUserDto.setLastName(newLastName);
        updatedUserDto.setPhoneNumber(newPhoneNumber);
        updatedUserDto.setPhoneTypeId(newPhoneType);
        updatedUserDto.setApproverId(newApproverId);
        usersService.updateUser(existingUserId, accountId, updatedUserDto);
        verify(userDAO, times(1))
            .save(
                argThat(savingUser ->
                    savingUser.getEmail().equals(newEmail) &&
                    savingUser.getFirstName().equals(newFirstName) &&
                    savingUser.getLastName().equals(newLastName) &&
                    savingUser.getPhoneNumber().equals(newPhoneNumber) &&
                    savingUser.getPhoneType().equals(newPhoneType) &&
                    savingUser.getApprover().equals(newApproverUser)
                )
            );
        verify(userDAO, times(1)).findById(existingUserId);
        verify(accountRequestDAO, times(1)).findByEmailAndRejectionReasonIsNull(oldEmail);
        verify(accountRequestDAO, times(1))
            .save(
                argThat(savedAccountRequest ->
                    savedAccountRequest.getEmail().equals(newEmail) &&
                    savedAccountRequest.getFirstName().equals(newFirstName) &&
                    savedAccountRequest.getLastName().equals(newLastName) &&
                    savedAccountRequest.getPhoneNumber().equals(newPhoneNumber) &&
                    savedAccountRequest.getPhoneType().equals(newPhoneType)
                )
            );
        verify(authenticationService, times(1))
            .updateUserLoginEmailAndProfile(
                argThat(savingUser ->
                    savingUser.getEmail().equals(newEmail) &&
                    savingUser.getFirstName().equals(newFirstName) &&
                    savingUser.getLastName().equals(newLastName) &&
                    savingUser.getPhoneNumber().equals(newPhoneNumber) &&
                    savingUser.getPhoneType().equals(newPhoneType) &&
                    savingUser.getApprover().equals(newApproverUser)
                )
            );
    }

    @Test
    void updateUser_emailAlreadyInUse() {
        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setEmail(TEST_EMAIL_ALREADY_APPROVED);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(TEST_EMAIL_EXISTS);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(userDAO.findByEmail(TEST_EMAIL_EXISTS))
            .thenReturn(Optional.of(new com.reece.platform.accounts.model.entity.User()));
        when(accountRequestDAO.findByEmailAndRejectionReasonIsNull(any()))
            .thenReturn(Optional.of(new AccountRequest()));

        assertThrows(
            UpdateUserEmailAlreadyExistsException.class,
            () -> usersService.updateUser(VALID_USER_ID, VALID_ACCOUNT_ID, userDTO)
        );
    }

    @Test
    public void deleteUser_existsOnMultipleBillToAccounts() throws Exception {
        Account account = new Account();

        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();

        ErpsUsers erpsUsers = new ErpsUsers();

        account.setErpAccountId("6000");
        account.setErp(ErpEnum.ECLIPSE);

        BranchDTO branch = new BranchDTO();
        branch.setBrand("Reece");
        branch.setDomain("reece.com");

        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId("1000");

        UsersBillToAccounts usersBillToAccountsPrimary = new UsersBillToAccounts();

        UsersBillToAccounts usersBillToAccountsSecondary = new UsersBillToAccounts();

        List<UsersBillToAccounts> usersBillToAccounts = Arrays.asList(usersBillToAccountsSecondary);

        when(erpService.getEclipseAccount(eq("6000"), eq(false), eq(false))).thenReturn(erpAccountInfo);
        when(accountService.getHomeBranch(any())).thenReturn(branch);
        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(erpsUsersDAO.findByUserId(any())).thenReturn(Optional.of(erpsUsers));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(any(), any()))
            .thenReturn(Optional.of(usersBillToAccountsPrimary));
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(usersBillToAccounts);
        when(accountDAO.findById(any())).thenReturn(Optional.of(account));
        when(invitedUserDAO.findByEmail(any())).thenReturn(Optional.of(new InvitedUser()));
        when(accountRequestDAO.findByEmailAndRejectionReasonIsNull(any()))
            .thenReturn(Optional.of(new AccountRequest()));
        usersService.deleteUser(UUID.randomUUID(), UUID.randomUUID(), false);
        verifyNoInteractions(authenticationService);
    }

    @Test
    public void getContactInfo_WithAccountId() throws Exception {
        ContactInfoDTO expected = new ContactInfoDTO();
        expected.setIsBranchInfo(null);
        expected.setEmailAddress(TEST_EMAIL_EXISTS);
        expected.setPhoneNumber(TEST_MOBILE_NO);

        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(VALID_ACCOUNT_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));

        when(userDAO.findUserByAccountIdAndRoleName(any(), any())).thenReturn(Optional.of(user));
        assertEquals(
            expected,
            usersService.getContactInfo(VALID_USER_ID),
            "Expected & Actual ContactInfoDTO should be match"
        );
    }

    @Test
    public void getContactInfo_WithAccountIdAndAccountAdmin() throws Exception {
        ContactInfoDTO expected = new ContactInfoDTO();
        expected.setIsBranchInfo(null);
        expected.setEmailAddress(TEST_EMAIL_EXISTS);
        expected.setPhoneNumber(TEST_MOBILE_NO);

        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(VALID_ACCOUNT_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));

        when(userDAO.findUserByAccountIdAndRoleName(any(), any())).thenReturn(Optional.of(user));
        assertEquals(
            expected,
            usersService.getContactInfo(VALID_USER_ID),
            "Expected & Actual ContactInfoDTO should be match"
        );
    }

    @Test
    public void getContactInfo_WithAccountIdAndBranchPhoneNumberAndWithOutAccountAdmin() throws Exception {
        ContactInfoDTO expected = new ContactInfoDTO();
        expected.setIsBranchInfo(true);
        expected.setPhoneNumber(TEST_MOBILE_NO);

        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(VALID_ACCOUNT_ID);
        accountRequest.setBranchPhoneNumber(TEST_MOBILE_NO);

        Account account = new Account();
        account.setId(VALID_ACCOUNT_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);
        when(accountDAO.findById(any())).thenReturn(Optional.of(account));

        when(userDAO.findUserByAccountIdAndRoleName(any(), any())).thenReturn(Optional.empty());
        assertEquals(
            expected,
            usersService.getContactInfo(VALID_USER_ID),
            "Expected & Actual ContactInfoDTO should be match"
        );
    }

    @Test
    public void getContactInfo_WithAccountId_AndWithOutAccountAdminAndBranchPhoneNumber() throws Exception {
        ContactInfoDTO expected = new ContactInfoDTO();
        expected.setIsBranchInfo(true);

        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(VALID_ACCOUNT_ID);

        Account account = new Account();
        account.setId(VALID_ACCOUNT_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);
        when(accountDAO.findById(any())).thenReturn(Optional.of(account));

        when(userDAO.findUserByAccountIdAndRoleName(any(), any())).thenReturn(Optional.empty());
        assertEquals(
            expected,
            usersService.getContactInfo(VALID_USER_ID),
            "Expected & Actual ContactInfoDTO should be match"
        );
    }

    @Test
    public void getContactInfo_WithOutAccountId() throws Exception {
        ContactInfoDTO expected = new ContactInfoDTO();
        expected.setIsBranchInfo(true);
        expected.setPhoneNumber(TEST_MOBILE_NO);

        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(null);
        accountRequest.setBranchPhoneNumber(TEST_MOBILE_NO);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);

        assertEquals(
            expected,
            usersService.getContactInfo(VALID_USER_ID),
            "Expected & Actual ContactInfoDTO should be match"
        );
    }

    @Test
    void getContactInfo_AccountNotFoundException() {
        val user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setPhoneNumber(TEST_MOBILE_NO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(VALID_ACCOUNT_ID);
        accountRequest.setBranchPhoneNumber(TEST_MOBILE_NO);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(accountRequestDAO.findMostRecentRequestByEmail(any())).thenReturn(accountRequest);
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        when(userDAO.findUserByAccountIdAndRoleName(any(), any())).thenReturn(Optional.of(user));
        assertThrows(AccountNotFoundException.class, () -> usersService.getContactInfo(INVALID_USER_ID));
    }

    @Test
    void getContactInfo_UserNotFoundException() {
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getContactInfo(INVALID_USER_ID));
    }

    @Test
    void checkUsersForApprover_success() throws Exception {
        val user = new com.reece.platform.accounts.model.entity.User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        List<User> userList = new ArrayList<>();
        userList.add(user);

        var account = new Account();
        account.setErp(ErpEnum.ECLIPSE);

        when(userDAO.findAllByApproverId(any())).thenReturn(userList);
        when(accountDAO.findById(any())).thenReturn(Optional.of(account));

        var userDTO = new UsersForApproverDTO();
        userDTO.setEmail(EMAIL);
        userDTO.setFirstName(FIRST_NAME);
        userDTO.setLastName(LAST_NAME);
        List<UsersForApproverDTO> userListDTO = new ArrayList<>();
        userListDTO.add(userDTO);

        assertEquals(userListDTO, usersService.checkUsersForApprover(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void checkUsersForApprover_AccountNotFoundException() {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(
            AccountNotFoundException.class,
            () -> usersService.checkUsersForApprover(UUID.randomUUID(), UUID.randomUUID())
        );
    }
}
