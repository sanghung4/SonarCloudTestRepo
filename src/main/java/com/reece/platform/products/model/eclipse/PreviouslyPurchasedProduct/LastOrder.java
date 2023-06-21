package com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct;

import java.io.Serializable;
import lombok.Data;

@Data
public class LastOrder implements Serializable {

    public String lastDate;
    public String lastQuantity;
}
