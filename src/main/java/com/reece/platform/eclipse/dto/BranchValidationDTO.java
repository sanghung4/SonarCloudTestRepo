package com.reece.platform.eclipse.dto;

import lombok.*;

@Data
@Builder
public class BranchValidationDTO {

    private Boolean isValid;

    private EclipseBranchDetails branch;
}
