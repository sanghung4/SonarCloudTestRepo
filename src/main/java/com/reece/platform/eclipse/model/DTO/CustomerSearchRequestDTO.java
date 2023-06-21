package com.reece.platform.eclipse.model.DTO;

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

    public static CustomerSearchRequestDTO validate(CustomerSearchRequestDTO input) {
        if(input.getIncludeTotalItems() == null) {
            input.setIncludeTotalItems(true);
        }
        if(input.getPageSize() == null) {
            input.setPageSize(25);
        } else if(input.getPageSize() < 1 || input.getPageSize() > 50) {
            input.setPageSize(25);
        }
        if(input.getStartIndex() == null) {
            input.setStartIndex(1);
        }
        return input;
    }
}
