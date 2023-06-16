package com.reece.platform.eclipse.dto.productsearch.kourier;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KourierProduct {

    private String productId;
    private String displayField;
    private String catalogId;
    private String upc;

    @JsonAlias("imgURL")
    private String imageUrl;
}
