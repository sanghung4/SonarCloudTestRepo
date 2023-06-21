package com.reece.platform.eclipse.exceptions;

public class EclipseTokenException extends Exception {
    private static final String DEFAULT_MESSAGE = "Failed to retrieve Eclipse session token.";

    public EclipseTokenException() {
        super(DEFAULT_MESSAGE);
    }
}
