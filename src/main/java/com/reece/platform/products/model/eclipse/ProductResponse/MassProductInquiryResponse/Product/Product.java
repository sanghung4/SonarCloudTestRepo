package com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.AvailabilityList;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Pricing.Pricing;
import lombok.Data;

@Data
public class Product {

    private PartIdentifiers partIdentifiers;
    private AvailabilityList availabilityList;
    private PricingUOM pricingUOM;
    private Pricing pricing;
    private String priceLine;
    private String status;
    private String description;
}
