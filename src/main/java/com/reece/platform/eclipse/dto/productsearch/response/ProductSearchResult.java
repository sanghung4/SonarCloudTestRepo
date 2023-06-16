package com.reece.platform.eclipse.dto.productsearch.response;

import java.util.List;
import lombok.Data;

@Data
public class ProductSearchResult {

    private String upc;
    private int id; // part number for now
    private String catalogNumber;
    private String description;
    private List<ProductWebReference> webReferences;
}
