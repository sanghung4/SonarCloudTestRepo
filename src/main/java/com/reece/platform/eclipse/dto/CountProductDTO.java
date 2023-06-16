package com.reece.platform.eclipse.dto;

import com.reece.platform.eclipse.dto.inventory.KourierCountProductDTO;
import lombok.Data;

@Data
public class CountProductDTO {

    private String productId;
    private String productDescription;
    private String locationId;
    private String uom;
    private String catalogNumber;
    private String imageUrl;
    private String controlNum;

    public CountProductDTO(KourierCountProductDTO productDTO) {
        productId = productDTO.getProductID();
        productDescription = productDTO.getProductDesc();
        locationId = productDTO.getLocation();
        uom = productDTO.getUom();
        catalogNumber = productDTO.getCatalogId();
        imageUrl = productDTO.getImageUrl();
        controlNum = productDTO.getControlNo();
    }
}
