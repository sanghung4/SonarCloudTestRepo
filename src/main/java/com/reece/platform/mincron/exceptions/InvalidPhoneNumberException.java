package com.reece.platform.mincron.exceptions;

public class InvalidPhoneNumberException extends Exception {
    private static final String DEFAULT_MESSAGE = "Invalid phone number format. Must be of format xxx-yyy-zzzz";

    public InvalidPhoneNumberException() { super(DEFAULT_MESSAGE); }
}
