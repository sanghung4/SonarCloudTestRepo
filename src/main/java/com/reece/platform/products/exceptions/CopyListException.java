package com.reece.platform.products.exceptions;

public class CopyListException extends Exception {

    private static final String DEFAULT_MESSAGE = "Lists could not be copied from the account %S to the account %S";

    public CopyListException(String fromBillTo, String toBillTo) {
        super(String.format(DEFAULT_MESSAGE, fromBillTo, toBillTo));
    }
}
