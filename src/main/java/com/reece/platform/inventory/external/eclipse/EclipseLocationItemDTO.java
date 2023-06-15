package com.reece.platform.inventory.external.eclipse;

import lombok.Data;

@Data
public class EclipseLocationItemDTO {

    private String productId;
    private String description;
    private String unitOfMeasureName;
    private String unitOfMeasureQuantity;
    private String catalogNumber;
    private String imageUrl;
    private String controlNum;
}
