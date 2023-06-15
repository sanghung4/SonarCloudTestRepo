package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductSearchRequestDTO {
    private String searchTerm;
    private Integer searchInputType;
    private Integer currentPage;
    private Integer pageSize;
    private String erpSystem;
    //Expect 0-3
    private Integer categoryLevel;
    private List<String> resultFields;
}