package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseStagePickDTO {
    private String invoiceNumber;
    private String branchId;
    private String tote;
    private Boolean skipStagedWarningFlag = true;
    private Boolean skipInvalidLocationWarningFlag = true;
    private Boolean updateLocationOnly = false;
    private String finalLocation;
    private String location;
    private List<PackageDTO> packageList;
}
