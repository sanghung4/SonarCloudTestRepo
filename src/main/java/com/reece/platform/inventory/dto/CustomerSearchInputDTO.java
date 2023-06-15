package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerSearchInputDTO {
    private List<String> id;
    private String keyword;
    private Integer pageSize;
    private Integer currentPage;
}
