package com.reece.platform.accounts.exception;

public class UserAlreadyExistsInviteUserException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to invite user.  User with email %s already exists.\"}";

    public UserAlreadyExistsInviteUserException(String email) {
        super(String.format(DEFAULT_MESSAGE, email));
    }
}
