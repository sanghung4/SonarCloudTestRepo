package com.reece.platform.inventory.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import me.alidg.errors.annotation.ExposeAsArg;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.BAD_REQUEST, errorCode = "ECLIPSE_CONNECT_EXCEPTION")
public class EclipseConnectException extends RuntimeException {
    @ExposeAsArg(0)
    private final String eclipseMessage;

    public EclipseConnectException(String message) {
        super(message);
        eclipseMessage = message;
    }
}
