package com.reece.platform.accounts.exception;

public class FeatureNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Feature with id %s not found.\"}";

    public FeatureNotFoundException(String id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
