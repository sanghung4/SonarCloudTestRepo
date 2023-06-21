package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.AccountNotFoundException;
import com.reece.platform.accounts.exception.BranchNotFoundException;
import com.reece.platform.accounts.exception.UserNotFoundException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.InvitedUserResponseDTO;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.repository.InvitedUserDAO;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserInviteService {

    @Value("${enable_notification_calls:true}")
    private Boolean enableNotificationCalls;

    @Autowired
    private InvitedUserDAO invitedUserDAO;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public InvitedUser inviteUser(InviteUserDTO inviteUserDTO)
        throws BranchNotFoundException, AccountNotFoundException {
        InvitedUser invitedUser = new InvitedUser(inviteUserDTO);
        invitedUser.setEmailSent(true);
        InvitedUser invite = invitedUserDAO.save(invitedUser);

        if (enableNotificationCalls) {
            BranchDTO branch = accountService.getHomeBranch(inviteUserDTO.getBillToAccountId());
            notificationService.sendInviteUserEmail(branch, invite);
        }

        return invite;
    }

    @Transactional
    public InvitedUser inviteAccountOwner(InviteAccountOwnerDTO inviteAccountOwnerDTO)
        throws BranchNotFoundException, AccountNotFoundException {
        InvitedUser invite = invitedUserDAO.save(new InvitedUser(inviteAccountOwnerDTO));

        if (enableNotificationCalls) {
            BranchDTO branch = accountService.getHomeBranch(UUID.fromString(inviteAccountOwnerDTO.getErpAccountId()));
            notificationService.sendInviteUserEmail(branch, invite);
        }

        return invite;
    }

    /**
     * Get invited user record from DB
     * @param inviteId
     * @return the InvitedUserResponseDTO object
     * @throws UserNotFoundException thrown if invite does not exist
     */
    public InvitedUserResponseDTO getInvitedUser(UUID inviteId) throws UserNotFoundException {
        var invitedUser = invitedUserDAO.findById(inviteId).orElseThrow(UserNotFoundException::new);
        return new InvitedUserResponseDTO(invitedUser);
    }

    /**
     * Check if given email has an invite and if the invitee has been sent an email
     *
     * @param email email to check for invite and if email has been sent
     * @return boolean indicating if email has been sent for given invite
     * @throws UserNotFoundException thrown if invite doesn't exist
     */
    public boolean isInviteEmailSent(String email) throws UserNotFoundException {
        var invitedUser = invitedUserDAO.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (invitedUser.isCompleted()) {
            throw new UserNotFoundException();
        }

        return invitedUser.isEmailSent();
    }

    /**
     * Resends the invite email to the given email for user in DB
     *
     * @param email email of user to resend invite for
     * @throws UserNotFoundException thrown when user doesn't exist
     * @throws BranchNotFoundException thrown when home branch of bill to not found
     * @throws AccountNotFoundException thrown when bill-to account not found
     */
    @Transactional
    public void resendInviteEmail(String email)
        throws UserNotFoundException, BranchNotFoundException, AccountNotFoundException {
        var invitedUser = invitedUserDAO.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (enableNotificationCalls) {
            BranchDTO branch = accountService.getHomeBranch(invitedUser.getBillToAccountId());
            notificationService.sendInviteUserEmail(branch, invitedUser);
            invitedUser.setEmailSent(true);
            invitedUserDAO.save(invitedUser);
        }
    }
}
