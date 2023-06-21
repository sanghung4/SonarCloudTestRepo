package com.reece.platform.eclipse.exceptions;

public class ToteUnavailableException extends Exception{
    private static final String DEFAULT_MESSAGE = "Tote %s not available for staging.";

    public ToteUnavailableException(String pickId) {
        super(String.format(DEFAULT_MESSAGE, pickId));
    }
}
