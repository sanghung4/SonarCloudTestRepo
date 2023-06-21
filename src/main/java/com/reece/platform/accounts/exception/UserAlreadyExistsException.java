package com.reece.platform.accounts.exception;

public class UserAlreadyExistsException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"User with email %s already exists.\"}";

    public UserAlreadyExistsException(String email) {
        super(String.format(DEFAULT_MESSAGE, email));
    }
}
