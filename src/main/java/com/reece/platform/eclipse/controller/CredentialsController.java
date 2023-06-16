package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.inventory.EclipseCredentials;
import com.reece.platform.eclipse.exceptions.InvalidEclipseCredentialsException;
import com.reece.platform.eclipse.service.EclipseSessionService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    private final EclipseSessionService eclipseSessionService;

    @PostMapping("_valid")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validate(@RequestBody EclipseCredentials eclipseCredentials) {
        val validCredentials = eclipseSessionService.verifyCredentials(eclipseCredentials);

        if (validCredentials.isEmpty()) {
            throw new InvalidEclipseCredentialsException();
        }
    }
}