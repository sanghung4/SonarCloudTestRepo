package com.reece.platform.products.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ErrorsFoundInFileException extends Exception {

    private List<String> errors = new ArrayList<String>();

    public ErrorsFoundInFileException(List<String> errors) {
        super(errors.stream().map(s -> String.format("\"%s\"", s)).toList().toString());
        this.errors = errors;
    }
}
