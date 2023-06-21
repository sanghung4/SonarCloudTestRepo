package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.ErpUserInformationDTO;
import com.reece.platform.accounts.service.*;
import com.reece.platform.accounts.utils.Constants;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { UsersController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@TestPropertySource(properties = { "enable_contact_refresh=true" })
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersController usersController;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UsersService usersService;

    @MockBean
    private ErpService erpService;

    @MockBean
    private FeaturesService featuresService;

    @MockBean
    private AuthenticationService authenticationService;

    UUID validUserId = UUID.fromString("d6e76ced-e059-4492-a45f-a4ab4f2de32d");
    UUID invalidUserId = UUID.randomUUID();
    UUID userIdErpNotFound = UUID.randomUUID();
    List<BillToAccountDTO> billTos = new ArrayList<>();
    List<ShipToAccountDTO> shipTos = new ArrayList<>();
    private static final String AUTH =
        "Bearer eyJraWQiOiItYlVObXhLMndvLUR4OFg5elRlb0drTU1DZWFlMW8wRnVjYzdNVWV1QkZ3IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjUzcHBmeHNqYmhoTXVmZVpZZFdHSlZSZzFrbDl4b0lTeXRGUzdsTHkzdEkiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjEwNDc1Mzk3LCJleHAiOjE2MTA0Nzg5OTcsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MWY5MGRvOFRHRnpkR0M0eDciLCJzY3AiOlsiZW1haWwiLCJwcm9maWxlIiwib3BlbmlkIl0sInN1YiI6ImJyYW50LmNhcnRoZWxAZGlhbGV4YS5jb20iLCJlY29tbVVzZXJJZCI6ImQ2ZTc2Y2VkLWUwNTktNDQ5Mi1hNDVmLWE0YWI0ZjJkZTMyZCIsImVjb21tUGVybWlzc2lvbnMiOlsiYXBwcm92ZV91c2Vyc19hY2NvdW50cyJdfQ.WNDiQU96E2XxqaWWs93_F88xA-EsvAw-slU7T7uXwExCxMf7tKtzyr6Z6_5jgiSawznodu-fv599PuF5XJZK0JqzAyPUGGcSz1qEaMr5mDSxH19qsyz44zUhjbgDlJ4cFgmSTrfSvbiBGyXs_4pY94X-zUey_h5oXRNvpPDeugSAl1l1uASE_SMzeJoqE4xZ45yOK8K27pysGvXotEpanZgDN97-Gqc9HYljj86Efhd9LCm_35f9pBQ7qD2BaeIDOL1Zg4NbUtOyDQFiMJaYe4hCwqSAse4ph7Ci47cGHj3yqaZ3HmXUtJiHz9YxOccc0ZEUJaDbJbzro70obV7sTA";

    @BeforeEach
    public void setup() throws Exception {
        ShipToAccountDTO shipTo = new ShipToAccountDTO();
        shipTo.setId(UUID.randomUUID());
        shipTo.setName("test");
        shipTos.add(shipTo);
        BillToAccountDTO billTo = new BillToAccountDTO();
        billTo.setId(UUID.randomUUID());
        billTo.setName("test");
        billTo.setShipTos(shipTos);
        billTos.add(billTo);

        when(usersService.getUserAccounts(validUserId)).thenReturn(billTos);
        when(usersService.getUserAccounts(invalidUserId)).thenThrow(new UserNotFoundException());
        doThrow(new UserNotFoundException()).when(usersService).resendVerificationEmail(invalidUserId, false);
    }

    /**
     * Validate User Email
     */

    @Test
    void validateUserEmail_userAlreadyExists() throws Exception {
        var email = "test@test.com";
        doThrow(new UserAlreadyExistsException(email)).when(usersService).validateUserEmail(email);
        this.mockMvc.perform(get("/users/_validate?email={email}", email)).andExpect(status().isConflict());
    }

    @Test
    void validateUserEmail_emailIsValid() throws Exception {
        var email = "test@test.com";
        doReturn(new EmailValidationDTO()).when(usersService).validateUserEmail(email);
        MvcResult result =
            this.mockMvc.perform(get("/users/_validate?email={email}", email))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        EmailValidationDTO resultObject = new ObjectMapper()
            .readValue(result.getResponse().getContentAsString(), EmailValidationDTO.class);
        assertEquals(true, resultObject.getIsValid());
        assertEquals(false, resultObject.getIsEmployee());
    }

    @Test
    void validateEmployeeEmail_emailIsNotValid() throws Exception {
        var email = "test@test.com";
        doThrow(new UserAlreadyExistsException(email)).when(usersService).validateUserEmail(email);
        this.mockMvc.perform(get("/users/_validate?email={email}", email)).andExpect(status().is4xxClientError());
    }

    @Test
    void validateEmployeeEmail_emailIsValid() throws Exception {
        var email = "test@test.com";
        EmailValidationDTO ev = new EmailValidationDTO();
        ev.setIsEmployee(true);
        ev.setIsValid(true);
        doReturn(ev).when(usersService).validateUserEmail(email);
        MvcResult result =
            this.mockMvc.perform(get("/users/_validate?email={email}", email))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        EmailValidationDTO resultObject = new ObjectMapper()
            .readValue(result.getResponse().getContentAsString(), EmailValidationDTO.class);
        assertEquals(true, resultObject.getIsValid());
        assertEquals(true, resultObject.getIsEmployee());
    }

    /**
     * Create User Endpoint
     */

    @Test
    void createUser_success()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        when(usersService.createUser(userRegistrationDto, null)).thenReturn(new UserDTO());
        var response = usersController.createUser(userRegistrationDto, null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void createUser_withInvite_success()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenReturn(new UserDTO());
        var response = usersController.createUser(userRegistrationDto, inviteId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void createUser_verifyAccountFailed()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenThrow(new VerifyAccountException());

        assertThrows(VerifyAccountException.class, () -> usersController.createUser(userRegistrationDto, inviteId));
    }

    @Test
    void createUser_userAlreadyExists()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenThrow(new UserAlreadyExistsException(""));

        assertThrows(UserAlreadyExistsException.class, () -> usersController.createUser(userRegistrationDto, inviteId));
    }

    @Test
    void createUser_TermsNotAccepted()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenThrow(new TermsNotAcceptedException());

        assertThrows(TermsNotAcceptedException.class, () -> usersController.createUser(userRegistrationDto, inviteId));
    }

    @Test
    void createUser_accountNotFound()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> usersController.createUser(userRegistrationDto, inviteId));
    }

    @Test
    void createUser_invalidInvite()
        throws VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var userRegistrationDto = new UserRegistrationDTO();
        var inviteId = UUID.randomUUID();
        when(usersService.createUser(userRegistrationDto, inviteId)).thenThrow(new InvalidInviteException());

        assertThrows(InvalidInviteException.class, () -> usersController.createUser(userRegistrationDto, inviteId));
    }

    /**
     * Create Employee Endpoint
     */

    @Test
    void createEmployeeUser_success()
        throws UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, UserNotEmployeeException {
        var userRegistrationDto = new CreateEmployeeDTO();
        when(usersService.createEmployeeUser(userRegistrationDto)).thenReturn(new UserDTO());
        var response = usersController.createEmployee(userRegistrationDto);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void createEmployeeUser_invalidUser()
        throws UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, UserNotEmployeeException {
        var userRegistrationDto = new CreateEmployeeDTO();
        when(usersService.createEmployeeUser(userRegistrationDto)).thenThrow(new UserAlreadyExistsException(""));

        assertThrows(UserAlreadyExistsException.class, () -> usersController.createEmployee(userRegistrationDto));
    }

    /**
     * Employee resend-verification
     */
    @Test
    void employee_sendVerificationEmail() throws Exception {
        doNothing().when(usersService).resendEmployeeVerificationEmail(any());
        this.mockMvc.perform(post("/users/employees/{userId}/resend-verification", validUserId))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void employee_sendVerificationEmailFail() throws Exception {
        doThrow(new UserNotFoundException()).when(usersService).resendEmployeeVerificationEmail(any());
        this.mockMvc.perform(post("/users/employees/{userId}/resend-verification", validUserId))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Employee verification
     */
    @Test
    void employeeVerify_success() throws Exception {
        doNothing().when(usersService).verifyEmployee(any());
        this.mockMvc.perform(post("/users/employees/_verify?userId={userId}", UUID.randomUUID()))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void employeeVerify_fail() throws Exception {
        doThrow(new UserNotFoundException()).when(usersService).verifyEmployee(any());
        this.mockMvc.perform(post("/users/employees/_verify?userId={userId}", UUID.randomUUID()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserAccounts_success() throws Exception {
        ResponseEntity<List<BillToAccountDTO>> response = usersController.getUserAccounts(validUserId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), billTos);
    }

    @Test
    void getUserAccounts_notFound() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/users/{userId}/accounts", invalidUserId)).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    void resendVerificationEmail_success() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/users/{userId}/resend-verification", validUserId)).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    void resendVerificationEmail_userNotFound() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/users/{userId}/resend-verification", invalidUserId)).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getUser_success() throws UserNotFoundException {
        when(usersService.getUser(any(), any())).thenReturn(new ApiUserResponseDTO());
        assertDoesNotThrow(() -> {
            ResponseEntity<ApiUserResponseDTO> response = usersController.getUser(UUID.randomUUID().toString(), false);
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody(), new ApiUserResponseDTO());
        });
    }

    @Test
    void getUser_notFound() throws Exception {
        when(usersService.getUser(any(), any())).thenThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> usersController.getUser(UUID.randomUUID().toString(), false));
    }

    @Test
    void getApprover_success() throws Exception {
        when(usersService.getContactInfo(any())).thenReturn(new ContactInfoDTO());
        ResponseEntity<ContactInfoDTO> response = usersController.getContactInfo(UUID.randomUUID());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), new ContactInfoDTO());
    }

    @Test
    void getApprover_accountNotFound() throws Exception {
        when(usersService.getContactInfo(any())).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> usersController.getContactInfo(UUID.randomUUID()));
    }

    @Test
    void getApprover_userNotFound() throws Exception {
        when(usersService.getContactInfo(any())).thenThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> usersController.getContactInfo(UUID.randomUUID()));
    }

    @Test
    void getErpUserInformation_success() throws Exception {
        UUID accountId = UUID.randomUUID();
        ErpUserInformationDTO erpUserInformationDTO = new ErpUserInformationDTO();
        when(usersService.getErpUserInformation(validUserId, accountId)).thenReturn(erpUserInformationDTO);
        ResponseEntity<ErpUserInformationDTO> response = usersController.getErpUserInformation(
            validUserId,
            accountId,
            AUTH
        );
        assertEquals(
            response.getBody(),
            erpUserInformationDTO,
            "Expected controller to return mocked erp user information"
        );
        assertEquals(
            response.getStatusCode(),
            HttpStatus.OK,
            "Expected controller to return 200 OK response for valid erp user information"
        );
    }

    @Test
    void getErpUserInformation_userNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(usersService.getErpUserInformation(validUserId, accountId)).thenThrow(new UserNotFoundException());
        assertThrows(
            UserNotFoundException.class,
            () -> usersController.getErpUserInformation(validUserId, accountId, AUTH)
        );
    }

    @Test
    void getErpUserInformation_accountNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(usersService.getErpUserInformation(validUserId, accountId)).thenThrow(new AccountNotFoundException());
        assertThrows(
            AccountNotFoundException.class,
            () -> usersController.getErpUserInformation(validUserId, accountId, AUTH)
        );
    }

    @Test
    void getErpUserInformation_unauthorized() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ResponseEntity<ErpUserInformationDTO> erpUserInformationDTOResponseEntity = usersController.getErpUserInformation(
            userId,
            accountId,
            AUTH
        );
        assertEquals(
            erpUserInformationDTOResponseEntity.getStatusCode(),
            HttpStatus.UNAUTHORIZED,
            "Expected unauthorized response from erp user information function"
        );
    }

    @Test
    void updateUserPassword_success() throws Exception {
        UpdateUserPasswordDTO updateUserPasswordDTO = new UpdateUserPasswordDTO();
        when(authenticationService.isUserUpdatingOwnAccount(any(), any())).thenReturn(true);
        doNothing().when(usersService).updateUserPassword(any(), any());
        ResponseEntity<Boolean> response = usersController.updateUserPassword(
            new UUID(0, 0),
            updateUserPasswordDTO,
            "Bearer " + Constants.authToken("")
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void updateUserPassword_userNotFound() throws Exception {
        UpdateUserPasswordDTO updateUserPasswordDTO = new UpdateUserPasswordDTO();
        when(authenticationService.isUserUpdatingOwnAccount(any(), any())).thenReturn(false);
        doNothing().when(usersService).updateUserPassword(any(), any());
        assertThrows(
            UserUnauthorizedException.class,
            () ->
                usersController.updateUserPassword(
                    new UUID(0, 0),
                    updateUserPasswordDTO,
                    "Bearer " + Constants.authToken("")
                )
        );
    }

    @Test
    void recreateErpAccounts_authorized() throws Exception {
        String authorization = "authed";
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> emails = List.of("test@test.com");
        String content = objectMapper.writeValueAsString(emails);
        when(authenticationService.userCanRefreshContact(authorization)).thenReturn(true);
        MvcResult result =
            this.mockMvc.perform(
                    post("/users/erps-users")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                )
                .andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        verify(erpService, times(1)).recreateErpAccounts(eq(emails));
    }

    @Test
    void recreateErpAccounts_contactRefreshDisabled() throws Exception {
        String authorization = "unauthed";
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> emails = List.of("test@test.com");
        String content = objectMapper.writeValueAsString(emails);
        when(authenticationService.userCanRefreshContact(authorization)).thenReturn(false);
        MvcResult result =
            this.mockMvc.perform(
                    post("/users/erps-users")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                )
                .andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.UNAUTHORIZED.value());
        verify(erpService, times(0)).recreateErpAccounts(eq(emails));
    }

    @Test
    void verifyUser_Success() throws Exception {
        UUID token = UUID.randomUUID();
        doNothing().when(usersService).verifyUserToken(token);
        var result = usersController.verifyUser(token);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }
}
