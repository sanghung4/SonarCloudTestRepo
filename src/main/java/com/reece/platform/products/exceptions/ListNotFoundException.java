package com.reece.platform.products.exceptions;

public class ListNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "List with id %s not found.";

    public ListNotFoundException(String listId) {
        super(String.format(DEFAULT_MESSAGE, listId));
    }
}
