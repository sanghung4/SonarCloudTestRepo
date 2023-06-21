package com.reece.platform.accounts.exception;

public class PhoneTypeNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Phone type not found.\"}";
    public PhoneTypeNotFoundException() { super(DEFAULT_MESSAGE); }
}
