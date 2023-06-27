package com.reece.platform.picking.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.UNAUTHORIZED, errorCode = "INVALID_ECLIPSE_CREDENTIALS")
public class InvalidEclipseCredentialsException extends RuntimeException {
}
