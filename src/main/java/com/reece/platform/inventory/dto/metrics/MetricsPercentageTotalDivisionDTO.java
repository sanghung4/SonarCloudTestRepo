package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

@Data
public class MetricsPercentageTotalDivisionDTO {
    private String division;
    private Integer userCount;
    private Double userPercentage;
    private Integer loginCount;
    private Double loginPercentage;
    private Integer branchCount;
    private Double branchPercentage;
}
