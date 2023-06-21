package com.reece.platform.notifications.controller;

import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.notifications.exception.SalesforceMarketingCloudException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler({ IOException.class })
    public ResponseEntity<String> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MessageRejectedException.class,
            MailFromDomainNotVerifiedException.class,
            ConfigurationSetDoesNotExistException.class,
            ConfigurationSetSendingPausedException.class,
            AccountSendingPausedException.class,
            SalesforceMarketingCloudException.class,
            JsonProcessingException.class
    })
    public ResponseEntity<String> handleEmailFailException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
