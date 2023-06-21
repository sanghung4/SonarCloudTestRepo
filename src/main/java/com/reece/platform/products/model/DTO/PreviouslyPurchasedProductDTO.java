package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.LastOrder;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.Quantity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreviouslyPurchasedProductDTO implements Serializable {

    private ProductDTO product;
    private Quantity quantity;
    private LastOrder lastOrder;
}
