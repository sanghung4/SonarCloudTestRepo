package com.reece.platform.products.exceptions;

public class CartNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Cart with id %s not found.";
    private static final String ACCOUNT_USER_ID_MESSAGE =
        "Active cart associated with account id %s and user id %s not found.";

    public CartNotFoundException(String cartId) {
        super(String.format(DEFAULT_MESSAGE, cartId));
    }

    public CartNotFoundException(String userId, String accountId) {
        super(String.format(ACCOUNT_USER_ID_MESSAGE, accountId, userId));
    }
}
