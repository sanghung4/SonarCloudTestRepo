package com.reece.platform.eclipse.exceptions;

public class NoEclipseCredentialsException extends RuntimeException {
    public NoEclipseCredentialsException() {
        super("No Eclipse Credentials Found");
    }
}
