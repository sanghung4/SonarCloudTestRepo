package com.reece.platform.accounts.exception;

public class AccountNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Account with given ID not found.\"}";

    public AccountNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
