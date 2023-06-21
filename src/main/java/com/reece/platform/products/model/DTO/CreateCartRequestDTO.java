package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class CreateCartRequestDTO {

    private String application;
    private String branchNumber;
    private String jobName;
    private String jobNumber;
    private String accountId;
    private String userId;
    private String contractNumber;
    private String shoppingCartId;
    private String rePrice;
    private ShipmentDetailDTO shipmentDetail;
}
