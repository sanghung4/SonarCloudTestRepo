package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerSearchRequestDTO {
    private List<String> id;
    private String keyword;
    private Integer startIndex;
    private Integer pageSize;
    private Boolean includeTotalItems;
    private String Type;
    private String branchId;
    private String territoryId;
    private String outsideSalesRep;
    private String insideSalesRep;
    private String salesRep;
    private Date updatedAfter;

    public static CustomerSearchRequestDTO fromEntity(CustomerSearchInputDTO input) {
        CustomerSearchRequestDTO request = new CustomerSearchRequestDTO();
        request.setKeyword(input.getKeyword());
        request.setId(input.getId());
        request.setPageSize(input.getPageSize());
        if(input.getCurrentPage() != null && input.getPageSize() != null
            && input.getCurrentPage() > 0 && input.getPageSize() > 0 && input.getPageSize() <= 50) {
            request.setStartIndex((input.getCurrentPage()-1) * input.getPageSize() + 1);
        } else {
            request.setStartIndex(1);
        }
        return request;
    }
}
