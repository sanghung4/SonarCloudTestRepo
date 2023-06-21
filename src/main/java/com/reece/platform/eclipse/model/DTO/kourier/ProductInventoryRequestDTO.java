package com.reece.platform.eclipse.model.DTO.kourier;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ProductInventoryRequestDTO {

    @Valid
    @NotNull(message = "Invalid parameter: 'productId' must not be null")
    private String productId;

    private boolean useCache;

}
