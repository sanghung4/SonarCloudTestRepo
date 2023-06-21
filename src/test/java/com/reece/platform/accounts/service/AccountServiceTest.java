package com.reece.platform.accounts.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.okta.sdk.resource.user.User;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.EntitySearchResult;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import com.reece.platform.accounts.utilities.DecodedToken;
import com.reece.platform.accounts.utilities.ErpUtility;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { AccountService.class })
class AccountServiceTest {

    private CreateUserDTO newUser;
    private CreateUserDTO existingUser;
    private CreateUserDTO termsNotAcceptedUser;
    private DecodedToken token;
    private DecodedToken badToken;

    private static final String MOCK_AUTH_ID = "TEST";

    private static final String TEST_EMAIL_EXISTS = "test@test.com";
    private static final String TEST_EMAIL_ALREADY_APPROVED = "approved@test.com";
    private static final String TEST_EMAIL_NOT_EXISTS = "test1@test.com";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final UUID UUID_ALREADY_APPROVED = UUID.randomUUID();
    private static final UUID BAD_UUID = UUID.randomUUID();
    private static final UUID USER_NOT_FOUND_UUID = UUID.randomUUID();
    private static final UUID ROLE_NOT_FOUND = UUID.randomUUID();
    private static final UUID VALID_ROLE = UUID.randomUUID();
    private static final UUID VALID_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID ACCOUNT_ID_NOT_FOUND = UUID.randomUUID();
    private static final UUID MINCRON_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID ECLIPSE_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID VALID_USER_ID = UUID.randomUUID();
    private static final UUID ECLIPSE_ERP_ID = UUID.randomUUID();
    private static final UUID UNKNOWN_ERP_ID = UUID.randomUUID();
    private static final UUID MINCRON_ERP_ID = UUID.randomUUID();
    public static final String SHIP_TO_ERP_ACCOUNT_ID = "6000";
    public static final String BILL_TO_ERP_ACCOUNT_ID = "6000";
    private static final String VALID_ROLE_NAME = "user";
    private static final String FIRST_NAME = "first";
    private static final String LAST_NAME = "last";
    private static final PhoneTypeEnum MOBILE_PHONE_TYPE = PhoneTypeEnum.MOBILE;
    public static final String EMAIL = "email";

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

    @MockBean
    private AccountRequestDAO accountRequestDAO;

    @MockBean
    private AccountDAO accountDAO;

    @MockBean
    private BranchService branchService;

    @MockBean
    private ErpService erpService;

    @MockBean
    private ErpsUsersDAO erpsUsersDAO;

    @MockBean
    private InvitedUserDAO invitedUserDAO;

    @MockBean
    private RoleDAO roleDAO;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private UsersBillToAccountsDAO usersBillToAccountsDAO;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ProductsService productsService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UsersService usersService;

    @SpyBean
    private AccountService accountService;

    private MockRestServiceServer mockServer;

    @MockBean
    private TaskService taskService;

    private final ArgumentCaptor<AccountRequest> accountRequestArgumentCaptor = ArgumentCaptor.forClass(
        AccountRequest.class
    );

    @SneakyThrows
    @BeforeEach
    public void setup() {
        AccountRequest accountRequest = new AccountRequest();
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setAccountNumber(TEST_ACCOUNT_NUMBER);
        accountInfoDTO.setErpId(ErpEnum.ECLIPSE);
        accountInfoDTO.setCompanyName(BILL_TO_NAME);

        newUser = new CreateUserDTO();
        newUser.setTosAccepted(true);
        newUser.setPpAccepted(true);
        newUser.setPhoneTypeId(MOBILE_PHONE_TYPE);
        newUser.setBranchId(TEST_BRANCH_ID);
        existingUser = new CreateUserDTO();
        existingUser.setTosAccepted(true);
        existingUser.setPpAccepted(true);
        User authUser = mock(User.class);
        newUser.setAccountInfo(accountInfoDTO);
        existingUser.setAccountInfo(accountInfoDTO);
        accountRequest.setAccountId(VALID_ACCOUNT_ID);

        termsNotAcceptedUser = new CreateUserDTO();
        termsNotAcceptedUser.setPpAccepted(false);
        termsNotAcceptedUser.setTosAccepted(false);
        termsNotAcceptedUser.setEmail(TEST_EMAIL_EXISTS);
        termsNotAcceptedUser.setAccountInfo(accountInfoDTO);
        when(authUser.getId()).thenReturn(MOCK_AUTH_ID);

        existingUser.setEmail(TEST_EMAIL_EXISTS);
        newUser.setEmail(TEST_EMAIL_NOT_EXISTS);

        com.reece.platform.accounts.model.entity.User testUser = new com.reece.platform.accounts.model.entity.User();
        testUser.setId(TEST_UUID);

        when(userDAO.save(any(com.reece.platform.accounts.model.entity.User.class))).thenReturn(testUser);
        when(userDAO.findByEmail(TEST_EMAIL_EXISTS))
            .thenReturn(Optional.of(new com.reece.platform.accounts.model.entity.User()));
        when(userDAO.findById(BAD_UUID)).thenReturn(Optional.empty());
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setActingBranchManagerEmail(ACTING_BRANCH_MANAGER_EMAIL);
        branchDTO.setActingBranchManager(ACTING_BRANCH_MANAGER_NAME);
        when(branchService.getBranch(TEST_BRANCH_ID)).thenReturn(branchDTO);
        when(authenticationService.createUser(any(), any())).thenReturn(authUser);

        com.reece.platform.accounts.model.entity.User validUser = new com.reece.platform.accounts.model.entity.User();
        validUser.setId(TEST_UUID);
        validUser.setEmail(TEST_EMAIL_EXISTS);

        when(userDAO.findById(TEST_UUID)).thenReturn(Optional.of(validUser));
        when(roleDAO.findById(VALID_ROLE)).thenReturn(Optional.of(new Role()));
        when(authenticationService.userCanManageAccountRequests(token, TEST_UUID)).thenReturn(true);
        when(accountRequestDAO.findByEmailAndRejectionReasonIsNull(TEST_EMAIL_EXISTS))
            .thenReturn(Optional.of(accountRequest));

        ReflectionTestUtils.setField(accountService, "enableNotificationCalls", true);
        String[] employeeDomains = {
            "morrisonsupply.com",
            "morscohvacsupply.com",
            "expresspipe.com",
            "devoreandjohnson.com",
            "wholesalespecialities.com",
            "expressionshomegallery.com",
            "fortiline.com",
            "morsco.com",
            "reece.com",
        };
        ReflectionTestUtils.setField(accountService, "employeeDomains", employeeDomains);

        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        //productsService = new ProductsService(restTemplateBuilder.build());
        ReflectionTestUtils.setField(productsService, "productServiceUrl", "http://test.com");
    }

    @Test
    void validateAccount_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccount(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccount_success_shipTo() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Reece");
        erpAccountInfo.setShipFlag(true);
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBillToId("1234");

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccount(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccount_accountNotFound() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(erpService.getErpBillToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccount(request));
    }

    @Test
    void validateAccount_accountNotFound_NoBillToAndNoShipTo() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setShipFlag(false);

        when(erpService.getErpBillToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccount(request));
    }

