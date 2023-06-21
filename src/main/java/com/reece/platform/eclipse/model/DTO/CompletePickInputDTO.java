package com.reece.platform.eclipse.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletePickInputDTO {
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
    private String warehouseId;
    private String isLot;
    private Boolean isSerial;
    private String pickGroup;
    private Boolean isOverrideProduct;
    private String startPickTime;
    private String ignoreLockToteCheck;
}
