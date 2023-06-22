package com.reece.platform.mincron.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.as400.data.PcmlException;
import com.reece.platform.mincron.exceptions.AccountNotFoundException;
import com.reece.platform.mincron.exceptions.InvalidPhoneNumberException;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException exception) {
        return new ResponseEntity<>("Error parsing Mincron response: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<String> handleInvalidPhoneNumberException(InvalidPhoneNumberException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PcmlException.class)
    public ResponseEntity<String> handlePcmlException(PcmlException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MincronException.class)
    public ResponseEntity<ErrorDTO> handleMincronException(MincronException exception) {
        return new ResponseEntity<>(new ErrorDTO(exception.getCode().orElse("HTTP_" + exception.getHttpStatus()), exception.getMessage()), exception.getHttpStatus());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorDTO> handleParseException(ParseException exception) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST.toString(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
