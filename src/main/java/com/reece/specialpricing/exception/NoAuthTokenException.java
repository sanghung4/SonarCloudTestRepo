package com.reece.specialpricing.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.UNAUTHORIZED, errorCode = "NO_AUTH_TOKEN")
public class NoAuthTokenException extends RuntimeException {
}