    @Test
    void validateAccount_accountZipDoesNotMatch() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("76543");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        erpAccountInfo.setShipFlag(true);
        erpAccountInfo.setBillToId("12345");

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccount(request));
    }

    @Test
    void validateAccount_traderAccount_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBillToFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccount(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
        assertTrue(response.getIsTradeAccount());
    }

    @Test
    void validateAccount_nonTraderAccount() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        erpAccountInfo.setBranchFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccount(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
        assertFalse(response.getIsTradeAccount());
    }

    @Test
    void validateAccount_nonTraderAccountWithIsBillToFalse() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBranchFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccount(request));
    }

    @Test
    void validateAccountNew_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccountNew_MincronShipTo_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Fortiline Waterworks");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        List<Account> mockAccountList = new ArrayList<Account>();
        Account account = new Account();
        Account paraentAccount = new Account();
        paraentAccount.setErpAccountId("1234");
        account.setParentAccount(paraentAccount);
        mockAccountList.add(account);

        var erp = ErpUtility.getErpFromBrand(request.getBrand());
        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(accountDAO.findByErpAccountIDAndErp(request.getAccountNumber(), erp.name())).thenReturn(mockAccountList);
        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccountNew_MincronBillTo_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Fortiline Waterworks");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        List<Account> mockAccountList = new ArrayList<Account>();
        Account account = new Account();
        mockAccountList.add(account);

        var erp = ErpUtility.getErpFromBrand(request.getBrand());
        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(accountDAO.findByErpAccountIDAndErp(request.getAccountNumber(), erp.name())).thenReturn(mockAccountList);
        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccountNew_success_shipTo() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Reece");
        erpAccountInfo.setShipFlag(true);
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBillToId("1234");

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void validateAccountNew_accountNotFound() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(erpService.getErpBillToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccountNew(request));
    }

    @Test
    void validateAccountNew_accountNotFound_NoBillToAndNoShipTo() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setShipFlag(false);

        when(erpService.getErpBillToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccountNew(request));
    }

    @Test
    void validateAccountNew_accountZipDoesNotMatch() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("76543");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        erpAccountInfo.setShipFlag(true);
        erpAccountInfo.setBillToId("12345");

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccountNew(request));
    }

    @Test
    void validateAccountNew_traderAccount_success() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBillToFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
        assertTrue(response.getIsTradeAccount());
    }

    @Test
    void validateAccountNew_nonTraderAccount() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(true);
        erpAccountInfo.setBranchFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        var response = accountService.validateAccountNew(request);

        assertEquals(response.getAccountName(), erpAccountInfo.getCompanyName());
        assertFalse(response.getIsTradeAccount());
    }

    @Test
    void validateAccountNew_nonTraderAccountWithIsBillToFalse() throws AccountNotFoundException {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("1234");
        request.setBrand("Reece");
        request.setZipcode("12345");

        var erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setZip("12345");
        erpAccountInfo.setCompanyName("Company A");
        erpAccountInfo.setBillToFlag(false);
        erpAccountInfo.setBranchFlag(true);

        when(erpService.getErpBillToAccount(anyString(), any())).thenReturn(erpAccountInfo);
        when(erpService.getErpBillToOrShipToAccount(anyString(), any())).thenReturn(erpAccountInfo);

        assertThrows(AccountNotFoundException.class, () -> accountService.validateAccountNew(request));
    }

    @Test
    void getRejectedAccountRequests_nonexistingAccount() throws AccountNotFoundException {
        when(accountDAO.existsById(any())).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () -> accountService.getRejectedAccountRequests(new UUID(1, 1)));
    }

    @Test
    void getRejectedAccountRequests_existingAccount() throws AccountNotFoundException {
        when(accountDAO.existsById(any())).thenReturn(true);
        when(accountRequestDAO.findAllByRejectionReasonNotNullAndAccountId(any())).thenReturn(any());
        accountService.getRejectedAccountRequests(new UUID(1, 1));
        verify(accountRequestDAO, times(1)).findAllByRejectionReasonNotNullAndAccountId(any());
    }

    @Test
    void syncShipToAccount_accountNotFound() throws AccountNotFoundException {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.syncShipToAccount(new UUID(1, 1)));
    }

    @Test
    void syncShipToAccount_accountFound() throws AccountNotFoundException {
        Account acc = new Account();
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        acc.setErp(ErpEnum.ECLIPSE);
        acc.setShipToAccounts(Arrays.asList(new Account()));
        List<Account> shipTos = Arrays.asList(acc);
        erpAccountInfo.setCompanyName("test");
        when(accountDAO.findById(any())).thenReturn(Optional.of(acc));
        when(accountDAO.findAllByParentAccountId(any())).thenReturn(shipTos);
        when(erpService.getEclipseAccount(any(), anyBoolean(), anyBoolean())).thenReturn(erpAccountInfo);
        when(accountDAO.save(any())).thenReturn(null);
        var result = accountService.syncShipToAccount(new UUID(1, 1));
        assertEquals("null - test - null null, null null", result.get(0).getName());
    }

    @Test
    void findOrCreateAccount_existingAccount() {
        var erpAccount = new ErpAccountInfo();

        var uuid = UUID.randomUUID();
        var account = new Account();
        account.setId(uuid);
        account.setBillto(true);

        List<Account> acct = new ArrayList<>();
        acct.add(account);
        when(accountDAO.findAllByErpAccountIdAndErp(any(), any())).thenReturn(acct);
        var result = accountService.findOrCreateAccount(erpAccount, ErpEnum.ECLIPSE, null);

        assertEquals(account.getId(), result.getId());
    }

    @Test
    void findOrCreateAccount_createAccount() {
        var erpAccount = new ErpAccountInfo();

        var uuid = UUID.randomUUID();
        var account = new Account();
        account.setId(uuid);
        List<Account> acct = new ArrayList<>();
        when(accountDAO.findAllByErpAccountIdAndErp(any(), any())).thenReturn(acct);
        when(accountDAO.save(any())).thenReturn(account);
        var result = accountService.findOrCreateAccount(erpAccount, ErpEnum.ECLIPSE, null);

        assertEquals(account.getId(), result.getId());
    }

    @Test
    void getErpAccount_ecommId() {
        Account account = new Account();
        account.setErpAccountId("123");

        when(accountDAO.findById(any())).thenReturn(Optional.of(account));
        assertDoesNotThrow(() -> accountService.getErpAccount(null, UUID.randomUUID().toString(), ""));
        verify(accountDAO, times(1)).findById(any());
    }

    @Test
    void getErpAccount_ecommId_notFound() {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(
            AccountNotFoundException.class,
            () -> accountService.getErpAccount(null, UUID.randomUUID().toString(), "")
        );
    }

    @Test
    void createAccount_nonExistingUser() throws Exception {
        ErpAccountInfo accountInfo = new ErpAccountInfo();
        accountInfo.setErpAccountId(BILL_TO_ID);
        accountInfo.setCompanyName(BILL_TO_NAME);
        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpAccount(TEST_ACCOUNT_NUMBER, ErpEnum.ECLIPSE)).thenReturn(accountInfo);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));
        AccountRequestDTO userCreated = accountService.createAccount(newUser, null);
        assertEquals(userCreated.getId(), TEST_UUID, "Expected account service to return UUID on non-existing user");
    }

    @Test
    void createAccount_branchManagerEmail() throws Exception {
        ErpAccountInfo accountInfo = new ErpAccountInfo();
        accountInfo.setErpAccountId(BILL_TO_ID);
        accountInfo.setCompanyName(BILL_TO_NAME);
        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpAccount(TEST_ACCOUNT_NUMBER, ErpEnum.ECLIPSE)).thenReturn(accountInfo);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.empty());
        AccountRequestDTO userCreated = accountService.createAccount(newUser, null);
        assertEquals(userCreated.getId(), TEST_UUID, "Expected account service to return UUID on non-existing user");
        verify(notificationService, times(1))
            .sendNewCustomerEmailBranchManager(
                argThat(
                    (
                        newCustomerNotificationDTO ->
                            newCustomerNotificationDTO.getBillToName().equals(BILL_TO_NAME) &&
                            newCustomerNotificationDTO.getManagerFirstName().equals(ACTING_BRANCH_MANAGER_NAME) &&
                            newCustomerNotificationDTO.getEmail().equals(ACTING_BRANCH_MANAGER_EMAIL)
                    )
                )
            );
    }

    @Test
    void createAccount_branchExists() throws Exception {
        ErpAccountInfo accountInfo = new ErpAccountInfo();
        accountInfo.setErpAccountId(BILL_TO_ID);
        accountInfo.setCompanyName(BILL_TO_NAME);
        accountInfo.setBranchId(BILL_TO_BRANCH_ID);
        BranchDTO branch = new BranchDTO();
        val phone = "1234567890";
        branch.setPhone(phone);
        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpAccount(TEST_ACCOUNT_NUMBER, ErpEnum.ECLIPSE)).thenReturn(accountInfo);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));
        when(branchService.getBranch(any())).thenReturn(branch);
        AccountRequestDTO userCreated = accountService.createAccount(newUser, null);
        assertEquals(userCreated.getId(), TEST_UUID, "Expected account service to return UUID on non-existing user");
        verify(accountRequestDAO, times(1)).save(accountRequestArgumentCaptor.capture());
        assertEquals(
            phone,
            accountRequestArgumentCaptor.getValue().getBranchPhoneNumber(),
            "Expected phone number for branch to be set"
        );
    }

    @Test
    void createAccount_multipleAccountsWithSameAccountNumber() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID erpId = UUID.randomUUID();
        List<Account> accounts = new ArrayList<>();
        Account billTo = new Account();
        billTo.setId(accountId);
        billTo.setBillto(true);
        accounts.add(billTo);

        Account shipTo = new Account();
        shipTo.setBillto(false);

        ErpAccountInfo accountInfo = new ErpAccountInfo();
        accountInfo.setErpAccountId(newUser.getAccountInfo().getAccountNumber());
        accountInfo.setCompanyName(newUser.getAccountInfo().getCompanyName());
        Account accountWithAdminUser = buildAccount();

        when(accountDAO.findAllByErpAccountIdAndErp(eq(TEST_ACCOUNT_NUMBER), any())).thenReturn(accounts);
        when(erpService.getErpAccount(newUser.getAccountInfo().getAccountNumber(), ErpEnum.ECLIPSE))
            .thenReturn(accountInfo);
        when(accountDAO.findById(any())).thenReturn(Optional.of(accountWithAdminUser));

        AccountRequestDTO userCreated = accountService.createAccount(newUser, null);
        assertEquals(userCreated.getId(), TEST_UUID, "Expected account service to return UUID on non-existing user");

        verify(accountRequestDAO, times(1)).save(accountRequestArgumentCaptor.capture());
        assertEquals(
            accountRequestArgumentCaptor.getValue().getAccountId(),
            accountId,
            "Expected bill to account to be used when two accounts exist with the same account number"
        );
    }

    /**
     * Validate that sendNewUserAwaitingApprovalEmail is called when accountId is provided and user is not an employee
     */
    @Test
    void createAccount_sendNewUserAwaitingApprovalEmail() throws Exception {
        Account accountWithAdminUser = buildAccount();

        List<Account> accounts = new ArrayList<>();
        accounts.add(accountWithAdminUser);
        ErpAccountInfo accountInfo = new ErpAccountInfo();
        accountInfo.setErpAccountId(BILL_TO_ID);
        accountInfo.setCompanyName(BILL_TO_NAME);

        when(userDAO.findByEmail(TEST_EMAIL_NOT_EXISTS)).thenReturn(Optional.empty());
        when(erpService.getErpAccount(TEST_ACCOUNT_NUMBER, ErpEnum.ECLIPSE)).thenReturn(accountInfo);
        when(accountDAO.findAllByErpAccountIdAndErp(any(), any())).thenReturn(accounts);
        when(accountDAO.findById(any())).thenReturn(Optional.of(accountWithAdminUser));

        assertDoesNotThrow(() -> {
            accountService.createAccount(newUser, null);
            verify(notificationService, times(1))
                .sendNewUserAwaitingApprovalEmail(
                    argThat(newUserDTO ->
                        newUserDTO.getAccountAdmins().get(0).getEmail().equals(EMAIL) &&
                        newUserDTO.getAccountAdmins().get(0).getFirstName().equals(FIRST_NAME)
                    )
                );
        });
    }

    @Test
    void createAccount_existingUser() {
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> accountService.createAccount(existingUser, null)
        );
        assertEquals(
            exception.getMessage(),
            String.format("{\"error\":\"User with email %s already exists.\"}", TEST_EMAIL_EXISTS),
            "Expected status code of exception to be BAD_REQUEST"
        );
    }

    @Test
    void createAccount_termsNotAcceptedUser() {
        TermsNotAcceptedException exception = assertThrows(
            TermsNotAcceptedException.class,
            () -> accountService.createAccount(termsNotAcceptedUser, null)
        );
        assertEquals(
            exception.getMessage(),
            "{\"error\":\"Unable to create user.  User has not accepted terms of service and/or privacy policy.\"}"
        );
    }

    @Test
    void createAccount_preApprovedUser_inviteAlreadyCompleted() throws AccountNotFoundException {
        InvitedUser completedInvite = new InvitedUser();
        completedInvite.setCompleted(true);
        var account = new ErpAccountInfo();
        account.setErpAccountId(BILL_TO_ID);
        account.setCompanyName(BILL_TO_NAME);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(completedInvite));
        when(erpService.getErpAccount(newUser.getAccountInfo().getAccountNumber(), ErpEnum.ECLIPSE))
            .thenReturn(account);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));
        InvalidInviteException exception = assertThrows(
            InvalidInviteException.class,
            () -> accountService.createAccount(newUser, UUID.randomUUID())
        );
        assertEquals(exception.getMessage(), "{\"error\":\"Invalid invite ID.\"}");
    }

    @Test
    void createAccount_preApprovedUser() throws Exception {
        InvitedUser userInvite = new InvitedUser();
        userInvite.setCompleted(false);
        userInvite.setEmail(newUser.getEmail());
        userInvite.setUserRoleId(VALID_ROLE);
        var account = new ErpAccountInfo();
        account.setErpAccountId(BILL_TO_ID);
        account.setCompanyName(BILL_TO_NAME);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(userInvite));
        when(erpService.getErpAccount(newUser.getAccountInfo().getAccountNumber(), ErpEnum.ECLIPSE))
            .thenReturn(account);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));
        Role role = new Role();
        when(roleDAO.findById(VALID_ROLE)).thenReturn(Optional.of(role));
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setErp(ErpEnum.ECLIPSE);
        when(accountRequestDAO.findMostRecentRequestByEmail(anyString())).thenReturn(accountRequest);

        ErpContactCreationResponse erpContactCreationResponse = new ErpContactCreationResponse();
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        when(erpService.createErpAccount(any())).thenReturn(erpContactCreationResponse);

        Account billToAccounts = new Account();
        billToAccounts.setId(UUID.randomUUID());
        billToAccounts.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        billToAccounts.setErp(ErpEnum.ECLIPSE);
        when(accountDAO.findByErpAccountIdAndErp(BILL_TO_ERP_ACCOUNT_ID, ErpEnum.ECLIPSE))
            .thenReturn(Optional.of(billToAccounts));

        ErpAccounts erpAccounts = new ErpAccounts();
        ErpAccountInfo billToInfo = new ErpAccountInfo();
        billToInfo.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        erpAccounts.setBillTo(billToInfo);

        Account billToAccount = new Account();
        when(accountDAO.findById(billToAccounts.getId())).thenReturn(Optional.of(billToAccount));

        when(erpService.getEclipseAccount(BILL_TO_ERP_ACCOUNT_ID, false, false)).thenReturn(erpAccountInfo);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));

        AccountRequestDTO accountRequestDTO = accountService.createAccount(newUser, UUID.randomUUID());
        verify(notificationService, times(0)).sendNewUserAwaitingApprovalEmail(any());
        verify(invitedUserDAO, times(1)).save(any());
    }

    @Test
    void createAccount_preApprovedUser_emailMismatching() throws AccountNotFoundException {
        ErpAccountInfo account = new ErpAccountInfo();
        account.setErpAccountId(BILL_TO_ID);
        account.setCompanyName(BILL_TO_NAME);

        InvitedUser completedInvite = new InvitedUser();
        completedInvite.setEmail("someRandomEmail@email.com");
        completedInvite.setCompleted(false);
        when(invitedUserDAO.findById(any())).thenReturn(Optional.of(completedInvite));
        when(erpService.getErpAccount(BILL_TO_ID, ErpEnum.ECLIPSE)).thenReturn(account);
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.of(new Account()));
        InvalidInviteException exception = assertThrows(
            InvalidInviteException.class,
            () -> accountService.createAccount(newUser, UUID.randomUUID())
        );
        assertEquals(
            exception.getMessage(),
            String.format("{\"error\":\" Email %s does not match invite.\"}", newUser.getEmail())
        );
    }

    @Test
    void getAllUnapprovedAccountRequests_matches() {
        List<AccountRequest> accountRequests = new ArrayList<>();
        AccountRequest accountRequest = new AccountRequest();
        accountRequests.add(accountRequest);

        when(accountDAO.existsById(any())).thenReturn(true);
        when(
            accountRequestDAO.findAllByAccountIdAndIsCompletedAndIsEmployeeAndRejectionReasonNull(
                eq(null),
                eq(false),
                eq(false)
            )
        )
            .thenReturn(accountRequests);

        List<AccountRequest> returnedAccountRequests = accountService.getAllUnapprovedAccountRequests();
        assertEquals(
            returnedAccountRequests,
            accountRequests,
            "Expected returned account requests to equal mocked value on requests without an account id."
        );
    }

    @Test
    void getUnapprovedAccountRequests_accountIdNotFound() {
        when(accountDAO.existsById(ACCOUNT_ID_NOT_FOUND)).thenReturn(false);

        assertThrows(
            AccountNotFoundException.class,
            () -> accountService.getUnapprovedAccountRequests(ACCOUNT_ID_NOT_FOUND)
        );
    }

    @Test
    void getUnapprovedAccountRequests_validAccountId() throws Exception {
        List<AccountRequest> accountRequests = new ArrayList<>();
        when(accountDAO.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        when(accountDAO.existsById(any())).thenReturn(true);
        when(accountRequestDAO.findAllByAccountId(VALID_ACCOUNT_ID)).thenReturn(accountRequests);

        List<AccountRequest> returnedAccountRequests = accountService.getUnapprovedAccountRequests(VALID_ACCOUNT_ID);
        assertEquals(
            returnedAccountRequests,
            accountRequests,
            "Expected returned account requests to equal mocked value on valid account id."
        );
    }

    @Test
    void getUnapprovedAccountRequests_shipToAccounts() throws Exception {
        List<AccountRequest> accountRequests = new ArrayList<>();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(TEST_UUID);
        accountRequests.add(accountRequest);

        Account mockedAccount = new Account();
        List<Account> shipToAccounts = new ArrayList<>();
        Account shipToAccount = new Account();
        shipToAccount.setId(UUID.randomUUID());
        shipToAccounts.add(shipToAccount);
        mockedAccount.setId(TEST_UUID);
        mockedAccount.setShipToAccounts(shipToAccounts);

        when(accountDAO.findById(TEST_UUID)).thenReturn(Optional.of(mockedAccount));
        when(accountDAO.existsById(any())).thenReturn(true);
        when(
            accountRequestDAO.findAllByAccountIdsAndIsCompletedAndIsEmployee(
                argThat(accountIdSet -> accountIdSet.containsAll(Arrays.asList(TEST_UUID, shipToAccount.getId()))),
                eq(false),
                eq(false)
            )
        )
            .thenReturn(accountRequests);

        List<AccountRequest> returnedAccountRequests = accountService.getUnapprovedAccountRequests(TEST_UUID);
        assertEquals(
            returnedAccountRequests,
            accountRequests,
            "Expected returned account requests to equal mocked value with null accountId."
        );
    }

    @Test
    void getRoles_validateRepoCall() {
        List<Role> roleList = new ArrayList<>();
        when(roleDAO.findAll()).thenReturn(roleList);

        List<Role> roles = accountService.getRoles();

        assertEquals(roles, roleList, "Expected repository call to roles DAO in getRoles method");
    }

    @Test
    void getAccountUsers_validAccount() throws Exception {
        UsersBillToAccounts usersBillToAccounts = new UsersBillToAccounts();
        usersBillToAccounts.setUserId(VALID_USER_ID);

        Role role = new Role();
        role.setId(VALID_ROLE);
        role.setName(VALID_ROLE_NAME);

        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setLastModifiedDate(Date.from(Instant.now()));
        erpsUsers.setLastModifiedBy(FIRST_NAME + " " + LAST_NAME);
        Set<ErpsUsers> erpsUsersSet = new HashSet<>();
        erpsUsersSet.add(erpsUsers);

        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setId(VALID_USER_ID);
        user.setEmail(TEST_EMAIL_EXISTS);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setRole(role);
        user.setErpsUsers(erpsUsersSet);

        com.reece.platform.accounts.model.entity.User userWithNoRole = new com.reece.platform.accounts.model.entity.User();
        userWithNoRole.setId(BAD_UUID);
        userWithNoRole.setEmail(TEST_EMAIL_NOT_EXISTS);
        userWithNoRole.setFirstName(FIRST_NAME);
        userWithNoRole.setLastName(LAST_NAME);
        userWithNoRole.setErpsUsers(erpsUsersSet);

        when(accountDAO.existsById(VALID_ACCOUNT_ID)).thenReturn(true);
        when(usersBillToAccountsDAO.findAllByAccountId(VALID_ACCOUNT_ID))
            .thenReturn(Collections.singletonList(usersBillToAccounts));
        when(userDAO.findAllById(Collections.singletonList(VALID_USER_ID))).thenReturn(List.of(user, userWithNoRole));
        when(roleDAO.findById(VALID_ROLE)).thenReturn(Optional.of(role));

        List<ApiUserResponseDTO> accountUserDTOS = accountService.getAccountUsers(VALID_ACCOUNT_ID);

        assertEquals(1, accountUserDTOS.size(), "Expected one user account to return.");
        ApiUserResponseDTO accountUserDTO = accountUserDTOS.get(0);
        assertEquals(
            TEST_EMAIL_EXISTS,
            accountUserDTO.getEmail(),
            "Expected user account to return with mocked email."
        );
        assertEquals(
            FIRST_NAME,
            accountUserDTO.getFirstName(),
            "Expected user account to return with mocked first name."
        );
        assertEquals(LAST_NAME, accountUserDTO.getLastName(), "Expected user account to return with mocked last name.");
        assertEquals(VALID_USER_ID, accountUserDTO.getId(), "Expected user account to return with mocked id.");
        assertEquals(
            role.getId(),
            accountUserDTO.getRole().getId(),
            "Expected user account to return with mocked role."
        );
    }

    @Test
    void getAccountUsers_accountNotFound() {
        when(accountDAO.existsById(ACCOUNT_ID_NOT_FOUND)).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountUsers(ACCOUNT_ID_NOT_FOUND));
    }

    @Test
    void getWillCallLocations_success() {
        String erpAccountId = "55";
        String homeBranchId = "22";
        Account account = new Account();
        account.setErpAccountId(erpAccountId);
        account.setErp(ErpEnum.ECLIPSE);
        account.setId(VALID_ACCOUNT_ID);

        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId(homeBranchId);
        BranchDTO branchDTO = new BranchDTO();

        assertDoesNotThrow(() -> {
            when(accountDAO.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));
            when(erpService.getErpAccount(erpAccountId, ErpEnum.ECLIPSE)).thenReturn(erpAccountInfo);
            when(branchService.getBranch(homeBranchId)).thenReturn(branchDTO);

            List<BranchDTO> branchDTOS = accountService.getWillCallLocations(VALID_ACCOUNT_ID);

            assertEquals(branchDTOS.size(), 1, "Expected branch list size to equal number of mocked branches");
            assertEquals(branchDTOS.get(0), branchDTO, "Expected branch returned to equal mocked branch");
        });
    }

    @Test
    void getWillCallLocations_accountNotFound() {
        when(accountDAO.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getWillCallLocations(VALID_ACCOUNT_ID));
    }

    @Test
    void getWillCallLocations_erpAccountNotFound() throws Exception {
        String erpAccountId = "55";
        Account account = new Account();
        account.setErpAccountId(erpAccountId);
        account.setId(VALID_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        when(accountDAO.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(erpService.getErpAccount(erpAccountId, ErpEnum.ECLIPSE)).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> accountService.getWillCallLocations(VALID_ACCOUNT_ID));
    }

    @Test
    void getWillCallLocations_branchNotFound() throws Exception {
        String erpAccountId = "55";
        String homeBranchId = "22";
        Account account = new Account();
        account.setErpAccountId(erpAccountId);
        account.setId(VALID_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        when(accountDAO.findById(VALID_ACCOUNT_ID)).thenReturn(Optional.of(account));

        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId(homeBranchId);
        when(erpService.getErpAccount(erpAccountId, ErpEnum.ECLIPSE)).thenReturn(erpAccountInfo);
        when(branchService.getBranch(homeBranchId)).thenThrow(new BranchNotFoundException());

        assertThrows(BranchNotFoundException.class, () -> accountService.getWillCallLocations(VALID_ACCOUNT_ID));
    }

    @Test
    void getWillCallLocations_mincron() {
        Account account = new Account();
        account.setErp(ErpEnum.MINCRON);

        assertDoesNotThrow(() -> {
            when(accountDAO.findById(any())).thenReturn(Optional.of(account));

            List<BranchDTO> branchDTOS = accountService.getWillCallLocations(MINCRON_ACCOUNT_ID);
            assertEquals(branchDTOS, new ArrayList<BranchDTO>());
        });
    }

    @Test
    void getHomeBranch_eclipse() {
        Account account = new Account();
        account.setErpAccountId(SHIP_TO_ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.ECLIPSE);
        account.setBillto(true);

        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId(SHIP_TO_BRANCH_ID);

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(SHIP_TO_BRANCH_ID);

        assertDoesNotThrow(() -> {
            when(accountDAO.findById(ECLIPSE_ACCOUNT_ID)).thenReturn(Optional.of(account));
            when(erpService.getEclipseAccount(SHIP_TO_ERP_ACCOUNT_ID, false, false)).thenReturn(erpAccountInfo);
            when(branchService.getBranch(erpAccountInfo.getBranchId())).thenReturn(branchDTO);

            BranchDTO returnedBranch = accountService.getHomeBranch(ECLIPSE_ACCOUNT_ID);

            verify(erpService, times(0)).getMincronAccount(anyString(), anyBoolean(), anyBoolean());
            verify(erpService, times(1)).getEclipseAccount(anyString(), anyBoolean(), anyBoolean());
            assertEquals(
                erpAccountInfo.getBranchId(),
                returnedBranch.getBranchId(),
                "Branch Id from getBranch call should match branch Id on shipTo account"
            );
        });
    }

    @Test
    void getHomeBranch_mincron_shipTo() {
        Account account = new Account();
        account.setErpAccountId(SHIP_TO_ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.MINCRON);

        Account parentAccount = new Account();
        parentAccount.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        parentAccount.setErp(ErpEnum.MINCRON);
        parentAccount.setBillto(true);

        account.setParentAccount(parentAccount);

        ErpAccountInfo billToErpAccountInfo = new ErpAccountInfo();
        billToErpAccountInfo.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        billToErpAccountInfo.setBranchId(BILL_TO_BRANCH_ID);

        ErpAccountInfo shipToErpAccountInfo = new ErpAccountInfo();
        shipToErpAccountInfo.setErpAccountId(SHIP_TO_ERP_ACCOUNT_ID);
        shipToErpAccountInfo.setBranchId(SHIP_TO_BRANCH_ID);

        billToErpAccountInfo.setShipToAccounts(List.of(shipToErpAccountInfo));

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(SHIP_TO_BRANCH_ID);

        assertDoesNotThrow(() -> {
            when(accountDAO.findById(MINCRON_ACCOUNT_ID)).thenReturn(Optional.of(account));
            when(erpService.getMincronAccount(eq(BILL_TO_ERP_ACCOUNT_ID), eq(true), eq(false)))
                .thenReturn(billToErpAccountInfo);
            when(branchService.getBranch(shipToErpAccountInfo.getBranchId())).thenReturn(branchDTO);

            BranchDTO returnedBranch = accountService.getHomeBranch(MINCRON_ACCOUNT_ID);

            verify(erpService, times(1)).getMincronAccount(BILL_TO_ERP_ACCOUNT_ID, true, false);
            verify(erpService, times(0)).getEclipseAccount(anyString(), anyBoolean(), anyBoolean());
            assertEquals(
                shipToErpAccountInfo.getBranchId(),
                returnedBranch.getBranchId(),
                "Branch Id from getBranch call should match branch Id on shipTo account"
            );
        });
    }

    @Test
    void getHomeBranch_mincron_billTo() {
        Account account = new Account();
        account.setErpAccountId(BILL_TO_ERP_ACCOUNT_ID);
        account.setErp(ErpEnum.MINCRON);
        account.setBillto(true);

        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setBranchId(BILL_TO_BRANCH_ID);

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId(BILL_TO_BRANCH_ID);

        assertDoesNotThrow(() -> {
            when(accountDAO.findById(eq(MINCRON_ACCOUNT_ID))).thenReturn(Optional.of(account));
            when(erpService.getMincronAccount(eq(BILL_TO_ERP_ACCOUNT_ID), eq(true), eq(false)))
                .thenReturn(erpAccountInfo);
            when(branchService.getBranch(erpAccountInfo.getBranchId())).thenReturn(branchDTO);

            BranchDTO returnedBranch = accountService.getHomeBranch(MINCRON_ACCOUNT_ID);

            verify(erpService, times(1)).getMincronAccount(BILL_TO_ERP_ACCOUNT_ID, true, false);
            verify(erpService, times(0)).getEclipseAccount(anyString(), anyBoolean(), anyBoolean());
            assertEquals(
                erpAccountInfo.getBranchId(),
                returnedBranch.getBranchId(),
                "Branch Id from getBranch call should match branch Id on billTo account"
            );
        });
    }

    @Test
    void getHomeBranch_AccountNotFound() {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getHomeBranch(MINCRON_ACCOUNT_ID));
    }

    @Test
    public void createJobForm_success() throws Exception {
        UUID accountId = UUID.randomUUID();
        String mockResponse = "success";
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setAccountId(accountId.toString());
        when(erpService.createJobForm(jobFormDTO)).thenReturn(mockResponse);

        String response = accountService.createJobForm(jobFormDTO);
        assertEquals(mockResponse, response);
    }

    @Test
    public void createJobForm_accountNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setAccountId(accountId.toString());
        when(erpService.createJobForm(jobFormDTO)).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> accountService.createJobForm(jobFormDTO));
    }

    @Test
    public void syncErpAccountsToDb_removeShipToIfDeletedFromErp_eclipse() throws Exception {
        String shipToKeep = "erpAccountKeep";
        String shipToDelete = "erpAccountDelete";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        Account shipToDeleteEcomm = new Account();
        // Data is set on objects in reverse order from instantiation
        shipToDeleteEcomm.setErpAccountId(shipToDelete);
        shipToDeleteEcomm.setId(UUID.randomUUID());
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToDeleteEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.ECLIPSE);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp = new ErpAccountInfo();
        shipToAccountFromErp.setErpAccountId(shipToKeep);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);

        when(erpService.getEclipseAccount(eq(TEST_ACCOUNT_NUMBER), eq(false), eq(true)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findAllByErpAccountIdAndParentAccountId(any(), any())).thenReturn(List.of(shipToDeleteEcomm));
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(1)).deleteById(any());
    }

    @Test
    public void syncErpAccountsToDb_removeShipToIfDeletedFromErp_mincron() throws Exception {
        String shipToKeep = "erpAccountKeep";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        Account shipToDeleteEcomm = new Account();
        // Data is set on objects in reverse order from instantiation
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToDeleteEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.MINCRON);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp = new ErpAccountInfo();
        shipToAccountFromErp.setErpAccountId(shipToKeep);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);

        when(erpService.getMincronAccount(eq(TEST_ACCOUNT_NUMBER), eq(true), eq(false)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findAllByErpAccountIdAndParentAccountId(any(), any())).thenReturn(List.of(shipToDeleteEcomm));
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(1)).deleteById(any());
    }

    @Test
    public void syncErpAccountsToDb_noShipTosRemoved_eclipse() throws Exception {
        String shipToKeep = "erpAccountKeep";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        Account shipToEcomm = new Account();
        // Data is set on objects in reverse order from instantiation
        shipToEcomm.setId(UUID.randomUUID());
        shipToEcomm.setErpAccountId(shipToKeep);
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.ECLIPSE);
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp = new ErpAccountInfo();
        shipToAccountFromErp.setErpAccountId(shipToKeep);
        List<ErpAccountInfo> shipToAccountsFromErp = new ArrayList<>();
        shipToAccountsFromErp.add(shipToAccountFromErp);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToAccountFromErp.setShipToAccountIds(List.of(shipToKeep));

        when(erpService.getEclipseAccount(eq(TEST_ACCOUNT_NUMBER), eq(false), eq(true)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findByErpAccountIdAndParentAccountId(any(), any())).thenReturn(Optional.of(shipToEcomm));
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(0)).deleteById(any());
    }

    @Test
    public void syncErpAccountsToDb_noShipTosRemoved_mincron() throws Exception {
        String shipToKeep = "erpAccountKeep";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        Account shipToEcomm = new Account();
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.MINCRON);
        // Data is set on objects in reverse order from instantiation
        shipToEcomm.setId(UUID.randomUUID());
        shipToEcomm.setErpAccountId(shipToKeep);
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp = new ErpAccountInfo();
        shipToAccountFromErp.setErpAccountId(shipToKeep);
        List<ErpAccountInfo> shipToAccountsFromErp = new ArrayList<>();
        shipToAccountsFromErp.add(shipToAccountFromErp);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToAccountFromErp.setShipToAccountIds(List.of(shipToKeep));

        when(erpService.getMincronAccount(eq(TEST_ACCOUNT_NUMBER), eq(true), eq(false)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findByErpAccountIdAndParentAccountId(any(), any())).thenReturn(Optional.of(billToEcomm));
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(0)).delete(any());
    }

    @Test
    public void scrubRole_returnsScrubbedPermissions() throws Exception {
        Method method = AccountService.class.getDeclaredMethod("scrubRole", Role.class);
        method.setAccessible(true);

        Role role = new Role();
        role.setPermissions(Set.of(new Permission()));
        val actualPermissions = method.invoke(accountService, role);
        Field privatePermissions = Role.class.
                getDeclaredField("permissions");

        privatePermissions.setAccessible(true);

        val fieldValue = (Set) privatePermissions.get(actualPermissions);
        assertNull(fieldValue);
    }

    @Test
    public void getApprovers_throwsExceptionWhenAccountNotFound() throws Exception {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getApprovers(new UUID(1,1)));
    }

    @Test
    public void syncERPAccountsToDB_updatesBillToAccountAddress() throws Exception {
        Account maxAccount = new Account();
        Account maxAccountShipToAccount = new Account();
        maxAccount.setId(new UUID(1,1));
        maxAccount.setShipToAccounts(Arrays.asList(maxAccountShipToAccount));
        maxAccount.setErpAccountId("8181818");
        maxAccountShipToAccount.setErpAccountId("9191919");
        maxAccount.setAddress("I should get changed");
        maxAccountShipToAccount.setAddress("I should not get, changed 77777");
        maxAccount.setErp(ErpEnum.ECLIPSE);
        ErpAccountInfo erpAccount = new ErpAccountInfo();
        ErpAccountInfo erpAccountShipTo = new ErpAccountInfo();
        erpAccount.setShipToAccounts(Arrays.asList(erpAccountShipTo));
        erpAccount.setShipToAccountIds(Arrays.asList("9191919"));
        erpAccountShipTo.setErpAccountId("9191919");
        erpAccountShipTo.setStreet1("I should not");
        erpAccountShipTo.setCity("get");
        erpAccountShipTo.setState("changed");
        erpAccountShipTo.setZip("77777");
        erpAccount.setErpAccountId("8181818");
        erpAccount.setStreet1("I");
        erpAccount.setCity("Am");
        erpAccount.setState("Now Changed");
        erpAccount.setZip("77777");

        when(erpService.getEclipseAccount(maxAccount.getErpAccountId(), false, true)).thenReturn(erpAccount);
        when(accountDAO.findById(maxAccount.getId())).thenReturn(Optional.of(maxAccount));

        accountService.syncErpAccountsToDb(maxAccount);

        verify(accountDAO, times(2)).save(any());
        verify(accountDAO, times(1)).saveAll(any());
    }

    @Test
    public void syncERPAccountsToDB_updatesShipToAccountAddress() throws Exception {
        Account maxAccount = new Account();
        Account maxAccountShipToAccount = new Account();
        maxAccount.setId(new UUID(1,1));
        maxAccount.setShipToAccounts(Arrays.asList(maxAccountShipToAccount));
        maxAccount.setErpAccountId("8181818");
        maxAccountShipToAccount.setErpAccountId("9191919");
        maxAccount.setAddress("I should not get, changed 77777");
        maxAccountShipToAccount.setAddress("I should get, changed 77777");
        maxAccount.setErp(ErpEnum.ECLIPSE);
        ErpAccountInfo erpAccount = new ErpAccountInfo();
        ErpAccountInfo erpAccountShipTo = new ErpAccountInfo();
        erpAccount.setShipToAccounts(Arrays.asList(erpAccountShipTo));
        erpAccount.setShipToAccountIds(Arrays.asList("9191919"));
        erpAccountShipTo.setErpAccountId("9191919");
        erpAccountShipTo.setStreet1("I");
        erpAccountShipTo.setCity("Am");
        erpAccountShipTo.setState("Now Changed");
        erpAccountShipTo.setZip("77777");
        erpAccount.setErpAccountId("8181818");
        erpAccount.setStreet1("I should not");
        erpAccount.setCity("get");
        erpAccount.setState("changed");
        erpAccount.setZip("77777");

        when(erpService.getEclipseAccount(maxAccount.getErpAccountId(), false, true)).thenReturn(erpAccount);
        when(accountDAO.findById(maxAccount.getId())).thenReturn(Optional.of(maxAccount));

        accountService.syncErpAccountsToDb(maxAccount);

        verify(accountDAO, times(2)).save(any());
        verify(accountDAO, times(1)).saveAll(any());
    }

    @Test
    public void syncErpAccountsToDb_newShipToAdded_eclipse() throws Exception {
        String shipToKeep = "erpAccountKeep";
        String newShipToAccount = "newShipToAccount";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        Account shipToEcomm = new Account();
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.ECLIPSE);
        // Data is set on objects in reverse order from instantiation
        shipToEcomm.setId(UUID.randomUUID());
        shipToEcomm.setErpAccountId(shipToKeep);
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);
        billToEcomm.setName(newShipToAccount);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp1 = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp2 = new ErpAccountInfo();
        shipToAccountFromErp1.setErpAccountId(shipToKeep);
        shipToAccountFromErp2.setErpAccountId(newShipToAccount);
        List<ErpAccountInfo> shipToAccountsFromErp = new ArrayList<>();
        shipToAccountsFromErp.add(shipToAccountFromErp1);
        shipToAccountsFromErp.add(shipToAccountFromErp2);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToAccountFromErp.setShipToAccountIds(Arrays.asList(shipToKeep, newShipToAccount));

        String newShipToCompanyName = "COMPANY NAME";
        String shipToStreet = "123 street";
        String shipToCity = "city";
        String shipToState = "state";
        String shipToZip = "zip";
        ErpAccountInfo newShiptoErpInfo = new ErpAccountInfo();
        newShiptoErpInfo.setCompanyName(newShipToCompanyName);
        newShiptoErpInfo.setCity(shipToCity);
        newShiptoErpInfo.setStreet1(shipToStreet);
        newShiptoErpInfo.setState(shipToState);
        newShiptoErpInfo.setZip(shipToZip);

        when(erpService.getEclipseAccount(eq(TEST_ACCOUNT_NUMBER), eq(false), eq(true)))
            .thenReturn(billToAccountFromErp);
        when(erpService.getEclipseAccount(eq(newShipToAccount), eq(false), eq(false))).thenReturn(newShiptoErpInfo);
        when(accountDAO.findByErpAccountIdAndParentAccountId(any(), any())).thenReturn(Optional.empty());
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(0)).delete(any());
    }

    @Test
    public void syncErpAccountsToDb_newShipToAdded_mincron() throws Exception {
        String shipToKeep = "erpAccountKeep";
        String newShipToAccount = "newShipToAccount";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.MINCRON);
        Account shipToEcomm = new Account();
        // Data is set on objects in reverse order from instantiation
        shipToEcomm.setId(UUID.randomUUID());
        billToEcomm.setId(TEST_UUID);
        List<Account> shipToAccountFromEcomm = new ArrayList<>();
        shipToAccountFromEcomm.add(shipToEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp1 = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp2 = new ErpAccountInfo();
        shipToAccountFromErp1.setErpAccountId(shipToKeep);
        shipToAccountFromErp2.setErpAccountId(newShipToAccount);
        List<ErpAccountInfo> shipToAccountsFromErp = new ArrayList<>();
        shipToAccountsFromErp.add(shipToAccountFromErp1);
        shipToAccountsFromErp.add(shipToAccountFromErp2);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToAccountFromErp.setShipToAccountIds(Arrays.asList(shipToKeep, newShipToAccount));

        String newShipToCompanyName = "COMPANY NAME";
        String shipToStreet = "123 street";
        String shipToCity = "city";
        String shipToState = "state";
        String shipToZip = "zip";
        ErpAccountInfo newShiptoErpInfo = new ErpAccountInfo();
        newShiptoErpInfo.setCompanyName(newShipToCompanyName);
        newShiptoErpInfo.setCity(shipToCity);
        newShiptoErpInfo.setStreet1(shipToStreet);
        newShiptoErpInfo.setState(shipToState);
        newShiptoErpInfo.setZip(shipToZip);
        newShiptoErpInfo.setErpAccountId(newShipToAccount);
        billToAccountFromErp.setShipToAccounts(List.of(newShiptoErpInfo));

        when(erpService.getMincronAccount(eq(TEST_ACCOUNT_NUMBER), eq(true), eq(false)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findByErpAccountIdAndParentAccountId(any(), any())).thenReturn(Optional.empty());
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));

        accountService.syncErpAccountsToDb(billToEcomm);

        verify(accountDAO, times(0)).delete(any());
        verify(accountDAO, times(2)).save(any());
    }

    @Test
    public void refreshAccounts_success() throws Exception {
        String erpAccountId1 = "123";
        String erpAccountId2 = "321";
        Account account1 = new Account();
        account1.setErpAccountId(erpAccountId1);

        Account account2 = new Account();
        account2.setErpAccountId(erpAccountId2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        String companyName1 = "company name 1";
        String companyName2 = "company name 2";
        String street1 = "street 1";
        String city1 = "city 1";
        String state1 = "state 1";
        String street2 = "street 2";
        String city2 = "city 2";
        String state2 = "state 2";
        String zip1 = "zip 1";
        String zip2 = "zip 2";

        ErpAccountInfo erpAccountInfo1 = new ErpAccountInfo();
        erpAccountInfo1.setCompanyName(companyName1);
        erpAccountInfo1.setStreet1(street1);
        erpAccountInfo1.setState(state1);
        erpAccountInfo1.setCity(city1);
        erpAccountInfo1.setZip(zip1);
        ErpAccountInfo erpAccountInfo2 = new ErpAccountInfo();
        erpAccountInfo2.setCompanyName(companyName2);
        erpAccountInfo2.setStreet1(street2);
        erpAccountInfo2.setState(state2);
        erpAccountInfo2.setCity(city2);
        erpAccountInfo2.setZip(zip2);
        CompletableFuture<Integer> partitionedAccounts = CompletableFuture.completedFuture(1);

        when(accountDAO.findAll()).thenReturn(accounts);
        when(taskService.getEclipseAccounts(accounts)).thenReturn(partitionedAccounts);
        when(erpService.getEclipseAccount(erpAccountId1, false, false)).thenReturn(erpAccountInfo1);
        when(erpService.getEclipseAccount(erpAccountId2, false, false)).thenReturn(erpAccountInfo2);
        accountService.refreshAccounts();
        assertDoesNotThrow(() -> accountService.refreshAccounts());
    }

    @Test
    public void refreshAccounts_accountNotFound() throws Exception {
        String erpAccountId1 = "123";
        String erpAccountId2 = "321";
        Account account1 = new Account();
        account1.setErpAccountId(erpAccountId1);

        Account account2 = new Account();
        account2.setErpAccountId(erpAccountId2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        String companyName1 = "company name 1";
        String street1 = "street 1";
        String city1 = "city 1";
        String state1 = "state 1";
        String zip1 = "zip 1";
        ErpAccountInfo erpAccountInfo1 = new ErpAccountInfo();
        erpAccountInfo1.setCompanyName(companyName1);
        erpAccountInfo1.setStreet1(street1);
        erpAccountInfo1.setState(state1);
        erpAccountInfo1.setCity(city1);
        erpAccountInfo1.setZip(zip1);

        CompletableFuture<Integer> partitionedAccounts = CompletableFuture.completedFuture(1);
        when(accountDAO.findAll()).thenReturn(accounts);
        when(taskService.getEclipseAccounts(accounts)).thenThrow(new NoSuchElementException());
        when(erpService.getEclipseAccount(erpAccountId1, false, false)).thenReturn(erpAccountInfo1);
        when(erpService.getEclipseAccount(erpAccountId2, false, false)).thenThrow(AccountNotFoundException.class);
        assertDoesNotThrow(() -> accountService.refreshAccounts());
    }

    @Test
    public void deleteEcommAccount_accountNotFound() {
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            accountService.deleteEcommAccount(UUID.randomUUID().toString(), ErpEnum.ECLIPSE);
        });
    }

    @Test
    public void deleteEcommAccount_accountNoBillTo() {
        when(accountDAO.findByErpAccountIdAndErp(any(), any())).thenReturn(Optional.empty());
        when(accountDAO.findAllByParentAccountId(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> {
            val response = accountService.deleteEcommAccount(UUID.randomUUID().toString(), ErpEnum.ECLIPSE);
            assertEquals(0, response.getBillToCount());
            assertEquals(0, response.getShipToCount());
            assertEquals(0, response.getUserCount());
        });
    }

    @Test
    public void deleteEcommAccount_success() throws URISyntaxException {
        ReflectionTestUtils.setField(accountService, "enableNotificationCalls", false);

        val billToErpAccountId = "35648";

        val billto = new Account();
        billto.setId(UUID.randomUUID());
        billto.setErpAccountId(billToErpAccountId);
        billto.setBillto(true);
        List<Account> accounts = new ArrayList<>();
        accounts.add(billto);

        // Shiptos
        val shipto1 = new Account();
        shipto1.setId(UUID.randomUUID());

        val shipto2 = new Account();
        shipto2.setId(UUID.randomUUID());

        // Users
        val user1 = new com.reece.platform.accounts.model.entity.User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@test.com");

        val user1ba = new UsersBillToAccounts();
        user1ba.setAccountId(billto.getId());
        user1ba.setUserId(user1.getId());

        val user2 = new com.reece.platform.accounts.model.entity.User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@test.com");

        val user2ba = new UsersBillToAccounts();
        user2ba.setAccountId(billto.getId());
        user2ba.setUserId(user2.getId());

        when(accountDAO.findAllByErpAccountIdAndErp(anyString(), any())).thenReturn(accounts);
        when(accountDAO.findAllByParentAccountId(billto.getId())).thenReturn(List.of(shipto1, shipto2));

        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI("http://test.com/cart")))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(
                withSuccess(
                    "{\"cartsDeleted\": 1, \"lineItemsDeleted\": 5, \"success\": true}",
                    MediaType.APPLICATION_JSON
                )
            );

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto1.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess("{\"deletedCount\": 1, \"success\": true}", MediaType.APPLICATION_JSON));

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto2.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess("{\"deletedCount\": 10, \"success\": true}", MediaType.APPLICATION_JSON));

        when(userDAO.findByBillToAccounts_Id(any())).thenReturn(List.of(user1, user2));
        when(accountDAO.findById(billto.getId())).thenReturn(Optional.of(billto));
        when(userDAO.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userDAO.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user1.getId(), billto.getId()))
            .thenReturn(Optional.of(user1ba));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user2.getId(), billto.getId()))
            .thenReturn(Optional.of(user2ba));
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> {
            val res = accountService.deleteEcommAccount(billToErpAccountId, ErpEnum.ECLIPSE);
            assertEquals(true, res.isSuccess());
            assertEquals(2, res.getUserCount());
            assertEquals(2, res.getShipToCount());
        });

        verify(accountRequestDAO, times(1)).deleteByAccountId(billto.getId());
        verify(invitedUserDAO, times(1)).deleteAllByBillToAccountId(billto.getId());
        verify(accountRequestDAO, times(3)).deleteByAccountId(any());
        verify(invitedUserDAO, times(2)).findByEmail(any());
        verify(accountDAO, times(2)).deleteAll(any());
    }

    @Test
    public void deleteEcommAccount_sameBillToAndShipTo() throws URISyntaxException {
        ReflectionTestUtils.setField(accountService, "enableNotificationCalls", false);

        val billToErpAccountId = "35648";

        val billto = new Account();
        billto.setId(UUID.randomUUID());
        billto.setErpAccountId(billToErpAccountId);
        billto.setBillto(true);
        List<Account> accounts = new ArrayList<>();
        accounts.add(billto);

        // Shiptos
        val shipto1 = new Account();
        shipto1.setId(UUID.randomUUID());
        shipto1.setErpAccountId(billToErpAccountId);

        val shipto2 = new Account();
        shipto2.setId(UUID.randomUUID());

        // Users
        val user1 = new com.reece.platform.accounts.model.entity.User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@test.com");

        val user1ba = new UsersBillToAccounts();
        user1ba.setAccountId(billto.getId());
        user1ba.setUserId(user1.getId());

        val user2 = new com.reece.platform.accounts.model.entity.User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@test.com");

        val user2ba = new UsersBillToAccounts();
        user2ba.setAccountId(billto.getId());
        user2ba.setUserId(user2.getId());

        when(accountDAO.findAllByErpAccountIdAndErp(anyString(), any())).thenReturn(accounts);
        when(accountDAO.findAllByParentAccountId(billto.getId())).thenReturn(List.of(shipto1, shipto2));

        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI("http://test.com/cart")))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(
                withSuccess(
                    "{\"cartsDeleted\": 1, \"lineItemsDeleted\": 5, \"success\": true}",
                    MediaType.APPLICATION_JSON
                )
            );

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto1.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess("{\"deletedCount\": 1, \"success\": true}", MediaType.APPLICATION_JSON));

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto2.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess("{\"deletedCount\": 10, \"success\": true}", MediaType.APPLICATION_JSON));

        when(userDAO.findByBillToAccounts_Id(any())).thenReturn(List.of(user1, user2));
        when(accountDAO.findById(billto.getId())).thenReturn(Optional.of(billto));
        when(userDAO.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userDAO.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user1.getId(), billto.getId()))
            .thenReturn(Optional.of(user1ba));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user2.getId(), billto.getId()))
            .thenReturn(Optional.of(user2ba));
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> {
            val res = accountService.deleteEcommAccount(billToErpAccountId, ErpEnum.ECLIPSE);
            assertEquals(true, res.isSuccess());
            assertEquals(2, res.getUserCount());
            assertEquals(2, res.getShipToCount());
        });

        verify(accountRequestDAO, times(1)).deleteByAccountId(billto.getId());
        verify(invitedUserDAO, times(1)).deleteAllByBillToAccountId(billto.getId());
        verify(accountRequestDAO, times(3)).deleteByAccountId(any());
        verify(invitedUserDAO, times(2)).findByEmail(any());
        verify(accountDAO, times(2)).deleteAll(any());
    }

    @Test
    public void deleteEcommAccount_productServiceError() throws URISyntaxException {
        ReflectionTestUtils.setField(accountService, "enableNotificationCalls", false);

        val billToErpAccountId = "35648";

        val billto = new Account();
        billto.setId(UUID.randomUUID());
        billto.setErpAccountId(billToErpAccountId);
        billto.setBillto(true);
        List<Account> accounts = new ArrayList<>();
        accounts.add(billto);

        // Shiptos
        val shipto1 = new Account();
        shipto1.setId(UUID.randomUUID());

        val shipto2 = new Account();
        shipto2.setId(UUID.randomUUID());

        // Users
        val user1 = new com.reece.platform.accounts.model.entity.User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@test.com");

        val user1ba = new UsersBillToAccounts();
        user1ba.setAccountId(billto.getId());
        user1ba.setUserId(user1.getId());

        val user2 = new com.reece.platform.accounts.model.entity.User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@test.com");

        val user2ba = new UsersBillToAccounts();
        user2ba.setAccountId(billto.getId());
        user2ba.setUserId(user2.getId());

        when(accountDAO.findAllByErpAccountIdAndErp(anyString(), any())).thenReturn(accounts);
        when(accountDAO.findAllByParentAccountId(billto.getId())).thenReturn(List.of(shipto1, shipto2));

        mockServer
            .expect(ExpectedCount.twice(), requestTo(new URI("http://test.com/cart")))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withServerError());

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto1.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withServerError());

        mockServer
            .expect(ExpectedCount.once(), requestTo(new URI("http://test.com/deliveries/" + shipto2.getId())))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withServerError());

        when(userDAO.findByBillToAccounts_Id(any())).thenReturn(List.of(user1, user2));
        when(accountDAO.findById(billto.getId())).thenReturn(Optional.of(billto));
        when(userDAO.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userDAO.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user1.getId(), billto.getId()))
            .thenReturn(Optional.of(user1ba));
        when(usersBillToAccountsDAO.findByUserIdAndAccountId(user2.getId(), billto.getId()))
            .thenReturn(Optional.of(user2ba));
        when(usersBillToAccountsDAO.findAllByUserId(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> {
            val res = accountService.deleteEcommAccount(billToErpAccountId, ErpEnum.ECLIPSE);
            assertEquals(true, res.isSuccess());
            assertEquals(2, res.getUserCount());
            assertEquals(2, res.getShipToCount());
        });

        verify(accountRequestDAO, times(1)).deleteByAccountId(billto.getId());
        verify(invitedUserDAO, times(1)).deleteAllByBillToAccountId(billto.getId());
        verify(accountRequestDAO, times(3)).deleteByAccountId(any());
        verify(invitedUserDAO, times(2)).findByEmail(any());
        verify(accountDAO, times(2)).deleteAll(any());
    }

    @Test
    public void getAccountAdmins_shouldReturnEmptyListIfAccountIdIsNull() {
        val result = accountService.getAccountAdmins(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAccountAdmins_shouldReturnListOfAdminsForValidAccount() {
        val adminRole = new Role();
        adminRole.setName(RoleEnum.ACCOUNT_ADMIN.label);

        val admin1 = new com.reece.platform.accounts.model.entity.User();
        val admin2 = new com.reece.platform.accounts.model.entity.User();
        admin1.setRole(adminRole);
        admin2.setRole(adminRole);

        Account account = new Account();
        account.setUsers(Set.of(admin1, admin2));

        when(accountDAO.findById(any())).thenReturn(Optional.of(account));

        val result = accountService.getAccountAdmins(TEST_UUID);
        assertEquals(2, result.size());
    }

    @Test
    public void getAccountAdmins_shouldReturnEmptyListIfAccountHasNoAdmins() {
        val adminRole = new Role();
        adminRole.setName(RoleEnum.STANDARD_ACCESS.label);

        val user1 = new com.reece.platform.accounts.model.entity.User();
        val user2 = new com.reece.platform.accounts.model.entity.User();
        user1.setRole(adminRole);

        Account account = new Account();
        account.setUsers(Set.of(user1, user2));

        when(accountDAO.findById(any())).thenReturn(Optional.of(account));

        val result = accountService.getAccountAdmins(TEST_UUID);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAccountAdmins_shouldReturnEmptyListIfAccountIsNotFound() {
        when(accountDAO.findById(any())).thenReturn(Optional.empty());

        val result = accountService.getAccountAdmins(TEST_UUID);
        assertTrue(result.isEmpty());
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setId(TEST_UUID);

        Role role = new Role();
        role.setName(RoleEnum.ACCOUNT_ADMIN.label);

        com.reece.platform.accounts.model.entity.User adminUser = new com.reece.platform.accounts.model.entity.User();
        adminUser.setFirstName(FIRST_NAME);
        adminUser.setEmail(EMAIL);
        adminUser.setRole(role);

        Set<com.reece.platform.accounts.model.entity.User> userSet = new HashSet<>();
        userSet.add(adminUser);
        account.setUsers(userSet);

        account.setBillto(true);

        return account;
    }

    @Test
    void searchEntity_success() throws AccountNotFoundException {
        EntitySearchResult resultDTO = new EntitySearchResult();
        resultDTO.setIsBillTo(true);
        ResponseEntity<EntitySearchResult> res = new ResponseEntity<EntitySearchResult>(resultDTO, HttpStatus.OK);
        when(erpService.searchEntity(anyString())).thenReturn(res);
        var result = accountService.searchEntity(anyString());
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void searchEntity_notFound() throws AccountNotFoundException {
        when(erpService.searchEntity(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> accountService.searchEntity(anyString()));
    }

    @Test
    public void updateAccountHomeBranch_success() {
        String branchId = "1001";
        Account account = new Account();
        Account shipTo = new Account();
        account.setShipToAccounts(List.of(shipTo));

        accountService.updateAccountHomeBranch(account, branchId);
        verify(accountDAO, times(1)).save(any());
    }

    @Test
    void getApprovers_success() throws Exception {
        Account account = new Account();
        Role role = new Role();
        role.setName("Standard Access");
        Permission permission = new Permission();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        role.setPermissions(permissionSet);
        account.setErpAccountId(ECLIPSE_ACCOUNT_ID.toString());
        com.reece.platform.accounts.model.entity.User user = new com.reece.platform.accounts.model.entity.User();
        user.setRole(role);
        Set<com.reece.platform.accounts.model.entity.User> userSet = new HashSet<>();
        userSet.add(user);
        account.setUsers(userSet);
        Optional opt = Optional.of(account);
        when(accountDAO.findById(ECLIPSE_ACCOUNT_ID)).thenReturn(opt);
        var response = accountService.getApprovers(ECLIPSE_ACCOUNT_ID);
        assertNotNull(response);
    }
}
