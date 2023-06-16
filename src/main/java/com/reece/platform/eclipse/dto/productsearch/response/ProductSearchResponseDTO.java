package com.reece.platform.eclipse.dto.productsearch.response;

import java.util.List;
import lombok.Data;

@Data
public class ProductSearchResponseDTO {

    private ProductSearchMetadata metadata;
    private List<ProductSearchResult> results;
}
