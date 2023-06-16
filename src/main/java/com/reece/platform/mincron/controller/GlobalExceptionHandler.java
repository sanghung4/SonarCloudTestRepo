package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.dto.ErrorResponseDTO;
import com.reece.platform.mincron.exception.MincronException;
import com.reece.platform.mincron.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(MincronException.class)
    public ResponseEntity<ErrorResponseDTO> handleMincronException(MincronException exception) {
        var response = new ErrorResponseDTO(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException exception) {
        var response = new ErrorResponseDTO(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
