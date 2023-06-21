package com.reece.platform.accounts.controller;

import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.ErpUserInformationDTO;
import com.reece.platform.accounts.service.AuthenticationService;
import com.reece.platform.accounts.service.ErpService;
import com.reece.platform.accounts.service.FeaturesService;
import com.reece.platform.accounts.service.UsersService;
import com.reece.platform.accounts.utilities.DecodedToken;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ErpService erpService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FeaturesService featuresService;

    @Value("${enable_contact_refresh:false}")
    private Boolean enableContactRefresh;

    /**
     * Check if user email is already in use
     * @param email
     * @return
     * @throws UserAlreadyExistsException
     */
    @GetMapping("/_validate")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<EmailValidationDTO> validateUserEmail(@RequestParam String email)
        throws UserAlreadyExistsException {
        return ResponseEntity.ok(usersService.validateUserEmail(email));
    }

    /**
     * Create User
     * Creates a maX user for an individual registering under an existing customer account
     * @param userRegistrationDTO
     * @param inviteId
     * @return
     * @throws ResourceException
     * @throws VerifyAccountException
     * @throws UserAlreadyExistsException
     * @throws TermsNotAcceptedException
     * @throws AccountNotFoundException
     * @throws InvalidInviteException
     */
    @PostMapping
    public @ResponseBody ResponseEntity<UserDTO> createUser(
        @Valid @RequestBody UserRegistrationDTO userRegistrationDTO,
        @RequestParam(required = false) UUID inviteId
    )
        throws ResourceException, VerifyAccountException, UserAlreadyExistsException, TermsNotAcceptedException, AccountNotFoundException, InvalidInviteException, BranchNotFoundException {
        var user = usersService.createUser(userRegistrationDTO, inviteId);
        return ResponseEntity.ok(user);
    }

    /**
     * Create an employee user
     * @param newEmployeeDTO
     * @return
     * @throws UserAlreadyExistsException
     * @throws TermsNotAcceptedException
     * @throws UserNotEmployeeException
     */
    @PostMapping("/employees")
    public @ResponseBody ResponseEntity<UserDTO> createEmployee(@Valid @RequestBody CreateEmployeeDTO newEmployeeDTO)
        throws UserAlreadyExistsException, TermsNotAcceptedException, UserNotEmployeeException {
        var employee = usersService.createEmployeeUser(newEmployeeDTO);
        return ResponseEntity.ok(employee);
    }

    /**
     * Send the employee verification email
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    @PostMapping("/employees/{userId}/resend-verification")
    public @ResponseBody ResponseEntity<String> resendEmployeeVerificationEmail(@PathVariable UUID userId)
        throws UserNotFoundException {
        usersService.resendEmployeeVerificationEmail(userId);
        return new ResponseEntity<>("Verification email resent.", HttpStatus.OK);
    }

    /**
     * Verify an employee registration
     * @param userId
     * @return
     * @throws UserNotFoundException
     * @throws VerificationTokenNotValidException
     */
    @PostMapping("/employees/_verify")
    public @ResponseBody ResponseEntity<Boolean> verifyEmployee(@RequestParam UUID userId)
        throws UserNotFoundException, VerificationTokenNotValidException {
        usersService.verifyEmployee(userId);
        return ResponseEntity.ok(true);
    }

    /**
     * Get a user
     * @param userIdOrEmail UUID OR email of user to fetch
     * @return user information
     * @throws UserNotFoundException if user does not exist
     */
    @GetMapping("/{userIdOrEmail}")
    public @ResponseBody ResponseEntity<ApiUserResponseDTO> getUser(
        @PathVariable String userIdOrEmail,
        @RequestParam(required = false, defaultValue = "false") Boolean includeShipTos
    ) throws UserNotFoundException {
        return new ResponseEntity<>(usersService.getUser(userIdOrEmail, includeShipTos), HttpStatus.OK);
    }

    /**
     * Get all accounts for a user including billTos and shipTos
     * @param userId UUID of the user
     * @return list of billTo accounts with shipTos nested within
     * @throws UserNotFoundException if no user is not found
     */
    @GetMapping("/{userId}/accounts")
    public @ResponseBody ResponseEntity<List<BillToAccountDTO>> getUserAccounts(@PathVariable UUID userId)
        throws UserNotFoundException, BranchNotFoundException {
        return new ResponseEntity<>(usersService.getUserAccounts(userId), HttpStatus.OK);
    }

    // TODO: Delete
    /**
     * Resends the employee verification email for the given user
     * @param userId ID of the user to resend verification email
     * @return success message if completed without exception
     */
    @PostMapping("/{userId}/resend-verification")
    public @ResponseBody ResponseEntity<String> resendVerificationEmail(
        @PathVariable UUID userId,
        @RequestParam(required = false, defaultValue = "false") Boolean isWaterworksSubdomain
    ) throws UserNotFoundException {
        usersService.resendVerificationEmail(userId, isWaterworksSubdomain);
        return ResponseEntity.ok("Verification email resent");
    }

    /**
     * Entry point for fetching user's erp information including log in and erp account id
     *
     * @return response status and erp user information
     */
    @GetMapping(value = "/{userId}/erp/{shipToAccountId}")
    public @ResponseBody ResponseEntity<ErpUserInformationDTO> getErpUserInformation(
        @PathVariable UUID userId,
        @PathVariable UUID shipToAccountId,
        @RequestHeader String authorization
    ) throws AccountNotFoundException, UserNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!token.ecommUserId.equals(String.valueOf(userId))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        ErpUserInformationDTO erpUserInformationDTO = usersService.getErpUserInformation(userId, shipToAccountId);
        return new ResponseEntity<>(erpUserInformationDTO, HttpStatus.OK);
    }

    /**
     * Get Contact information for user
     * @param userId
     * @return
     * @throws UserNotFoundException
     * @throws AccountNotFoundException
     */
    @GetMapping("/{userId}/contact-info")
    public @ResponseBody ResponseEntity<ContactInfoDTO> getContactInfo(@PathVariable UUID userId)
        throws UserNotFoundException, AccountNotFoundException {
        return new ResponseEntity<>(usersService.getContactInfo(userId), HttpStatus.OK);
    }

    /**
     * NOTE: DEV / TEST ENV USE ONLY
     * This endpoint will re-create contacts in the ERP systems for every user email listed in the request.
     * Use if the users have been deleted from the ERP for some reason.
     *
     * @param userEmails list of emails for which ERP contacts should be re-created
     * @return 200 if successful
     * @throws UserUnauthorizedException
     */
    @PostMapping("erps-users")
    public @ResponseBody ResponseEntity<String> recreateErpUsers(
        @RequestHeader String authorization,
        @RequestBody List<String> userEmails
    ) throws UserUnauthorizedException {
        if (!authenticationService.userCanRefreshContact(authorization) || !enableContactRefresh) {
            throw new UserUnauthorizedException();
        }
        erpService.recreateErpAccounts(userEmails);
        return new ResponseEntity<>("User accounts re-created.", HttpStatus.OK);
    }

    /**
     * Entry point for updating user password
     *
     * @param userId
     * @param updateUserPasswordDTO user's old and new password
     * @param authorization user's token
     * @return if the update operation is successful (i.e. no exception thrown) then return true
     * @throws UserNotFoundException
     * @throws UserUnauthorizedException
     * @throws UnsupportedEncodingException
     */
    @PutMapping("{userId}/password")
    public @ResponseBody ResponseEntity<Boolean> updateUserPassword(
        @PathVariable UUID userId,
        @RequestBody UpdateUserPasswordDTO updateUserPasswordDTO,
        @RequestHeader("Authorization") String authorization
    ) throws UserNotFoundException, UserUnauthorizedException, UpdatePasswordException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!authenticationService.isUserUpdatingOwnAccount(token, userId)) {
            throw new UserUnauthorizedException();
        }
        usersService.updateUserPassword(userId, updateUserPasswordDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // TODO: Delete
    @PostMapping("/verify")
    public @ResponseBody ResponseEntity<Boolean> verifyUser(@RequestParam UUID token)
        throws UserNotFoundException, VerificationTokenNotValidException, UserRoleNotFoundException, BranchNotFoundException, UserAlreadyApprovedException, AccountNotFoundException {
        if(featuresService.getFeatures().stream().filter(x -> x.getIsEnabled() && x.getName().equals("NEW_REGISTRATION")).findFirst().orElse(null) != null) {
            usersService.verifyEmployee(token);
        } else {
            usersService.verifyUserToken(token);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
