package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class ProductInventoryBranchDTO {

    private String branchId;
    private String branchName;
    private Integer availableOnHand;
}
