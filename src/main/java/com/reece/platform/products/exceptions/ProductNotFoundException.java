package com.reece.platform.products.exceptions;

public class ProductNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Product with id %s not found.\"}";

    public ProductNotFoundException(String productId) {
        super(String.format(DEFAULT_MESSAGE, productId));
    }

    public ProductNotFoundException() {
        super(String.format("{\"error\":\"Products not found.\"}"));
    }
}
