package com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct;

import java.io.Serializable;
import lombok.Data;

@Data
public class Quantity implements Serializable {

    public String uom;
    public String umqt;
    public String quantity;
}
