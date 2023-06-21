package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.okta.sdk.client.Client;
import com.okta.sdk.ds.DataStore;
import com.okta.sdk.impl.resource.DefaultUserBuilder;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.application.AppUser;
import com.okta.sdk.resource.application.AppUserCredentials;
import com.okta.sdk.resource.application.Application;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.*;
import com.reece.platform.accounts.exception.BranchNotFoundException;
import com.reece.platform.accounts.exception.UpdatePasswordException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.Role;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import com.reece.platform.accounts.model.repository.AccountRequestDAO;
import com.reece.platform.accounts.model.repository.RoleDAO;
import com.reece.platform.accounts.model.repository.UserDAO;
import com.reece.platform.accounts.model.seed.PermissionType;
import com.reece.platform.accounts.utilities.DecodedToken;
import com.reece.platform.accounts.utils.Constants;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = { AuthenticationService.class })
class AuthenticationServiceTest {

    @MockBean
    private Client client;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private AccountRequestDAO accountRequestDAO;

    @MockBean
    private RoleDAO roleDAO;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private DecodedToken token;

    @MockBean
    private UserBuilder userBuilder;

    public static final String AUTH_ID = "auth id";
    public static final String APP_USER_ID = "app User Id";
    public static final String PHONE_NUMBER = "phone number";
    public static final String OKTA_HOME_PHONE_ATTRIBUTE = "homePhone";
    public static final String OKTA_PRIMARY_PHONE_ATTRIBUTE = "primaryPhone";
    public static final String OKTA_MOBILE_PHONE_ATTRIBUTE = "mobilePhone";
    public static final String PERMISSIONS_ATTRIBUTE_NAME = "ecommPermissions";
    public static final String USERID_ATTRIBUTE_NAME = "ecommUserId";
    public static final String IS_EMPLOYEE_ATTRIBUTE_NAME = "isEmployee";
    public static final String IS_VERIFIED_ATTRIBUTE_NAME = "isVerified";
    private static final String FORTILINE = "Fortiline";

