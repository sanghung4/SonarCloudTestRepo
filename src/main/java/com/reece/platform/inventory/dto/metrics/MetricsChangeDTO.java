package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

@Data
public class MetricsChangeDTO {
    private String metric;
    private Integer quantity;
    private String percentageChange;
}
