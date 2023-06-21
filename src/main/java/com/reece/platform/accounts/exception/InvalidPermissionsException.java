package com.reece.platform.accounts.exception;

public class InvalidPermissionsException  extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Invalid user permissions.\"}";

    public InvalidPermissionsException() {
        super(DEFAULT_MESSAGE);
    }
}
