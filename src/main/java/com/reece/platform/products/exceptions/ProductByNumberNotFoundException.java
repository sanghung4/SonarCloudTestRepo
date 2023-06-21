package com.reece.platform.products.exceptions;

public class ProductByNumberNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Product with product number %s not found.\"}";

    public ProductByNumberNotFoundException(String prodNum) {
        super(String.format(DEFAULT_MESSAGE, prodNum));
    }
}
