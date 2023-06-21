package com.reece.platform.products.branches.model.DTO;

import lombok.Data;

@Data
public class UpdateBranchDTO {

    private Boolean isActive;
    private Boolean isAvailableInStoreFinder;
    private Boolean isShoppable;
    private Boolean isPricingOnly;
}
