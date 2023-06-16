package com.reece.platform.eclipse.exceptions;

public class KourierAddToCountException extends Exception {
    private static final String MESSAGE_TEMPLATE = "Error calling AddToCount API: %s";

    private final String errorCode;

    public KourierAddToCountException(String errorCode, String message) {
        super(String.format(MESSAGE_TEMPLATE, message));
        this.errorCode = errorCode;
    }
}
