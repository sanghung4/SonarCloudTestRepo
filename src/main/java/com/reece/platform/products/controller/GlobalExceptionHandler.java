package com.reece.platform.products.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.*;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(
        {
            CartNotFoundException.class,
            ItemNotFoundException.class,
            BranchNotFoundPricingAndAvailabilityException.class,
            WillCallNotFoundException.class,
            DeliveryNotFoundException.class,
            DeliveryAndWillCallNotFoundException.class,
            UserNotFoundException.class,
            AccountNotFoundException.class,
            PendingOrderNotFoundException.class,
            ListNotFoundException.class,
        }
    )
    public ResponseEntity<String> handleResourceNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ CartAlreadyExistsException.class, InvalidShipViaDuringCheckoutException.class })
    public ResponseEntity<String> handleCartAlreadyExistsException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ QtyIncrementInvalidException.class })
    public ResponseEntity<String> handleQtyIncrementInvalidException(QtyIncrementInvalidException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UserUnauthorizedException.class, UnsupportedEncodingException.class })
    public ResponseEntity<String> handleUnauthorizedException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleEclipseException(HttpClientErrorException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ JsonProcessingException.class })
    public ResponseEntity<String> handleServerErrors(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EclipseException.class)
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MincronException.class)
    public ResponseEntity<String> handleMinBadRequestException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElasticsearchException.class)
    public ResponseEntity<String> handleElasticsearchException(ElasticsearchException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> handleDateParseException(ParseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<String> handleContractNotFoundException(ContractNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyProductIndexJobException.class)
    public ResponseEntity<String> handleEmptyProductIndexJobException(EmptyProductIndexJobException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductByNumberNotFoundException.class)
    public ResponseEntity<String> handleProductByNumberNotFoundException(ProductByNumberNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> handleMissingHeaderException(MissingRequestHeaderException e) {
        if (e.getHeaderName().equalsIgnoreCase("authorization")) {
            val problem = Problem
                .builder()
                .withType(Problem.DEFAULT_TYPE)
                .withTitle(Status.UNAUTHORIZED.getReasonPhrase())
                .withStatus(Status.UNAUTHORIZED)
                .build();

            return new ResponseEntity<>(problem, UNAUTHORIZED);
        }

        val violations = List.of(new Violation("headers[\"" + e.getHeaderName() + "\"]", "must not be blank"));
        val type = ConstraintViolationProblem.TYPE;
        val status = Status.BAD_REQUEST;

        val problem = new ConstraintViolationProblem(type, status, violations);

        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleEclipseException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ErrorsFoundInFileException.class)
    public ResponseEntity<String> handleErrorsFoundInFileException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(InvoiceDateRangeException.class)
    public ResponseEntity<String> handleInvoiceDateRangeException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(AddItemToListFoundException.class)
    public ResponseEntity<String> handleAddToListFoundInFileException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<String> handleOrderAlreadyExistsException(OrderAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AddItemsToCartFoundException.class)
    public ResponseEntity<String> handleAddItemsToCartFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(AddItemsToCartDataException.class)
    public ResponseEntity<String> handleAddItemsToCartDataException(AddItemsToCartDataException exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }
}
