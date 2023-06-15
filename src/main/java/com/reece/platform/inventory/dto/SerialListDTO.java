package com.reece.platform.inventory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SerialListDTO {

    @Min(value = 1, message = "Invalid parameter: 'line' is less than 1, which is not valid")
    private Integer line;

    @NotBlank(message = "Invalid parameter: 'serial' is blank, which is not valid")
    private String serial;
}
