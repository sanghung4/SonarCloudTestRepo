package com.reece.platform.eclipse.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Product with id %s not found.";

    public ProductNotFoundException(String productId) {
        super(String.format(DEFAULT_MESSAGE, productId));
    }
}
