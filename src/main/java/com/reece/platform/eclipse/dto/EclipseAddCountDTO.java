package com.reece.platform.eclipse.dto;

import lombok.Value;

@Value
public class EclipseAddCountDTO {

    String countId;
    String productId;
    String location;
    int quantity;
    String userId;

    public EclipseAddCountDTO(String userId, String batchId, String locationId, UpdateCountDTO updateCountDTO) {
        this.userId = userId;
        this.countId = batchId;
        this.location = locationId;
        this.productId = updateCountDTO.getProductId();
        this.quantity = updateCountDTO.getQuantity();
    }
}
