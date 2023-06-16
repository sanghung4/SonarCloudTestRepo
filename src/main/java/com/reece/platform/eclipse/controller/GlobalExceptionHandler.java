package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.ErrorDTO;
import com.reece.platform.eclipse.exceptions.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(EclipseException.class)
    public ResponseEntity<String> handleEclipseException(EclipseException eclipseException) {
        return new ResponseEntity<>(eclipseException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductImageUrlNotFoundException(ProductNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductImageUrlNotFoundException.class)
    public ResponseEntity<String> handleProductImageUrlNotFoundException(ProductImageUrlNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBranchException.class)
    public ResponseEntity<String> handleInvalidBranchException(InvalidBranchException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<String> handleInvalidProductException(InvalidProductException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(
        {
            InterruptedException.class,
            ExecutionException.class,
            TimeoutException.class,
            ParseException.class,
            JAXBException.class,
            XMLStreamException.class,
        }
    )
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEclipseCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidEclipseCredentialsException(
        InvalidEclipseCredentialsException exception
    ) {
        return new ResponseEntity<>(
            new ErrorDTO("INVALID_ECLIPSE_CREDENTIALS", exception.getMessage()),
            HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(NoEclipseCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleNoEclipseCredentialsException(NoEclipseCredentialsException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("NO_ECLIPSE_CREDENTIALS", exception.getMessage()),
            HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(EclipseConnectException.class)
    public ResponseEntity<ErrorDTO> handleEclipseConnectException(EclipseConnectException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("ECLIPSE_CONNECT_EXCEPTION", exception.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NextLocationNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNextLocationNotFoundException(NextLocationNotFoundException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("NEXT_LOCATION_NOT_FOUND", exception.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EclipseLoadCountsException.class)
    public ResponseEntity<ErrorDTO> handEclipseLoadCountsException(EclipseLoadCountsException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("ECLIPSE_LOAD_COUNT_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    //Picking controller
    @ExceptionHandler(InvalidToteException.class)
    public ResponseEntity<ErrorDTO> handleInvalidToteException(InvalidToteException exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_TOTE", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PickNotFoundException.class)
    public ResponseEntity<ErrorDTO> handlePickNotFoundException(PickNotFoundException exception) {
        return new ResponseEntity<>(new ErrorDTO("PICK_NOT_FOUND", exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSerializedProductException.class)
    public ResponseEntity<ErrorDTO> handleInvalidSerializedProductException(
        InvalidSerializedProductException exception
    ) {
        return new ResponseEntity<>(
            new ErrorDTO("INVALID_SERIALIZED_PRODUCT", exception.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EclipseTokenException.class)
    public ResponseEntity<ErrorDTO> handleEclipseTokenException(EclipseTokenException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("ECLIPSE_TOKEN_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ToteUnavailableException.class)
    public ResponseEntity<ErrorDTO> handleToteUnavailableException(ToteUnavailableException exception) {
        return new ResponseEntity<>(new ErrorDTO("TOTE_UNAVAILABLE", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ToteLockedException.class)
    public ResponseEntity<ErrorDTO> handleToteLockedException(ToteLockedException exception) {
        return new ResponseEntity<>(new ErrorDTO("TOTE_LOCKED", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SplitQuantityException.class)
    public ResponseEntity<ErrorDTO> handleSplitQuantityException(SplitQuantityException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("SPLIT_QUANTITY_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(CloseOrderException.class)
    public ResponseEntity<ErrorDTO> handleCloseOrderException(CloseOrderException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("CLOSE_ORDER_EXCEPTION", exception.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(KourierException.class)
    public ResponseEntity<ErrorDTO> handleKourierException(KourierException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("KOURIER_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(KourierInventoryException.class)
    public ResponseEntity<ErrorDTO> handleKourierException(KourierInventoryException exception) {
        return new ResponseEntity<>(new ErrorDTO(exception.getCode(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidProductSearchException.class)
    public ResponseEntity<ErrorDTO> handleInvalidProductSearchException(
            InvalidProductSearchException exception
    ) {
        return new ResponseEntity<>(
                new ErrorDTO("INVALID_PRODUCT_SEARCH", exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
