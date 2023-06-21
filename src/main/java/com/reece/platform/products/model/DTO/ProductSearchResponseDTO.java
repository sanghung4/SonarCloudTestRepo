package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ProductSearchResponseDTO {

    private PaginationResponseDTO pagination;
    private AggregationResponseDTO aggregates;
    private List<ProductDTO> products;
    private List<ProductSearchFilterDTO> selectedAttributes;
    private List<ProductSearchFilterDTO> selectedCategories;
    // Expect 0 - 3
    private Integer categoryLevel;
}
