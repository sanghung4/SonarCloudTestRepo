package com.reece.platform.inventory.dto.variance;

import lombok.Data;

@Data
public class VarianceLocationSummaryDTO {
    String id;
    Long totalProducts;
    Long totalCounted;
    Double netVarianceCost;
    Double grossVarianceCost;
}
