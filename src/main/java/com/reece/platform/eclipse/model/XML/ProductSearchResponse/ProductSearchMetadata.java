package com.reece.platform.eclipse.model.XML.ProductSearchResponse;

import lombok.Data;

@Data
public class ProductSearchMetadata {
    private int startIndex;
    private int pageSize;
    private int totalItems;
}
