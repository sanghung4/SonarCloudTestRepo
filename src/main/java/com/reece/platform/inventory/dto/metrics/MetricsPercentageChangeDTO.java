package com.reece.platform.inventory.dto.metrics;

import lombok.Data;

import java.util.List;

@Data
public class MetricsPercentageChangeDTO {
    private String type;
    private List<MetricsPercentageChangeDivisionDTO> response;
}
