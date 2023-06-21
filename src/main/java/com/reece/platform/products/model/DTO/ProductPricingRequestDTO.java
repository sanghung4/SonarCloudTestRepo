package com.reece.platform.products.model.DTO;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductPricingRequestDTO {

    @Valid
    @NotNull(message = "Invalid parameter: 'customerId' must not be null")
    private String customerId;

    @Valid
    @NotNull(message = "Invalid parameter: 'branchId' must not be null")
    private String branchId;

    @Valid
    @NotNull(message = "Invalid parameter: 'productIds' must not be null")
    @NotEmpty(message = "Invalid parameter: 'productIds' must not be empty")
    private List<String> productIds;

    private boolean includeListData;
}
