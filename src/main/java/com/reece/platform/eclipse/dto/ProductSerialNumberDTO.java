package com.reece.platform.eclipse.dto;

import java.util.List;
import lombok.Data;

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
