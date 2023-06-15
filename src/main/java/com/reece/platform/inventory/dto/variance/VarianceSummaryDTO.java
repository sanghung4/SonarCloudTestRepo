package com.reece.platform.inventory.dto.variance;

import com.reece.platform.inventory.model.variance.VarianceSummary;
import lombok.Data;

import java.util.Objects;

@Data
public class VarianceSummaryDTO {
    private String netTotalCost;
    private String grossTotalCost;
    private Integer locationQuantity;
    private Integer productQuantity;
    private Integer differenceQuantity;
    private Double differencePercentage;

    public static VarianceSummaryDTO fromEntity(VarianceSummary input) {
        VarianceSummaryDTO res = new VarianceSummaryDTO();
        res.netTotalCost = Objects.toString(input.getNetTotalVarianceCost());
        res.grossTotalCost = Objects.toString(input.getGrossTotalVarianceCost());
        res.locationQuantity = input.getTotalNumberOfLocations();
        res.productQuantity = input.getTotalNumberOfProducts();
        res.differenceQuantity = input.getTotalQuantity();
        return res;
    }
}
