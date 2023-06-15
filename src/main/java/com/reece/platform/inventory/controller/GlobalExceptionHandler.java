package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.ErrorDTO;
import com.reece.platform.inventory.exception.*;
import com.reece.platform.inventory.exception.InvalidProductSearchException;
import com.reece.platform.inventory.exception.VarianceNotFoundDTO;
import com.reece.platform.inventory.exception.VarianceNotFoundException;
import com.reece.platform.inventory.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(VarianceNotFoundException.class)
    public ResponseEntity<VarianceNotFoundDTO> handleVarianceNotFoundException(VarianceNotFoundException exception) {
        return new ResponseEntity<>(
            new VarianceNotFoundDTO(false, "ERPCP-1007", exception.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidProductSearchException.class)
    public ResponseEntity<ErrorDTO> handleInvalidProductSearchException(InvalidProductSearchException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("INVALID_PRODUCT_SEARCH", exception.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InValidCountDeletionException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCountDeletionException(InValidCountDeletionException exception) {
        return new ResponseEntity<>(
            new ErrorDTO(exception.getErrorCode(), exception.getErrorMessage()),
            exception.getHttpStatus()
        );
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorDTO> handleInvalidDateException() {
        return new ResponseEntity<>(
            new ErrorDTO("INVALID_DATE_FORMAT", String.format("Date pattern must be : %S", DateUtil.datePattern)),
            HttpStatus.BAD_REQUEST
        );
    }
}
