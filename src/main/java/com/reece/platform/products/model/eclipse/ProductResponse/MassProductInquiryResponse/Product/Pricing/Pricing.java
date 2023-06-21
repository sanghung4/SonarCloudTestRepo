package com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Pricing;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import lombok.Data;

@Data
public class Pricing {

    private String eclipsePartNumber;

    private String entityId;

    private PricingBranch pricingBranch;

    private String currency;

    private Quantity quantity;

    private String listPrice;

    private String customerPrice;

    private String extendedPrice;
}
