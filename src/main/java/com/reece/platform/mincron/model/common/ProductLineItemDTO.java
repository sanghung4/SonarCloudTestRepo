package com.reece.platform.mincron.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductLineItemDTO {
    private String orderNumber;
    private String displayOnly;
    private String productNumber;
    private String description;
    private String uom;
    private String lineComments;
    private String quantityOrdered;
    private String quantityReleasedToDate;
    private String quantityBackOrdered;
    private String quantityShipped;
    private String sequenceNumber;
    private String unitPrice;
    private String netPrice;
    private String extendedPrice;
    private String lineNumber;
    private String orderLineItemTypeCode;
    private String pricingUom;
}

