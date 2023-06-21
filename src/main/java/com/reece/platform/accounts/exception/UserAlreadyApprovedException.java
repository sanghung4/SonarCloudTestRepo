package com.reece.platform.accounts.exception;

public class UserAlreadyApprovedException extends Exception {
    private static final String DEFAULT_MESSAGE = "User approval has already been processed.";

    public UserAlreadyApprovedException() {
        super(DEFAULT_MESSAGE);
    }
}
