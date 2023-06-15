package com.reece.platform.inventory.model.ProductSearch;

import lombok.Data;

@Data
public class ProductSearchMetadata {
    private int startIndex;
    private int pageSize;
    private int totalItems;
}