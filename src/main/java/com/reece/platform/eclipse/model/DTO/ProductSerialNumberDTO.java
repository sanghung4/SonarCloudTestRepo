package com.reece.platform.eclipse.model.DTO;


import com.reece.platform.eclipse.model.generated.SerialList;
import lombok.Data;

import java.util.List;

@Data
public class ProductSerialNumberDTO {
    private String productId;
    private String orderId;
    private String generationId;
    private String invoiceId;
    private String quantity;
    private String description;
    private String location;
    private String warehouseId;
    private List<SerialList> serialList;
    private List<SerialList> nonStockSerialNumbers;
}
