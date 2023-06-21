package com.reece.platform.accounts.exception;

public class UserInviteAlreadyExistsException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to invite user.  User with email %s has already been invited.\"}";

    public UserInviteAlreadyExistsException(String email) {
        super(String.format(DEFAULT_MESSAGE, email));
    }
}
