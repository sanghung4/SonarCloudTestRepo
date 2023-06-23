package com.reece.punchoutcustomersync.dto.kourier;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the price information for a product for a specific customer and catalog.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerProductBranchPriceDto {
    /**
     * The customer ID.
     */
    @SerializedName("CustomerID")
    private String customerId;

    /**
     * The branch.
     */
    @SerializedName("Branch")
    private String branch;

    /**
     * A list of product pricing details.
     */
    @SerializedName("Products")
    private List<CustomersPriceProductDto> products;
}
