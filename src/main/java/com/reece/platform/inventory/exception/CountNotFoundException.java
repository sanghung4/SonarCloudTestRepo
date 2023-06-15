package com.reece.platform.inventory.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.NOT_FOUND, errorCode = "COUNT_NOT_FOUND")
public class CountNotFoundException extends RuntimeException {
}
