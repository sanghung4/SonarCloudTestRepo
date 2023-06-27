package com.reece.specialpricing.snowflake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SnowflakeSpecialPriceId.class)
public class SnowflakeSpecialPrice {
    @Id
    @Column(name = "branch_id")
    private String branchId;

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Id
    @Column(name = "customer_rate_card")
    private String customerRateCard;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "sales_person")
    private String salesPerson;

    @Column(name = "price_line")
    private String priceLine;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "manufacturer_part_number")
    private String manufacturerPartNumber;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "basis_name")
    private String basisName;

    @Column(name = "basis_number")
    private String basisNumber;

    @Column(name = "standard_cost")
    private Double standardCost;

    @Column(name = "cmp")
    private Double cmp;

    @Column(name = "rate_card_price")
    private Double rateCardPrice;

    @Column(name = "customer_sales_qty")
    private Integer customerSalesQty;

    @Column(name = "current_special_price")
    private Double currentSpecialPrice;

    @Column(name = "customer_sales_value")
    private Double customerSalesValue;

    @Column(name = "avg_price_by_branch")
    private Double avgPriceByBranch;

    @Column(name = "avg_price_by_market")
    private Double avgPriceByMarket;

    @Column(name = "avg_price_by_region")
    private Double avgPriceByRegion;

    @Column(name = "avg_price_by_division")
    private Double avgPriceByDivision;

    @Column(name = "typical_price")
    private Double typicalPrice;

    @Column(name = "recommended_special_price")
    private Double recommendedSpecialPrice;

    @Column(name = "recommended_action")
    private String recommendedAction;

    @Column(name = "customer_contract_territory")
    private String territory;
}