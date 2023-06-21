package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class GetOrderResponseMincronDTO {

    private String orderNumber;
    private String invoiceNumber;
    private String status;
    private String orderDate;
    private String trackingNumber;
    private String purchaseOrderNumber;
    private String jobNumber;
    private String jobName;
    private String shipDate;
    private String orderTotal;
    private String contractNumber;
}
