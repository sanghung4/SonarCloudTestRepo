package com.reece.platform.products.branches.exception;

public class BranchesNotGivenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"No branch Ids given.\"}";

    public BranchesNotGivenException() {
        super(DEFAULT_MESSAGE);
    }

    public BranchesNotGivenException(String str) {
        super(str);
    }
}
