package com.reece.platform.products.exceptions;

public class CartAlreadyExistsException extends Exception {

    private static final String DEFAULT_MESSAGE = "Cart associated with account %s and user %s already exists.";

    public CartAlreadyExistsException(String accountId, String userId) {
        super(String.format(DEFAULT_MESSAGE, accountId, userId));
    }
}
