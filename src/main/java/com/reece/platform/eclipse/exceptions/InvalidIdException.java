package com.reece.platform.eclipse.exceptions;

public class InvalidIdException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Invalid Id Specified.\"}";
    private static final String INVALID_ID_MESSAGE = "{\"error\":\"Invalid Id Specified: %s\"}";

    public InvalidIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidIdException(String userId) {
        super(String.format(INVALID_ID_MESSAGE, userId));
    }
}
