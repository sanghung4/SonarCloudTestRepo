package com.reece.platform.products.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String accountId, String orderNumber) {
        super("Order '" + orderNumber + "' not found for account '" + accountId + "'.");
    }
}
