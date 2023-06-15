package com.reece.platform.inventory.dto;

import lombok.Data;

@Data
public class WarehouseUserPickDTO {
    private String productId;
    private String description;
    private Integer quantity;
    private String uom;
    private String locationType;
    private String location;
    private String lot;
    private String splitId;
    private String orderId;
    private Integer generationId;
    private Integer lineId;
    private String shipVia;
    private String tote;
    private String userId;
    private String branchId;
    private String cutDetail;
    private String cutGroup;
    private Boolean isParallelCut;
    private String warehouseID;
    private String isLot;
    private Boolean isSerial;
    private String pickGroup;
}