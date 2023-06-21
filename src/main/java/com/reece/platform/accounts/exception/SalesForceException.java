package com.reece.platform.accounts.exception;

import java.util.Optional;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SalesForceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Optional<String> code;

    public SalesForceException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    public SalesForceException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = Optional.ofNullable(code);
    }
}
