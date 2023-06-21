package com.reece.platform.products.exceptions;

import java.util.ArrayList;
import java.util.List;

public class AddItemToListFoundException extends Exception {

    private List<String> errors = new ArrayList<String>();

    public AddItemToListFoundException(List<String> errors) {
        super(errors.stream().map(s -> String.format("\"%s\"", s)).toList().toString());
        this.errors = errors;
    }

    private static final String DEFAULT_MESSAGE = "Can not add more than 600 products in List.";

    public AddItemToListFoundException() {
        super(String.format(DEFAULT_MESSAGE));
    }
}
