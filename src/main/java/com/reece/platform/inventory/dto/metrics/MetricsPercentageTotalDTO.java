package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricsPercentageTotalDTO {
    private String type;
    private List<MetricsPercentageTotalDivisionDTO> response;
}
