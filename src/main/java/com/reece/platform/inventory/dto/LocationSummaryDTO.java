package com.reece.platform.inventory.dto;

import lombok.Value;

@Value
public class LocationSummaryDTO {
    String id;
    boolean committed;
    long totalProducts;
    long totalCounted;
}
