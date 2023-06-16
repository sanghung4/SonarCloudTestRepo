package com.reece.platform.eclipse.exceptions;

public class InvalidWarehouseIdException extends Exception {

    private static final String DEFAULT_MESSAGE = "WarehouseId in the API URL has a special character, which does not get handled properly. Please contact customer support";

    public InvalidWarehouseIdException() {
        super(DEFAULT_MESSAGE);
    }
}
