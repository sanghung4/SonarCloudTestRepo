package com.reece.platform.eclipse.dto.inventory;

import lombok.Data;

@Data
public class KourierCountProductDTO {

    private String productID;
    private String productDesc;
    private String location;
    private String uom;
    private String catalogId;
    private String imageUrl;
    private String controlNo;
}
