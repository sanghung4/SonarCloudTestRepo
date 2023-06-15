package com.reece.platform.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductSearchFilterDTO {

    private String attributeType;
    private String attributeValue;

    public ProductSearchFilterDTO(String attributeType, String attributeValue) {
        this.attributeType = attributeType;
        this.attributeValue = attributeValue;
    }
}