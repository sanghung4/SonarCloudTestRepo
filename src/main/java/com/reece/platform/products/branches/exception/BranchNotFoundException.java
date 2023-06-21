package com.reece.platform.products.branches.exception;

public class BranchNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Branch with given ID not found.\"}";

    public BranchNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BranchNotFoundException(String str) {
        super(str);
    }
}
