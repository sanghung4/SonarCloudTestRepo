package com.reece.platform.inventory.dto;

import javax.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class UpdateCountDTO {

    @NotBlank(message = "Invalid Parameter: 'productId' is blank, which is not valid")
    String productId;

    Integer quantity;

    String locationId;
}
