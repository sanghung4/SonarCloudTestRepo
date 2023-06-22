package com.reece.platform.mincron.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class OrderHeaderDTO {
    private String orderNumber;
    private String orderStatus;
    private String jobName;
    private String jobNumber;
    private String orderDate;
    private String orderBy;
    private String purchaseOrderNumber;
    private String shipDate;
    private String shipmentMethod;
    private List<String> specialInstructions;
    private float subTotal;
    private float taxAmount;
    private float totalAmount;
    private float otherCharges;
    private int branchNumber;
    private AddressDTO shipToAddress;
    private boolean isDelivery;
    private String terms;
    private String contractNumber;
    private String dueDate;
    private String invoiceDate;
}

