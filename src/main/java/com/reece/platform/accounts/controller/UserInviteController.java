package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.InvitedUserResponseDTO;
import com.reece.platform.accounts.service.UserInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller()
@RequestMapping("/invite")
public class UserInviteController {
    private final UserInviteService userInviteService;

    @Autowired
    public UserInviteController(
            UserInviteService userInviteService
    ) {
        this.userInviteService = userInviteService;
    }

    @GetMapping("{inviteId}")
    public @ResponseBody ResponseEntity<InvitedUserResponseDTO> getInvitedUser(@PathVariable UUID inviteId)
            throws UserNotFoundException {
        InvitedUserResponseDTO response = userInviteService.getInvitedUser(inviteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Checks if given email has an existing invite and if the email has been sent for that invite
     *
     * @param email email to check invite for
     * @return boolean indicating if email has been sent
     * @throws UserNotFoundException when invite for the given email doesn't exist
     */
    @GetMapping("/email-sent")
    public @ResponseBody ResponseEntity<Boolean> getInviteEmailSent(@RequestParam String email)
            throws UserNotFoundException {
        return new ResponseEntity<>(userInviteService.isInviteEmailSent(email), HttpStatus.OK);
    }

    /**
     * Resends the invite email for the user with the given ID
     * @param email email of invited user
     * @return success response
     */
    @GetMapping("/resend-legacy-invite")
    public @ResponseBody ResponseEntity<String> resendInviteEmail(@RequestParam String email)
            throws AccountNotFoundException, UserNotFoundException, BranchNotFoundException {
        userInviteService.resendInviteEmail(email);
        return new ResponseEntity<>("Invite email resent.", HttpStatus.OK);
    }


}
