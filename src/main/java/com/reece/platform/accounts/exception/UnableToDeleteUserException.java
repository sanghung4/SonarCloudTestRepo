package com.reece.platform.accounts.exception;

public class UnableToDeleteUserException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to delete user. User is the owner of current billTo account.\"}";

    public UnableToDeleteUserException() { super(DEFAULT_MESSAGE); }
}
