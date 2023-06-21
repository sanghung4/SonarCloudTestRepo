package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.exceptions.EclipseConnectException;
import com.reece.platform.eclipse.exceptions.InvalidEclipseCredentialsException;
import com.reece.platform.eclipse.exceptions.NoEclipseCredentialsException;
import com.reece.platform.eclipse.external.ec.EclipseConnectService;
import com.reece.platform.eclipse.external.ec.EclipseCredentials;
import com.reece.platform.eclipse.external.ec.EclipseCredentialsStore;
import com.reece.platform.eclipse.model.DTO.ErrorDTO;
import com.reece.platform.eclipse.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/credentials")
public class CredentialsController {
    private final EclipseCredentialsStore eclipseCredentialsStore;
    private final EclipseConnectService eclipseConnectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void setCredentials(@RequestHeader("Authorization") String authHeader, @RequestBody EclipseCredentials eclipseCredentials) {
        val userId = TokenUtils.extractUserId(authHeader);
        eclipseCredentialsStore.putCredentials(userId, eclipseCredentials);
    }

    @GetMapping("_valid")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validate(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);
        val validCredentials = eclipseConnectService.validateCredentials(credentials);

        if (!validCredentials) {
            throw new InvalidEclipseCredentialsException();
        }
    }

}
