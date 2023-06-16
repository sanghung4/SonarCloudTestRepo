package com.reece.platform.eclipse.exceptions;

public class SplitQuantityException extends Exception {

    private static final String MESSAGE_TEMPLATE = "Split Quantity Error: %s";

    public SplitQuantityException(String message) {
        super(String.format(MESSAGE_TEMPLATE, message));
    }
}
