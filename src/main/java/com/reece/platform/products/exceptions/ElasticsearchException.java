package com.reece.platform.products.exceptions;

import org.springframework.http.HttpStatus;

public class ElasticsearchException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ElasticsearchException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
