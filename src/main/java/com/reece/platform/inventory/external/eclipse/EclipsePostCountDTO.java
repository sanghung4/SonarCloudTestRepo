package com.reece.platform.inventory.external.eclipse;

import com.reece.platform.inventory.dto.internal.PostCountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EclipsePostCountDTO {

    String productId;
    Integer quantity;

    public EclipsePostCountDTO(PostCountDTO countDTO) {
        productId = countDTO.getProductId();
        quantity = countDTO.getQuantity();
    }
}
