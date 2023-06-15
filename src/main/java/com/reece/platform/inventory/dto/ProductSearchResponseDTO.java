package com.reece.platform.inventory.dto;

import java.util.List;

import com.reece.platform.inventory.model.ProductSearch.ProductSearchMetadata;
import com.reece.platform.inventory.model.ProductSearch.ProductSearchResult;
import lombok.Data;

@Data
public class ProductSearchResponseDTO {
    private ProductSearchMetadata metadata;
    private List<ProductSearchResult> results;
}

