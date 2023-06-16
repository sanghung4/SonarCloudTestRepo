package com.reece.platform.eclipse.dto.inventory;

import com.reece.platform.eclipse.dto.UpdateCountDTO;
import lombok.Data;

@Data
public class KourierUpdateCountDTO {

    private String branch;
    private String countId;
    private String locationNum;
    private String productId;
    private Integer quantity;

    public KourierUpdateCountDTO(String countId, String locationId, UpdateCountDTO c) {
        this.branch = null;
        this.countId = countId;
        this.locationNum = locationId;
        this.productId = c.getProductId();
        this.quantity = c.getQuantity();
    }
}
