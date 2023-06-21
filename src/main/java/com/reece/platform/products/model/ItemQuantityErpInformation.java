package com.reece.platform.products.model;

import lombok.Data;

@Data
public class ItemQuantityErpInformation {

    private int qty;
    private int minIncrementQty;
    private ErpUserInformation erpUserInformation;
}
