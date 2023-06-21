package com.reece.platform.products.exceptions;

import com.reece.platform.products.model.entity.LineItems;
import java.util.ArrayList;
import java.util.List;

public class AddItemsToCartFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "Can not add more than 600 products in Cart.";

    public AddItemsToCartFoundException() {
        super(String.format(DEFAULT_MESSAGE));
    }
}
