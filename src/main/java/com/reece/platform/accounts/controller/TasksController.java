package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TasksController {

    private final AccountService accountService;

    @Autowired
    public TasksController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Temporary endpoint that refreshes all accounts in DB with proper name and new address column values
     * Endpoint should be removed once it has been deployed and executed in each environment (dev, UAT, and prod)
     * @return list of accounts refreshed
     */
    @PutMapping("/refresh-accounts")
    public ResponseEntity<String> refreshAccounts() {
        accountService.refreshAccounts();
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }
}
