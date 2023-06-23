package com.reece.punchoutcustomersync.dto.kourier;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Representation of the prices for a customer.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomersPriceDto {
    @SerializedName("CustomersPrice")
    private CustomerProductBranchPriceDto customerProductBranchPrice;
}
