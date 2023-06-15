package com.reece.platform.inventory.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.NOT_FOUND, errorCode = "LOCATION_NOT_FOUND")
public class LocationNotFoundException extends RuntimeException {
}
