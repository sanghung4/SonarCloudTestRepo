package com.reece.platform.inventory.model.ProductSearch;

import lombok.Data;

@Data
public class ProductSearchResult {
    private String upc;
    private int id; // part number for now
    private String catalogNumber;
    private String description;
    private ProductWebReference[] webReferences;
}
