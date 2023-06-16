package com.reece.platform.mincron.dto.variance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VarianceSummaryDTO {
    private Integer totalNumberOfProducts;
    private Integer totalQuantity;
    private Integer totalNumberOfLocations;
    private Double grossTotalVarianceCost;
    private Double netTotalVarianceCost;
}
