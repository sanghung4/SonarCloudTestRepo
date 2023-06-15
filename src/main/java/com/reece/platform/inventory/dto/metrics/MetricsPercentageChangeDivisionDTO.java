package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

@Data
public class MetricsPercentageChangeDivisionDTO {
    private String division;
    private String userChange;
    private String loginChange;
    private String branchChange;
}
