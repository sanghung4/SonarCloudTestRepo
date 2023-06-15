package com.reece.platform.inventory.exception;

public class EclipseLoadCountsException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to complete Eclipse Load Counts\"}";

    public EclipseLoadCountsException() { super(DEFAULT_MESSAGE); }

    public EclipseLoadCountsException(Throwable cause) { super(DEFAULT_MESSAGE, cause); }
}
