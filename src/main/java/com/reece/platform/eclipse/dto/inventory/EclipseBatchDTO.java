package com.reece.platform.eclipse.dto.inventory;

import lombok.Value;

@Value
public class EclipseBatchDTO {

    String countId;
    String branchId;
    String branchName;

    public EclipseBatchDTO(ValidateCountResponseDTO response) {
        var data = response.getValidateCount();

        this.countId = data.getCountId();
        this.branchId = data.getBranch();
        this.branchName = data.getBranchName();
    }
}
