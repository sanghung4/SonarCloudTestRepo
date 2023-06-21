package com.reece.platform.products.branches.exception;

public class WorkdayException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Error reaching Workday service.\"}";

    public WorkdayException(String message) {
        super(message == null || message.isEmpty() ? DEFAULT_MESSAGE: message);
    }
}
