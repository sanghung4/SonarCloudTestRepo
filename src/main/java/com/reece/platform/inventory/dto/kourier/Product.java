package com.reece.platform.inventory.dto.kourier;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    public String productId;
    public String displayField;

    //*catalogueId is mapped to part number due to existing behaviour
    @JsonAlias("catalogId")
    private String partNumber;

    private String upc;

    @JsonAlias("imageUrl")
    private String productImageUrl;
}
