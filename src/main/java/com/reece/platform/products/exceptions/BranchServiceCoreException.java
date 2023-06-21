package com.reece.platform.products.exceptions;

import org.springframework.http.*;

public class BranchServiceCoreException extends Exception {

    private final HttpStatus httpStatus;

    public BranchServiceCoreException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
