package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.ProductSearchResponse.ProductSearchMetadata;
import com.reece.platform.eclipse.model.XML.ProductSearchResponse.ProductSearchResult;
import lombok.Data;

import java.util.List;

@Data
public class ProductSearchResponseDTO {
    private ProductSearchMetadata metadata;
    private List<ProductSearchResult> results;
}

