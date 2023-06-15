package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

@Data
public class MetricsBranchDTO {
    private String id;
    private String city;
    private String state;
    private Integer userCount;
    private Integer loginCount;
    private String divisionId;
}
