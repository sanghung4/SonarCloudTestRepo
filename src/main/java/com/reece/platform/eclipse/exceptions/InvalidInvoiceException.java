package com.reece.platform.eclipse.exceptions;

public class InvalidInvoiceException extends Exception {
    private static final String DEFAULT_MESSAGE = "Invoice number provided is not correct.";

    public InvalidInvoiceException() {
        super(String.format(DEFAULT_MESSAGE));
    }
}
