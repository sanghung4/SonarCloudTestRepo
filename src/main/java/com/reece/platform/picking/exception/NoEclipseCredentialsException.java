package com.reece.platform.picking.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.UNAUTHORIZED, errorCode = "NO_ECLIPSE_CREDENTIALS_FOUND")
public class NoEclipseCredentialsException extends RuntimeException {
}
