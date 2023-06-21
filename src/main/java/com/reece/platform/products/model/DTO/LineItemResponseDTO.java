package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.entity.LineItems;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineItemResponseDTO {

    public LineItemResponseDTO(LineItems lineItem, ProductDTO product) {
        this.product = product;
        convertFromLineItem(lineItem);
    }

    public LineItemResponseDTO(LineItems lineItem) {
        convertFromLineItem(lineItem);
    }

    public LineItemResponseDTO(OrderLineItemResponseDTO orderLineItem) {
        this.id = UUID.randomUUID();
        this.erpPartNumber = orderLineItem.getErpPartNumber();
        this.quantity = orderLineItem.getOrderQuantity();
        this.pricePerUnit = Math.round(orderLineItem.getUnitPrice() * 100);
        this.uom = orderLineItem.getUom();
        this.quantity = orderLineItem.getOrderQuantity();
        this.product = new ProductDTO(orderLineItem);
        this.qtyAvailable = orderLineItem.getAvailableQuantity();
    }

    private void convertFromLineItem(LineItems lineItem) {
        this.id = lineItem.getId();
        this.erpPartNumber = lineItem.getErpPartNumber();
        this.customerPartNumber = lineItem.getCustomerPartNumber();
        this.quantity = lineItem.getQuantity();
        this.pricePerUnit = lineItem.getPricePerUnit();
        this.uom = lineItem.getUom();
        this.qtyAvailable = lineItem.getQtyAvailable();
        this.listIds = new ArrayList<>();
    }

    private UUID id;
    private String erpPartNumber;
    private String customerPartNumber;
    private int quantity;
    private int pricePerUnit;
    private String uom;
    private int qtyAvailable;
    private ProductDTO product;

    private List<String> listIds;
}
