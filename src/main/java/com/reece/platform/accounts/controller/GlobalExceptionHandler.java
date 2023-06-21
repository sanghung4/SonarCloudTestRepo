package com.reece.platform.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.exception.*;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler({ UpdatePasswordException.class })
    public ResponseEntity<String> handleForbiddenException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    //Account controller
    @ExceptionHandler(
        {
            TermsNotAcceptedException.class,
            InvalidInviteException.class,
            ResourceException.class,
            RejectionReasonNotFoundException.class,
            InvalidPhoneNumberException.class,
            UnsupportedEncodingException.class,
            EclipseException.class,
        }
    )
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(
        { UserUnauthorizedException.class, InvalidPermissionsException.class, UserNotEmployeeException.class }
    )
    public ResponseEntity<String> handleUnauthorizedException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ JsonProcessingException.class, ConnectException.class })
    public ResponseEntity<String> handleJsonProcessingException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(
        {
            UserAlreadyApprovedException.class,
            UserAlreadyExistsException.class,
            UserAlreadyExistsInviteUserException.class,
            UserInviteAlreadyExistsException.class,
            UnableToDeleteUserException.class,
            UpdateUserEmailAlreadyExistsException.class,
        }
    )
    public ResponseEntity<String> handleConflictException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(
        {
            BranchNotFoundException.class,
            NoSuchElementException.class,
            UserNotFoundException.class,
            UserRoleNotFoundException.class,
            AccountNotFoundException.class,
            PhoneTypeNotFoundException.class,
            VerificationTokenNotValidException.class,
            VerifyAccountException.class,
            SalesForceException.class,
        }
    )
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardInUseException.class)
    public ResponseEntity<String> handleCardInUseException(CardInUseException cardInUseException) {
        return new ResponseEntity<>(cardInUseException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ FeatureNotFoundException.class })
    public ResponseEntity<String> handleResourceNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
