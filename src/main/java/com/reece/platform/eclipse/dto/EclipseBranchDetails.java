package com.reece.platform.eclipse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EclipseBranchDetails {

    private String branchId;
    private String branchName;
}
