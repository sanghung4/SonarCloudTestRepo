package com.reece.platform.accounts.exception;

public class UserRoleNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"User role with given id not found.\"}";

    public UserRoleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
