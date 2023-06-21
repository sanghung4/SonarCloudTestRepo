package com.reece.platform.products.exceptions;

public class InvalidShipViaDuringCheckoutException extends Exception {

    private static final String DEFAULT_MESSAGE = "Your current branch is not set up for %s orders.";

    public InvalidShipViaDuringCheckoutException(String deliveryMethod, String suggestedDeliveryMethod) {
        super(String.format(DEFAULT_MESSAGE, deliveryMethod, suggestedDeliveryMethod));
    }
}
