package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private String productDescription;
    private Integer quantity;
    private String unitPrice;
    private String productTotal;
}
