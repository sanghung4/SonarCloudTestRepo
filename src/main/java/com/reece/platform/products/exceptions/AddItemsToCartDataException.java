package com.reece.platform.products.exceptions;

public class AddItemsToCartDataException extends Exception{
    private static final String DEFAULT_MESSAGE = "Missing data required to add product for productId: %s";

    public AddItemsToCartDataException(String productId) {
        super(String.format(DEFAULT_MESSAGE, productId));
    }
}
