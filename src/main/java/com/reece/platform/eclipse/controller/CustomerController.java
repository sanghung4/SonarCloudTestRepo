package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.Customer;
import com.reece.platform.eclipse.dto.ErrorDTO;
import com.reece.platform.eclipse.exceptions.CustomerNotFoundException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.service.EclipseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final EclipseService eclipseService;

    /**
     * Get customer by id
     * @param customerId
     * @return
     * @throws EclipseTokenException
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{customerId}")
    public @ResponseBody Customer getCustomerById(@PathVariable String customerId)
        throws EclipseTokenException, CustomerNotFoundException {
        return eclipseService.getCustomerById(customerId);
    }

    @ExceptionHandler(EclipseTokenException.class)
    public ResponseEntity<ErrorDTO> handleEclipseTokenException(EclipseTokenException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("ECLIPSE_TOKEN_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        return new ResponseEntity<>(new ErrorDTO("CUSTOMER_NOT_FOUND", exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
