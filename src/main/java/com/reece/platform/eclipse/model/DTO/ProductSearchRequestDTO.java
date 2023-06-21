package com.reece.platform.eclipse.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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