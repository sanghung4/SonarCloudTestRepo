package com.reece.platform.products.exceptions;

public class DeliveryAndWillCallNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "No delivery or will-call found for cartId %s.  Create a delivery or will-call first.";

    public DeliveryAndWillCallNotFoundException(String cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId));
    }
}
