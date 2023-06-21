package com.reece.platform.eclipse.exceptions;

public class ProductNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Product with id %d not found.\"}";

    public ProductNotFoundException(int productId) {
        super(String.format(DEFAULT_MESSAGE, productId));
    }
}