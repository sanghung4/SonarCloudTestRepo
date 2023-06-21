package com.reece.platform.accounts.exception;

public class BranchNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"No branch found for that ID.\"}";
    public BranchNotFoundException() { super(DEFAULT_MESSAGE); }
}
