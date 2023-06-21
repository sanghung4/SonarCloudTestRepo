package com.reece.platform.products.exceptions;

public class UserUnauthorizedException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "{\"error\":\"Current user is unauthorized to perform this action.\"}";

    public UserUnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UserUnauthorizedException(String str) {
        super(str);
    }
}
