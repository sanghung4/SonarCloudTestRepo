package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.exceptions.CustomerNotFoundException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.DTO.CustomerSearchRequestDTO;
import com.reece.platform.eclipse.model.DTO.CustomerSearchResponseDTO;
import com.reece.platform.eclipse.model.DTO.ErrorDTO;
import com.reece.platform.eclipse.model.generated.Customer;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private EclipseService eclipseService;

    @Autowired
    public CustomersController(EclipseService eclipseService) {
        this.eclipseService = eclipseService;
    }

    /**
     * Search customer by keyword/name/id
     * @param request Request object to be parsed into query params
     * @return
     * @throws EclipseTokenException
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/_search")
    public @ResponseBody CustomerSearchResponseDTO getCustomerSearch(@RequestBody CustomerSearchRequestDTO request) throws EclipseTokenException {
        return eclipseService.getCustomerSearch(request);
    }

    /**
     * Get customer by id
     * @param customerId
     * @return
     * @throws EclipseTokenException
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{customerId}")
    public @ResponseBody Customer getCustomerById(@PathVariable String customerId) throws EclipseTokenException, CustomerNotFoundException {
        return eclipseService.getCustomerById(customerId);
    }
    
    @ExceptionHandler(EclipseTokenException.class)
    public ResponseEntity<ErrorDTO> handleEclipseTokenException(EclipseTokenException exception) {
        return new ResponseEntity<>(new ErrorDTO("ECLIPSE_TOKEN_EXCEPTION", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        return new ResponseEntity<>(new ErrorDTO("CUSTOMER_NOT_FOUND", exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
