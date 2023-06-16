package com.reece.platform.eclipse.dto.productsearch.kourier;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKourierSearchResponse {

    private String errorCode;
    private String errorMessage;
    private int productIdCount;
    private List<KourierProduct> products;
}
