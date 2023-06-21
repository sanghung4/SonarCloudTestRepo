package com.reece.platform.products.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDeleteResponseDTO {

    public CartDeleteResponseDTO(long cartsDeleted, long lineItemsDeleted, Boolean success) {
        this.lineItemsDeleted = lineItemsDeleted;
        this.success = success;
    }

    private long lineItemsDeleted;
    private long cartsDeleted;
    private Boolean success;
}
