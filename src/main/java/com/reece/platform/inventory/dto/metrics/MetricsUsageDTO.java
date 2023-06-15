package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricsUsageDTO {
    private String type;
    private List<MetricsDivisionDTO> response;
}
