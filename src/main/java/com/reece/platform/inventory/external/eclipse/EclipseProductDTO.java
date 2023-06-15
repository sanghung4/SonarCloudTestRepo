package com.reece.platform.inventory.external.eclipse;

import lombok.Data;

@Data
public class EclipseProductDTO {

    private String productId;
    private String productDescription;
    private String locationId;
    private String uom;
    private String catalogNumber;
    private String imageUrl;
    private String controlNum;
}
