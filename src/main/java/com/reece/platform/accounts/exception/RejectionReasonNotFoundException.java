package com.reece.platform.accounts.exception;

public class RejectionReasonNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Reason not found.\"}";

    public RejectionReasonNotFoundException() { super(DEFAULT_MESSAGE); }

    public RejectionReasonNotFoundException(String str) { super(str); }
}


