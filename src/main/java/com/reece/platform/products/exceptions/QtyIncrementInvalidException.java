package com.reece.platform.products.exceptions;

public class QtyIncrementInvalidException extends Exception {

    private static final String DEFAULT_MESSAGE = "Product must be added or updated in increments of %d.";

    public QtyIncrementInvalidException(int minQtyIncrement) {
        super(String.format(DEFAULT_MESSAGE, minQtyIncrement));
    }
}
