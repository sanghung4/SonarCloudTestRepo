package com.reece.platform.eclipse.dto.productsearch.response;

import lombok.Data;

@Data
public class ProductSearchMetadata {

    private int startIndex;
    private int pageSize;
    private int totalItems;
}
