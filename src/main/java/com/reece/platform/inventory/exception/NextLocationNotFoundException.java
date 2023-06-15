package com.reece.platform.inventory.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.NOT_FOUND, errorCode = "NEXT_LOCATION_NOT_FOUND")
public class NextLocationNotFoundException extends RuntimeException {
}
