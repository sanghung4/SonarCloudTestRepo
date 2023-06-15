package com.reece.platform.inventory.exception;

import org.springframework.http.HttpStatus;
import me.alidg.errors.annotation.ExceptionMapping;

@ExceptionMapping(statusCode = HttpStatus.INTERNAL_SERVER_ERROR, errorCode = "ADD_TO_COUNT_ECLIPSE_EXCEPTION")
public class EclipseAddToCountException extends RuntimeException {
    
    private final String eclipseMessage;
    
    public EclipseAddToCountException(String message) {
        super(message);
        eclipseMessage = message;
    }

}
