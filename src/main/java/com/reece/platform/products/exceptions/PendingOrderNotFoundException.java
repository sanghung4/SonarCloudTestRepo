package com.reece.platform.products.exceptions;

import java.util.UUID;

public class PendingOrderNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Pending order with order id %s not found.\"}";
    private static final String CART_MESSAGE = "{\"error\":\"Pending order with cart id %s not found.\"}";

    public PendingOrderNotFoundException(String orderId) {
        super(String.format(DEFAULT_MESSAGE, orderId));
    }

    public PendingOrderNotFoundException(UUID cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId.toString()));
    }
}
