package com.reece.platform.accounts.controller;

import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.entity.AccountRequest;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.entity.Role;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.repository.AccountDAO;
import com.reece.platform.accounts.model.repository.AccountRequestDAO;
import com.reece.platform.accounts.model.repository.InvitedUserDAO;
import com.reece.platform.accounts.model.repository.UserDAO;
import com.reece.platform.accounts.service.*;
import com.reece.platform.accounts.utilities.DecodedToken;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/account")
public class AccountController {

    // TODO: Delete Dependency
    @Autowired
    private AccountRequestDAO accountRequestDAO;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ErpService erpService;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private InvitedUserDAO invitedUserDAO;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserInviteService userInviteService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/_validate")
    public @ResponseBody ResponseEntity<AccountValidationResponseDTO> validateAccount(
        @Valid @RequestBody AccountValidationRequestDTO accountValidationRequestDTO
    ) throws AccountNotFoundException {
        var response = accountService.validateAccount(accountValidationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/_validateNew")
    public @ResponseBody ResponseEntity<AccountValidationResponseDTO> validateAccountNew(
            @Valid @RequestBody AccountValidationRequestDTO accountValidationRequestDTO
    ) throws AccountNotFoundException {
        var response = accountService.validateAccountNew(accountValidationRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch account information for account ID
     * @param accountId ID for business account, either ERP ID or eComm UUID
     * @return the account information
     * @throws AccountNotFoundException if account is not found in system or ERP
     */
    @GetMapping("{accountId}")
    public @ResponseBody ResponseEntity<List<ErpAccountInfo>> getErpAccount(
        @RequestHeader(required = false) String authorization,
        @PathVariable String accountId,
        @RequestParam String brand
    ) throws AccountNotFoundException {
        // We can assume the user is authed at this point since gql checks
        // This should work for either eCommerce account numbers (UUID) or ERP account numbers (INT)
        var response = accountService.getErpAccount(authorization, accountId, brand);
        return ResponseEntity.ok(response);
    }

    // TODO: Delete
    /**
     * Search account information for account ID
     * @param accountId ID for business account, ERP ID
     * @return if the  account is billTo or not
     * @throws AccountNotFoundException if account is not found in system or ERP
     */
    @GetMapping("/entitySearch/{accountId}")
    public @ResponseBody ResponseEntity<EntitySearchResponseDTO> searchEntity(@PathVariable String accountId)
        throws AccountNotFoundException {
        return accountService.searchEntity(accountId);
    }

    /**
     * Entry point for user creation
     *
     * @param createUserDTO user data from graph-ql
     * @return response status and string
     * @throws ResourceException exception from Authentication provider (Okta)
     */
    @PostMapping
    public @ResponseBody ResponseEntity<AccountRequestDTO> createUserAccount(
        @RequestBody CreateUserDTO createUserDTO,
        @RequestParam(required = false) UUID inviteId
    )
        throws ResourceException, UserAlreadyExistsException, TermsNotAcceptedException, PhoneTypeNotFoundException, BranchNotFoundException, UserNotFoundException, UserAlreadyApprovedException, UserRoleNotFoundException, InvalidInviteException, AccountNotFoundException, UserNotEmployeeException {
        AccountRequestDTO accountRequestDTO = accountService.createAccount(createUserDTO, inviteId);

        return new ResponseEntity<>(accountRequestDTO, HttpStatus.OK);
    }

    /**
     * Entry point for uploading tax document to account
     *
     * @param jobFormDTO contains all the job information
     * @return DTO with file upload data
     * @throws AccountNotFoundException when given account ID doesn't exist
     */
    @PostMapping("/job-form")
    public @ResponseBody ResponseEntity<String> createJobForm(@RequestBody JobFormDTO jobFormDTO)
        throws AccountNotFoundException {
        String jobFormUploadResponse = accountService.createJobForm(jobFormDTO);
        return new ResponseEntity<>(jobFormUploadResponse, HttpStatus.OK);
    }

    /**
     * Syncs db with eclipse account for particular billToAccount
     *
     * @param billToAccountId for getting particular shipToAccount
     * @return true if success
     * @throws ResourceException when account does not exist
     */
    @GetMapping("{billToAccountId}/refreshAccount")
    public @ResponseBody ResponseEntity<List<ShipToAccountDTO>> billToAccountSync(@PathVariable UUID billToAccountId)
        throws NoSuchElementException, AccountNotFoundException {
        return ResponseEntity.ok(accountService.syncShipToAccount(billToAccountId));
    }

    // TODO: Move to UsersController
    /**
     * Syncs user's profile to Okta, specifically for permissions
     * @param userId ID of user to sync
     * @return true if success
     * @throws ResourceException when account does not exist
     */
    @PostMapping("{userId}/sync")
    public @ResponseBody ResponseEntity<Void> syncUserProfileToAuthProvider(@PathVariable UUID userId)
        throws NoSuchElementException {
        usersService.syncUserProfileToAuthProvider(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Fetch the approvers
     *
     * @param billToAccountId
     * @return a list of available approvers
     */
    @GetMapping("/approvers/{billToAccountId}")
    public @ResponseBody ResponseEntity<List<ApproverDTO>> getApprovers(@PathVariable UUID billToAccountId)
        throws AccountNotFoundException {
        var users = accountService.getApprovers(billToAccountId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // TODO: Delete Endpoint
    /**
     * Entry point for user approval
     *
     * @param approveUserDTO user approval data from graph-ql
     * @return response status and string
     */
    @PostMapping(value = "/approval")
    public @ResponseBody ResponseEntity<Void> approveUserAccount(
        @RequestBody ApproveUserDTO approveUserDTO,
        @RequestHeader("Authorization") String authorization
    )
        throws UserUnauthorizedException, UserNotFoundException, UserAlreadyApprovedException, UserRoleNotFoundException, BranchNotFoundException, AccountNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);

        // Note - the front end passes in the account request ID as userId
        val accountId = accountRequestDAO
            .findById(UUID.fromString(approveUserDTO.getUser().getUserId()))
            .orElseThrow(UserNotFoundException::new)
            .getAccountId();

        if (!authenticationService.userCanManageAccountRequests(token, accountId)) {
            throw new UserUnauthorizedException();
        }

        usersService.approveUser(approveUserDTO.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: Delete Endpoint
    /**
     * Reject a user
     */
    @PostMapping(value = "/reject/{accountRequestId}")
    public @ResponseBody ResponseEntity<Void> rejectUserAccount(
        @RequestHeader String authorization,
        @PathVariable UUID accountRequestId,
        @RequestBody RejectUserDTO rejectUserDTO
    )
        throws UserUnauthorizedException, UserNotFoundException, RejectionReasonNotFoundException, BranchNotFoundException, AccountNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);

        val account = accountRequestDAO.findById(accountRequestId).orElseThrow(AccountNotFoundException::new);
        val accountId = account.getAccountId();

        if (!authenticationService.userCanManageAccountRequests(token, accountId)) {
            throw new UserUnauthorizedException();
        }

        usersService.rejectUser(
            accountRequestId,
            rejectUserDTO.getRejectionReasonEnum(),
            rejectUserDTO.getCustomRejectionReason(),
            token
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: Delete Endpoint
    /**
     * Entry point for user approval for first user in system. This should NOT be exposed outside the cluster.
     * For dev use only.
     *
     * @param approveUserDTO user approval data from graph-ql
     * @return response status and string
     */
    @PostMapping(value = "/internal-approval")
    public @ResponseBody ResponseEntity<Void> approveUserAccountInternal(@RequestBody ApproveUserDTO approveUserDTO)
        throws UserNotFoundException, UserAlreadyApprovedException, UserRoleNotFoundException, BranchNotFoundException, AccountNotFoundException {
        usersService.approveUser(approveUserDTO.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: Delete
    /**
     * Entry point for fetching account requests by accountid
     *
     * @return response status and account requests
     */
    @GetMapping(value = "/{accountId}/unapproved-requests")
    public @ResponseBody ResponseEntity<List<AccountRequest>> getAccountRequests(
        @PathVariable(required = true) UUID accountId,
        @RequestHeader("Authorization") String authorization
    ) throws AccountNotFoundException, InvalidPermissionsException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);

        if (
            !authenticationService.userCanManageAccountRequests(token, accountId)
        ) throw new InvalidPermissionsException();

        List<AccountRequest> accountRequestList = accountService.getUnapprovedAccountRequests(accountId);

        return new ResponseEntity<>(accountRequestList, HttpStatus.OK);
    }

    // TODO: Delete
    /**
     * Entry point for fetching account requests without accountid
     *
     * @return response status and account requests
     */
    @GetMapping(value = "/unapproved-requests")
    public @ResponseBody ResponseEntity<List<AccountRequest>> getAllAccountRequests(
        @RequestHeader("Authorization") String authorization
    ) throws InvalidPermissionsException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);

        if (!authenticationService.userCanManageAccountRequests(token, null)) throw new InvalidPermissionsException();

        List<AccountRequest> accountRequestList = accountService.getAllUnapprovedAccountRequests();

        return new ResponseEntity<>(accountRequestList, HttpStatus.OK);
    }

    // TODO: Delete
    /**
     * Entry point for fetching rejected account requests
     *
     * @return response status and rejected account requests
     */
    @GetMapping(value = "/{accountId}/rejected-requests")
    public @ResponseBody ResponseEntity<List<AccountRequest>> getRejectedAccountRequests(
        @PathVariable UUID accountId,
        @RequestHeader("Authorization") String authorization
    ) throws AccountNotFoundException, InvalidPermissionsException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (
            !authenticationService.userCanManageAccountRequests(token, accountId)
        ) throw new InvalidPermissionsException();

        List<AccountRequest> accountRequestList = accountService.getRejectedAccountRequests(accountId);
        return new ResponseEntity<>(accountRequestList, HttpStatus.OK);
    }

    /**
     * Entry point for fetching user roles
     *
     * @return response status and user roles
     */
    @GetMapping(value = "/roles")
    public @ResponseBody ResponseEntity<List<Role>> getRoles() {
        List<Role> roleList = accountService.getRoles();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    /**
     * Entry point for user update
     *
     * @param userDTO user data from graph-ql
     * @return response user dto
     * @throws
     */
    @PutMapping("{accountId}/users/{userId}")
    public @ResponseBody ResponseEntity<UserDTO> updateUser(
        @PathVariable UUID accountId,
        @PathVariable UUID userId,
        @RequestBody UserDTO userDTO,
        @RequestHeader("Authorization") String authorization
    )
        throws UserNotFoundException, UserRoleNotFoundException, UserUnauthorizedException, AccountNotFoundException, PhoneTypeNotFoundException, InvalidPhoneNumberException, UpdateUserEmailAlreadyExistsException, BranchNotFoundException {
        // Check permissions
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);

        if (!authenticationService.userCanManageUsers(token, userId, accountId)) {
            throw new UserUnauthorizedException();
        }

        // Update user in erp systems
        erpService.updateErpUser(accountId, userId, userDTO);
        // Update user in our system
        UserDTO userUpdated = usersService.updateUser(userId, accountId, userDTO);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    /**
     * Delete a user for a given account from E-commerce system and ERP's.
     *
     * @param accountId E-commerce accountId
     * @param userId E-commerce userId
     * @param userLeftCompany Boolean of whether employee that has been deleted has left the company
     * @param authorization auth token
     * @return bool
     * @throws UserUnauthorizedException
     * @throws AccountNotFoundException
     * @throws UserNotFoundException
     */
    @DeleteMapping("{accountId}/users/{userId}")
    public @ResponseBody ResponseEntity<Boolean> deleteUser(
        @PathVariable UUID accountId,
        @PathVariable UUID userId,
        @RequestParam("userLeftCompany") Boolean userLeftCompany,
        @RequestHeader("Authorization") String authorization
    )
        throws UserUnauthorizedException, AccountNotFoundException, UserNotFoundException, EclipseException, BranchNotFoundException, UnableToDeleteUserException {
        // Check permissions
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!authenticationService.userCanManageUsers(token, userId, accountId)) {
            throw new UserUnauthorizedException();
        }
        usersService.deleteUser(accountId, userId, userLeftCompany);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Get the users for  a approver for a given account from E-commerce system and ERP's.
     *
     * @param accountId E-commerce accountId
     * @param userId E-commerce userId
     * @param authorization auth token
     * @return bool
     * @throws UserUnauthorizedException
     * @throws AccountNotFoundException
     * @throws UserNotFoundException
     */
    @GetMapping("{accountId}/users/{userId}")
    public @ResponseBody ResponseEntity<UsersForApproverResponse> checkUsersForApprover(
        @PathVariable UUID accountId,
        @PathVariable UUID userId,
        @RequestHeader("Authorization") String authorization
    )
        throws UserUnauthorizedException, AccountNotFoundException, UserNotFoundException, EclipseException, BranchNotFoundException, UnableToDeleteUserException {
        // Check permissions
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!authenticationService.userCanManageUsers(token, userId, accountId)) {
            throw new UserUnauthorizedException();
        }
        List<UsersForApproverDTO> users = usersService.checkUsersForApprover(accountId, userId);
        var response = new UsersForApproverResponse();
        response.setUsers(users);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch the list of will call branch locations
     *
     * @param shipToAccountId ship to account id to fetch branch locations for
     * @return a list of will-call branch locations
     */
    @GetMapping("/{shipToAccountId}/will-call-locations")
    public @ResponseBody ResponseEntity<List<BranchDTO>> getWillCallLocations(@PathVariable UUID shipToAccountId)
        throws BranchNotFoundException, AccountNotFoundException {
        List<BranchDTO> locations = accountService.getWillCallLocations(shipToAccountId);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    /**
     * Fetch the home branch for an account
     *
     * @param shipToAccountId ship to account id to fetch branch locations for
     * @return the home branch details
     */
    @GetMapping("/{shipToAccountId}/home-branch")
    public @ResponseBody ResponseEntity<BranchDTO> getHomeBranch(@PathVariable UUID shipToAccountId)
        throws BranchNotFoundException, AccountNotFoundException {
        return new ResponseEntity<>(accountService.getHomeBranch(shipToAccountId), HttpStatus.OK);
    }

    // TODO: Move to UserInviteController
    @PostMapping(value = "/pre-approval")
    public @ResponseBody ResponseEntity<InvitedUser> invitePreApprovedUser(
        @RequestHeader String authorization,
        @RequestBody InviteUserDTO inviteUserDTO
    )
        throws UserUnauthorizedException, AccountNotFoundException, UserInviteAlreadyExistsException, UserAlreadyExistsInviteUserException, BranchNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (
            !authenticationService.userCanManageAccountRequests(token, inviteUserDTO.getBillToAccountId())
        ) throw new UserUnauthorizedException();

        Optional<Account> account = accountDAO.findById(inviteUserDTO.getBillToAccountId());
        if (account.isEmpty()) throw new AccountNotFoundException();

        Optional<InvitedUser> existingInvite = invitedUserDAO.findByEmail(inviteUserDTO.getEmail());
        if (existingInvite.isPresent()) throw new UserInviteAlreadyExistsException(inviteUserDTO.getEmail());

        if (userDAO.findByEmail(inviteUserDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsInviteUserException(inviteUserDTO.getEmail());
        }

        InvitedUser invitedUser = userInviteService.inviteUser(inviteUserDTO);

        return new ResponseEntity<>(invitedUser, HttpStatus.OK);
    }

    @PostMapping(value = "/owner-pre-approval")
    public @ResponseBody ResponseEntity<InvitedUser> inviteAccountOwner(
        @RequestHeader String authorization,
        @RequestBody InviteAccountOwnerDTO inviteAccountOwnerDTO
    ) throws UserUnauthorizedException, AccountNotFoundException, BranchNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!authenticationService.userCanManageAccountRequests(token, null)) {
            throw new UserUnauthorizedException();
        }

        InvitedUser invitedUser = userInviteService.inviteAccountOwner(inviteAccountOwnerDTO);
        return new ResponseEntity<>(invitedUser, HttpStatus.OK);
    }

    /**
     * Entry point for fetching users based on account id
     *
     * @return response status and account users
     */
    @GetMapping(value = "{accountId}/users")
    public @ResponseBody ResponseEntity<List<ApiUserResponseDTO>> getAccountUsers(@PathVariable UUID accountId)
        throws AccountNotFoundException {
        List<ApiUserResponseDTO> accountUsers = accountService.getAccountUsers(accountId);
        return new ResponseEntity<>(accountUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{shipToAccountId}/find-ecomm-ship-to-id/{erp}")
    public ResponseEntity<UUID> getEcommShipToId(@PathVariable String shipToAccountId, @PathVariable ErpEnum erp)
        throws AccountNotFoundException {
        UUID ecommShipToId = accountService.getAccountByErpId(shipToAccountId, erp);
        return new ResponseEntity<>(ecommShipToId, HttpStatus.OK);
    }

    @GetMapping(value = "/{billToErpAccountId}/find-ecomm-bill-to-id/{erp}")
    public ResponseEntity<UUID> getEcommBillToId(@PathVariable String billToErpAccountId, @PathVariable ErpEnum erp)
        throws AccountNotFoundException {
        UUID ecommShipToId = accountService.getAccountByErpId(billToErpAccountId, erp);
        return new ResponseEntity<>(ecommShipToId, HttpStatus.OK);
    }

    // TODO: handle Mincron
    @DeleteMapping(value = "{billToAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody DeleteEcommAccountResponseDTO deleteEcommAccount(@PathVariable String billToAccountId) {
        return accountService.deleteEcommAccount(billToAccountId, ErpEnum.ECLIPSE);
    }
}
