package com.reece.platform.accounts.service;

import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.API.ApiUserResponseDTO;
import com.reece.platform.accounts.model.DTO.ERP.EntitySearchResult;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.model.seed.PermissionType;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import com.reece.platform.accounts.utilities.ErpUtility;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AccountService {

    @Value("${employee_domains}")
    private String[] employeeDomains;

    @Value("${employee_eclipse_account:287169}")
    private String employeeEclipseAccount;

    @Value("${enable_notification_calls:true}")
    private Boolean enableNotificationCalls;

    // TODO: Remove dependency
    @Autowired
    private AccountRequestDAO accountRequestDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UsersBillToAccountsDAO usersBillToAccountsDAO;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ErpsUsersDAO erpsUsersDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ErpService erpService;

    @Autowired
    private InvitedUserDAO invitedUserDAO;

    @Autowired
    private BranchService branchService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskService taskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    // TODO: Delete function
    /**
     * For new users, create an account in the authentication provider and save user info in the users and account_requests tables.
     *
     * @param createUserDTO user info to save in authentication provider and eComm DB
     * @param inviteId ID of invite for pre-approved user
     * @return true if user created successfully, false if user already exists
     * @throws ResourceException exception thrown from authentication provider for invalid user data (i.e. password requirements not met)
     */
    @Transactional
    public AccountRequestDTO createAccount(CreateUserDTO createUserDTO, UUID inviteId)
        throws ResourceException, UserAlreadyExistsException, TermsNotAcceptedException, PhoneTypeNotFoundException, BranchNotFoundException, UserNotFoundException, UserAlreadyApprovedException, UserRoleNotFoundException, InvalidInviteException, AccountNotFoundException, UserNotEmployeeException {
        boolean isEmployee = isReeceDomain(createUserDTO.getEmail());

        if (!isEmployee && Strings.isBlank(createUserDTO.getAccountInfo().getAccountNumber())) {
            throw new UserNotEmployeeException();
        }

        // validate user has accepted terms of service and privacy policy
        if (!(createUserDTO.isTosAccepted() && createUserDTO.isPpAccepted())) {
            throw new TermsNotAcceptedException();
        }

        // validate user doesn't already exist in eComm DB unless user was migrated from legacy system
        Optional<User> existingUser = userDAO.findByEmail(createUserDTO.getEmail());
        if (existingUser.isPresent()) {
            Optional<InvitedUser> optionalUserInvite = invitedUserDAO.findById(inviteId);
            // if user has a processed or non-existent invite or an authID set, assume they have been created and throw exception
            if (
                optionalUserInvite.isEmpty() ||
                optionalUserInvite.get().isCompleted() ||
                existingUser.get().getAuthId() != null
            ) {
                throw new UserAlreadyExistsException(createUserDTO.getEmail());
            }
        }

        // Fetch the branch associated with the account if given branchId
        BranchDTO branch = null;
        if (createUserDTO.getBranchId() != null && !createUserDTO.getBranchId().isBlank()) {
            branch = branchService.getBranch(createUserDTO.getBranchId());
        }

        if (isEmployee) {
            // TODO: tw - hardcoding to eclipse for the time being
            var accountInfo = new AccountInfoDTO(ErpEnum.ECLIPSE, employeeEclipseAccount, "Reece");
            createUserDTO.setAccountInfo(accountInfo);
        }

        var phoneType = createUserDTO.getPhoneTypeId();

        var accountInfo = erpService.getErpAccount(
            createUserDTO.getAccountInfo().getAccountNumber(),
            createUserDTO.getAccountInfo().getErpId()
        );

        UUID accountId = getAccountId(
            createUserDTO.getAccountInfo().getAccountNumber(),
            createUserDTO.getAccountInfo().getErpId()
        );

        com.okta.sdk.resource.user.User authUser = authenticationService.createUser(createUserDTO, branch);

        // create user request entry in eComm DB
        AccountRequest accountRequest = new AccountRequest(
            createUserDTO,
            false,
            accountId,
            accountInfo,
            createUserDTO.getPhoneTypeId(),
            branch != null ? branch.getPhone() : null,
            isEmployee
        );
        accountRequestDAO.save(accountRequest);

        // create user in eComm DB
        Date tosAcceptedAt = new Date(System.currentTimeMillis());
        Date ppAcceptedAt = new Date(System.currentTimeMillis());
        User eCommUser;
        // existing users from legacy migration will have a User entry but will need to be updated with registration information
        if (existingUser.isPresent()) {
            existingUser.get().setAuthId(authUser.getId());
            existingUser.get().setEmail(createUserDTO.getEmail());
            existingUser.get().setFirstName(createUserDTO.getFirstName());
            existingUser.get().setLastName(createUserDTO.getLastName());
            existingUser.get().setPhoneNumber(createUserDTO.getPhoneNumber());
            existingUser.get().setTosAcceptedAt(tosAcceptedAt);
            existingUser.get().setPpAcceptedAt(ppAcceptedAt);
            existingUser.get().setPhoneType(phoneType);
            eCommUser = userDAO.save(existingUser.get());
        } else {
            eCommUser =
                new User(
                    authUser.getId(),
                    createUserDTO.getEmail(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    createUserDTO.getPhoneNumber(),
                    tosAcceptedAt,
                    ppAcceptedAt,
                    phoneType
                );
            eCommUser = userDAO.save(eCommUser);
        }

        boolean isPreApproved = false;
        if (!isEmployee) {
            // check if user is pre-approved via invite
            Optional<InvitedUser> optionalUserInvite = Optional.empty();
            if (inviteId != null) {
                optionalUserInvite = invitedUserDAO.findById(inviteId);
            }
            if (optionalUserInvite.isPresent()) {
                InvitedUser userInvite = optionalUserInvite.get();
                if (userInvite.isCompleted()) throw new InvalidInviteException();
                if (!userInvite.getEmail().equals(createUserDTO.getEmail())) throw new InvalidInviteException(
                    createUserDTO.getEmail()
                ); else {
                    isPreApproved = true;
                    ApproveUser preApprovedUser = new ApproveUser();
                    preApprovedUser.setUserId(eCommUser.getId().toString());
                    preApprovedUser.setUserRoleId(userInvite.getUserRoleId().toString());
                    preApprovedUser.setApproverId(userInvite.getApproverId());
                    usersService.approveUser(preApprovedUser);
                    userInvite.setCompleted(true);
                    invitedUserDAO.save(userInvite);
                }
            }
        }
        authenticationService.updateUserPermissions(eCommUser);

        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setId(eCommUser.getId());
        accountRequestDTO.setEmployee(isEmployee);
        accountRequestDTO.setVerified(false);

        if (enableNotificationCalls) {
            if (isEmployee) {
                EmployeeVerificationDTO employeeVerificationDTO = new EmployeeVerificationDTO(
                    createUserDTO,
                    accountRequest.getVerificationToken()
                );
                notificationService.sendEmployeeVerificationEmail(employeeVerificationDTO);
            } else if (branch != null) {
                List<UserDTO> accountAdmins = getAccountAdmins(accountId);
                if (accountAdmins.isEmpty()) {
                    // only send new customer notification when user is not an employee and accountId isn't found in eComm or account does not have any admins
                    NewCustomerNotificationDTO newCustomerNotificationDTO = new NewCustomerNotificationDTO(
                        branch,
                        accountInfo,
                        createUserDTO,
                        false
                    );
                    notificationService.sendNewCustomerEmailBranchManager(newCustomerNotificationDTO);
                } else if (!isPreApproved) {
                    NewCustomerNotificationDTO newUserNotificationDTO = new NewCustomerNotificationDTO(
                        branch,
                        accountInfo,
                        createUserDTO,
                        true
                    );
                    newUserNotificationDTO.setAccountAdmins(accountAdmins);
                    notificationService.sendNewUserAwaitingApprovalEmail(newUserNotificationDTO);
                }
            }
        }

        return accountRequestDTO;
    }

    /**
     * Get Erp Account Information for account Number
     *  - verify that account and zipcode match
     * @param request
     * @return
     * @throws AccountNotFoundException
     */
    public AccountValidationResponseDTO validateAccount(AccountValidationRequestDTO request)
        throws AccountNotFoundException {
        var accountInfo = getAccountByNumberAndBrand(request.getAccountNumber(), request.getBrand());
        String companyName;
        verifyAccount(accountInfo);

        if (accountInfo.isShipFlag()) {
            // fetch the billTo account from eclipse
            var billToAccount = getAccountByNumberAndBrand(accountInfo.getBillToId(), request.getBrand());
            verifyZipCode(request, billToAccount.getZip());
            companyName = billToAccount.getCompanyName();
        } else {
            // If account is billTo, just verify zip
            verifyZipCode(request, accountInfo.getZip());
            companyName = accountInfo.getCompanyName();
        }

        boolean isTradeAccount = isTradeAccount(accountInfo);
        return new AccountValidationResponseDTO(companyName, isTradeAccount);
    }

    public AccountValidationResponseDTO validateAccountNew(AccountValidationRequestDTO request)
        throws AccountNotFoundException {
        var erp = ErpUtility.getErpFromBrand(request.getBrand());
        var accountInfo = new ErpAccountInfo();

        if (erp.equals(ErpEnum.ECLIPSE)) {
            accountInfo = getAccountByNumberAndBrandNew(request.getAccountNumber(), request.getBrand());
            verifyAccount(accountInfo);
            if (accountInfo.isShipFlag() && accountInfo.getBillToId() != null) {
                // fetch the billTo account from eclipse
                accountInfo = getAccountByNumberAndBrand(accountInfo.getBillToId(), request.getBrand());
            }
        } else {
            accountInfo = getAccountByNumberAndBrandNew(request.getAccountNumber(), request.getBrand());
            // verifyAccount(accountInfo);
            if (accountInfo.isShipFlag() && accountInfo.getBillToId() != null) {
                // fetch the billTo account from eclipse
                accountInfo = getAccountByNumberAndBrand(accountInfo.getBillToId(), request.getBrand());
            }
        }

        Boolean isTradeAccount = isTradeAccount(accountInfo);
        verifyZipCode(request, accountInfo.getZip());

        if (!isTradeAccount) {
            return new AccountValidationResponseDTO(accountInfo.getCompanyName(), false);
        }

        return new AccountValidationResponseDTO(accountInfo.getCompanyName(), true);
    }

    private static void verifyZipCode(AccountValidationRequestDTO request, String zipCode)
        throws AccountNotFoundException {
        var accountZipCode = AccountDataFormatting.formatZipcode(zipCode);
        if (!Objects.equals(accountZipCode, request.getZipcode())) {
            throw new AccountNotFoundException();
        }
    }

    private void verifyAccount(ErpAccountInfo accountInfo) throws AccountNotFoundException {
        //verify that account is not shipTo neither is not billTo
        if (!accountInfo.isShipFlag() && !accountInfo.isBillToFlag()) {
            throw new AccountNotFoundException();
        }
    }

    private boolean isTradeAccount(ErpAccountInfo accountInfo) {
        return (
            accountInfo.isBillToFlag() &&
            !accountInfo.isBranchFlag() &&
            !accountInfo.getCompanyName().toLowerCase().contains("cash customer")
        );
    }

    /**
     * Get Account Information by Account number and Brand
     * @param accountNumber
     * @param brand
     * @return
     * @throws AccountNotFoundException
     */
    public ErpAccountInfo getAccountByNumberAndBrand(String accountNumber, String brand)
        throws AccountNotFoundException {
        var erp = ErpUtility.getErpFromBrand(brand);
        return erpService.getErpBillToAccount(accountNumber, erp);
    }

    public ErpAccountInfo getAccountByNumberAndBrandNew(String accountNumber, String brand)
        throws AccountNotFoundException {
        var erp = ErpUtility.getErpFromBrand(brand);
        return erpService.getErpBillToOrShipToAccount(accountNumber, erp);
    }

    /**
     * Create an eComm account in the eComm DB (accounts and erp_accounts)
     *
     * @param erpAccount account returned from ERP call
     * @return account created
     */
    @Transactional
    Account findOrCreateAccount(ErpAccountInfo erpAccount, ErpEnum erp, Account parentAccount) {
        // new account to create in eComm DB, first user approved
        var existingAccount = accountDAO
            .findAllByErpAccountIdAndErp(erpAccount.getErpAccountId(), erp)
            .stream()
            .filter(Account::isBillto)
            .findFirst();
        if (existingAccount.isPresent()) {
            return existingAccount.get();
        }

        Account accountToAdd = new Account();
        accountToAdd.setName(erpAccount.getCompanyName());
        accountToAdd.setParentAccount(parentAccount);
        accountToAdd.setErpAccountId(erpAccount.getErpAccountId());
        accountToAdd.setAddress(
            AccountDataFormatting.formatAccountAddress(
                erpAccount.getStreet1(),
                erpAccount.getCity(),
                erpAccount.getState(),
                erpAccount.getZip()
            )
        );
        accountToAdd.setErpAccountId(erpAccount.getErpAccountId());
        accountToAdd.setErp(erp);
        accountToAdd.setBranchId(erpAccount.getBranchId());
        accountToAdd.setBillto(parentAccount == null);

        return accountDAO.save(accountToAdd);
    }

    /**
     * Create an eComm account in the eComm DB (accounts and erp_accounts)
     *
     * @param erpAccount account returned from ERP call
     * @return account created
     */
    @Transactional
    public Optional<Account> createEcommAccount(ErpAccountInfo erpAccount, ErpEnum erp, Account parentAccount) {
        // new account to create in eComm DB, first user approved
        if (
            parentAccount == null && accountDAO.findByErpAccountIdAndErp(erpAccount.getErpAccountId(), erp).isPresent()
        ) {
            return Optional.empty();
        }
        if (
            parentAccount != null &&
            accountDAO
                .findByErpAccountIdAndParentAccountId(erpAccount.getErpAccountId(), parentAccount.getId())
                .isPresent()
        ) {
            return Optional.empty();
        }

        Account accountToAdd = new Account();

        if (erpAccount.getCompanyName() != null) {
            accountToAdd.setName(erpAccount.getCompanyName());
        }

        if (parentAccount != null) {
            accountToAdd.setParentAccount(parentAccount);
        }

        accountToAdd.setErpAccountId(erpAccount.getErpAccountId());

        accountToAdd.setAddress(
            AccountDataFormatting.formatAccountAddress(
                erpAccount.getStreet1(),
                erpAccount.getCity(),
                erpAccount.getState(),
                erpAccount.getZip()
            )
        );

        accountToAdd.setErpAccountId(erpAccount.getErpAccountId());
        accountToAdd.setErp(erp);
        accountToAdd.setBranchId(erpAccount.getBranchId());
        accountDAO.save(accountToAdd);

        return Optional.of(accountToAdd);
    }

    public List<ErpAccountInfo> getErpAccount(String authorization, String accountId, String brand)
        throws AccountNotFoundException {
        var erp = ErpUtility.getErpFromBrand(brand);
        List<ErpAccountInfo> accountInfos;
        try {
            UUID ecommAccountId = UUID.fromString(accountId);
            var id = accountDAO.findById(ecommAccountId).orElseThrow(AccountNotFoundException::new).getErpAccountId();
            accountInfos = erpService.getErpAccounts(id, erp);
        } catch (IllegalArgumentException exception) {
            accountInfos = erpService.getErpAccounts(accountId, erp);
        }

        return accountInfos.stream().map(acc -> acc.sanitize(authorization == null)).collect(Collectors.toList());
    }

    public ResponseEntity<EntitySearchResponseDTO> searchEntity(String accountId) throws AccountNotFoundException {
        ResponseEntity<EntitySearchResult> result = erpService.searchEntity(accountId);
        EntitySearchResponseDTO responseDTO = new EntitySearchResponseDTO(result.getBody());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Transactional
    public DeleteEcommAccountResponseDTO deleteEcommAccount(String erpAccountId, ErpEnum erp) {
        val response = new DeleteEcommAccountResponseDTO();

        val billtos = accountDAO
            .findAllByErpAccountIdAndErp(erpAccountId, erp)
            .stream()
            .filter(Account::isBillto)
            .collect(Collectors.toList());

        billtos.forEach(billto -> {
            log.info("Beginning delete of account " + erpAccountId);

            val shiptos = accountDAO.findAllByParentAccountId(billto.getId());
            response.setShipToCount(shiptos.size() + response.getShipToCount());

            // TODO: Remove this line
            accountRequestDAO.deleteByAccountId(billto.getId());
            invitedUserDAO.deleteAllByBillToAccountId(billto.getId());
            log.info("Removed account requests for billto");

            shiptos.forEach(shipto -> {
                log.info("Removing ship to " + shipto.getErpAccountId());

                // Clear any requests that might be set on a shipto level
                // TODO: Remove this line
                accountRequestDAO.deleteByAccountId(shipto.getId());

                // Clear carts before removing users
                // Note that we do it by shipto because employees won't be listed as users under the account but will have carts
                productsService
                    .deleteCartsByShipToId(shipto.getId())
                    .ifPresentOrElse(
                        res ->
                            log.info(
                                res.getCartsDeleted() +
                                " carts deleted with " +
                                res.getLineItemsDeleted() +
                                " line items deleted"
                            ),
                        () -> log.error("Error deleting carts for shipto: {}", shipto.getErpAccountId())
                    );

                productsService
                    .deleteDeliveries(shipto.getId())
                    .ifPresentOrElse(
                        res ->
                            log.info(
                                "Removed " +
                                res.getDeletedCount() +
                                " deliveries for shipto " +
                                shipto.getErpAccountId()
                            ),
                        () -> log.error("Error removing deliveries for shipto ")
                    );

                accountDAO.delete(shipto);
                log.info("Removed erps_accounts for shipto");
            });

            val users = userDAO.findByBillToAccounts_Id(billto.getId());

            // Remove the users
            users.forEach(user -> {
                try {
                    log.info("Removing user with email: " + user.getEmail());

                    invitedUserDAO.findByEmail(user.getEmail()).ifPresent(invitedUserDAO::delete);
                    log.info("User invites deleted");

                    usersService.deleteUser(billto.getId(), user.getId(), false);

                    log.info("Deleted user {}", user.getEmail());
                } catch (AccountNotFoundException e) {
                    log.error("Bill to account not found for user", e);
                } catch (UserNotFoundException e) {
                    log.error("User not found exception deleting " + user.getEmail(), e);
                } catch (EclipseException e) {
                    log.error("Eclipse exception deleting " + user.getEmail(), e);
                } catch (BranchNotFoundException e) {
                    log.error("Branch not found exception deleting " + user.getEmail(), e);
                } catch (UnableToDeleteUserException e) {
                    log.error("Unable to delete user exception deleting " + user.getEmail(), e);
                }
            });

            response.setUserCount(response.getUserCount() + users.size());

            accountDAO.deleteAll(shiptos);
            log.info("Removed all shiptos for billto {}", billto.getErpAccountId());

            accountDAO.delete(billto);
            log.info("Removed billto erp account");
        });

        accountDAO.deleteAll(billtos);

        response.setSuccess(true);
        return response;
    }

    /**
     * Fetch list of users that can approve for an account
     *
     * @param billToAccountId Bill-To Account Id
     * @return list of users that can approve on an account
     */
    public List<ApproverDTO> getApprovers(UUID billToAccountId) throws AccountNotFoundException {
        var billToAccount = accountDAO.findById(billToAccountId);

        if (billToAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }

        return billToAccount
            .get()
            .getUsers()
            .stream()
            .filter(u -> u.getPermissions().stream().anyMatch(p -> p.getName() == PermissionType.approve_cart))
            .map(ApproverDTO::new)
            .collect(Collectors.toList());
    }

    // TODO: Remove function
    /**
     * Fetch all account requests for the given accountId
     *
     * @param accountId account to pull account requests for
     * @return all account requests for the given account id
     */
    public List<AccountRequest> getUnapprovedAccountRequests(UUID accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);

        if (account.getShipToAccounts() == null || account.getShipToAccounts().isEmpty()) {
            return accountRequestDAO.findAllByAccountIdAndIsCompletedAndIsEmployeeAndRejectionReasonNull(
                accountId,
                false,
                false
            );
        }

        Set<UUID> allAccounts = account.getShipToAccounts().stream().map(Account::getId).collect(Collectors.toSet());
        allAccounts.add(accountId);
        return accountRequestDAO.findAllByAccountIdsAndIsCompletedAndIsEmployee(allAccounts, false, false);
    }

    // TODO: Remove function
    /**
     * Fetch all account requests where accountId is null
     *
     * @return all account requests
     */
    public List<AccountRequest> getAllUnapprovedAccountRequests() {
        return accountRequestDAO.findAllByAccountIdAndIsCompletedAndIsEmployeeAndRejectionReasonNull(
            null,
            false,
            false
        );
    }

    // TODO: Remove function
    /**
     * Fetch all rejected account requests for the given accountId
     *
     * @param accountId account to pull account requests for
     * @return all account requests that have been rejected for the given account id
     */
    public List<AccountRequest> getRejectedAccountRequests(UUID accountId) throws AccountNotFoundException {
        if (!accountDAO.existsById(accountId)) {
            throw new AccountNotFoundException();
        }

        return accountRequestDAO.findAllByRejectionReasonNotNullAndAccountId(accountId);
    }

    /**
     * Retrieve all possible roles for user
     *
     * @return all roles
     */
    public List<Role> getRoles() {
        return roleDAO
            .findAll()
            .stream()
            .filter(role -> !role.getIsInternal())
            .map(this::scrubRole)
            .collect(Collectors.toList());
    }

    /**
     * Scrubs out relationships in Role entity that are not lazy loading
     * TODO: look into why lazy loading wasn't working for relationships in Role; fix and remove this role scrubbing function
     *
     * @param role role to be scrubbed
     * @return scrubbed role
     */
    private Role scrubRole(Role role) {
        role.setPermissions(null);
        return role;
    }

    /**
     * Retrieve all users information associated with the given account id.
     *
     * @param accountId accountId to find users for
     * @return account user DTO with user information for all users with given account id
     */
    public List<ApiUserResponseDTO> getAccountUsers(UUID accountId) throws AccountNotFoundException {
        if (!accountDAO.existsById(accountId)) {
            throw new AccountNotFoundException();
        }

        // retrieve eComm users by given accountId and massage out needed data for DTO
        return userDAO
            .findAllById(
                usersBillToAccountsDAO
                    .findAllByAccountId(accountId)
                    .stream()
                    .map(UsersBillToAccounts::getUserId)
                    .collect(Collectors.toList())
            )
            .stream()
            .map(ApiUserResponseDTO::new)
            .filter(u -> u.getRole() != null && !(u.getRole().getName().equals(RoleEnum.MORSCO_ADMIN.label)))
            .collect(Collectors.toList());
    }

    @Transactional
    public void syncErpAccountsToDb(Account account) throws AccountNotFoundException {
        List<Account> shipToAccounts = account.getShipToAccounts();
        List<Account> accountsToSave = new ArrayList<>();
        var shouldSave = false;

        // build list of existing ship to accounts in eComm
        Set<String> existingShipToAccountIds = shipToAccounts != null && !shipToAccounts.isEmpty()
                ? shipToAccounts.stream().map((Account::getErpAccountId)).collect(Collectors.toSet())
                : new HashSet<>();

        // build list of actual ship to accounts from ERP system
        ErpEnum erpEnum = account.getErp();
        ErpAccountInfo billToAccountERP;
        boolean isEclipse = erpEnum.equals(ErpEnum.ECLIPSE);
        if (isEclipse) {
            billToAccountERP = erpService.getEclipseAccount(account.getErpAccountId(), false, true);
        } else {
            billToAccountERP = erpService.getMincronAccount(account.getErpAccountId(), true, false);
        }


        List<String> actualShipToAccountIds = billToAccountERP.getShipToAccountIds() == null
                ? (
                (billToAccountERP.getShipToAccounts() != null && !billToAccountERP.getShipToAccounts().isEmpty())
                        ? billToAccountERP.getShipToAccounts().stream().map(ErpAccountInfo::getErpAccountId).toList()
                        : new ArrayList<>()
        )
                : billToAccountERP.getShipToAccountIds();

        List<ErpAccountInfo> actualShipToAccountsERP = billToAccountERP.getShipToAccounts() != null
                ? billToAccountERP.getShipToAccounts()
                : new ArrayList<>();

        Account billToAccountDB = account;

        // Update BillTo account address in eComm database if changed
        String billToAccountAddress = AccountDataFormatting.formatAccountAddress(
                billToAccountERP.getStreet1(),
                billToAccountERP.getCity(),
                billToAccountERP.getState(),
                billToAccountERP.getZip()
        );
        if (billToAccountDB.getAddress() == null || !billToAccountDB.getAddress().equals(billToAccountAddress)) {
            billToAccountDB.setAddress(billToAccountAddress);
            shouldSave = true;
        }

        if (billToAccountDB.getName() == null || !billToAccountDB.getName().equals(billToAccountERP.getCompanyName())) {
            billToAccountDB.setName(billToAccountERP.getCompanyName());
            shouldSave = true;
        }

        if (shouldSave) {
            accountDAO.save(billToAccountDB);
        }
        
        List<String> newShipToAccountIds = actualShipToAccountIds
                .stream()
                .filter(element -> !existingShipToAccountIds.contains(element))
                .toList();

        List<ErpAccountInfo> newShipToAccounts = new ArrayList<>();
        if (!newShipToAccountIds.isEmpty()) {
            if (isEclipse) {
                for (String shipToId : newShipToAccountIds) {
                    newShipToAccounts.add(erpService.getEclipseAccount(shipToId, false, false));
                }
            } else {
                newShipToAccounts.addAll(
                        billToAccountERP
                                .getShipToAccounts()
                                .stream()
                                .filter(shipTo -> newShipToAccountIds.contains(shipTo.getErpAccountId()))
                                .toList()
                );
            }

            newShipToAccounts.forEach(newAccount ->
                    createEcommAccount(newAccount, billToAccountERP.getErp(), billToAccountDB)
            );
        }

        // Update the Home Branch
        if (isEclipse) {
            updateAccountHomeBranch(account, billToAccountERP.getBranchId());
        }

        // Remove ShipTos that do not exist anymore in ERP
        List<String> removedShipToAccountIds = existingShipToAccountIds
                .stream()
                .filter(element -> !actualShipToAccountIds.contains(element))
                .collect(Collectors.toList());

        removeShipToIfDeletedFromErp(account, removedShipToAccountIds);

        // Update ShipTo Addresses
        final List<String> shipToIdsToIgnore = Stream.concat(newShipToAccountIds.stream(), removedShipToAccountIds.stream()).toList();
        List<Account> shipTosToUpdateAddress = billToAccountDB.getShipToAccounts().stream().filter(x -> !shipToIdsToIgnore.contains(x.getErpAccountId())).toList();

        for (Account shipTo : shipTosToUpdateAddress) {
            var shipToERP = actualShipToAccountsERP.stream().filter(x -> x.getErpAccountId().equals(shipTo.getErpAccountId())).findFirst().orElse(null);

            if (shipToERP != null) {
                String shipToAccountAddress = AccountDataFormatting.formatAccountAddress(
                        shipToERP.getStreet1(),
                        shipToERP.getCity(),
                        shipToERP.getState(),
                        shipToERP.getZip()
                );
                if (shipTo.getAddress() == null || !shipTo.getAddress().equals(shipToAccountAddress)) {
                    shipTo.setAddress(shipToAccountAddress);
                }
                if (shipTo.getName() == null || !shipTo.getName().equals(shipToERP.getCompanyName())) {
                    shipTo.setName(shipToERP.getCompanyName());
                }
                accountsToSave.add(shipTo);
            }
        }
        if (!accountsToSave.isEmpty()) {
            accountDAO.saveAll(accountsToSave);
        }
    }

    /**
     * Remove shipTo account(s) from e-commerce system if not present on the same account in the erp system.
     *
     * @param parentAccount account information from e-commerce system
     * @param shipToErpAccountIdsToRemoveFromEcomm ship tos not found in Eclipse
     */
    @Transactional
    public void removeShipToIfDeletedFromErp(Account parentAccount, List<String> shipToErpAccountIdsToRemoveFromEcomm) {
        if (shipToErpAccountIdsToRemoveFromEcomm != null && !shipToErpAccountIdsToRemoveFromEcomm.isEmpty()) {
            shipToErpAccountIdsToRemoveFromEcomm.forEach(erpAccountId -> {
                List<Account> accounts = accountDAO.findAllByErpAccountIdAndParentAccountId(
                    erpAccountId,
                    parentAccount.getId()
                );
                accounts.forEach(account -> {
                    accountDAO.deleteById(account.getId());
                });
            });
        }
    }

    /**
     * Fetch will call locations for the given account id
     *
     * @param shipToAccountId account id to fetch location information for
     * @return list of branch locations
     * @throws BranchNotFoundException when branch isn't found from branch service
     * @throws AccountNotFoundException when account with given ID does not exist either in eComm or ERP system
     */
    public List<BranchDTO> getWillCallLocations(UUID shipToAccountId)
        throws BranchNotFoundException, AccountNotFoundException {
        Account account = accountDAO.findById(shipToAccountId).orElseThrow(AccountNotFoundException::new);

        // TODO - update this for Mincron accounts when that functionality is implemented. For now just return empty list.
        if (account.getErp() == ErpEnum.MINCRON) return new ArrayList<>();

        ErpAccountInfo erpAccountInfo = erpService.getErpAccount(account.getErpAccountId(), account.getErp());

        return Collections.singletonList(branchService.getBranch(erpAccountInfo.getBranchId()));
    }

    /**
     * Fetch home branch for the given account id
     *
     * @param accountId account id to fetch location information for
     * @return branch details
     * @throws BranchNotFoundException when branch isn't found from branch service
     * @throws AccountNotFoundException when account with given ID does not exist either in eComm or ERP system
     */
    public BranchDTO getHomeBranch(UUID accountId) throws BranchNotFoundException, AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);

        if (account.getErp() == ErpEnum.MINCRON) {
            Account parentAccount = account.getParentAccount();
            String branchId;

            if (parentAccount != null) {
                ErpAccountInfo erpAccountInfo = erpService.getMincronAccount(
                    parentAccount.getErpAccountId(),
                    true,
                    false
                );

                branchId =
                    erpAccountInfo
                        .getShipToAccounts()
                        .stream()
                        .filter(shipToAccount -> shipToAccount.getErpAccountId().equals(account.getErpAccountId()))
                        .findFirst()
                        .orElseThrow(AccountNotFoundException::new)
                        .getBranchId();
            } else {
                ErpAccountInfo erpAccountInfo = erpService.getMincronAccount(account.getErpAccountId(), true, false);
                branchId = erpAccountInfo.getBranchId();
            }
            return branchService.getBranch(branchId);
        }
        ErpAccountInfo erpAccountInfo = erpService.getEclipseAccount(account.getErpAccountId(), false, false);
        return branchService.getBranch(erpAccountInfo.getBranchId());
    }

    /**
     * Uploads job form and tax document to eclipse server
     *
     * @param jobFormDTO job form information
     * @return string indicating success or failure
     * @throws AccountNotFoundException when given account id is not found in ecomm DB
     */
    @Transactional
    public String createJobForm(JobFormDTO jobFormDTO) throws AccountNotFoundException {
        return erpService.createJobForm(jobFormDTO);
    }

    public boolean isReeceDomain(String domain) {
        List<String> domainList = Arrays.asList(employeeDomains);
        String testEmailDomain = domain.substring(domain.indexOf("@") + 1);

        return domainList.contains(testEmailDomain);
    }

    public UUID getAccountByErpId(String shipToId, ErpEnum erp) throws AccountNotFoundException {
        return accountDAO.findByErpAccountIdAndErp(shipToId, erp).orElseThrow(AccountNotFoundException::new).getId();
    }

    @Transactional
    public List<ShipToAccountDTO> syncShipToAccount(UUID billToAccountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(billToAccountId).orElseThrow(AccountNotFoundException::new);
        syncErpAccountsToDb(account);
        List<Account> shipTos = accountDAO.findAllByParentAccountId(billToAccountId);
        return shipTos
            .stream()
            .map(shipTo -> new ShipToAccountDTO(shipTo, shipTo.getErp()))
            .collect(Collectors.toList());
    }

    /**
     * To update home branch in MAX database for Bill to and related Ship to accounts
     * @param
     * @return
     */
    @Transactional
    public void updateAccountHomeBranch(Account account, String branchId) {
        account.getShipToAccounts().forEach(a -> a.setBranchId(branchId));
        account.setBranchId(branchId);
        accountDAO.save(account);
    }

    /**
     * Fetch account admins if any exist. Return empty list if accountId is null
     * @param accountId
     * @return
     */
    public List<UserDTO> getAccountAdmins(UUID accountId) {
        List<UserDTO> accountAdmins = new ArrayList<>();
        if (accountId != null) {
            Optional<Account> account = accountDAO.findById(accountId);
            if (account.isPresent() && account.get().getUsers() != null) {
                account.get()
                        .getUsers()
                        .stream()
                        .filter(u -> u.getRole() != null && u.getRole().getName().equals(RoleEnum.ACCOUNT_ADMIN.label))
                        .forEach(u -> {
                             accountAdmins.add(new UserDTO(u));
                        });
            }
        }

        return accountAdmins;
    }

    /**
     * Get Account Id
     * @param accountId
     * @param erpId
     * @return
     */
    private UUID getAccountId(String accountId, ErpEnum erpId) {
        Optional<Account> account = accountDAO
            .findAllByErpAccountIdAndErp(accountId, erpId)
            .stream()
            .filter(Account::isBillto)
            .findFirst();
        return account.map(Account::getId).orElse(null);
    }

    /**
     *  Endpoint that updates the existing accounts in parallel with the new structure (EntityName as name column and previously formatted address in new address column)
     *
     */
    @Transactional
    public void refreshAccounts() {
        long startTime = System.currentTimeMillis();
        List<Account> allAccounts = accountDAO.findAll();
        LOGGER.info("Starting account refresh for {} total accounts.", allAccounts.size());
        int partitionSize = allAccounts.size() > 20000 ? 1000 : 500;
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        // Partition get ERP accounts in parallel
        int index = 0;
        try {
            for (int i = 0; i < allAccounts.size(); i += partitionSize) {
                var partitionedList = allAccounts.subList(i, Math.min(i + partitionSize, allAccounts.size()));
                CompletableFuture<Integer> partitionedAccounts = taskService.getEclipseAccounts(partitionedList);
                futures.add(partitionedAccounts);
            }
            futures.forEach(CompletableFuture::join);

            for (CompletableFuture<Integer> future : futures) {
                index = index + future.get();
            }
            LOGGER.info("Refreshed account #{} of {}", index, allAccounts.size());
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            LOGGER.info(
                "Account refresh completed for {} total accounts.  Time elapsed: {} seconds",
                allAccounts.size(),
                duration / 1000
            );
        } catch (Exception e) {
            LOGGER.error("Refresh Account Failed: " + e.getMessage());
        }
    }
}
