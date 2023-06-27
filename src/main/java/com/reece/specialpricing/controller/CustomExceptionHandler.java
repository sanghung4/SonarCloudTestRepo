package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.pojo.ErrorDetails;
import com.reece.specialpricing.model.pojo.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MethodArgumentNotValidException exception){
        var responseDetails = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetails(error.getField(), error.getDefaultMessage(), "InvalidParameter"))
                .collect(Collectors.toList());

        return new ErrorResponse(responseDetails.size(), responseDetails);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(BindException exception){
        var responseDetails = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetails(error.getField(), error.getDefaultMessage(), "InvalidParameter"))
                .collect(Collectors.toList());

        return new ErrorResponse(responseDetails.size(), responseDetails);
    }
}
