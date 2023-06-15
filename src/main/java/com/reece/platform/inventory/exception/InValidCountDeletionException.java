package com.reece.platform.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InValidCountDeletionException extends RuntimeException {

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;

    public InValidCountDeletionException(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
