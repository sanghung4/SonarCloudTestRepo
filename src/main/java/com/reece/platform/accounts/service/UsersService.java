package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.ErpUserInformationDTO;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.DivisionEnum;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import com.reece.platform.accounts.utilities.DecodedToken;
import com.reece.platform.accounts.utilities.ErpUtility;
import com.reece.platform.accounts.utilities.GetCurrentTimeUTC;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reece.platform.accounts.model.enums.ErpEnum.MINCRON;
import static com.reece.platform.accounts.utilities.AccountDataFormatting.formatAccountAddress;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    @Value("${employee_domains}")
    private String[] employeeDomains;

    @Value("${employee_eclipse_account:287169}")
    private String employeeEclipseAccount;

    @Value("${enable_notification_calls:true}")
    private Boolean enableNotificationCalls;

    @Autowired
    private AccountService accountService;

    // TODO: Delete dependency
    @Autowired
    private AccountRequestDAO accountRequestDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ErpsUsersDAO erpsUsersDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UsersBillToAccountsDAO usersBillToAccountsDAO;

    @Autowired
    private BranchService branchService;

    @Autowired
    private InvitedUserDAO invitedUserDAO;

    @Autowired
    private ErpService erpService;

    @Autowired
    private FeaturesService featuresService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    /**
     * Check if user already exists - if they do, throw exception
     * If user is employee, must also be of proper domain
     * @param email
     * @throws UserAlreadyExistsException
     */
    public EmailValidationDTO validateUserEmail(String email) throws UserAlreadyExistsException {
        val user = userDAO.findByEmail(email);
        val oktaUser = authenticationService.getUserByEmail(email);
        if (user.isPresent() || oktaUser.isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        EmailValidationDTO userValidation = new EmailValidationDTO();
        if (isReeceDomain(email)) {
            userValidation.setIsEmployee(true);
        }

        return userValidation;
    }

    /**
     * Create User for Existing Account
     * @param createUserDTO
     * @param inviteId
     * @return
     * @throws TermsNotAcceptedException
     * @throws UserAlreadyExistsException
     * @throws AccountNotFoundException
     * @throws VerifyAccountException
     * @throws InvalidInviteException
     */
    @Transactional
    public UserDTO createUser(UserRegistrationDTO createUserDTO, UUID inviteId)
        throws TermsNotAcceptedException, UserAlreadyExistsException, AccountNotFoundException, VerifyAccountException, InvalidInviteException, BranchNotFoundException {
        // Validate that they have accepted terms and private policy
        if (!(createUserDTO.isTosAccepted() && createUserDTO.isPpAccepted())) {
            throw new TermsNotAcceptedException();
        }

        // validate user does not exist in db unless they are a legacy user
        Optional<User> existingUser = userDAO.findByEmail(createUserDTO.getEmail());
        if (existingUser.isPresent()) {
            if (inviteId == null) throw new UserAlreadyExistsException(createUserDTO.getEmail());
            Optional<InvitedUser> userInvite = invitedUserDAO.findById(inviteId);
            // If a user does not have an invite
            // Or if a user has a completed invite
            // Or if a user has an auth id from okta
            if (userInvite.isEmpty() || userInvite.get().isCompleted() || existingUser.get().getAuthId() != null) {
                throw new UserAlreadyExistsException(createUserDTO.getEmail());
            }
        }

        // validate that account exists
        var accountNumber = createUserDTO.getAccountNumber();
        var erp = ErpUtility.getErpFromBrand(createUserDTO.getBrand());
        // TODO: Make sure this same type of billto from shipto lookup is occurring.
        var accountInfo = erpService.getErpBillToAccount(accountNumber, erp);

        // validate that account zipcode and user zipcode match
        var accountZipcode = AccountDataFormatting.formatZipcode(accountInfo.getZip());
        if (!Objects.equals(accountZipcode, createUserDTO.getZipcode())) {
            throw new VerifyAccountException();
        }

        // Determine new users role
        Role role;
        if (inviteId != null) {
            // Validate Invite if one exists
            var invitedUser = invitedUserDAO.findById(inviteId).orElseThrow(InvalidInviteException::new);
            if (invitedUser.isCompleted()) throw new InvalidInviteException();
            if (!invitedUser.getEmail().equals(createUserDTO.getEmail())) throw new InvalidInviteException(
                createUserDTO.getEmail()
            );

            invitedUser.setCompleted(true);
            invitedUserDAO.save(invitedUser);

            role = roleDAO.findById(invitedUser.getUserRoleId()).orElseThrow(InvalidInviteException::new);
        } else {
            role = roleDAO.findByName(RoleEnum.STANDARD_ACCESS.label);
        }

        // validate billTo account from ERP exists in eComm system; create if not
        Account billToAccount = accountService.findOrCreateAccount(accountInfo, erp, null);

        // Fetch the branch associated with the account if given branchId
        BranchDTO branch;
        if (billToAccount.getBranchId() != null && !billToAccount.getBranchId().isBlank()) {
            branch = branchService.getBranch(billToAccount.getBranchId());
        } else {
            branch = accountService.getHomeBranch(billToAccount.getId());
        }

        var authUser = authenticationService.createNewUser(createUserDTO, branch);

        Date tosAcceptedAt = new Date(System.currentTimeMillis());
        Date ppAcceptedAt = new Date(System.currentTimeMillis());

        // Update Legacy User or Create a new record
        User newUser;
        if (existingUser.isPresent()) {
            existingUser.get().setAuthId(authUser.getId());
            existingUser.get().setEmail(createUserDTO.getEmail());
            existingUser.get().setFirstName(createUserDTO.getFirstName());
            existingUser.get().setLastName(createUserDTO.getLastName());
            existingUser.get().setPhoneNumber(createUserDTO.getPhoneNumber());
            existingUser.get().setTosAcceptedAt(tosAcceptedAt);
            existingUser.get().setPpAcceptedAt(ppAcceptedAt);
            existingUser.get().setPhoneType(createUserDTO.getPhoneType());
            existingUser.get().setRole(role);
            newUser = userDAO.save(existingUser.get());
        } else {
            newUser =
                new User(
                    authUser.getId(),
                    createUserDTO.getEmail(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    createUserDTO.getPhoneNumber(),
                    tosAcceptedAt,
                    ppAcceptedAt,
                    createUserDTO.getPhoneType()
                );
            newUser.setRole(role);
            newUser = userDAO.save(newUser);
        }

        authenticationService.updateUserPermissions(newUser);

        // Setup User in ERP and link BillTo / ShipTo Account
        ErpContactCreationResponse erpContact = erpService.createErpContact(newUser, accountNumber, erp);

        // Create records in maX db for contact and billTo association with user
        createUserContactData(erpContact, newUser, erp);
        createUserBillToAccount(newUser.getId(), billToAccount.getId());

        // Send out new user notification to branch manager?
        if (enableNotificationCalls) {
            notificationService.sendUserWelcomeEmail(branch, newUser);

            if (branch != null) {
                List<UserDTO> accountAdmins = accountService.getAccountAdmins(billToAccount.getId());
                NewCustomerNotificationDTO newUserNotificationDTO = new NewCustomerNotificationDTO(
                    branch,
                    accountInfo,
                    createUserDTO,
                    true
                );
                newUserNotificationDTO.setAccountAdmins(accountAdmins);

                notificationService.sendNewUserRegistrationEmail(newUserNotificationDTO);
            }
        }

        return new UserDTO(newUser);
    }

    /**
     * Create a new Employee User
     * @param employeeDTO
     * @return
     * @throws UserNotEmployeeException
     * @throws TermsNotAcceptedException
     * @throws UserAlreadyExistsException
     * @throws AccountNotFoundException
     */
    @Transactional
    public UserDTO createEmployeeUser(CreateEmployeeDTO employeeDTO)
        throws UserNotEmployeeException, TermsNotAcceptedException, UserAlreadyExistsException {
        // Assumption - employees would not be invited to platform.

        // verify user is employee
        if (!isReeceDomain(employeeDTO.getEmail())) {
            throw new UserNotEmployeeException();
        }

        // validate that user has accepted terms of service and privacy policy
        if (!(employeeDTO.isTosAccepted() && employeeDTO.isPpAccepted())) {
            throw new TermsNotAcceptedException();
        }

        // validate that user does not already exist
        Optional<User> existingUser = userDAO.findByEmail(employeeDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(employeeDTO.getEmail());
        }

        var adminAccessRole = roleDAO.findByName(RoleEnum.MORSCO_ADMIN.label);
        var authUser = authenticationService.createEmployeeUser(employeeDTO);

        Date tosAcceptedAt = new Date(System.currentTimeMillis());
        Date ppAcceptedAt = new Date(System.currentTimeMillis());

        User newUser = new User(
            authUser.getId(),
            employeeDTO.getEmail(),
            employeeDTO.getFirstName(),
            employeeDTO.getLastName(),
            employeeDTO.getPhoneNumber(),
            tosAcceptedAt,
            ppAcceptedAt,
            employeeDTO.getPhoneType()
        );
        newUser.setRole(adminAccessRole);
        newUser = userDAO.save(newUser);

        if (enableNotificationCalls) {
            var employeeVerificationDTO = new EmployeeVerificationDTO(newUser);
            employeeVerificationDTO.setBrand(employeeDTO.getBrand());
            notificationService.sendEmployeeVerificationEmailNew(employeeVerificationDTO);
        }

        return new UserDTO(newUser);
    }

    /**
     * Fetch a user and optionally include shipTos
     * This is used by the products service to validate that a user can checkout with the account
     *
     * @param userIdOrEmail UUID or email of user to fetch
     * @param
     * @return user DTO
     * @throws UserNotFoundException if user does not exist
     */
    public ApiUserResponseDTO getUser(String userIdOrEmail, Boolean includeShipTos) throws UserNotFoundException {
        User user;
        if (EmailValidator.getInstance().isValid(userIdOrEmail)) {
            user = userDAO.findByEmail(userIdOrEmail).orElseThrow(UserNotFoundException::new);
        } else {
            user = userDAO.findById(UUID.fromString(userIdOrEmail)).orElseThrow(UserNotFoundException::new);
        }
        var userResponse = new ApiUserResponseDTO(user);

        if (!userResponse.getIsEmployee() && includeShipTos) {
            var shipTos = user
                .getBillToAccounts()
                .stream()
                .flatMap(p -> p.getShipToAccounts().stream())
                .map(Account::getId)
                .collect(Collectors.toList());

            userResponse.setEcommShipToIds(shipTos);
        }

        return userResponse;
    }

    /**
     * Delete a user from E-commerce and ERP system for a given account. If the user does not belong to any remaining accounts,
     * then delete them from the Users table and Okta.
     *
     * @param billToAccountId user's billToAccountId
     * @param userId user's userId
     */
    @Transactional
    public void deleteUser(UUID billToAccountId, UUID userId, Boolean userLeftCompany)
        throws AccountNotFoundException, UserNotFoundException, EclipseException, BranchNotFoundException, UnableToDeleteUserException {
        val billto = accountDAO.findById(billToAccountId).orElseThrow(AccountNotFoundException::new);
        erpService.deleteErpUser(billToAccountId, userId);
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        ErpsUsers erpsUsers = erpsUsersDAO.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        invitedUserDAO.deleteAllByApproverId(userId);
        invitedUserDAO.findByEmail(user.getEmail()).ifPresent(invitedUserDAO::delete);
        erpsUsersDAO.delete(erpsUsers);
        usersBillToAccountsDAO
            .findByUserIdAndAccountId(userId, billToAccountId)
            .ifPresent(usersBillToAccountsDAO::delete);

        List<UsersBillToAccounts> billToAccounts = usersBillToAccountsDAO.findAllByUserId(userId);
        if (billToAccounts.isEmpty()) {
            authenticationService.deleteUser(user);
            userDAO.delete(user);
        }

        // TODO: Remove dependency on accountRequests
        accountRequestDAO.findByEmailAndRejectionReasonIsNull(user.getEmail()).ifPresent(accountRequestDAO::delete);

        if (enableNotificationCalls) {
            // email sent to customer when account is deleted
            BranchDTO branch = accountService.getHomeBranch(billToAccountId);
            CustomerAccountDeletedDTO customerAccountDeletedDTO = new CustomerAccountDeletedDTO(
                user,
                billto.getErpAccountId(),
                billto.getName()
            );
            customerAccountDeletedDTO.setBrand(branch.getBrand());
            customerAccountDeletedDTO.setDomain(branch.getDomain());
            if (userLeftCompany) {
                // getting billToAccount home branch manager email
                // special case where it should be ok to pass in the billToAccountId instead of shipToAccountId
                String branchManagerEmail = accountService.getHomeBranch(billToAccountId).getActingBranchManagerEmail();
                List<String> adminEmailList = accountDAO
                    .findById(billToAccountId)
                    .orElseThrow(AccountNotFoundException::new)
                    .getUsers()
                    .stream()
                    .filter(u -> u.getRole().getName().equals(RoleEnum.ACCOUNT_ADMIN.label))
                    .map(User::getEmail)
                    .collect(Collectors.toList());
                customerAccountDeletedDTO.setAdminEmails(adminEmailList);
                customerAccountDeletedDTO.setBranchManagerEmail(branchManagerEmail);
            }
            notificationService.sendUserDeletedEmail(customerAccountDeletedDTO, userLeftCompany);
        }
        //delete billto and shipto if it is last user
        List<UsersBillToAccounts> usersBillToAccounts = usersBillToAccountsDAO.findAllByAccountId(billToAccountId);
        if (usersBillToAccounts.isEmpty()) {
            deleteBillToAndShipToAccounts(billto);
        }
    }

    /**
     * Delete a user from E-commerce and ERP system for a given account. If the user does not belong to any remaining accounts,
     * then delete them from the Users table and Okta.
     *
     * @param billToAccountId user's billToAccountId
     * @param userId user's userId
     */
    @Transactional
    public List<UsersForApproverDTO> checkUsersForApprover(UUID billToAccountId, UUID userId)
        throws AccountNotFoundException {
        accountDAO.findById(billToAccountId).orElseThrow(AccountNotFoundException::new);
        List<User> users = userDAO.findAllByApproverId(userId);
        List<UsersForApproverDTO> usersForApproverDTOList = new ArrayList<>();
        for (User user : users) {
            UsersForApproverDTO UsersForApproverDTO = new UsersForApproverDTO();
            UsersForApproverDTO.setEmail(user.getEmail());
            UsersForApproverDTO.setFirstName(user.getFirstName());
            UsersForApproverDTO.setLastName(user.getLastName());

            usersForApproverDTOList.add(UsersForApproverDTO);
        }
        return usersForApproverDTOList;
    }

    /**
     * Delete Bill to and Ship to accounts
     * and delete all user invites for the billTo account
     * @param billTo
     */
    @Transactional
    public void deleteBillToAndShipToAccounts(Account billTo) {
        invitedUserDAO.deleteAllByBillToAccountId(billTo.getId());
        List<Account> accountList = accountDAO.findAllByParentAccountId(billTo.getId());
        accountList.add(billTo);
        for (Account shipTo : accountList) {
            // TODO: Remove dependency on account requests
            if (accountRequestDAO.findAllByAccountId(shipTo.getId()).size() > 0) {
                accountRequestDAO.deleteByAccountId(shipTo.getId());
            }
            accountDAO.deleteById(shipTo.getId());
        }
    }

    // TODO: Delete function
    /**
     * Update related tables for user data for user approval with given information
     *
     * @param erpResponse response from ERP adapter
     * @param billToAccountId id of the billTo account
     * @param userToApprove user approval information
     * @param accountRequest account request information
     */
    @Transactional
    public void updateUserData(
        ErpContactCreationResponse erpResponse,
        UUID billToAccountId,
        User userToApprove,
        AccountRequest accountRequest
    ) {
        // Create billTo account with first user
        if (!accountRequest.isEmployee() && usersBillToAccountsDAO.findAllByUserId(userToApprove.getId()).isEmpty()) {
            UsersBillToAccounts usersBillToAccounts = new UsersBillToAccounts();
            usersBillToAccounts.setAccountId(billToAccountId);
            usersBillToAccounts.setRestricted(false);
            usersBillToAccounts.setUserId(userToApprove.getId());
            usersBillToAccountsDAO.save(usersBillToAccounts);
        }

        // insert erps users
        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setUserId(userToApprove.getId());
        erpsUsers.setErp(accountRequest.getErp());
        erpsUsers.setErpUserId(erpResponse.getContactId());
        erpsUsers.setErpUserLogin(erpResponse.getErpUsername());

        // TODO: need to salt/encrypt password instead of plaintext
        erpsUsers.setErpUserPassword(erpResponse.getErpPassword());

        erpsUsersDAO.save(erpsUsers);

        // Sync user roles to Okta
        authenticationService.updateUserPermissions(userToApprove);

        // mark account request as completed
        accountRequest.setCompleted(true);
        accountRequest.setAccountId(billToAccountId);
        accountRequestDAO.save(accountRequest);
    }

    /**
     * Create user contact data in erp and link user to a billTo account
     * @param erpContact
     * @param newUser
     * @param erp
     */
    @Transactional
    public void createUserContactData(ErpContactCreationResponse erpContact, User newUser, ErpEnum erp) {
        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setUserId(newUser.getId());
        erpsUsers.setErp(erp);
        erpsUsers.setErpUserId(erpContact.getContactId());
        erpsUsers.setErpUserLogin(erpContact.getErpUsername());

        // TODO: need to salt/encrypt password instead of plaintext
        erpsUsers.setErpUserPassword(erpContact.getErpPassword());
        erpsUsersDAO.save(erpsUsers);
    }

    /**
     * Create user bill to association in database
     * @param userId
     * @param billToAccountId
     */
    @Transactional
    public void createUserBillToAccount(UUID userId, UUID billToAccountId) {
        if (usersBillToAccountsDAO.findAllByUserId(userId).isEmpty()) {
            UsersBillToAccounts usersBillToAccounts = new UsersBillToAccounts();
            usersBillToAccounts.setAccountId(billToAccountId);
            usersBillToAccounts.setRestricted(false);
            usersBillToAccounts.setUserId(userId);
            usersBillToAccountsDAO.save(usersBillToAccounts);
        }
    }

    /**
     * Sync user's permissions to authentication provider
     * @param userId ID of User to sync
     *
     * @throws NoSuchElementException if user doesn't exist
     */
    @Transactional
    public void syncUserProfileToAuthProvider(UUID userId) throws NoSuchElementException {
        val user = userDAO.findById(userId).orElseThrow();
        authenticationService.updateUserPermissions(user);
    }

    // TODO: Delete function
    /**
     * Approve an account_request entry for the given user
     *
     * @param user user to approve
     * @throws UserNotFoundException thrown if the user trying to be approved doesn't exist in the eComm DB
     * @throws UserAlreadyApprovedException thrown is the user trying to be approved has already been approved
     * @throws UserRoleNotFoundException thrown if the roleId given does not exist in the eComm DB
     */
    @Transactional
    public void approveUser(ApproveUser user)
        throws UserNotFoundException, UserAlreadyApprovedException, UserRoleNotFoundException, BranchNotFoundException, AccountNotFoundException {
        // obtain user information to approve
        Optional<User> userToApprove = userDAO.findById(UUID.fromString(user.getUserId()));
        if (userToApprove.isEmpty()) {
            // check if UUID is an AccountRequest ID
            Optional<AccountRequest> accountRequest = accountRequestDAO.findById(UUID.fromString(user.getUserId()));
            if (accountRequest.isPresent()) {
                userToApprove = userDAO.findByEmail(accountRequest.get().getEmail());
            }
        }

        if (userToApprove.isEmpty()) {
            throw new UserNotFoundException();
        }

        // validate role id given exists in eComm DB
        Optional<Role> role = roleDAO.findById(UUID.fromString(user.getUserRoleId()));
        if (role.isEmpty()) {
            throw new UserRoleNotFoundException();
        }

        // Update approver
        if (user.getApproverId() != null) {
            var approver = userDAO.findById(user.getApproverId());
            if (approver.isEmpty()) {
                throw new UserNotFoundException("Approver does not exist");
            }

            userToApprove.get().setApprover(approver.get());
        }

        userToApprove.get().setRole(role.get());

        AccountRequest accountRequest = accountRequestDAO.findMostRecentRequestByEmail(userToApprove.get().getEmail());
        if (accountRequest.isCompleted()) {
            throw new UserAlreadyApprovedException();
        }

        ErpContactCreationResponse erpResponse = erpService.createErpAccount(accountRequest);
        val erp = accountRequest.getErp();

        // validate billTo account from ERP exists in eComm system; create if not
        var erpAccountInfo = erpService.getErpAccount(accountRequest.getAccountNumber(), erp);
        Account billToAccount = accountService.findOrCreateAccount(erpAccountInfo, erp, null);

        // update approved user data and add all ERP account information
        updateUserData(erpResponse, billToAccount.getId(), userToApprove.get(), accountRequest);

        if (!accountRequest.isEmployee() && enableNotificationCalls) {
            BranchDTO branch = accountService.getHomeBranch(billToAccount.getId());
            notificationService.sendUserApprovedEmail(branch, accountRequest);
        }
    }

    // TODO: Delete function
    /**
     * TODO: tw - we need to handle if there are multiple billToAccounts. This should
     * not currently be possible as it would require submitting a request on an account
     * that already exists in the system. In addition this will require refactoring the
     * accountRequestDAO.findByEmail call to return an array, since there could be
     * multiple requests per account. In the interest of time for the upcoming demo
     * I am skipping this and will return once we are ready to support multiple accounts
     * throughout the application.
     */
    @Transactional
    public void rejectUser(
        UUID accountRequestId,
        RejectionReasonEnum reason,
        String customRejectionReason,
        DecodedToken token
    ) throws UserNotFoundException, BranchNotFoundException, AccountNotFoundException {
        val accountRequest = accountRequestDAO.findById(accountRequestId).orElseThrow(UserNotFoundException::new);
        val user = userDAO.findByEmail(accountRequest.getEmail()).orElseThrow(UserNotFoundException::new);

        var rejectedBy = token.sub;
        var rejectedAt = GetCurrentTimeUTC.getCurrentDateTime();

        accountRequest.setRejectionReason(reason);
        accountRequest.setCustomRejectionReason(customRejectionReason);
        accountRequest.setRejectedBy(rejectedBy);
        accountRequest.setRejectedAt(rejectedAt);

        // Persist the reason and delete user
        accountRequestDAO.save(accountRequest);
        authenticationService.deleteUser(user);
        userDAO.delete(user);

        if (enableNotificationCalls) {
            BranchDTO branch;
            if (accountRequest.getAccountId() == null) {
                branch = branchService.getBranch(accountRequest.getBranchId());
            } else {
                branch = accountService.getHomeBranch(accountRequest.getAccountId());
            }
            notificationService.sendRejectUserEmail(accountRequest, branch);
        }
    }

    // TODO: Do we even need this anymore? Seems like this endpoint is not really in use
    /**
     * Update a user account
     * @param userId
     * @param accountId
     * @param userDTO
     * @return
     * @throws UserNotFoundException
     * @throws UserRoleNotFoundException
     * @throws UpdateUserEmailAlreadyExistsException
     * @throws BranchNotFoundException
     * @throws AccountNotFoundException
     */
    @Transactional
    public UserDTO updateUser(UUID userId, UUID accountId, UserDTO userDTO)
        throws UserNotFoundException, UserRoleNotFoundException, UpdateUserEmailAlreadyExistsException, BranchNotFoundException, AccountNotFoundException {
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);

        boolean isEmailUpdated = false;
        String oldEmail = user.getEmail();
        var req = accountRequestDAO.findByEmailAndRejectionReasonIsNull(oldEmail);

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(oldEmail)) {
            if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new UpdateUserEmailAlreadyExistsException(userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());
            req.ifPresent(r -> r.setEmail(userDTO.getEmail()));
            isEmailUpdated = true;
        }

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
            req.ifPresent(r -> r.setFirstName(userDTO.getFirstName()));
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
            req.ifPresent(r -> r.setLastName(userDTO.getLastName()));
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
            req.ifPresent(r -> r.setPhoneNumber(userDTO.getPhoneNumber()));
        }

        if (userDTO.getRoleId() != null) {
            Optional<Role> role = roleDAO.findById(userDTO.getRoleId());
            if (role.isEmpty()) {
                throw new UserRoleNotFoundException();
            }
            user.setRole(role.get());
        }

        if (userDTO.getPhoneTypeId() != null) {
            var phoneType = userDTO.getPhoneTypeId();
            user.setPhoneType(phoneType);
            req.ifPresent(r -> r.setPhoneType(phoneType));
        }

        if (userDTO.getApproverId() != null) {
            Optional<User> approver = userDAO.findById(userDTO.getApproverId());
            if (approver.isEmpty()) {
                throw new UserNotFoundException("No approver found for that ID.");
            }

            user.setApprover(approver.get());
        }

        User savedUser = userDAO.save(user);
        req.ifPresent(r -> accountRequestDAO.save(r));
        authenticationService.updateUserPermissions(savedUser);
        authenticationService.updateUserLoginEmailAndProfile(savedUser);
        if (isEmailUpdated && enableNotificationCalls) {
            BranchDTO branch = accountService.getHomeBranch(accountId);
            notificationService.sendUserLoginUpdatedEmail(branch, savedUser, oldEmail);
        }

        return new UserDTO(savedUser);
    }

    /**
     * Return a list of all accounts for a user
     *
     * @param userId UUID of user
     * @return Billto accounts, shipTo accounts nested within
     * @throws AccountNotFoundException if no accounts exist for user
     */
    @Transactional
    public List<BillToAccountDTO> getUserAccounts(UUID userId) throws UserNotFoundException, BranchNotFoundException {
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var role = user.getRole().getName();
        var isEmployee = role.equals(RoleEnum.MORSCO_ADMIN.label) || role.equals(RoleEnum.MORSCO_ENGINEER.label);

        Set<Account> billToAccounts;
        List<Account> shipTos;
        Map<UUID, List<ShipToAccountDTO>> shipToMap;

        if (isEmployee) {
            // If user is a Morsco employee, get all bill-to accounts
            billToAccounts = new HashSet<>(accountDAO.findAllByParentAccountId(null));
            shipTos = accountDAO.findAllByParentAccountIdNotNull();
            var branches = branchService.getAllBranches();
            var branchesMap = branches.stream().collect(Collectors.toMap(BranchDTO::getBranchId, branch -> branch));
            shipToMap = shipTos
                .stream()
                .map(shipTo -> {
                    ShipToAccountDTO shipToDTO = new ShipToAccountDTO(shipTo, shipTo.getErp());
                    try {
                        BranchDTO branchDTO = branchesMap.getOrDefault(shipTo.getBranchId(), null);
                        if (branchDTO == null) throw new BranchNotFoundException();
                        String branchAddress = formatAccountAddress(branchDTO.getAddress1(), branchDTO.getCity(),
                                branchDTO.getState(), branchDTO.getZip());
                        shipToDTO.setBranchAddress(branchAddress);
                    } catch (BranchNotFoundException e) {
                        shipToDTO.setBranchAddress(null);
                    }
                    return shipToDTO;
                })
                .collect(Collectors.groupingBy(ShipToAccountDTO::getParentAccountId, Collectors.toList()));

            return billToAccounts
                .stream()
                .map(billTo -> {
                    var shipTosForAcc = shipToMap.getOrDefault(billTo.getId(), null);
                    var erp = billTo.getErp();
                    //billToDTO will have added branch and division info for employee users
                    BillToAccountDTO billToDTO = new BillToAccountDTO(billTo, shipTosForAcc, erp);
                    //Set branch address to billTo DTO unless branch is not found, then just set as null
                    try {
                        BranchDTO branchDTO = branchesMap.getOrDefault(billTo.getBranchId(), null);
                        if (branchDTO == null) throw new BranchNotFoundException();
                        String branchAddress = formatAccountAddress(branchDTO.getAddress1(), branchDTO.getCity(),
                                branchDTO.getState(), branchDTO.getZip());
                        billToDTO.setBranchAddress(branchAddress);
                    } catch (BranchNotFoundException e) {
                        billToDTO.setBranchAddress(null);
                    }
                    return billToDTO;
                })
                .collect(Collectors.toList());
        } else {
            // If user is not a Morsco employee, get linked bill-to accounts
            billToAccounts = user.getBillToAccounts();

            for (Account account : billToAccounts) {
                try {
                    accountService.syncErpAccountsToDb(account);
                } catch (AccountNotFoundException exception) {
                    LOGGER.info("Unable to sync account: " + exception);
                }
            }

            var billToIds = billToAccounts.stream().map(Account::getId).collect(Collectors.toList());
            shipTos = accountDAO.findAllByParentAccountIdIn(billToIds);
            shipToMap = shipTos
                .stream()
                .map(shipTo -> new ShipToAccountDTO(shipTo, shipTo.getErp()))
                .collect(Collectors.groupingBy(ShipToAccountDTO::getParentAccountId, Collectors.toList()));

            return billToAccounts
                .stream()
                .map(billTo -> {
                    var shipTosForAcc = shipToMap.getOrDefault(billTo.getId(), null);
                    var erp = billTo.getErp();
                    return new BillToAccountDTO(billTo, shipTosForAcc, erp);
                })
                .collect(Collectors.toList());
            
        }
    }

    // TODO: Update to no longer use accountRequest for logic
    public ContactInfoDTO getContactInfo(UUID userId) throws UserNotFoundException, AccountNotFoundException {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        AccountRequest accountRequest = accountRequestDAO.findMostRecentRequestByEmail(user.getEmail());
        UUID accountId = accountRequest.getAccountId();
        if (
            accountId != null &&
            userDAO.findUserByAccountIdAndRoleName(accountId, RoleEnum.ACCOUNT_ADMIN.label).isPresent()
        ) {
            Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);
            boolean isShipTo = account.getParentAccount() != null;
            if (isShipTo) {
                account = account.getParentAccount();
            }
            //Set Account Admin (First User for this Account with Admin role) as Approver
            User approver = userDAO.findUserByAccountIdAndRoleName(account.getId(), RoleEnum.ACCOUNT_ADMIN.label).get();
            contactInfoDTO.setPhoneNumber(approver.getPhoneNumber());
            contactInfoDTO.setEmailAddress(approver.getEmail());
        } else if (accountRequest.getBranchPhoneNumber() != null) {
            contactInfoDTO.setIsBranchInfo(true);
            contactInfoDTO.setPhoneNumber(accountRequest.getBranchPhoneNumber());
        } else {
            // Fallback for when no contact info is found. Front end should serve a generic contact branch message.
            contactInfoDTO.setIsBranchInfo(true);
        }

        return contactInfoDTO;
    }

    /**
     * Retrieve erp information for the given user and account ids
     *
     * @param userId          user id to retrieve ERP login information for
     * @param shipToAccountId account id to retrieve the erp account id for
     * @return erp user information
     */
    public ErpUserInformationDTO getErpUserInformation(UUID userId, UUID shipToAccountId)
        throws UserNotFoundException, AccountNotFoundException {
        ErpUserInformationDTO erpUserInformationDTO = new ErpUserInformationDTO();

        ErpsUsers erpsUsers = erpsUsersDAO.findFirstByUserIdOrderByLastModifiedDateDesc(userId).orElseThrow(UserNotFoundException::new);
        erpUserInformationDTO.setPassword(erpsUsers.getErpUserPassword());
        erpUserInformationDTO.setUserId(erpsUsers.getErpUserLogin());

        Account account = accountDAO.findById(shipToAccountId).orElseThrow(AccountNotFoundException::new);
        erpUserInformationDTO.setErpAccountId(String.valueOf(account.getErpAccountId()));
        erpUserInformationDTO.setErpSystemName(account.getErp().toString());

        User user = userDAO.getOne(userId);
        erpUserInformationDTO.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));

        return erpUserInformationDTO;
    }

    /**
     * Finds user's record in DB and makes call out to authentication system to update user's password.
     *
     * @param userId
     * @param updateUserPasswordDTO user's old and new password
     * @throws UserNotFoundException
     */
    @Transactional
    public void updateUserPassword(UUID userId, UpdateUserPasswordDTO updateUserPasswordDTO)
        throws UserNotFoundException, UpdatePasswordException {
        var user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        authenticationService.updateUserPassword(user, updateUserPasswordDTO);

        if (enableNotificationCalls) {
            notificationService.sendUserLoginUpdatedEmail(null, user, null);
        }
    }

    // TODO: Delete this
    @Transactional
    public void verifyUserToken(UUID token)
        throws UserNotFoundException, VerificationTokenNotValidException, UserRoleNotFoundException, BranchNotFoundException, UserAlreadyApprovedException, AccountNotFoundException {
        AccountRequest accountRequest = accountRequestDAO
            .findByVerificationToken(token)
            .orElseThrow(VerificationTokenNotValidException::new);
        User user = userDAO
            .findByEmail(accountRequest.getEmail())
            .orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s not found.", accountRequest.getEmail()))
            );

        ApproveUser approveUser = new ApproveUser();
        approveUser.setUserId(user.getId().toString());
        Role role;
        if (accountRequest.isEmployee()) {
            role = roleDAO.findByName(RoleEnum.MORSCO_ADMIN.label);
        } else {
            role = roleDAO.findByName(RoleEnum.STANDARD_ACCESS.label);
        }
        approveUser.setUserRoleId(role.getId().toString());

        accountRequest.setVerified(true);
        accountRequestDAO.save(accountRequest);

        approveUser(approveUser);
    }

    /**
     * Verify an Employees account - complete employee registration process
     * @param userId
     * @throws UserNotFoundException
     * @throws VerificationTokenNotValidException
     */
    @Transactional
    public void verifyEmployee(UUID userId) throws UserNotFoundException, VerificationTokenNotValidException {
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);

        // Check if already verified - if user has an erpUser record, throw exception
        Optional<ErpsUsers> existingErpUser = erpsUsersDAO.findByUserId(userId);
        if (existingErpUser.isPresent()) {
            throw new VerificationTokenNotValidException();
        }

        // 1. Create erp contact for user
        var erpContact = erpService.createErpContact(user, employeeEclipseAccount, ErpEnum.ECLIPSE);

        // 2. Create erpsUsers record for admin
        ErpsUsers erpsUsers = new ErpsUsers();
        erpsUsers.setUserId(user.getId());
        erpsUsers.setErp(ErpEnum.ECLIPSE);
        erpsUsers.setErpUserId(erpContact.getContactId());
        erpsUsers.setErpUserLogin(erpContact.getErpUsername());
        // TODO: need to salt/encrypt password instead of plaintext
        erpsUsers.setErpUserPassword(erpContact.getErpPassword());
        erpsUsersDAO.save(erpsUsers);

        // 3. Update Permissions in Okta
        authenticationService.updateNewUserPermissions(user);
    }

    // TODO: DELETE THIS
    /**
     * Resend the verification email for the given userId
     *
     * @param userId userId to resend verification email for
     * @throws UserNotFoundException thrown when given userId is not found in DB
     */
    @Transactional
    public void resendVerificationEmail(UUID userId, boolean isWaterworksSubdomain) throws UserNotFoundException {
        EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO();
        Optional<User> maybeUser = userDAO.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        String userEmail = maybeUser.get().getEmail();
        AccountRequest accountRequest = accountRequestDAO.findMostRecentRequestByEmail(userEmail);
        employeeVerificationDTO.setEmail(userEmail);
        employeeVerificationDTO.setFirstName(maybeUser.get().getFirstName());
        employeeVerificationDTO.setLastName(maybeUser.get().getLastName());
        employeeVerificationDTO.setDomain(userEmail.substring(userEmail.indexOf("@") + 1, userEmail.lastIndexOf(".")));
        employeeVerificationDTO.setBrand(isWaterworksSubdomain ? ErpUtility.MINCRON_BRAND_NAME : "Reece");
        if (
            featuresService
                .getFeatures()
                .stream()
                .filter(x -> x.getIsEnabled() && x.getName().equals("NEW_REGISTRATION"))
                .findFirst()
                .orElse(null) !=
            null
        ) {
            employeeVerificationDTO.setVerificationToken(maybeUser.get().getId());
        } else {
            employeeVerificationDTO.setVerificationToken(accountRequest.getVerificationToken());
        }
        employeeVerificationDTO.setWaterworksSubdomain(isWaterworksSubdomain);

        notificationService.sendEmployeeVerificationEmail(employeeVerificationDTO);
    }

    /**
     * Re-send Employee Verification Email
     * @param userId
     * @throws UserNotFoundException
     */
    @Transactional
    public void resendEmployeeVerificationEmail(UUID userId) throws UserNotFoundException {
        var user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        notificationService.sendEmployeeVerificationEmail(new EmployeeVerificationDTO(user));
    }

    /**
     * Check if email is reece domain for employees
     * @param domain
     * @return
     */
    public boolean isReeceDomain(String domain) {
        List<String> domainList = Arrays.asList(employeeDomains);
        String testEmailDomain = domain.substring(domain.indexOf("@") + 1);
        return domainList.contains(testEmailDomain);
    }
}
