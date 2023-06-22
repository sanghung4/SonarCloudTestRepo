package com.reece.platform.mincron.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public class MincronException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final Optional<String> code;

    public MincronException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    public MincronException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = Optional.ofNullable(code);
    }
}
