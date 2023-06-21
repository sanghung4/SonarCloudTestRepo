package com.reece.platform.accounts.exception;

import org.springframework.http.HttpStatus;

public class EclipseException extends Exception {

    private final HttpStatus httpStatus;

    public EclipseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
