package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricsDivisionDTO {
    private String division;
    private Integer userCount;
    private Integer loginCount;
    private Integer branchCount;
    private List<MetricsBranchDTO> branches;
}
