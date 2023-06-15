package com.reece.platform.inventory.exception;

public class InvalidProductSearchException extends Exception {

    private static final String DEFAULT_MESSAGE = "keywords is missing or invalid. Please try again";

    public InvalidProductSearchException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidProductSearchException(String customMessage) {
        super(customMessage);
    }
}
