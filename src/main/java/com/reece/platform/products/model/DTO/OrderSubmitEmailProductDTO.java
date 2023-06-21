package com.reece.platform.products.model.DTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;

@Data
public class OrderSubmitEmailProductDTO {

    public OrderSubmitEmailProductDTO(OrderLineItemResponseDTO orderLineItemResponseDTO) {
        this.productDescription = orderLineItemResponseDTO.getProductName();
        this.quantity = orderLineItemResponseDTO.getOrderQuantity();
        this.unitPrice = orderLineItemResponseDTO.getUnitPrice();
        this.productTotal =
            String.format(
                "%.2f",
                BigDecimal.valueOf(quantity * unitPrice).setScale(2, RoundingMode.HALF_UP).floatValue()
            );
    }

    private String productDescription;
    private Integer quantity;
    private Float unitPrice;
    private String productTotal;
}
