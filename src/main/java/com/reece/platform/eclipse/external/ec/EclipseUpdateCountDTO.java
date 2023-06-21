package com.reece.platform.eclipse.external.ec;

import lombok.Value;

@Value
public class EclipseUpdateCountDTO {
    String batchNum;
    String locationNum;
    String productId;
    int quantity;
}
