package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.exception.UserNotFoundException;
import com.reece.platform.accounts.model.DTO.InvitedUserResponseDTO;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.service.UserInviteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes={UserInviteController.class})
@AutoConfigureMockMvc
public class UserInviteControllerTest {
    @MockBean
    private UserInviteService userInviteService;

    @Autowired
    private UserInviteController userInviteController;

    UUID validInviteId = UUID.fromString("d6e76ced-e059-4492-a45f-a4ab4f2de32d");
    UUID invalidInviteId = UUID.randomUUID();

    @Test
    void getUserInvite_success() throws UserNotFoundException {
        InvitedUser existingInvite = new InvitedUser();
        when(userInviteService.getInvitedUser(validInviteId)).thenReturn(new InvitedUserResponseDTO(existingInvite));
        ResponseEntity<InvitedUserResponseDTO> response = userInviteController.getInvitedUser(validInviteId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        InvitedUserResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(body, new InvitedUserResponseDTO(existingInvite));
    }

    @Test
    void getUserInvite_fail() throws UserNotFoundException {
        when(userInviteService.getInvitedUser(invalidInviteId)).thenReturn(new InvitedUserResponseDTO());
        ResponseEntity<InvitedUserResponseDTO> response = userInviteController.getInvitedUser(invalidInviteId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        InvitedUserResponseDTO body = response.getBody();
        assertEquals(body, new InvitedUserResponseDTO());
    }

    @Test
    void getInviteEmailSent_success() throws Exception {
        String email = "test";
        when(userInviteService.isInviteEmailSent(email)).thenReturn(true);
        ResponseEntity<Boolean> emailSentResponse = userInviteController.getInviteEmailSent(email);
        assertEquals(emailSentResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(emailSentResponse.getBody(), true);
    }

    @Test
    void getInviteEmailSent_userNotFound() throws Exception {
        String email = "test";
        when(userInviteService.isInviteEmailSent(email)).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> userInviteController.getInviteEmailSent(email));
    }

    @Test
    void resendInviteEmail_success() throws Exception {
        String email = "test";
        ResponseEntity<String> emailSentResponse = userInviteController.resendInviteEmail(email);
        assertEquals(emailSentResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(emailSentResponse.getBody(), "Invite email resent.");
        verify(userInviteService, times(1)).resendInviteEmail(email);
    }
}
