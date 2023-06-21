package com.reece.platform.eclipse.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.model.DTO.ErrorDTO;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(EclipseException.class)
    public ResponseEntity<String> handleEclipseException(EclipseException eclipseException) {
        return new ResponseEntity<>(eclipseException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductImageUrlNotFoundException.class)
    public ResponseEntity<String> handleProductImageUrlNotFoundException(ProductImageUrlNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({JSchException.class, SftpException.class, IOException.class})
    public ResponseEntity<String> handleFileTransferException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidIdException.class, InterruptedException.class, ExecutionException.class, TimeoutException.class, ParseException.class, JAXBException.class, XMLStreamException.class})
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditCardNotFoundException.class)
    public ResponseEntity<String> handleCreditCardNotFoundException(CreditCardNotFoundException creditCardNotFoundException) {
        return new ResponseEntity<>(creditCardNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardInUseException.class)
    public ResponseEntity<String> handleCardInUseException(CardInUseException cardInUseException) {
        return new ResponseEntity<>(cardInUseException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEclipseCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidEclipseCredentialsException(InvalidEclipseCredentialsException exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_ECLIPSE_CREDENTIALS", exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoEclipseCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleNoEclipseCredentialsException(NoEclipseCredentialsException exception) {
        return new ResponseEntity<>(new ErrorDTO("NO_ECLIPSE_CREDENTIALS", exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EclipseConnectException.class)
    public ResponseEntity<ErrorDTO> handleEclipseConnectException(EclipseConnectException exception) {
        return new ResponseEntity<>(new ErrorDTO("ECLIPSE_CONNECT_EXCEPTION", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NextLocationNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNextLocationNotFoundException(NextLocationNotFoundException exception) {
        return new ResponseEntity<>(new ErrorDTO("NEXT_LOCATION_NOT_FOUND", exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EclipseLoadCountsException.class)
    public ResponseEntity<ErrorDTO> handEclipseLoadCountsException(EclipseLoadCountsException exception) {
        return new ResponseEntity<>(new ErrorDTO("ECLIPSE_LOAD_COUNT_EXCEPTION", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<ErrorDTO> handleInvalidSerializedProductException(InvalidSerializedProductException exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_SERIALIZED_PRODUCT", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EclipseTokenException.class)
    public ResponseEntity<ErrorDTO> handleEclipseTokenException(EclipseTokenException exception) {
        return new ResponseEntity<>(new ErrorDTO("ECLIPSE_TOKEN_EXCEPTION", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
   @ExceptionHandler(ToteUnavailableException.class)
    public ResponseEntity<ErrorDTO> handleToteUnavailableException(ToteUnavailableException exception) {
        return new ResponseEntity<>(new ErrorDTO("TOTE_UNAVAILABLE", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ToteLockedException.class)
    public ResponseEntity<ErrorDTO> handleToteLockedException(ToteLockedException exception) {
        return new ResponseEntity<>(new ErrorDTO("TOTE_LOCKED", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInvoiceException.class)
    public ResponseEntity<ErrorDTO> handleInvalidInvoiceException(InvalidInvoiceException exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_INVOICE", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SplitQuantityException.class)
    public ResponseEntity<ErrorDTO> handleSplitQuantityException(SplitQuantityException exception) {
        return new ResponseEntity<>(new ErrorDTO("SPLIT_QUANTITY_EXCEPTION", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CloseOrderException.class)
    public ResponseEntity<ErrorDTO> handleCloseOrderException(CloseOrderException exception) {
        return new ResponseEntity<>(new ErrorDTO("CLOSE_ORDER_EXCEPTION", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KourierException.class)
    public ResponseEntity<ErrorDTO> handleKourierException(KourierException exception) {
        return new ResponseEntity<>(new ErrorDTO("KOURIER_EXCEPTION", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
