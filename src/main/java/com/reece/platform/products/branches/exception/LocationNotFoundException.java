package com.reece.platform.products.branches.exception;

public class LocationNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Unable to locate address.\"}";

    public LocationNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
