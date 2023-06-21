package com.reece.platform.products.exceptions;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class ItemAlreadyInListException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "{\"error\":\"Unable to add item to list. Item with erp part number %s is already in the list with ID %s.\"}";

    @Getter
    @Setter
    private UUID itemId;

    public ItemAlreadyInListException(String erpPartNumber, UUID listId, UUID itemId) {
        super(String.format(DEFAULT_MESSAGE, erpPartNumber, listId));
        this.itemId = itemId;
    }
}
