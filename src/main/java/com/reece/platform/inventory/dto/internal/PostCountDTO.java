package com.reece.platform.inventory.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCountDTO {

    private String branch;
    private String countId;
    private String locationNum;
    private String productId;
    private Integer quantity;
    private String tag;
}
