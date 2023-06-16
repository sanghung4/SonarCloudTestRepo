package com.reece.platform.eclipse.exceptions;

public class CloseOrderException extends Exception {

    private static final String MESSAGE_TEMPLATE = "Close Order Error: %s";

    public CloseOrderException(String message) {
        super(String.format(MESSAGE_TEMPLATE, message));
    }
}
