package com.reece.platform.products.exceptions;

public class ItemNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Item with id %s associated with cart id %s not found.";

    public ItemNotFoundException(String itemId, String cartId) {
        super(String.format(DEFAULT_MESSAGE, itemId, cartId));
    }
}
