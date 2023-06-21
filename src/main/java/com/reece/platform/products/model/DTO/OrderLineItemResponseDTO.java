package com.reece.platform.products.model.DTO;

import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.ImageUrls;
import com.reece.platform.products.model.entity.LineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemResponseDTO {

    public OrderLineItemResponseDTO(LineItems lineItems) {
        this.erpPartNumber = lineItems.getErpPartNumber();
        this.orderQuantity = lineItems.getQuantity();
        this.unitPrice = lineItems.getPricePerUnit();
        this.uom = lineItems.getUom();
    }

    public OrderLineItemResponseDTO(ProductDTO product, ProductLineItem productLineItem) {
        this.erpPartNumber = productLineItem.getProductNumber();
        this.unitPrice = Float.parseFloat(productLineItem.getUnitPrice());
        this.productName = productLineItem.getDescription();
        if (productLineItem.getQuantityOrdered() != null) {
            this.orderQuantity = Integer.parseInt(productLineItem.getQuantityOrdered());
        }
        if (productLineItem.getQuantityBackOrdered() != null) {
            this.backOrderedQuantity = Integer.parseInt(productLineItem.getQuantityBackOrdered());
        }
        if (productLineItem.getQuantityShipped() != null) {
            this.shipQuantity = Integer.parseInt(productLineItem.getQuantityShipped());
        }
        this.productOrderTotal = Float.parseFloat(productLineItem.getExtendedPrice());
        this.sequenceNumber = productLineItem.getSequenceNumber();
        this.uom = productLineItem.getUom();
        this.lineNumber = productLineItem.getLineNumber();
        this.lineComments = productLineItem.getLineComments();
        this.pricingUom = productLineItem.getPricingUom();

        if (product != null) {
            this.productId = product.getId();
            this.imageUrls = product.getImageUrls();
            this.manufacturerName = product.getManufacturerName();
            this.manufacturerNumber = product.getManufacturerNumber();
            this.productName = product.getName();
        }
    }

    private float unitPrice;
    private String erpPartNumber;
    private int orderQuantity;
    private int backOrderedQuantity;
    private int shipQuantity;
    private int availableQuantity;
    private String uom;
    private float productOrderTotal;
    private ImageUrls imageUrls;
    private String manufacturerName;
    private String manufacturerNumber;
    private String productName;
    private String productId;
    private String status;
    private String sequenceNumber;
    private String lineNumber;
    private String lineComments;
    private String pricingUom;
}
