package com.reece.punchoutcustomersync.dto.kourier;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of the price response from Kourier.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PriceResponseDto {
    /**
     * The customer price details.
     */
    @SerializedName("CustomersPrice")
    private CustomersPriceDto customersPrice;
}
