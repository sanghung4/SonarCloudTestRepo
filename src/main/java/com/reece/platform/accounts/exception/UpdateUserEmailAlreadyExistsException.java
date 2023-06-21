package com.reece.platform.accounts.exception;

public class UpdateUserEmailAlreadyExistsException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to update user email. User with email %s already exists.\"}";

    public UpdateUserEmailAlreadyExistsException(String email) {
        super(String.format(DEFAULT_MESSAGE, email));
    }
}
