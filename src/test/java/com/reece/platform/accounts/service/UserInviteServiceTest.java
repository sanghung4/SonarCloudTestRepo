package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.reece.platform.accounts.exception.AccountNotFoundException;
import com.reece.platform.accounts.exception.BranchNotFoundException;
import com.reece.platform.accounts.exception.UserNotFoundException;
import com.reece.platform.accounts.model.DTO.BranchDTO;
import com.reece.platform.accounts.model.DTO.InviteAccountOwnerDTO;
import com.reece.platform.accounts.model.DTO.InviteUserDTO;
import com.reece.platform.accounts.model.DTO.InvitedUserResponseDTO;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.repository.AccountDAO;
import com.reece.platform.accounts.model.repository.InvitedUserDAO;
import java.util.Optional;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = { UserInviteService.class })
public class UserInviteServiceTest {

    @MockBean
    private InvitedUserDAO invitedUserDAO;

    @MockBean
    private AccountService accountService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private AccountDAO accountDAO;

    @Autowired
    private UserInviteService userInviteService;

    UUID validInviteId = UUID.fromString("d6e76ced-e059-4492-a45f-a4ab4f2de32d");

    @SneakyThrows
    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(userInviteService, "enableNotificationCalls", true);
    }

    @Test
    void getUserInvite_success() {
        InvitedUser invitedUser = new InvitedUser();
        invitedUser.setId(validInviteId);
        invitedUser.setErp(ErpEnum.ECLIPSE);
        when(invitedUserDAO.findById(validInviteId)).thenReturn(Optional.of(invitedUser));

        assertDoesNotThrow(() -> {
            InvitedUserResponseDTO response = userInviteService.getInvitedUser(validInviteId);
            assertEquals(response.getId(), validInviteId);
        });
    }

    @Test
    void getUserInvite_fail() {
        when(invitedUserDAO.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userInviteService.getInvitedUser(validInviteId));
    }

    @Test
    void inviteUser_success() throws Exception {
        String email = "email";
        InvitedUser invitedUser = new InvitedUser();
        BranchDTO branchDTO = new BranchDTO();
        invitedUser.setBillToAccountId(UUID.randomUUID());
        when(
            invitedUserDAO.save(
                argThat(invitedUser1 -> invitedUser1.getEmail().equals(email) && invitedUser1.isEmailSent())
            )
        )
            .thenReturn(invitedUser);
        when(accountService.getHomeBranch(invitedUser.getBillToAccountId())).thenReturn(branchDTO);

        InviteUserDTO inviteUserDTO = new InviteUserDTO();
        inviteUserDTO.setBillToAccountId(invitedUser.getBillToAccountId());
        inviteUserDTO.setEmail(email);
        InvitedUser invitedUser1 = userInviteService.inviteUser(inviteUserDTO);
        verify(notificationService, times(1)).sendInviteUserEmail(eq(branchDTO), eq(invitedUser));
        assertEquals(invitedUser1, invitedUser, "Expected return to equal mocked invite saved in DAO.");
    }

    @Test
    void isInviteEmailSent_success() throws Exception {
        InvitedUser invitedUser = new InvitedUser();
        invitedUser.setEmailSent(true);
        String email = "email";
        when(invitedUserDAO.findByEmail(email)).thenReturn(Optional.of(invitedUser));
        boolean isEmailSent = userInviteService.isInviteEmailSent(email);
        assertTrue(isEmailSent);
    }

    @Test
    void isInviteEmailSent_userNotFound() {
        String email = "email";
        when(invitedUserDAO.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userInviteService.isInviteEmailSent(email));
    }

    @Test
    void resendInviteEmail_fail() {
        String email = "email";
        when(invitedUserDAO.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userInviteService.resendInviteEmail(email));
    }

    @Test
    void resendInviteEmail_success() throws Exception {
        String email = "email";
        InvitedUser invitedUser = new InvitedUser();
        BranchDTO branchDTO = new BranchDTO();
        invitedUser.setBillToAccountId(UUID.randomUUID());
        when(invitedUserDAO.findByEmail(email)).thenReturn(Optional.of(invitedUser));
        when(accountService.getHomeBranch(invitedUser.getBillToAccountId())).thenReturn(branchDTO);
        userInviteService.resendInviteEmail(email);
        verify(notificationService, times(1)).sendInviteUserEmail(eq(branchDTO), eq(invitedUser));
    }

    @Test
    void inviteAccountOwner_success() throws BranchNotFoundException, AccountNotFoundException {
        String erpAccountID = UUID.randomUUID().toString();
        InviteAccountOwnerDTO inviteAccountOwnerDTO = new InviteAccountOwnerDTO();
        inviteAccountOwnerDTO.setErpAccountId(erpAccountID);
        BranchDTO branchDTO = new BranchDTO();
        InvitedUser invite = new InvitedUser();
        invite.setBillToAccountId(UUID.fromString(inviteAccountOwnerDTO.getErpAccountId()));
        when(invitedUserDAO.save(new InvitedUser(inviteAccountOwnerDTO))).thenReturn(invite);
        doNothing().when(notificationService).sendInviteUserEmail(eq(branchDTO), eq(invite));
        when(accountService.getHomeBranch(UUID.fromString(inviteAccountOwnerDTO.getErpAccountId())))
            .thenReturn(branchDTO);
        userInviteService.inviteAccountOwner(inviteAccountOwnerDTO);
    }
}
