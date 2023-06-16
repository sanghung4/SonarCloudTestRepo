package com.reece.platform.eclipse.dto.inventory;

import lombok.Value;

@Value
public class EclipseLocationItemDTO {

    String productId;
    String description;
    String unitOfMeasureName;
    String unitOfMeasureQuantity;
    String catalogNumber;

    public EclipseLocationItemDTO(KourierLocationProductDTO productDTO) {
        productId = productDTO.getProductId();
        description = productDTO.getProductDescription();
        unitOfMeasureName = productDTO.getUom();
        unitOfMeasureQuantity = null;
        catalogNumber = null;
    }
}
