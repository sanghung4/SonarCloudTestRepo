package com.reece.platform.products.exceptions;

public class EmptyProductIndexJobException extends Exception {

    private static final String DEFAULT_MESSAGE = "No products found during product indexing job run.";

    public EmptyProductIndexJobException() {
        super(DEFAULT_MESSAGE);
    }
}
