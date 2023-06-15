package com.reece.platform.inventory.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderRequestDTO {

    @NotBlank(message = "Invalid parameter: 'orderId' is blank, which is not valid")
    private String orderId;

    @NotBlank(message = "Invalid parameter: 'pickerId' is blank, which is not valid")
    private String pickerId;
}
