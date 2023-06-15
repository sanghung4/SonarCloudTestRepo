package com.reece.platform.inventory.dto.variance;

import com.reece.platform.inventory.model.variance.VarianceDetails;
import lombok.Data;

import java.util.List;

@Data
public class VarianceDetailsDTO {
    private List<VarianceDetails> counts;
}
