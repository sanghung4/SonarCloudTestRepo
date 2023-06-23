package com.reece.punchoutcustomersync.dto.kourier;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a quantity break for a product price.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QtyBreakDto {

    /**
     * The price at which the product will be sold at the given quantity break.
     */
    @SerializedName("QtyBreakSellPrice")
    private String qtyBreakSellPrice;

    /**
     * The number of items that must be purchased to qualify for the given price.
     */
    @SerializedName("QtyBreakOrderQty")
    private String qtyBreakOrderQty;

    /**
     * The unit of measure (UOM) for the order quantity.
     */
    @SerializedName("QtyBreakOrderUOM")
    private String qtyBreakOrderUOM;

    /**
     * The number of order quantities included in the given quantity break.
     */
    @SerializedName("QtyBreakUOMPerQty")
    private String qtyBreakUOMPerQty;
}
