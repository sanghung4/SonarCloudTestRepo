package com.reece.platform.products.exceptions;

public class WillCallNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Will-call order for cartId %s not found.  Create a will-call first.";

    public WillCallNotFoundException(String cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId));
    }
}
