package com.reece.platform.inventory.model.variance;

import lombok.Data;

@Data
public class VarianceSummary {
    private Integer totalNumberOfProducts;
    private Integer totalQuantity;
    private Integer totalNumberOfLocations;
    private Double grossTotalVarianceCost;
    private Double netTotalVarianceCost;
}
