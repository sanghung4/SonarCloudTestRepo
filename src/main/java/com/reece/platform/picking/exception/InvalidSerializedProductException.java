package com.reece.platform.picking.exception;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.BAD_REQUEST, errorCode = "INVALID_SERIALIZED_PRODUCT")
public class InvalidSerializedProductException extends RuntimeException {

}
