package com.reece.platform.products.exceptions;

public class AccountNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Account with id %s not found.";

    public AccountNotFoundException(String accountId) {
        super(String.format(DEFAULT_MESSAGE, accountId));
    }
}
