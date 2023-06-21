package com.reece.platform.products.branches.exception;

public class GeocodingException extends Exception {

    private static final String DEFAULT_MESSAGE = "{\"error\":\"Error reaching geocoding service.\"}";

    public GeocodingException() {
        super(DEFAULT_MESSAGE);
    }
}
