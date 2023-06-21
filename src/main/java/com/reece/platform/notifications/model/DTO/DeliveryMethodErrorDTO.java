package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class DeliveryMethodErrorDTO {

    private String billToEntityId;
    private String shipToEntityId;
    private String orderedBy;
    private Boolean isDelivery;
    private String branchId;
    private String userEmail;
}
