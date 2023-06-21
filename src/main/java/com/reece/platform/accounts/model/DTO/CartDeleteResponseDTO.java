package com.reece.platform.accounts.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDeleteResponseDTO {

    public CartDeleteResponseDTO(long cartsDeleted, long lineItemsDeleted, Boolean success) {
        this.cartsDeleted = cartsDeleted;
        this.lineItemsDeleted = lineItemsDeleted;
        this.success = success;
    }

    private long cartsDeleted;
    private long lineItemsDeleted;
    private Boolean success;
}
