package com.reece.platform.products.model.DTO;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginationResponseDTO implements Serializable {

    private Integer currentPage;
    private Integer pageSize;
    private Integer totalItemCount;

    public PaginationResponseDTO(Integer totalItemCount, Integer pageSize, Integer currentPage) {
        this.totalItemCount = totalItemCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
