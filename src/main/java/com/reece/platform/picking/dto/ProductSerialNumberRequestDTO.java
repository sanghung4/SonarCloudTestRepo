package com.reece.platform.picking.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProductSerialNumberRequestDTO {

    @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid")
    private String branchId;

    @NotBlank(message = "Invalid parameter: 'warehouseId' is blank, which is not valid")
    private String warehouseId;

    @NotEmpty(message = "Invalid parameter: 'serialNumberList' is blank, which is not valid")
    @Valid
    private List<SerialListDTO> serialNumberList;

    private boolean ignoreStockCheck;
}
