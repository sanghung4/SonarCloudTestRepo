package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.entity.LineItems;
import lombok.Data;

@Data
public class LineItemDTO {

    public LineItemDTO(LineItems lineItems) {
        this.erpPartNumber = lineItems.getErpPartNumber();
        this.customerPartNumber = lineItems.getCustomerPartNumber();
        this.qtyAvailable = lineItems.getQtyAvailable();
        this.quantity = lineItems.getQuantity();
        this.uom = lineItems.getUom();
        this.umqty = lineItems.getUmqt();
    }

    private String erpPartNumber;

    private String customerPartNumber;

    private int quantity;

    private int qtyAvailable;

    private String uom;

    private String umqty;
}
