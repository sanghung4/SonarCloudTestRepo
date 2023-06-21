package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.SalesOrderResponse.LineItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineItemResponseDTO {
    private float unitPrice;
    private String erpPartNumber;
    private int orderQuantity;
    private int shipQuantity;
    private String uom;
    private float productOrderTotal;
    private String productName;

    public LineItemResponseDTO(LineItem lineItem) {
        this.setUnitPrice(lineItem.getLineItemPrice().getUnitPrice());
        this.setErpPartNumber(lineItem.getPartIdentifiers().getEclipsePartNumber());
        this.setOrderQuantity(lineItem.getQtyOrdered().getQuantity().getValue());
        this.setShipQuantity(lineItem.getQtyShipped().getQuantity().getValue());
        this.setProductOrderTotal(lineItem.getLineItemPrice().getExtendedPrice());
        this.setProductName(lineItem.getPartIdentifiers().getDescription());
        this.setUom(lineItem.getQtyOrdered().getQuantity().getUom());
    }
}
