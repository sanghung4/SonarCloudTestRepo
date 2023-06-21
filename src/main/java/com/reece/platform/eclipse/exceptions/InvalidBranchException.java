package com.reece.platform.eclipse.exceptions;

public class InvalidBranchException extends Exception {
    private static final String DEFAULT_MESSAGE = "Branch is missing or invalid. Please try again";

    public InvalidBranchException() {
        super(DEFAULT_MESSAGE);
    }
}