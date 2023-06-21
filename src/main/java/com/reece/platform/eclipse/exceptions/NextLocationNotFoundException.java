package com.reece.platform.eclipse.exceptions;

public class NextLocationNotFoundException extends RuntimeException {
    public NextLocationNotFoundException() {
        super("No more locations");
    }
}
