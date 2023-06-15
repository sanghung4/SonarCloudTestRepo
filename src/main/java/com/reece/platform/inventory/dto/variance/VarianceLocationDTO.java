package com.reece.platform.inventory.dto.variance;

import com.reece.platform.inventory.dto.LocationProductDTO;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class VarianceLocationDTO {
    String id;
    String code;
    Long totalProducts;
    Long totalCounted;
    Double netVarianceCost;
    Double grossVarianceCost;
    Boolean committed;
    List<VarianceLocationProductDTO> products;
}
