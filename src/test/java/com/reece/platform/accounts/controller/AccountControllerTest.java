package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.service.*;
import com.reece.platform.accounts.utilities.DecodedToken;
import com.reece.platform.accounts.utils.Constants;
import io.jaegertracing.internal.utils.Http;
import java.util.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

@SpringBootTest(classes = { AccountController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountController controller;

    @MockBean
    private AccountRequestDAO accountRequestDAO;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ErpService erpService;

    @MockBean
    private AccountDAO accountDAO;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private InvitedUserDAO invitedUserDAO;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserInviteService userInviteService;

    @MockBean
    private UsersService usersService;

    private CreateUserDTO newUser;
    private CreateUserDTO existingUser;
    private CreateUserDTO termsUnacceptedUser;

    private DecodedToken token;
    private DecodedToken badToken;

    private UUID existingUserId = new UUID(0, 0);
    private UUID badUserId = new UUID(0, 0);

    private static final String TEST_EMAIL = "test@test.com";
    private static final UUID TEST_UUID = UUID.randomUUID();

    private List<Role> roles;
    private Role role;

    private static final String ROLE_NAME = "Role name";
    private static final String ROLE_DESC = "Role desc";
    private static final String SUCCESS_STRING = "success";

    private JobFormDTO jobFormDTO;

    @BeforeEach
    public void setup() throws Exception {
        newUser = new CreateUserDTO();
        newUser.setTosAccepted(true);
        newUser.setPpAccepted(true);
        existingUser = new CreateUserDTO();
        existingUser.setTosAccepted(true);
        existingUser.setPpAccepted(true);
        termsUnacceptedUser = new CreateUserDTO();
        termsUnacceptedUser.setTosAccepted(false);
        termsUnacceptedUser.setPpAccepted(false);

        jobFormDTO = new JobFormDTO();

        roles = new ArrayList<>();

        role = new Role();
        role.setName(ROLE_NAME);
        role.setDescription(ROLE_DESC);
        role.setId(UUID.randomUUID());
        role.setPermissions(new HashSet<>());
        roles.add(role);

        token = mock(DecodedToken.class);
        badToken = mock(DecodedToken.class);

        existingUser.setEmail(TEST_EMAIL);

        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setVerified(false);
        accountRequestDTO.setEmployee(false);
        accountRequestDTO.setId(TEST_UUID);

        when(accountService.createAccount(newUser, null)).thenReturn(accountRequestDTO);
        when(accountService.createAccount(termsUnacceptedUser, null)).thenThrow(new TermsNotAcceptedException());
        when(accountService.getRoles()).thenReturn(roles);

        when(accountService.createJobForm(jobFormDTO)).thenReturn(SUCCESS_STRING);
    }

    @Test
    void validateAccount_validRequest() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccount(any())).thenReturn(new AccountValidationResponseDTO("valid", true));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccount(request);
        assertEquals(responseEntity.getBody().getAccountName(), "valid");
        verify(accountService, times(1)).validateAccount(request);
    }

    @Test
    void validateAccount_validRequestWithTraderAccountValidationOK() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccount(any())).thenReturn(new AccountValidationResponseDTO("valid", true));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccount(request);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getAccountName(), "valid");
        assertTrue(responseEntity.getBody().getIsTradeAccount());
        verify(accountService, times(1)).validateAccount(request);
    }

    @Test
    void validateAccount_validRequestWithTraderAccountValidationNonOK() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccount(any())).thenReturn(new AccountValidationResponseDTO("valid", false));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccount(request);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getAccountName(), "valid");
        assertFalse(responseEntity.getBody().getIsTradeAccount());
        verify(accountService, times(1)).validateAccount(request);
    }

    @Test
    void validateAccount_accountNotFound() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccount(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.validateAccount(request));
    }

    @Test
    void validateAccountNew_validRequest() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccountNew(any())).thenReturn(new AccountValidationResponseDTO("valid", true));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccountNew(request);
        assertEquals(responseEntity.getBody().getAccountName(), "valid");
        verify(accountService, times(1)).validateAccountNew(request);
    }

    @Test
    void validateAccountNew_validRequestWithTraderAccountValidationOK() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccountNew(any())).thenReturn(new AccountValidationResponseDTO("valid", true));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccountNew(request);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getAccountName(), "valid");
        assertTrue(responseEntity.getBody().getIsTradeAccount());
        verify(accountService, times(1)).validateAccountNew(request);
    }

    @Test
    void validateAccountNew_validRequestWithTraderAccountValidationNonOK() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccountNew(any())).thenReturn(new AccountValidationResponseDTO("valid", false));
        ResponseEntity<AccountValidationResponseDTO> responseEntity = controller.validateAccountNew(request);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getAccountName(), "valid");
        assertFalse(responseEntity.getBody().getIsTradeAccount());
        verify(accountService, times(1)).validateAccountNew(request);
    }

    @Test
    void validateAccountNew_accountNotFound() throws Exception {
        var request = new AccountValidationRequestDTO();
        request.setAccountNumber("123456");
        request.setBrand("Reece");
        request.setZipcode("12345");

        when(accountService.validateAccountNew(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.validateAccountNew(request));
    }

    @Test
    void createUserAccount_newUser() throws Exception {
        ResponseEntity<AccountRequestDTO> responseEntity = controller.createUserAccount(newUser, null);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK, "Expected OK response on new user");
        assertNotNull(
            responseEntity.getBody(),
            "Expected body from create user with non-existing email to be non-null."
        );
        assertEquals(
            responseEntity.getBody().getId(),
            TEST_UUID,
            "Expected body from create user with non-existing email to be indicate success."
        );
    }

    @Test
    void createJobForm_success() throws Exception {
        ResponseEntity<String> responseEntity = controller.createJobForm(new JobFormDTO());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK, "Expected OK response on create job form");
        assertNotNull(responseEntity.getBody(), "Expected body from create job form to not be null.");
        assertEquals(
            responseEntity.getBody(),
            SUCCESS_STRING,
            "Expected body from create job form to equal mocked response."
        );
    }

    @Test
    void syncUser_success() throws Exception {
        doNothing().when(usersService).syncUserProfileToAuthProvider(eq(existingUserId));
        MvcResult result = this.mockMvc.perform(post(String.format("/account/%s/sync", existingUserId))).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    void syncUser_notFound() throws Exception {
        doThrow(new NoSuchElementException()).when(usersService).syncUserProfileToAuthProvider(eq(badUserId));
        MvcResult result = this.mockMvc.perform(post(String.format("/account/%s/sync", badUserId))).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getErpAccount_success() throws Exception {
        when(accountService.getErpAccount(anyString(), anyString(), anyString())).thenReturn(new ArrayList<>());
        ResponseEntity<List<ErpAccountInfo>> response = controller.getErpAccount(null, "123", "Reece");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getErpAccount_notFound() throws Exception {
        when(accountService.getErpAccount(any(), any(), anyString())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.getErpAccount(null, "123", "Reece"));
    }

    @Test
    void deleteUser_success() throws Exception {
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(true);
        ResponseEntity<Boolean> response = controller.deleteUser(
            new UUID(0, 0),
            new UUID(0, 0),
            false,
            "Bearer " + Constants.authToken("")
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deleteUser_userUnauthorizedException() throws Exception {
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(false);
        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.deleteUser(new UUID(0, 0), new UUID(0, 0), false, "Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void updateUser_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(new UUID(0, 0));
        userDTO.setEmail("wow");
        userDTO.setFirstName("Me");
        userDTO.setLastName("yeah");
        userDTO.setPhoneNumber("123235235");
        User user = new User();
        HashSet accountsSet = new HashSet();
        Account account = new Account();
        account.setId(new UUID(0, 0));
        accountsSet.add(account);
        user.setBillToAccounts(accountsSet);

        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(true);
        when(usersService.updateUser(any(), any(), any())).thenReturn(new UserDTO());
        when(erpService.updateErpUser(any(), any(), any())).thenReturn(null);

        ResponseEntity<UserDTO> res = controller.updateUser(
            new UUID(0, 0),
            new UUID(0, 0),
            userDTO,
            "Bearer " + Constants.authToken("")
        );
        verify(erpService, times(1)).updateErpUser(any(), any(), any());
        verify(usersService, times(1)).updateUser(any(), any(), any());
    }

    @Test
    void updateUser_notFound() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(new UUID(0, 0));
        userDTO.setEmail("wow");
        userDTO.setFirstName("Me");
        userDTO.setLastName("yeah");
        userDTO.setPhoneNumber("123235235");
        User user = new User();
        HashSet accountsSet = new HashSet();
        Account account = new Account();
        account.setId(new UUID(0, 0));
        accountsSet.add(account);
        user.setBillToAccounts(accountsSet);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(true);
        when(erpService.updateErpUser(any(), any(), any())).thenReturn(null);
        when(usersService.updateUser(any(), any(), any())).thenThrow(new UserNotFoundException());

        assertThrows(
            UserNotFoundException.class,
            () -> controller.updateUser(new UUID(0, 0), new UUID(0, 0), userDTO, "Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void updateUser_failure() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(new UUID(0, 0));
        userDTO.setEmail("wow");
        userDTO.setFirstName("Me");
        userDTO.setLastName("yeah");
        userDTO.setPhoneNumber("123235235");
        User user = new User();
        HashSet accountsSet = new HashSet();
        Account account = new Account();
        account.setId(new UUID(0, 0));
        accountsSet.add(account);
        user.setBillToAccounts(accountsSet);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(false);
        when(erpService.updateErpUser(any(), any(), any())).thenReturn(null);
        when(usersService.updateUser(any(), any(), any())).thenThrow(new UserNotFoundException());

        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.updateUser(new UUID(0, 0), new UUID(0, 0), userDTO, "Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void getAccountUsers_success() throws Exception {
        when(accountService.getAccountUsers(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<ApiUserResponseDTO>> response = controller.getAccountUsers(UUID.randomUUID());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getAccountUsers_notFound() throws Exception {
        when(accountService.getAccountUsers(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.getAccountUsers(UUID.randomUUID()));
    }

    @Test
    void getApprovers_success() throws Exception {
        when(accountService.getAccountUsers(any())).thenReturn(new ArrayList<>());
        var response = controller.getApprovers(UUID.randomUUID());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getApprovers_accountNotFound() throws Exception {
        when(accountService.getAccountUsers(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.getAccountUsers(UUID.randomUUID()));
    }

    @Test
    void getAccountRequests_invalidPermissions() throws Exception {
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        assertThrows(
            InvalidPermissionsException.class,
            () -> controller.getAccountRequests(UUID.randomUUID(), "Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void getAccountRequests_success() throws Exception {
        when(accountService.getUnapprovedAccountRequests(any())).thenReturn(new ArrayList<>());
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);

        var response = controller.getAccountRequests(UUID.randomUUID(), "Bearer " + Constants.authToken(""));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getAllAccountRequests_invalidPermissions() throws Exception {
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        assertThrows(
            InvalidPermissionsException.class,
            () -> controller.getAllAccountRequests("Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void getAllAccountRequests_success() throws Exception {
        when(accountService.getAllUnapprovedAccountRequests()).thenReturn(new ArrayList<>());
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);

        var response = controller.getAllAccountRequests("Bearer " + Constants.authToken(""));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void invitePreApprovedUser_success() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setEmail("preapproved@morsco.com");
        inviteUserDTO.setFirstName("firstName");
        inviteUserDTO.setLastName("lastName");
        inviteUserDTO.setUserRoleId(new UUID(0, 0));
        inviteUserDTO.setBillToAccountId(new UUID(0, 0));
        inviteUserDTO.setApproverId(new UUID(0, 0));
        inviteUserDTO.setErpAccountId("123123");

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));
        when(invitedUserDAO.findByEmail(any())).thenReturn(Optional.empty());

        ResponseEntity<InvitedUser> response = controller.invitePreApprovedUser(
            "Bearer " + Constants.authToken(""),
            inviteUserDTO
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        verify(userInviteService, times(1)).inviteUser(any());
    }

    @Test
    void invitePreApprovedUser_userUnauthorized() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setEmail("preapproved@morsco.com");
        inviteUserDTO.setFirstName("firstName");
        inviteUserDTO.setLastName("lastName");
        inviteUserDTO.setUserRoleId(new UUID(0, 0));
        inviteUserDTO.setBillToAccountId(new UUID(0, 0));
        inviteUserDTO.setApproverId(new UUID(0, 0));
        inviteUserDTO.setErpAccountId("123123");

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);

        UserUnauthorizedException exception = assertThrows(
            UserUnauthorizedException.class,
            () -> controller.invitePreApprovedUser("Bearer " + Constants.authToken(""), inviteUserDTO)
        );
        assertEquals(
            exception.getMessage(),
            String.format("{\"error\":\"Current user is unauthorized to perform this action.\"}")
        );
    }

    @Test
    void invitePreApprovedUser_billToAccountNotFound() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setEmail("preapproved@morsco.com");
        inviteUserDTO.setFirstName("firstName");
        inviteUserDTO.setLastName("lastName");
        inviteUserDTO.setUserRoleId(new UUID(0, 0));
        inviteUserDTO.setBillToAccountId(new UUID(0, 0));
        inviteUserDTO.setApproverId(new UUID(0, 0));
        inviteUserDTO.setErpAccountId("123123");

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        when(accountDAO.findById(any())).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
            AccountNotFoundException.class,
            () -> controller.invitePreApprovedUser("Bearer " + Constants.authToken(""), inviteUserDTO)
        );
        assertEquals(exception.getMessage(), "{\"error\":\"Account with given ID not found.\"}");
    }

    @Test
    void invitePreApprovedUser_userInviteAlreadyInvited() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setEmail("preapproved@morsco.com");
        inviteUserDTO.setFirstName("firstName");
        inviteUserDTO.setLastName("lastName");
        inviteUserDTO.setUserRoleId(new UUID(0, 0));
        inviteUserDTO.setBillToAccountId(new UUID(0, 0));
        inviteUserDTO.setApproverId(new UUID(0, 0));
        inviteUserDTO.setErpAccountId("123123");

        InvitedUser invitedUser = new InvitedUser(inviteUserDTO);

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));
        when(invitedUserDAO.findByEmail(any())).thenReturn(Optional.of(invitedUser));

        UserInviteAlreadyExistsException exception = assertThrows(
            UserInviteAlreadyExistsException.class,
            () -> controller.invitePreApprovedUser("Bearer " + Constants.authToken(""), inviteUserDTO)
        );
        assertEquals(
            exception.getMessage(),
            String.format(
                "{\"error\":\"Unable to invite user.  User with email %s has already been invited.\"}",
                inviteUserDTO.getEmail()
            )
        );
    }

    @Test
    void invitePreApprovedUser_userAlreadyExists() throws Exception {
        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setEmail("preapproved@morsco.com");
        inviteUserDTO.setFirstName("firstName");
        inviteUserDTO.setLastName("lastName");
        inviteUserDTO.setUserRoleId(new UUID(0, 0));
        inviteUserDTO.setBillToAccountId(new UUID(0, 0));
        inviteUserDTO.setApproverId(new UUID(0, 0));
        inviteUserDTO.setErpAccountId("123123");

        InvitedUser invitedUser = new InvitedUser(inviteUserDTO);
        User existingUser = new User();
        existingUser.setEmail(invitedUser.getEmail());

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        when(accountDAO.findById(any())).thenReturn(Optional.of(new Account()));
        when(userDAO.findByEmail(any())).thenReturn(Optional.of(existingUser));

        UserAlreadyExistsInviteUserException exception = assertThrows(
            UserAlreadyExistsInviteUserException.class,
            () -> controller.invitePreApprovedUser("Bearer " + Constants.authToken(""), inviteUserDTO)
        );
        assertEquals(
            exception.getMessage(),
            String.format(
                "{\"error\":\"Unable to invite user.  User with email %s already exists.\"}",
                inviteUserDTO.getEmail()
            )
        );
    }

    @Test
    void getWillCallLocations_success() throws Exception {
        UUID shipToAccountId = UUID.randomUUID();
        BranchDTO branchDTO = new BranchDTO();
        List<BranchDTO> mockedBranchList = Collections.singletonList(branchDTO);
        when(accountService.getWillCallLocations(shipToAccountId)).thenReturn(mockedBranchList);
        ResponseEntity<List<BranchDTO>> branchDTOS = controller.getWillCallLocations(shipToAccountId);
        assertEquals(branchDTOS.getBody(), mockedBranchList, "Expected mocked DTOs to return");
        assertEquals(branchDTOS.getStatusCode(), HttpStatus.OK, "Expected OKAY response to return");
    }

    @Test
    void getWillCallLocations_branchNotFoundException() throws Exception {
        UUID shipToAccountId = UUID.randomUUID();
        when(accountService.getWillCallLocations(shipToAccountId)).thenThrow(new BranchNotFoundException());
        assertThrows(BranchNotFoundException.class, () -> controller.getWillCallLocations(shipToAccountId));
    }

    @Test
    void getWillCallLocations_accountNotFoundException() throws Exception {
        UUID shipToAccountId = UUID.randomUUID();
        when(accountService.getWillCallLocations(shipToAccountId)).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.getWillCallLocations(shipToAccountId));
    }

    @Test
    void getAccountByErpId_accountNotFoundException() throws Exception {
        when(accountService.getAccountByErpId(anyString(), any())).thenThrow(new AccountNotFoundException());
        mockMvc.perform(get("/account/11362/find-ecomm-bill-to-id/ECLIPSE")).andExpect(status().isNotFound());
    }

    @Test
    void searchEntity_Success() throws Exception {
        EntitySearchResponseDTO resultDTO = new EntitySearchResponseDTO();
        resultDTO.setIsBillTo(true);
        ResponseEntity<EntitySearchResponseDTO> res = new ResponseEntity<EntitySearchResponseDTO>(
            resultDTO,
            HttpStatus.OK
        );
        when(accountService.searchEntity(anyString())).thenReturn(res);
        var result = controller.searchEntity(anyString());
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void searchEntity_notFound() throws Exception {
        when(accountService.searchEntity(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.searchEntity("123"));
    }

    @Test
    void billToAccountSync_Success() throws Exception {
        List<ShipToAccountDTO> resListShipToAccountDTO = new ArrayList<>();
        ShipToAccountDTO shipToAccountDTO = new ShipToAccountDTO();
        shipToAccountDTO.setId(UUID.randomUUID());

        ShipToAccountDTO shipToAccountDTO1 = new ShipToAccountDTO();
        shipToAccountDTO1.setId(UUID.randomUUID());

        ResponseEntity<List<ShipToAccountDTO>> res = new ResponseEntity<List<ShipToAccountDTO>>(
            resListShipToAccountDTO,
            HttpStatus.OK
        );

        UUID TEST_UUID = UUID.randomUUID();
        String TEST_ACCOUNT_NUMBER = "123";
        String shipToKeep = "erpAccountKeep";
        // Ecomm data set-up
        Account billToEcomm = new Account();
        billToEcomm.setId(UUID.randomUUID());

        List<Account> shipToAccountFromEcomm = new ArrayList<>();

        Account shipToEcomm = new Account();
        // Data is set on objects in reverse order from instantiation
        shipToEcomm.setId(UUID.randomUUID());
        shipToAccountFromEcomm.add(shipToEcomm);
        billToEcomm.setShipToAccounts(shipToAccountFromEcomm);
        billToEcomm.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToEcomm.setErp(ErpEnum.ECLIPSE);

        // ERP data set-up
        ErpAccountInfo billToAccountFromErp = new ErpAccountInfo();
        ErpAccountInfo shipToAccountFromErp = new ErpAccountInfo();
        shipToAccountFromErp.setErpAccountId(shipToKeep);
        billToAccountFromErp.setErpAccountId(TEST_ACCOUNT_NUMBER);
        billToAccountFromErp.setBranchId("100");

        when(erpService.getEclipseAccount(eq(TEST_ACCOUNT_NUMBER), eq(false), eq(false)))
            .thenReturn(billToAccountFromErp);
        when(accountDAO.findById(any())).thenReturn(Optional.of(billToEcomm));
        billToEcomm.setBranchId(billToAccountFromErp.getBranchId());

        when(accountService.syncShipToAccount(UUID.randomUUID())).thenReturn(resListShipToAccountDTO);

        var result = controller.billToAccountSync(UUID.randomUUID());
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void billToAccountSync_failure() throws Exception {
        when(accountService.syncShipToAccount(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> controller.billToAccountSync(UUID.randomUUID()));
    }

    @Test
    void checkUsersForApprover_Succes() throws Exception {
        List<UsersForApproverDTO> usersList = new ArrayList<>();
        UUID userID = UUID.randomUUID();
        UUID accountID = UUID.randomUUID();
        ResponseEntity<List<UsersForApproverDTO>> res = new ResponseEntity<List<UsersForApproverDTO>>(
            usersList,
            HttpStatus.OK
        );
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(true);
        when(usersService.checkUsersForApprover(any(), any())).thenReturn(usersList);
        var result = controller.checkUsersForApprover(accountID, userID, "Bearer " + Constants.authToken(""));
        assertEquals(res.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void checkUsersForApprover_UserUnauthorizedException() throws Exception {
        when(authenticationService.userCanManageUsers(any(), any(), any())).thenReturn(false);
        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.checkUsersForApprover(new UUID(0, 0), new UUID(0, 0), "Bearer " + Constants.authToken(""))
        );
    }

    @Test
    void approveUserAccount_Success() throws Exception {
        ApproveUserDTO approveUserDTO = new ApproveUserDTO();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();
        ApproveUser approverUser = new ApproveUser();
        approverUser.setUserId(userID);
        approveUserDTO.setUser(approverUser);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        Optional<AccountRequest> opt = Optional.of(accountRequest);

        doNothing().when(usersService).approveUser(any());
        when(accountRequestDAO.findById(UUID.fromString(userID))).thenReturn(opt);
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        var result = controller.approveUserAccount(approveUserDTO, "Bearer " + Constants.authToken(userID));
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void approveUserAccount_Failure() throws Exception {
        ApproveUserDTO approveUserDTO = new ApproveUserDTO();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();
        ApproveUser approverUser = new ApproveUser();
        approverUser.setUserId(userID);
        approveUserDTO.setUser(approverUser);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        Optional<AccountRequest> opt = Optional.of(accountRequest);

        when(accountRequestDAO.findById(UUID.fromString(userID))).thenReturn(opt);
        doNothing().when(usersService).approveUser(any());
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.approveUserAccount(approveUserDTO, "Bearer " + Constants.authToken(userID))
        );
    }

    @Test
    void rejectUserAccount_Success() throws Exception {
        RejectUserDTO rejectUserDTO = new RejectUserDTO();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();
        rejectUserDTO.setRejectionReasonEnum(RejectionReasonEnum.NOT_A_COMPANY_MEMBER);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        Optional<AccountRequest> opt = Optional.of(accountRequest);

        when(accountRequestDAO.findById(accountID)).thenReturn(opt);
        doNothing().when(usersService).rejectUser(any(), any(), any(), any());
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        var result = controller.rejectUserAccount("Bearer " + Constants.authToken(userID), accountID, rejectUserDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void rejectUserAccount_Failure() throws Exception {
        RejectUserDTO rejectUserDTO = new RejectUserDTO();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();
        rejectUserDTO.setRejectionReasonEnum(RejectionReasonEnum.NOT_A_COMPANY_MEMBER);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        Optional<AccountRequest> opt = Optional.of(accountRequest);

        when(accountRequestDAO.findById(accountID)).thenReturn(opt);
        doNothing().when(usersService).rejectUser(any(), any(), any(), any());
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.rejectUserAccount("Bearer " + Constants.authToken(userID), accountID, rejectUserDTO)
        );
    }

    @Test
    void getRejectedAccountRequests_Success() throws Exception {
        List<AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        listAccountRequest.add(accountRequest);

        Optional<AccountRequest> opt = Optional.of(accountRequest);

        when(accountService.getRejectedAccountRequests(accountID)).thenReturn(listAccountRequest);
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        var result = controller.getRejectedAccountRequests(accountID, "Bearer " + Constants.authToken(userID));
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getRejectedAccountRequests_Failure() throws Exception {
        List<AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountId(accountID);
        listAccountRequest.add(accountRequest);

        Optional<AccountRequest> opt = Optional.of(accountRequest);

        when(accountService.getRejectedAccountRequests(accountID)).thenReturn(listAccountRequest);
        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        assertThrows(
            InvalidPermissionsException.class,
            () -> controller.getRejectedAccountRequests(accountID, "Bearer " + Constants.authToken(userID))
        );
    }

    @Test
    void getRoles_Success() throws Exception {
        List<AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
        String userID = UUID.randomUUID().toString();
        UUID accountID = UUID.randomUUID();
        List<Role> roleList = new ArrayList<>();
        Role role1 = new Role();
        role1.setName("Admin");
        roleList.add(role1);

        when(accountService.getRoles()).thenReturn(roleList);
        var result = controller.getRoles();
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getHomeBranch_Success() throws Exception {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId("123");
        UUID shipToAccountId = UUID.randomUUID();

        when(accountService.getHomeBranch(shipToAccountId)).thenReturn(branchDTO);
        var result = controller.getHomeBranch(shipToAccountId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void inviteAccountOwner_Success() throws Exception {
        InvitedUser invitedUser = new InvitedUser();
        UUID userID = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        String accountID = "123";
        invitedUser.setId(userID);
        InviteAccountOwnerDTO inviteAccountOwnerDTO = new InviteAccountOwnerDTO();
        inviteAccountOwnerDTO.setErpAccountId(accountID);

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(true);
        when(userInviteService.inviteAccountOwner(inviteAccountOwnerDTO)).thenReturn(invitedUser);
        var result = controller.inviteAccountOwner("Bearer " + Constants.authToken(userId), inviteAccountOwnerDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void inviteAccountOwner_failure() throws Exception {
        InvitedUser invitedUser = new InvitedUser();
        UUID userID = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        String accountID = "123";
        invitedUser.setId(userID);
        InviteAccountOwnerDTO inviteAccountOwnerDTO = new InviteAccountOwnerDTO();
        inviteAccountOwnerDTO.setErpAccountId(accountID);

        when(authenticationService.userCanManageAccountRequests(any(), any())).thenReturn(false);
        when(userInviteService.inviteAccountOwner(inviteAccountOwnerDTO)).thenReturn(invitedUser);
        assertThrows(
            UserUnauthorizedException.class,
            () -> controller.inviteAccountOwner("Bearer " + Constants.authToken(userId), inviteAccountOwnerDTO)
        );
    }

    @Test
    void getEcommShipToId_Success() throws Exception {
        ErpEnum erpEnum = ErpEnum.valueOf("ECLIPSE");
        String shipToAccountId = UUID.randomUUID().toString();
        UUID ecommShipToId = UUID.randomUUID();
        when(accountService.getAccountByErpId(shipToAccountId, erpEnum)).thenReturn(ecommShipToId);
        var result = controller.getEcommShipToId(shipToAccountId, erpEnum);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getEcommBillToId_Success() throws Exception {
        ErpEnum erpEnum = ErpEnum.valueOf("ECLIPSE");
        String shipToAccountId = UUID.randomUUID().toString();
        UUID ecommShipToId = UUID.randomUUID();

        when(accountService.getAccountByErpId(shipToAccountId, erpEnum)).thenReturn(ecommShipToId);
        var result = controller.getEcommBillToId(shipToAccountId, erpEnum);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deleteEcommAccount_Success() throws Exception {
        String billToAccountId = UUID.randomUUID().toString();
        UUID ecommShipToId = UUID.randomUUID();
        DeleteEcommAccountResponseDTO deleteEcommAccountResponseDTO = new DeleteEcommAccountResponseDTO();
        deleteEcommAccountResponseDTO.setSuccess(true);
        when(accountService.deleteEcommAccount(billToAccountId, ErpEnum.ECLIPSE))
            .thenReturn(deleteEcommAccountResponseDTO);
        var result = controller.deleteEcommAccount(billToAccountId);
        assertEquals(result.isSuccess(), true);
    }

    @Test
    void approveUserAccountInternal_Success() throws Exception {
        ApproveUserDTO approveUserDTO = new ApproveUserDTO();
        ApproveUser approveUser = new ApproveUser();
        String userId = UUID.randomUUID().toString();
        approveUser.setUserId(userId);
        approveUserDTO.setUser(approveUser);
        doNothing().when(usersService).approveUser(eq(approveUser));
        var result = controller.approveUserAccountInternal(approveUserDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }
}
