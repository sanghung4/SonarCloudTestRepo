package com.reece.platform.products.exceptions;

import org.springframework.http.HttpStatus;

public class EclipseException extends RuntimeException {

    private final HttpStatus httpStatus;

    public EclipseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
