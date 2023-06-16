package com.reece.platform.eclipse.exceptions;

public class InvalidProductException extends Exception {

    private static final String DEFAULT_MESSAGE = "ProductId is missing or invalid. Please try again";

    public InvalidProductException() {
        super(DEFAULT_MESSAGE);
    }
}
