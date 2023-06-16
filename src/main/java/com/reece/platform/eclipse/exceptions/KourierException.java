package com.reece.platform.eclipse.exceptions;

public class KourierException extends Exception {

    private static final String MESSAGE_TEMPLATE = "Error calling Kourier API: %s";

    public KourierException(String message) {
        super(String.format(MESSAGE_TEMPLATE, message));
    }
}
