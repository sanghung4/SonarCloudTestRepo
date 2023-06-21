package com.reece.platform.eclipse.exceptions;

public class EclipseException extends Exception {

    private final String error;

    public EclipseException() {
        this("Eclipse encountered an issue", "Default error");
    }

    public EclipseException(String message, String error) {
        super(message);
        this.error = error;
    }

    public EclipseException(String message) {
        super(message);
        this.error = message;
    }

    public String getHttpStatus() {
        return error;
    }
}