package com.reece.platform.products.exceptions;

import org.springframework.http.HttpStatus;

public class NotificationCoreServiceException extends Exception {

    private final HttpStatus httpStatus;

    public NotificationCoreServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
