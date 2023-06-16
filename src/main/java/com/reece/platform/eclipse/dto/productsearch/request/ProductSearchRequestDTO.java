package com.reece.platform.eclipse.dto.productsearch.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDTO {

    private String searchTerm;
    private Integer searchInputType;
    private Integer currentPage;
    private Integer pageSize;
    private String erpSystem;
    // Expect 0 - 3
    private Integer categoryLevel;
    private List<String> resultFields;
}
