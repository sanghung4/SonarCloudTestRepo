package com.reece.platform.products.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    private static final String USER_ID_NOT_FOUND_MESSAGE = "User with id %s not found.";
    private static final String USER_EMAIL_NOT_FOUND_MESSAGE = "User with email %s not found.";

    public UserNotFoundException(UUID id) {
        super(String.format(USER_ID_NOT_FOUND_MESSAGE, id.toString()));
    }

    public UserNotFoundException(String email) {
        super(String.format(USER_EMAIL_NOT_FOUND_MESSAGE, email));
    }
}
