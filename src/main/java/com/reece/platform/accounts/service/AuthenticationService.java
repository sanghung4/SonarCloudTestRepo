package com.reece.platform.accounts.service;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.application.AppUser;
import com.okta.sdk.resource.application.Application;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.*;
import com.reece.platform.accounts.exception.UpdatePasswordException;
import com.reece.platform.accounts.exception.UserNotFoundException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.entity.Permission;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import com.reece.platform.accounts.model.repository.*;
import com.reece.platform.accounts.model.seed.PermissionType;
import com.reece.platform.accounts.utilities.DecodedToken;
import com.reece.platform.accounts.utilities.ErpUtility;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthenticationService {

    private final String PERMISSIONS_ATTRIBUTE_NAME = "ecommPermissions";
    private final String USERID_ATTRIBUTE_NAME = "ecommUserId";
    private final String IS_EMPLOYEE_ATTRIBUTE_NAME = "isEmployee";
    private final String IS_VERIFIED_ATTRIBUTE_NAME = "isVerified";

    @Value("${fortiline_okta_group}")
    private final String FORTILINE = "Fortiline";

    private final Map<String, String> maxNamesToOktaGroups;

    @Autowired
    private Client client;

    @Autowired
    private UserDAO userDAO;

    // TODO: remove dependency
    @Autowired
    private AccountRequestDAO accountRequestDAO;

    @Value("${spring.datasource.application_id}")
    private String applicationId;

    @Autowired
    public AuthenticationService(
            @Value("${morrisonsupply_max_name}") String morrisonSupplyMaxName,
            @Value("${morrisonsupply_okta_group_name}") String morrisonSupplyMaxOktaGroupName,
            @Value("${devoreandjohnson_max_name}") String devoreAndJohnsonMaxName,
            @Value("${devoreandjohnson_okta_group_name}") String devoreAndJohnsonMaxOktaGroupName,
            @Value("${murraysupply_max_name}") String murraySupplyMaxName,
            @Value("${murraysupply_okta_group_name}") String murraySupplyMaxOktaGroupName,
            @Value("${morscohvac_max_name}") String morscoHVACMaxName,
            @Value("${morscohvac_okta_group_name}") String morscoHVACMaxOktaGroupName,
            @Value("${farnsworthwholesale_max_name}") String farnsworthWholesaleMaxName,
            @Value("${farnsworthwholesale_okta_group_name}") String farnsworthWholesaleMaxOktaGroupName,
            @Value("${wholesalespecialties_max_name}") String wholesaleSpecialtiesMaxName,
            @Value("${wholesalespecialties_okta_group_name}") String wholesaleSpecialtiesMaxOktaGroupName,
            @Value("${expressionshomegallery_max_name}") String expressionsHomeGalleryMaxName,
            @Value("${expressionshomegallery_okta_group_name}") String expressionsHomeGalleryMaxOktaGroupName,
            @Value("${expresspipeandsupply_max_name}") String expressPipeAndSupplyMaxName,
            @Value("${expresspipeandsupply_okta_group_name}") String expressPipeAndSupplyMaxOktaGroupName,
            @Value("${toddpipe_max_name}") String toddPipeMaxName,
            @Value("${toddpipe_okta_group_name}") String toddPipeMaxOktaGroupName,
            @Value("${landBPipe_max_name}") String landBPipeMaxName,
            @Value("${landBPipe_okta_group_name}") String landBPipeMaxOktaGroupName,
            @Value("${irvinepipe_max_name}") String irvinePipeMaxName,
            @Value("${irvinepipe_okta_group_name}") String irvinePipeMaxOktaGroupName
    ) {
        maxNamesToOktaGroups =
                Map.ofEntries(
                        Map.entry(morrisonSupplyMaxName,
                                morrisonSupplyMaxOktaGroupName),
                        Map.entry(devoreAndJohnsonMaxName,
                                devoreAndJohnsonMaxOktaGroupName),
                        Map.entry(murraySupplyMaxName,
                                murraySupplyMaxOktaGroupName),
                        Map.entry(morscoHVACMaxName,
                                morscoHVACMaxOktaGroupName),
                        Map.entry(farnsworthWholesaleMaxName,
                                farnsworthWholesaleMaxOktaGroupName),
                        Map.entry(wholesaleSpecialtiesMaxName,
                                wholesaleSpecialtiesMaxOktaGroupName),
                        Map.entry(expressionsHomeGalleryMaxName,
                                expressionsHomeGalleryMaxOktaGroupName),
                        Map.entry(expressPipeAndSupplyMaxName,
                                expressPipeAndSupplyMaxOktaGroupName),
                        Map.entry(toddPipeMaxName,
                                toddPipeMaxOktaGroupName),
                        Map.entry(landBPipeMaxName,
                                landBPipeMaxOktaGroupName),
                        Map.entry(irvinePipeMaxName, irvinePipeMaxOktaGroupName)
                );
    }

    /**
     * Create a user in authentication provider given user information.
     *
     * @param createUserDTO user information to use to create user
     * @return user created
     * @throws ResourceException exception for invalid user data (i.e. password doesn't meet requirements)
     */
    @Transactional
    public User createUser(CreateUserDTO createUserDTO, BranchDTO branchDTO) throws ResourceException {
        UserBuilder userBuilder = UserBuilder
                .instance()
                .setEmail(createUserDTO.getEmail())
                .setFirstName(createUserDTO.getFirstName())
                .setLastName(createUserDTO.getLastName())
                .setMobilePhone(createUserDTO.getPhoneNumber())
                .setPassword(createUserDTO.getPassword().toCharArray())
                .setActive(true);

        if (createUserDTO.getIsWaterworksSubdomain()) {
            GroupList groupList = listGroups(FORTILINE);
            String groupId = groupList
                    .stream()
                    .filter(group -> group.getProfile().getName().equals(FORTILINE))
                    .findFirst()
                    .map(Group::getId)
                    .orElse(null);
            if (groupId != null) {
                userBuilder.addGroup(groupId);
            }
        } else if (branchDTO != null && branchDTO.getName() != "" && branchDTO.getName() != null) {
            String groupId = listGroups(null)
                    .stream()
                    .filter(group -> group.getProfile().getName().equals(maxNamesToOktaGroups.get(branchDTO.getName())))
                    .findFirst()
                    .map(Group::getId)
                    .orElse(null);
            if (groupId != null) {
                userBuilder.addGroup(groupId);
            }
        }

        return userBuilder.buildAndCreate(client);
    }

    // TODO: Clean this up (remove other methods) (We don't need two createUser methods that do the exact same thing.)

    /**
     * Create a user in authentication provider given user information (after user registration refactor)
     *
     * @param createUserDTO
     * @return user created
     * @throws ResourceException
     */
    @Transactional
    public User createNewUser(UserRegistrationDTO createUserDTO, BranchDTO branchDTO) throws ResourceException {
        var erp = ErpUtility.getErpFromBrand(createUserDTO.getBrand());

        UserBuilder userBuilder = UserBuilder
                .instance()
                .setEmail(createUserDTO.getEmail())
                .setFirstName(createUserDTO.getFirstName())
                .setLastName(createUserDTO.getLastName())
                .setMobilePhone(createUserDTO.getPhoneNumber())
                .setPassword(createUserDTO.getPassword().toCharArray())
                .setActive(true);

        // TODO: Combine the following if/else and just handle all group assignments the same.

        if (ErpUtility.getErpFromBrand(createUserDTO.getBrand()).equals(ErpEnum.MINCRON)) {
            GroupList groupList = listGroups(FORTILINE);
            String groupId = groupList
                    .stream()
                    .filter(group -> group.getProfile().getName().equals(FORTILINE))
                    .findFirst()
                    .map(Group::getId)
                    .orElse(null);
            if (groupId != null) {
                userBuilder.addGroup(groupId);
            }
        } else if (branchDTO != null && branchDTO.getName() != "" && branchDTO.getName() != null) {
            String groupId = listGroups(null)
                    .stream()
                    .filter(group -> group.getProfile().getName().equals(maxNamesToOktaGroups.get(branchDTO.getName())))
                    .findFirst()
                    .map(Group::getId)
                    .orElse(null);
            if (groupId != null) {
                userBuilder.addGroup(groupId);
            }
        }

        return userBuilder.buildAndCreate(client);
    }

    /**
     * Create a user in auth provider for an employee. Does not set it to active until user validates via email
     *
     * @param newEmployee
     * @return
     * @throws ResourceException
     */
    @Transactional
    public User createEmployeeUser(CreateEmployeeDTO newEmployee) throws ResourceException {
        return UserBuilder
                .instance()
                .setEmail(newEmployee.getEmail())
                .setFirstName(newEmployee.getFirstName())
                .setLastName(newEmployee.getLastName())
                .setMobilePhone(newEmployee.getPhoneNumber())
                .setPassword(newEmployee.getPassword().toCharArray())
                .setActive(true)
                .buildAndCreate(client);
    }

    // TODO: Delete updateUserPermissions

    /**
     * Update a user's permissions in the authentication system
     *
     * @param dbUser user entity
     * @return true if successful
     */
    @Transactional
    public void updateUserPermissions(com.reece.platform.accounts.model.entity.User dbUser) {
        Application application = client.getApplication(applicationId);
        AppUser appUser = application.getApplicationUser(dbUser.getAuthId());
        appUser
                .getProfile()
                .put(
                        PERMISSIONS_ATTRIBUTE_NAME,
                        dbUser.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())
                );
        appUser.getProfile().put(USERID_ATTRIBUTE_NAME, dbUser.getId());
        accountRequestDAO
                .findByEmailAndRejectionReasonIsNull(dbUser.getEmail())
                .ifPresent(req -> {
                    appUser.getProfile().put(IS_EMPLOYEE_ATTRIBUTE_NAME, req.isEmployee());
                    appUser.getProfile().put(IS_VERIFIED_ATTRIBUTE_NAME, req.isVerified());
                });
        appUser.update();
    }

    public void updateNewUserPermissions(com.reece.platform.accounts.model.entity.User dbUser) {
        var isEmployee = Objects.equals(dbUser.getRole().getName(), RoleEnum.MORSCO_ADMIN.label);
        Application application = client.getApplication(applicationId);
        AppUser appUser = application.getApplicationUser(dbUser.getAuthId());
        appUser
                .getProfile()
                .put(
                        PERMISSIONS_ATTRIBUTE_NAME,
                        dbUser.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())
                );

        appUser.getProfile().put(USERID_ATTRIBUTE_NAME, dbUser.getId());
        appUser.getProfile().put(IS_EMPLOYEE_ATTRIBUTE_NAME, isEmployee);
        appUser.getProfile().put(IS_VERIFIED_ATTRIBUTE_NAME, true);
        appUser.update();
    }

    /**
     * Update a user's login and e-mail in the authentication system
     *
     * @param dbUser user entity
     */
    @Transactional
    public void updateUserLoginEmailAndProfile(com.reece.platform.accounts.model.entity.User dbUser) {
        Application application = client.getApplication(applicationId);
        AppUser appUser = application.getApplicationUser(dbUser.getAuthId());
        appUser.getCredentials().setUserName(dbUser.getEmail());
        User user = client.getUser(appUser.getId());
        user.getProfile().setEmail(dbUser.getEmail());
        user.getProfile().setLogin(dbUser.getEmail());
        user.getProfile().setFirstName(dbUser.getFirstName());
        user.getProfile().setLastName(dbUser.getLastName());

        setPhoneOktaUserProfile(dbUser, user.getProfile());

        user.update();
    }

    /**
     * Updates user's phone number and phone type on Okta profile.
     *
     * @param dbUser      e-commerce user
     * @param userProfile e-commerce user's Okta profile
     */
    @Transactional
    private void setPhoneOktaUserProfile(
            com.reece.platform.accounts.model.entity.User dbUser,
            UserProfile userProfile
    ) {
        val userPhoneType = dbUser.getPhoneType();

        for (val phone : PhoneTypeEnum.values()) {
            userProfile.put(phone.getOktaAttributeName(), null);
        }

        userProfile.put(userPhoneType.getOktaAttributeName(), dbUser.getPhoneNumber());
    }

    /**
     * Update a user's password in the authentication system
     *
     * @param dbUser                user entity
     * @param updateUserPasswordDTO user's old and new password
     */
    @Transactional
    public void updateUserPassword(
            com.reece.platform.accounts.model.entity.User dbUser,
            UpdateUserPasswordDTO updateUserPasswordDTO
    ) throws UpdatePasswordException {
        Application application = client.getApplication(applicationId);
        AppUser appUser = application.getApplicationUser(dbUser.getAuthId());

        User user = client.getUser(appUser.getId());
        try {
            user.changePassword(
                    client
                            .instantiate(ChangePasswordRequest.class)
                            .setOldPassword(
                                    client
                                            .instantiate(PasswordCredential.class)
                                            .setValue(updateUserPasswordDTO.getOldUserPassword().toCharArray())
                            )
                            .setNewPassword(
                                    client
                                            .instantiate(PasswordCredential.class)
                                            .setValue(updateUserPasswordDTO.getNewUserPassword().toCharArray())
                            )
            );
        } catch (Exception e) {
            throw new UpdatePasswordException(e.getMessage());
        }
    }

    /**
     * Delete a user in Okta
     *
     * @param dbUser user entity
     */
    public void deleteUser(com.reece.platform.accounts.model.entity.User dbUser) {
        try {
            User user = client.getUser(dbUser.getAuthId());
            user.deactivate();
            user.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Check if user has appropriate permissions to approve users
     *
     * @param token     current user auth token to check permissions
     * @param accountId user to be approved account id
     * @return true if user is authorized to approve, false otherwise
     */
    public boolean userCanManageAccountRequests(DecodedToken token, UUID accountId) {
        /*
        User must either (a) have approve_account_user permission and belong to this account or
        (b) have approve_all_users permission
         */

        if (accountId == null) return token.containsPermission(PermissionType.approve_all_users);

        if (token.containsPermission(PermissionType.approve_all_users)) return true;

        if (!token.containsPermission(PermissionType.approve_account_user)) return false;
        else {
            return userDAO
                    .findById(UUID.fromString(token.ecommUserId))
                    .map(user -> {
                        Hibernate.initialize(user.getBillToAccounts());
                        List<UUID> requesterAccountIds = user
                                .getBillToAccounts()
                                .stream()
                                .map(Account::getId)
                                .collect(Collectors.toList());
                        List<UUID> shipToAccountIds = user
                                .getBillToAccounts()
                                .stream()
                                .flatMap(accounts -> accounts.getShipToAccounts().stream())
                                .map(Account::getId)
                                .collect(Collectors.toList());
                        requesterAccountIds.addAll(shipToAccountIds);
                        return requesterAccountIds.stream().anyMatch(id -> id.equals(accountId));
                    })
                    .orElse(false);
        }
    }

    public boolean userCanManageUsers(DecodedToken token, UUID userId, UUID accountId) throws UserNotFoundException {
        /*
        User must either (a) be the same ID as the updateUserDTO (editing own profile) or
        (b) have "edit_profile" permission and belong to the same account as the user being edited
         */

        if (isUserUpdatingOwnAccount(token, userId)) return true;

        if (token.containsPermission(PermissionType.approve_all_users)) return true;

        if (token.containsPermission(PermissionType.edit_profile)) {
            List<UUID> requesterAccountIds = userDAO
                    .findById(UUID.fromString(token.ecommUserId))
                    .map(user -> user.getBillToAccounts().stream().map(Account::getId).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            com.reece.platform.accounts.model.entity.User user = userDAO
                    .findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with given ID not found."));

            List<UUID> editedUserAccountIds = user
                    .getBillToAccounts()
                    .stream()
                    .map(Account::getId)
                    .collect(Collectors.toList());

            return requesterAccountIds.contains(accountId) && editedUserAccountIds.contains(accountId);
        } else return false;
    }

    public boolean isUserUpdatingOwnAccount(DecodedToken token, UUID userId) {
        return UUID.fromString(token.ecommUserId).equals(userId);
    }

    public boolean userCanToggleFeatures(DecodedToken token) {
        return token.containsPermission(PermissionType.toggle_features);
    }

    public boolean userCanRefreshContact(String token) {
        DecodedToken decodedToken = DecodedToken.getDecodedHeader(token);
        return decodedToken.containsPermission(PermissionType.refresh_contact);
    }

    /**
     * Lists groups tied to Okta account
     *
     * @return
     */
    public GroupList listGroups(String groupName) {
        if (groupName != null) {
            return client.listGroups(groupName, null, null);
        }
        return client.listGroups();
    }

    /**
     * Get a single Okta user if it exists, by email
     *
     * @param email Email address to search for user in Okta
     * @return
     */
    public Optional<User> getUserByEmail(String email) {
        UserList users = client.listUsers(email, null, null, null, null);
        return users.stream().findFirst();
    }
}
