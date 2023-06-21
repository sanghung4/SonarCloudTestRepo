package com.reece.platform.accounts.controller;

import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.service.AccountService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobsController {

    private final AccountService accountService;

    /**
     * Syncs db with eclipse account for all accounts
     * @return true if success
     * @throws ResourceException when  does not exist
     */
    @GetMapping("/refreshAccountAll")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Void> refreshAllAccounts() {
        accountService.refreshAccounts();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
