package com.reece.platform.products.exceptions;

public class DeliveryNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Delivery order for cartId %s not found.  Create a delivery first.";

    public DeliveryNotFoundException(String cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId));
    }
}
