package com.reece.platform.picking.dto;

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
    private List<SerialListDTO> serialList;
    private List<SerialListDTO> nonStockSerialNumbers;
}
