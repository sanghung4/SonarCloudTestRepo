package com.reece.platform.picking.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
public class SerialListDTO {

    @Min(value = 1, message = "Invalid parameter: 'line' is less than 1, which is not valid")
    private Integer line;

    @NotBlank(message = "Invalid parameter: 'serial' is blank, which is not valid")
    private String serial;
}
