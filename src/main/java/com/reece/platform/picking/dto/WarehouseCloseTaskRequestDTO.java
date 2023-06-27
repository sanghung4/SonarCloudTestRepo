package com.reece.platform.picking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseCloseTaskRequestDTO {

    @NotBlank(message = "Invalid parameter: 'invoiceNumber' is blank, which is not valid")
    private String invoiceNumber;

    @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid")
    private String branchId;

    private String finalLocation;

    @NotBlank(message = "Invalid parameter: 'tote' is blank, which is not valid")
    private String tote;

    private boolean skipStagedWarningFlag;

    private boolean skipInvalidLocationWarningFlag;

    private boolean updateLocationOnly;
}
