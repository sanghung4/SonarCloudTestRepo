package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class ProductDetailRequestDTO {

    private ItemWrapperDTO itemDTO;
    private String application;
    private String userId;
    private String accountId;
}
