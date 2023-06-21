package com.reece.platform.eclipse.model.XML.ProductSearchResponse;

import com.reece.platform.eclipse.model.generated.ProductWebReference;
import lombok.Data;

import java.util.List;

@Data
public class ProductSearchResult {
    private String upc;
    private int id; // part number for now
    private String catalogNumber;
    private String description;
    private List<ProductWebReference> webReferences;
}
