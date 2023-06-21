package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class DeleteEcommAccountResponseDTO {
    private boolean success;
    private Integer billToCount;
    private Integer shipToCount;
    private Integer userCount;

    public DeleteEcommAccountResponseDTO() {
        this.success = false;
        this.billToCount = 0;
        this.shipToCount = 0;
        this.userCount = 0;
    }
}
