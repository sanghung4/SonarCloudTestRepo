package com.reece.platform.eclipse.dto.inventory;

import lombok.Value;

@Value
public class EclipseUpdateCountDTO {

    String batchNum;
    String locationNum;
    String productId;
    int quantity;
}
