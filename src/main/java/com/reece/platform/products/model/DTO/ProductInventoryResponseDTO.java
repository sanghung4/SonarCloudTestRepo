package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ProductInventoryResponseDTO {

    private String productId;
    private String productDescription;
    private List<ProductInventoryBranchDTO> inventory;
}
