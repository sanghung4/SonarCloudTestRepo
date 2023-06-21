package com.reece.platform.accounts.exception;

public class VerificationTokenNotValidException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unable to verify token or not a valid token.";

    public VerificationTokenNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    public VerificationTokenNotValidException(String str) {
        super(str);
    }
}