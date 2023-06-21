package com.reece.platform.products.model.eclipse.ProductResponse;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.MassProductInquiryResponse;
import lombok.Data;

@Data
public class ProductResponse {

    public ProductResponse() {}

    private MassProductInquiryResponse massProductInquiryResponse;
}
