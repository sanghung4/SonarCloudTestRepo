package com.reece.platform.products.exceptions;

public class OrderAlreadyExistsException extends Exception {

    private static final String DEFAULT_MESSAGE = "Order associated with cart %s already exists.";

    public OrderAlreadyExistsException(String cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId));
    }
}
