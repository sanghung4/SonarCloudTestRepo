package com.reece.platform.inventory.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

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
