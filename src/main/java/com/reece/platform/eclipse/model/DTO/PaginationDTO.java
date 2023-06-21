package com.reece.platform.eclipse.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationDTO {
    private int currentPage;
    private int pageSize;
    private int totalItemCount;
}
