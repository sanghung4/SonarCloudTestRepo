package com.reece.platform.mincron.model;

import lombok.Data;

@Data
public class OrderDTO {
    String orderNumber;
    String invoiceNumber;
    String status;
    String orderDate;
    String invoiceDate;
    String trackingNumber;
    String purchaseOrderNumber;
    String jobNumber;
    String jobName;
    String shipDate;
    String orderTotal;
    String contractNumber;
}
