package com.reece.platform.products.model.DTO;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateItemQtyResponseDTO {

    private BigDecimal subtotal;
    private LineItemResponseDTO product;

    public UpdateItemQtyResponseDTO(LineItemResponseDTO product, BigDecimal subtotal) {
        this.product = product;
        this.subtotal = subtotal;
    }
}
