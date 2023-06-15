package com.reece.platform.inventory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehousePickCompleteDTO {

    @NotBlank(message = "Invalid parameter: 'productId' is blank, which is not valid")
    private String productId;

    @NotBlank(message = "Invalid parameter: 'description' is blank, which is not valid")
    private String description;

    @Min(value = 1, message = "Invalid parameter: 'quantity' is less than 1, which is not valid")
    private Integer quantity;

    @NotBlank(message = "Invalid parameter: 'uom' is blank, which is not valid")
    private String uom;

    @NotBlank(message = "Invalid parameter: 'locationType' is blank, which is not valid")
    private String locationType;

    private String location;

    private String lot;

    private String splitId;
    private String orderId;
    private Integer generationId;

    @Min(value = 1, message = "Invalid parameter: 'lineId' is less than 1, which is not valid")
    private Integer lineId;

    @NotBlank(message = "Invalid parameter: 'shipVia' is blank, which is not valid")
    private String shipVia;

    private String tote;

    @NotBlank(message = "Invalid parameter: 'userId' is blank, which is not valid")
    private String userId;

    @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid")
    private String branchId;

    private String cutDetail;
    private String cutGroup;
    private Boolean isParallelCut;

    @NotBlank(message = "Invalid parameter: 'warehouseID' is blank, which is not valid")
    private String warehouseID;

    private String isLot;

    private boolean isSerial;

    private String pickGroup;

    private boolean isOverrideProduct;

    private String startPickTime;

    private boolean ignoreLockToteCheck;
}
