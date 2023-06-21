package com.reece.platform.products.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveriesDeleteResponseDTO {

    public DeliveriesDeleteResponseDTO(long deletedCount, Boolean success) {
        this.deletedCount = deletedCount;
        this.success = success;
    }

    private long deletedCount;
    private Boolean success;
}
