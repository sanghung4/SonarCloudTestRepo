package com.reece.platform.accounts.exception;

public class UserNotEmployeeException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "{\"error\":\"Unable to create user. User is not a valid employee.\"}";

    public UserNotEmployeeException() {
        super(DEFAULT_MESSAGE);
    }
}
