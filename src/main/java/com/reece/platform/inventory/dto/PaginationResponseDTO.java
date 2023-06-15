package com.reece.platform.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginationResponseDTO {

    private Integer currentPage;
    private Integer pageSize;
    private Integer totalItemCount;

    public PaginationResponseDTO(Integer totalItemCount, Integer pageSize, Integer currentPage) {
        this.totalItemCount = totalItemCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }
}