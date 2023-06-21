package com.reece.platform.eclipse.model.XML.ProductSearchResponse;

import com.reece.platform.eclipse.model.generated.ProductWebReference;
import lombok.Data;

import java.util.List;

@Data
public class ProductBySearchResult {
    private String productId;
    private String displayField;
}
