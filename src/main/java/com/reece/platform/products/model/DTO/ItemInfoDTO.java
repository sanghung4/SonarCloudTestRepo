package com.reece.platform.products.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoDTO {

    private String productId;
    private int minIncrementQty;
    private int qty;
    private int qtyAvailable;
    private String uom;
    private float pricePerUnit;

}
