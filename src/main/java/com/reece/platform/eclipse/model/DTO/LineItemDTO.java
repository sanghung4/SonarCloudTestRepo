package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.SalesOrderResponse.LineItem;
import com.reece.platform.eclipse.model.XML.common.LineItemPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineItemDTO {
    private String erpPartNumber;

    private String customerPartNumber;

    private int quantity;

    private int qtyAvailable;

    private String uom;

    private String umqty;

    private LineItemPrice lineItemPrice;

    public LineItemDTO(LineItem lineItem) {
        this.lineItemPrice = lineItem.getLineItemPrice();
        this.erpPartNumber = lineItem.getPartIdentifiers().getEclipsePartNumber();
        var quantityInfo = lineItem.getQtyOrdered().getQuantity();
        this.quantity = quantityInfo.getValue();
        this.uom = quantityInfo.getUom();
        this.umqty = quantityInfo.getUmqt();
    }
}
