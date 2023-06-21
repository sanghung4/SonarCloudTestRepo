package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

@Data
public class VarianceSummaryDTO {
    private Integer totalNumberOfProducts;
    private Integer totalQuantity;
    private Integer totalNumberOfLocations;
    private Double grossTotalVarianceCost;
    private Double netTotalVarianceCost;
}
