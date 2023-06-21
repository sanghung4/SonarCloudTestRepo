package com.reece.platform.products.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductSearchFilterDTO {

    private String attributeType;
    private String attributeValue;

    public ProductSearchFilterDTO(String attributeType, String attributeValue) {
        this.attributeType = attributeType;
        this.attributeValue = attributeValue.replace("%2C", ",");
    }
}
