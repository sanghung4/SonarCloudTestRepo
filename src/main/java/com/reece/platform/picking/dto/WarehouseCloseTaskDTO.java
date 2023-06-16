package com.reece.platform.picking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseCloseTaskDTO {
    private String invoiceNumber;

    private String branchId;

    private String finalLocation;

    private List<WarehouseCloseTaskTotes> totes;

    private Boolean skipStagedWarningFlag;

    private Boolean skipInvalidLocationWarningFlag;

    private Boolean updateLocationOnly;
}
