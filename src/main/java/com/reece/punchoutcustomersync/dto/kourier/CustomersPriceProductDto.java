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
public class CustomersPriceProductDto {

    /**
     * The unique identifier for the product.
     */
    @SerializedName("ProductID")
    private String productId;

    /**
     * The unique identifier for the catalog.
     */
    @SerializedName("CatalogID")
    private String catalogID;

    /**
     * The unique identifier for the product from the customer's perspective.
     */
    @SerializedName("CustomerProductID")
    private String customerProductID;

    /**
     * The commission cost percentage for the product.
     */
    @SerializedName("CommissionCostPer")
    private float commissionCostPer;

    /**
     * The type of commission override for the product.
     */
    @SerializedName("CommissionOverrideType")
    private String commissionOverrideType;

    /**
     * The minimum order quantity for the product.
     */
    @SerializedName("OrderQty")
    private Integer orderQty;

    /**
     * The list price for the product.
     */
    @SerializedName("ListPrice")
    private float listPrice;

    /**
     * The price per unit of measure for the product.
     */
    @SerializedName("PricePer")
    private float pricePer;

    /**
     * The unit of measure for the product.
     */
    @SerializedName("UOM")
    private String uom;

    /**
     * The quantity of the unit of measure for the product.
     */
    @SerializedName("PerQty")
    private Integer perQty;

    /**
     * The price for the minimum order quantity of the product.
     */
    @SerializedName("OrderUOMPrice")
    private float orderUOMPrice;

    /**
     * The unit of measure for the minimum order quantity of the product.
     */
    @SerializedName("OrderUOM")
    private String orderUOM;

    /**
     * The minimum order quantity for the minimum order quantity unit of measure.
     */
    @SerializedName("OrderUOMMinQty")
    private Integer orderUOMMinQty;

    /**
     * The quantity of the unit of measure for the minimum order quantity.
     */
    @SerializedName("OrderPerQty")
    private Integer orderPerQty;

    /**
     * The lowest unit of measure for the product.
     */
    @SerializedName("LowestUOM")
    private String lowestUOM;

    /**
     * The sell price for the product.
     */
    @SerializedName("SellPrice")
    private float sellPrice;

    /**
     * The type of price override for the product.
     */
    @SerializedName("PriceOverrideType")
    private String priceOverrideType;

    /**
     * The unique identifier for the price matrix.
     */
    @SerializedName("PriceMatrixID")
    private String priceMatrixID;

    /**
     * The cost of goods sold percentage for the product.
     */
    @SerializedName("COGSPer")
    private float cogsPer;

    /**
     * The total available quantity of the product.
     */
    @SerializedName("TotalAvailableQty")
    private Integer totalAvailableQty;

    /**
     * The available quantity of the product for the specified branch.
     */
    @SerializedName("BranchAvailableQty")
    private Integer branchAvailableQty;

    /**
     * The error flag for the product.
     */
    @SerializedName("ErrorFlag")
    private Integer errorFlag;

    /**
     * The error message for the product.
     */
    @SerializedName("ErrorText")
    private String errorText;

    /**
     * The list of quantity breaks for the product.
     */
    @SerializedName("QtyBreaks")
    private List<QtyBreakDto> qtyBreaks;
}
