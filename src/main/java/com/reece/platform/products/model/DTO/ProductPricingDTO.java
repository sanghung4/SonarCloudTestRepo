package com.reece.platform.products.model.DTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductPricingDTO {

    private String productId;
    private String catalogId;
    private BigDecimal sellPrice;
    private String orderUom;
    private Integer orderPerQty;
    private Integer totalAvailableQty;
    private Integer branchAvailableQty;
    private List<String> listIds;
}
