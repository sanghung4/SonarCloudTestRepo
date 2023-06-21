package com.reece.platform.accounts.exception;

public class InvalidPhoneNumberException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Invalid phone number format.\"}";

    public InvalidPhoneNumberException() { super(DEFAULT_MESSAGE); }
}
