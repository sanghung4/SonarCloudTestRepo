package com.reece.platform.products.exceptions;

public class ExportListIntoCSVFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "CSV writing error";

    public ExportListIntoCSVFoundException(String accountId) {
        super(String.format(DEFAULT_MESSAGE, accountId));
    }
}
