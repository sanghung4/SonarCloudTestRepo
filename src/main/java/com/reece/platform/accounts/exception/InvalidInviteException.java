package com.reece.platform.accounts.exception;

public class InvalidInviteException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Invalid invite ID.\"}";
    private static final String INVALID_EMAIL_MESSAGE = "{\"error\":\" Email %s does not match invite.\"}";

    public InvalidInviteException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidInviteException(String email) {
        super(String.format(INVALID_EMAIL_MESSAGE, email));
    }
}
