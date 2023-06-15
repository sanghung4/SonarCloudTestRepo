package com.reece.platform.inventory.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.BAD_REQUEST, errorCode = "INVALID_DATE_FORMAT")
public class InvalidDateException extends RuntimeException {
}
