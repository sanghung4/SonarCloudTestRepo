package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricsOverviewDTO {
    private String type;
    private List<MetricsChangeDTO> response;
}
