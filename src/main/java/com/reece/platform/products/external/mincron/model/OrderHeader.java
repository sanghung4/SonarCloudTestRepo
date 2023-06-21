package com.reece.platform.products.external.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHeader {

    private String orderNumber;
    private String orderStatus;
    private String jobName;
    private String jobNumber;
    private String orderDate;
    private String invoiceDate;
    private String orderBy;
    private String purchaseOrderNumber;
    private Address shipToAddress;
    private String shipDate;
    private String shipmentMethod;
    private List<String> specialInstructions;
    private float subTotal;
    private float taxAmount;
    private float totalAmount;
    private float otherCharges;
    private String terms;
    private String contractNumber;
    private String dueDate;

    @JsonProperty
    private boolean isDelivery;

    private int branchNumber;
}
