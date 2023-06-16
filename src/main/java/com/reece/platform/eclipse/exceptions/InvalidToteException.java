package com.reece.platform.eclipse.exceptions;

public class InvalidToteException extends Exception {

    private static final String DEFAULT_MESSAGE = "Tote is missing or invalid. Please try again";

    public InvalidToteException() {
        super(DEFAULT_MESSAGE);
    }
}
