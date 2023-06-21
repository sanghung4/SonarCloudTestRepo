package com.reece.platform.accounts.exception;

public class TermsNotAcceptedException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to create user.  User has not accepted terms of service and/or privacy policy.\"}";

    public TermsNotAcceptedException() { super(DEFAULT_MESSAGE); }
}
