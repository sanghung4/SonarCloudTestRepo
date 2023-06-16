package com.reece.platform.picking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderRequestDTO {

    @NotBlank(message = "Invalid parameter: 'orderId' is blank, which is not valid")
    private String orderId;

    @NotBlank(message = "Invalid parameter: 'pickerId' is blank, which is not valid")
    private String pickerId;
}
