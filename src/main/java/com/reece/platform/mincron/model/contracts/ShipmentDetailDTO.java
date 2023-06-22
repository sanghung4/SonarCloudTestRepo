package com.reece.platform.mincron.model.contracts;

import lombok.Data;

@Data
public class ShipmentDetailDTO {
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
    private String shippingTaxJurisdiction;
    private String shipMethod;
}