    @Test
    void updateUserLoginEmailAndProfile() {
        String firstName = "first name";
        String lastName = "last name";
        String email = "email";
        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setEmail(email);
        dbUser.setLastName(lastName);
        dbUser.setFirstName(firstName);
        dbUser.setPhoneNumber(PHONE_NUMBER);
        PhoneTypeEnum homePhoneType = PhoneTypeEnum.HOME;
        dbUser.setPhoneType(homePhoneType);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        AppUserCredentials appUserCredentials = mock(AppUserCredentials.class);
        when(appUser.getCredentials()).thenReturn(appUserCredentials);
        when(appUser.getId()).thenReturn(APP_USER_ID);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        User oktaUser = mock(User.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(oktaUser.getProfile()).thenReturn(oktaUserProfile);
        when(client.getUser(APP_USER_ID)).thenReturn(oktaUser);
        when(client.getApplication(any())).thenReturn(application);

        authenticationService.updateUserLoginEmailAndProfile(dbUser);

        verify(oktaUserProfile, times(1)).setFirstName(firstName);
        verify(oktaUserProfile, times(1)).setLastName(lastName);
        verify(oktaUserProfile, times(1)).setEmail(email);
        verify(oktaUserProfile, times(1)).put(OKTA_HOME_PHONE_ATTRIBUTE, PHONE_NUMBER);
        verify(oktaUserProfile, times(1)).setLogin(email);
        verify(appUserCredentials, times(1)).setUserName(email);
        verify(oktaUser, times(1)).update();
    }

    @Test
    void updateNewUserPermissions() {
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(Set.of());

        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setRole(role);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(client.getApplication(any())).thenReturn(application);
        when(appUser.getProfile()).thenReturn(oktaUserProfile);

        authenticationService.updateNewUserPermissions(dbUser);
        verify(appUser, times(1)).update();
        verify(oktaUserProfile, times(1)).put(USERID_ATTRIBUTE_NAME, null);
        verify(oktaUserProfile, times(1)).put(IS_EMPLOYEE_ATTRIBUTE_NAME, true);
        verify(oktaUserProfile, times(1)).put(IS_VERIFIED_ATTRIBUTE_NAME, true);
    }

    @Test
    void setPhoneOktaUserProfile_home() {
        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setPhoneNumber(PHONE_NUMBER);
        PhoneTypeEnum homePhoneType = PhoneTypeEnum.HOME;
        dbUser.setPhoneType(homePhoneType);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        AppUserCredentials appUserCredentials = mock(AppUserCredentials.class);
        when(appUser.getCredentials()).thenReturn(appUserCredentials);
        when(appUser.getId()).thenReturn(APP_USER_ID);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        User oktaUser = mock(User.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(oktaUser.getProfile()).thenReturn(oktaUserProfile);
        when(client.getUser(APP_USER_ID)).thenReturn(oktaUser);
        when(client.getApplication(any())).thenReturn(application);

        authenticationService.updateUserLoginEmailAndProfile(dbUser);

        verify(oktaUserProfile, times(1)).put(OKTA_HOME_PHONE_ATTRIBUTE, PHONE_NUMBER);
        verify(oktaUserProfile, times(1)).put(OKTA_PRIMARY_PHONE_ATTRIBUTE, null);
        verify(oktaUserProfile, times(1)).put(OKTA_MOBILE_PHONE_ATTRIBUTE, null);
    }

    @Test
    void setPhoneOktaUserProfile_mobile() {
        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setPhoneNumber(PHONE_NUMBER);
        PhoneTypeEnum mobilePhoneType = PhoneTypeEnum.MOBILE;
        dbUser.setPhoneType(mobilePhoneType);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        AppUserCredentials appUserCredentials = mock(AppUserCredentials.class);
        when(appUser.getCredentials()).thenReturn(appUserCredentials);
        when(appUser.getId()).thenReturn(APP_USER_ID);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        User oktaUser = mock(User.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(oktaUser.getProfile()).thenReturn(oktaUserProfile);
        when(client.getUser(APP_USER_ID)).thenReturn(oktaUser);
        when(client.getApplication(any())).thenReturn(application);

        authenticationService.updateUserLoginEmailAndProfile(dbUser);

        verify(oktaUserProfile, times(1)).put(OKTA_HOME_PHONE_ATTRIBUTE, null);
        verify(oktaUserProfile, times(1)).put(OKTA_PRIMARY_PHONE_ATTRIBUTE, null);
        verify(oktaUserProfile, times(1)).put(OKTA_MOBILE_PHONE_ATTRIBUTE, PHONE_NUMBER);
    }

    @Test
    void setPhoneOktaUserProfile_office() {
        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setPhoneNumber(PHONE_NUMBER);
        PhoneTypeEnum officePhoneType = PhoneTypeEnum.OFFICE;
        dbUser.setPhoneType(officePhoneType);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        AppUserCredentials appUserCredentials = mock(AppUserCredentials.class);
        when(appUser.getCredentials()).thenReturn(appUserCredentials);
        when(appUser.getId()).thenReturn(APP_USER_ID);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        User oktaUser = mock(User.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(oktaUser.getProfile()).thenReturn(oktaUserProfile);
        when(client.getUser(APP_USER_ID)).thenReturn(oktaUser);
        when(client.getApplication(any())).thenReturn(application);

        authenticationService.updateUserLoginEmailAndProfile(dbUser);

        verify(oktaUserProfile, times(1)).put(OKTA_HOME_PHONE_ATTRIBUTE, null);
        verify(oktaUserProfile, times(1)).put(OKTA_PRIMARY_PHONE_ATTRIBUTE, PHONE_NUMBER);
        verify(oktaUserProfile, times(1)).put(OKTA_MOBILE_PHONE_ATTRIBUTE, null);
    }

    @Test
    void listGroups_successEmptySearch() {
        authenticationService.listGroups(null);
        verify(client, times(1)).listGroups();
    }

    @Test
    void listGroups_successSearchTerm() {
        authenticationService.listGroups(FORTILINE);
        verify(client, times(1)).listGroups(FORTILINE, null, null);
    }

    @Test
    public void userCanToggleFeatures_Success() {
        String tokenContent = "Bearer " + Constants.authToken("");
        DecodedToken token1 = DecodedToken.getDecodedHeader(tokenContent);
        when(token.containsPermission(PermissionType.toggle_features)).thenReturn(false);
        var response = authenticationService.userCanToggleFeatures(token1);
        assertEquals(response, false);
    }

    @Test
    void userCanRefreshContact_Success() {
        String tokenContent = "Bearer " + Constants.authToken("");
        when(token.containsPermission(PermissionType.refresh_contact)).thenReturn(true);
        var response = authenticationService.userCanRefreshContact(tokenContent);
        assertEquals(response, false);
    }

    @Test
    public void createUser_Failure() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("first name");
        createUserDTO.setEmail("Email");
        createUserDTO.setLastName("last name");
        createUserDTO.setPhoneNumber("123");
        createUserDTO.setPassword("@$@%$@$");
        createUserDTO.setIsWaterworksSubdomain(true);
        User user = mock(User.class);
        UserProfile userProfile = mock(UserProfile.class);
        user.setProfile(userProfile);
        GroupList groupList = mock(GroupList.class);
        DefaultUserBuilder defaultUserBuilder = mock(DefaultUserBuilder.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(user.getProfile()).thenReturn(oktaUserProfile);
        when(client.listGroups("Fortiline", null, null)).thenReturn(groupList);
        when(userBuilder.buildAndCreate(client)).thenReturn(user);
        assertThrows(Exception.class, () -> authenticationService.createUser(createUserDTO, null));
    }

    @Test
    public void createUser_Failure_NotFortiline() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("first name");
        createUserDTO.setEmail("Email");
        createUserDTO.setLastName("last name");
        createUserDTO.setPhoneNumber("123");
        createUserDTO.setPassword("@$@%$@$");
        createUserDTO.setIsWaterworksSubdomain(false);
        BranchDTO branch = new BranchDTO();
        branch.setName("Murray Supply");
        User user = mock(User.class);
        UserProfile userProfile = mock(UserProfile.class);
        user.setProfile(userProfile);
        GroupList groupList = mock(GroupList.class);
        DefaultUserBuilder defaultUserBuilder = mock(DefaultUserBuilder.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(user.getProfile()).thenReturn(oktaUserProfile);
        when(client.listGroups()).thenReturn(groupList);
        when(userBuilder.buildAndCreate(client)).thenReturn(user);
        assertThrows(Exception.class, () -> authenticationService.createUser(createUserDTO, branch));
    }

    @Test
    public void createNewUser_Failure() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName("first name");
        userRegistrationDTO.setEmail("Email");
        userRegistrationDTO.setLastName("last name");
        userRegistrationDTO.setPhoneNumber("123");
        userRegistrationDTO.setPassword("@$@%$@$");
        userRegistrationDTO.setBrand("MINCRON");
        User user = mock(User.class);
        UserProfile userProfile = mock(UserProfile.class);
        user.setProfile(userProfile);
        GroupList groupList = mock(GroupList.class);
        DefaultUserBuilder defaultUserBuilder = mock(DefaultUserBuilder.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(user.getProfile()).thenReturn(oktaUserProfile);
        when(client.listGroups("Fortiline", null, null)).thenReturn(groupList);
        when(userBuilder.buildAndCreate(client)).thenReturn(user);
        assertThrows(Exception.class, () -> authenticationService.createNewUser(userRegistrationDTO, null));
    }

    @Test
    public void createNewUser_Failure_NotFortiline() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName("first name");
        userRegistrationDTO.setEmail("Email");
        userRegistrationDTO.setLastName("last name");
        userRegistrationDTO.setPhoneNumber("123");
        userRegistrationDTO.setPassword("@$@%$@$");
        userRegistrationDTO.setBrand("MINCRON");
        User user = mock(User.class);
        UserProfile userProfile = mock(UserProfile.class);
        user.setProfile(userProfile);
        GroupList groupList = mock(GroupList.class);
        BranchDTO branch = new BranchDTO();
        branch.setName("Murray Supply");
        DefaultUserBuilder defaultUserBuilder = mock(DefaultUserBuilder.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(user.getProfile()).thenReturn(oktaUserProfile);
        when(client.listGroups()).thenReturn(groupList);
        when(userBuilder.buildAndCreate(client)).thenReturn(user);
        assertThrows(Exception.class, () -> authenticationService.createNewUser(userRegistrationDTO, branch));
    }

    @Test
    public void createEmployeeUser_Failure() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setFirstName("first name");
        createEmployeeDTO.setEmail("Email");
        createEmployeeDTO.setLastName("last name");
        createEmployeeDTO.setPhoneNumber("123");
        createEmployeeDTO.setPassword("@$@%$@$");
        User user = mock(User.class);
        UserProfile userProfile = mock(UserProfile.class);
        user.setProfile(userProfile);
        GroupList groupList = mock(GroupList.class);
        DefaultUserBuilder defaultUserBuilder = mock(DefaultUserBuilder.class);
        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(user.getProfile()).thenReturn(oktaUserProfile);
        when(client.listGroups("Fortiline", null, null)).thenReturn(groupList);
        when(userBuilder.buildAndCreate(client)).thenReturn(user);
        assertThrows(Exception.class, () -> authenticationService.createEmployeeUser(createEmployeeDTO));
    }

    @Test
    void updateUserPermissions() {
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(Set.of());

        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setRole(role);

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(client.getApplication(any())).thenReturn(application);
        when(appUser.getProfile()).thenReturn(oktaUserProfile);

        authenticationService.updateUserPermissions(dbUser);
        verify(appUser, times(1)).update();
        verify(oktaUserProfile, times(1)).put(USERID_ATTRIBUTE_NAME, null);
    }

    @Test
    void updateUserPassword() throws Exception {
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(Set.of());

        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setRole(role);

        UpdateUserPasswordDTO updateUserPasswordDTO = new UpdateUserPasswordDTO();
        updateUserPasswordDTO.setOldUserPassword("123");
        updateUserPasswordDTO.setNewUserPassword("321");

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        ChangePasswordRequest changePasswordRequest = mock(ChangePasswordRequest.class);
        UserCredentials userCredentials = mock(UserCredentials.class);
        PasswordCredential passwordCredential = mock(PasswordCredential.class);
        when(application.getApplicationUser(AUTH_ID)).thenReturn(appUser);

        UserProfile oktaUserProfile = mock(UserProfile.class);
        when(client.getApplication(any())).thenReturn(application);
        when(client.getUser(appUser.getId())).thenReturn(user);
        when(client.instantiate(ChangePasswordRequest.class)).thenReturn(changePasswordRequest);
        when(client.instantiate(PasswordCredential.class)).thenReturn(passwordCredential);
        // when(client.instantiate(PasswordCredential.class)).thenReturn(passwordCredential);
        when(client.instantiate(PasswordCredential.class)).thenReturn(passwordCredential);
        when(
            client
                .instantiate(ChangePasswordRequest.class)
                .setOldPassword(
                    client
                        .instantiate(PasswordCredential.class)
                        .setValue(updateUserPasswordDTO.getOldUserPassword().toCharArray())
                )
        )
            .thenReturn(changePasswordRequest);

        when(
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
            )
        )
            .thenReturn(userCredentials);

        when(appUser.getProfile()).thenReturn(oktaUserProfile);
        authenticationService.updateUserPassword(dbUser, updateUserPasswordDTO);
    }

    @Test
    void deleteUser() throws Exception {
        Role role = new Role();
        role.setName(RoleEnum.MORSCO_ADMIN.label);
        role.setPermissions(Set.of());

        Application application = mock(Application.class);
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        com.reece.platform.accounts.model.entity.User dbUser = new com.reece.platform.accounts.model.entity.User();
        dbUser.setAuthId(AUTH_ID);
        dbUser.setRole(role);
        when(client.getApplication(any())).thenReturn(application);
        when(client.getUser(appUser.getId())).thenReturn(user);

        authenticationService.deleteUser(dbUser);
    }
}
