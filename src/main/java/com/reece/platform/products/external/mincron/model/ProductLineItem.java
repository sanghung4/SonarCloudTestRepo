package com.reece.platform.products.external.mincron.model;

import lombok.Data;

@Data
public class ProductLineItem {

    private String orderNumber;
    private String displayOnly;
    private String productNumber;
    private String description;
    private String uom;
    private String lineComments;
    private String quantityOrdered;
    private String quantityReleasedToDate;
    private String quantityShipped;
    private String sequenceNumber;
    private String quantityBackOrdered;
    private String unitPrice;
    private String netPrice;
    private String extendedPrice;
    private String lineNumber;
    private String orderLineItemTypeCode;
    private String pricingUom;
}
