package com.reece.platform.eclipse.exceptions;

public class PickNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "Pick with id %s not found.";

    public PickNotFoundException(String pickId) {
        super(String.format(DEFAULT_MESSAGE, pickId));
    }
}
