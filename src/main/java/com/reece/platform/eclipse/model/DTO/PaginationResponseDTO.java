package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

@Data
public class PaginationResponseDTO {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalItemCount;
}