package com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.ProductList;
import lombok.Data;

@Data
public class MassProductInquiryResponse {

    private ProductList productList;
}
