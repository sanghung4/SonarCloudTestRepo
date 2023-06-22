package com.reece.platform.mincron.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends Exception {
    private final HttpStatus httpStatus;
    public AccountNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}