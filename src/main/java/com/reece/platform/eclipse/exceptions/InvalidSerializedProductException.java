package com.reece.platform.eclipse.exceptions;

public class InvalidSerializedProductException extends Exception {
    private static final String DEFAULT_MESSAGE = "This is not a serialized product";

    public InvalidSerializedProductException() {
        super(DEFAULT_MESSAGE);
    }
}
