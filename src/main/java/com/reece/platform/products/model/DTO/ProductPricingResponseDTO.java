package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPricingResponseDTO {

    private String customerId;
    private String branch;
    private List<ProductPricingDTO> products;
}
