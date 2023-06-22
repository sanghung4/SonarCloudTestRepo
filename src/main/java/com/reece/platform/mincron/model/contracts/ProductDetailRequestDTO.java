package com.reece.platform.mincron.model.contracts;

import lombok.Data;

@Data
public class ProductDetailRequestDTO {
    private ItemWrapperDTO itemDTO;
    private String application;
    private String userId;
    private String accountId;
}
