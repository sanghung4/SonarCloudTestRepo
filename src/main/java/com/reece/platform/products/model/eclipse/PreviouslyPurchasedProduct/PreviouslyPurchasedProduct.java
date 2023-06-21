package com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import lombok.Data;

@Data
public class PreviouslyPurchasedProduct {

    private Product product;
    private Quantity quantity;
    private LastOrder lastOrder;
}
