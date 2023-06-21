package com.reece.platform.eclipse.exceptions;

public class CustomerNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "Customer with id %s not found.";

    public CustomerNotFoundException(String customerId) {
        super(String.format(DEFAULT_MESSAGE, customerId));
    }
}
