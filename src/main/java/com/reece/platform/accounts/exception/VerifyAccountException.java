package com.reece.platform.accounts.exception;

public class VerifyAccountException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "{\"error\":\"The account number or zip code was incorrect. Please Try again\"}";

    public VerifyAccountException() {
        super(DEFAULT_MESSAGE);
    }
}
