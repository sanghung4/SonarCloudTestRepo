package com.reece.platform.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickingTaskDTO {
    private String orderId;
    private Integer generationId;
    private String invoiceId;
    private String branchId;
    private String pickGroup;
    private String assignedUserId;
    private Integer billTo;
    private Integer shipTo;
    private String shipToName;
    private String pickCount;
    private String shipVia;
    private Boolean isFromMultipleZones;
    private Double taskWeight;
    private String taskState;
}
