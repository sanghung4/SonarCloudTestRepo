package com.reece.specialpricing.postgres;

import com.reece.specialpricing.model.DynamicSortable;
import com.reece.specialpricing.snowflake.SNOWFLAKE_IMPORT_STATUS;
import com.reece.specialpricing.snowflake.SnowflakeSpecialPrice;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "special_price", schema="special_pricing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SpecialPriceId.class)
public class SpecialPrice implements DynamicSortable {
    @Id
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Id
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Id
    @Column(name = "branch", nullable = false)
    private String branch;

    @Column(name = "customer_display_name", nullable = false)
    private String customerDisplayName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "manufacturer_reference_number", nullable = false)
    private String manufacturerReferenceNumber;

    @Column(name = "salesperson", nullable = false)
    private String salesperson;

    @Column(name = "price_line", nullable = false)
    private String priceLine;

    @Column(name = "customer_sales_quantity", nullable = false)
    private int customerSalesQuantity;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "typical_price", nullable = false)
    private double typicalPrice;

    @Column(name = "rate_card_price", nullable = false)
    private double rateCardPrice;

    @Column(name = "recommended_price", nullable = false)
    private double recommendedPrice;

    @Column(name = "standard_cost", nullable = false)
    private double standardCost;

    @Column(name = "competitive_market_price", nullable = false)
    private double competitiveMarketPrice;

    @Column(name = "customer_sales_price", nullable = false)
    private double customerSalesPrice;

    @Column(name = "customer_contract_territory", nullable = true)
    private String territory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SNOWFLAKE_IMPORT_STATUS status;

    @Column(name="error_details")
    private String errorDetails;

    @CreatedDate
    @Column(name="created_at",nullable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private Date updatedAt;

    public SpecialPrice(SnowflakeSpecialPrice snowflakeSpecialPrice){
        this.productId = snowflakeSpecialPrice.getProductId() == null ? "" : snowflakeSpecialPrice.getProductId().trim();
        this.customerId = snowflakeSpecialPrice.getCustomerId() == null ? "" : snowflakeSpecialPrice.getCustomerId().trim();
        this.branch = snowflakeSpecialPrice.getBranchId() == null ? "" : snowflakeSpecialPrice.getBranchId().trim();
        this.customerDisplayName = snowflakeSpecialPrice.getCustomerName() == null ? "" : snowflakeSpecialPrice.getCustomerName().trim();
        this.manufacturer = snowflakeSpecialPrice.getManufacturer() == null ? "" : snowflakeSpecialPrice.getManufacturer().trim();
        this.displayName = snowflakeSpecialPrice.getProductDescription() == null ? "" : snowflakeSpecialPrice.getProductDescription().trim();
        this.manufacturerReferenceNumber = snowflakeSpecialPrice.getManufacturerPartNumber() == null ? "" : snowflakeSpecialPrice.getManufacturerPartNumber().trim();
        this.salesperson = snowflakeSpecialPrice.getSalesPerson() == null ? "" : snowflakeSpecialPrice.getSalesPerson().trim();
        this.priceLine = snowflakeSpecialPrice.getPriceLine() == null ? "" : snowflakeSpecialPrice.getPriceLine().trim();
        this.customerSalesQuantity = snowflakeSpecialPrice.getCustomerSalesQty() == null ? 0 : snowflakeSpecialPrice.getCustomerSalesQty();
        this.currentPrice = snowflakeSpecialPrice.getCurrentSpecialPrice() == null ? 0 : snowflakeSpecialPrice.getCurrentSpecialPrice();
        this.typicalPrice = snowflakeSpecialPrice.getTypicalPrice() == null ? 0 : snowflakeSpecialPrice.getTypicalPrice();
        this.rateCardPrice = snowflakeSpecialPrice.getRateCardPrice() == null ? 0 : snowflakeSpecialPrice.getRateCardPrice();
        this.recommendedPrice = snowflakeSpecialPrice.getRecommendedSpecialPrice() == null ? 0 : snowflakeSpecialPrice.getRecommendedSpecialPrice();
        this.competitiveMarketPrice = snowflakeSpecialPrice.getCmp() == null ? 0 : snowflakeSpecialPrice.getCmp();
        this.customerSalesPrice = snowflakeSpecialPrice.getCustomerSalesValue() == null ? 0 : snowflakeSpecialPrice.getCustomerSalesValue();
        this.standardCost = snowflakeSpecialPrice.getStandardCost() == null ? 0 : snowflakeSpecialPrice.getStandardCost();
        this.imageUrl = null;
        this.territory = snowflakeSpecialPrice.getTerritory() == null ? "DFLT" : snowflakeSpecialPrice.getTerritory();
    }

    public SpecialPrice(String productId, String customerId, String branch, String customerDisplayName, String imageUrl, String manufacturer, String displayName, String manufacturerReferenceNumber, String salesperson, String priceLine, int customerSalesQuantity, double currentPrice, double typicalPrice, double rateCardPrice, double recommendedPrice, double standardCost, double competitiveMarketPrice, double customerSalesPrice, String territory) {
        this.productId = productId;
        this.customerId = customerId;
        this.branch = branch;
        this.customerDisplayName = customerDisplayName;
        this.imageUrl = imageUrl;
        this.manufacturer = manufacturer;
        this.displayName = displayName;
        this.manufacturerReferenceNumber = manufacturerReferenceNumber;
        this.salesperson = salesperson;
        this.priceLine = priceLine;
        this.customerSalesQuantity = customerSalesQuantity;
        this.currentPrice = currentPrice;
        this.typicalPrice = typicalPrice;
        this.rateCardPrice = rateCardPrice;
        this.recommendedPrice = recommendedPrice;
        this.standardCost = standardCost;
        this.competitiveMarketPrice = competitiveMarketPrice;
        this.customerSalesPrice = customerSalesPrice;
        this.territory = territory;
    }

    public String getComparisonField(String fieldName){
        switch (fieldName) {
            case "displayName":
                return this.getDisplayName();
            case "manufacturer":
                return this.getManufacturer();
            case "branch":
                return this.getBranch();
            case "customerDisplayName":
                return this.getCustomerDisplayName();
            case "manufacturerReferenceNumber":
                return this.getManufacturerReferenceNumber();
            case "priceLine":
                return this.getPriceLine();
            default:
                return this.getProductId();
        }
    }

    public double getComparisonFieldForPrice(String fieldName) {
        switch (fieldName) {
            case "currentPrice":
                return this.getCurrentPrice();
            case "standardCost":
                return this.getStandardCost();
            case "typicalPrice":
                return this.getTypicalPrice();
            case "rateCardPrice":
                return this.getRateCardPrice();
            case "recommendedPrice":
                return this.getRecommendedPrice();
            default:
                return 0.0;
        }
    }

}
