package com.reece.platform.accounts.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveriesDeleteResponseDTO {
    private int deletedCount;
    private Boolean success;
}
