package com.reece.platform.mincron.dto.variance;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VarianceDetails {

    @JsonAlias("WhseLocation")
    private String location;

    @JsonAlias("ProductNumber")
    private String erpProductID;

    @JsonAlias("ProductDesc")
    private String productDescription;

    @JsonAlias("CountedQty")
    private Integer countQty;

    @JsonAlias("OnHandQty")
    private Integer onHandQty;

    @JsonAlias("QtyVariance")
    private Integer qtyDeviance;

    @JsonAlias("OnHandCost")
    private Double onHandCost;

    @JsonAlias("PercentVar")
    private Double percentDeviance;

    @JsonAlias("CountedCost")
    private Double countedCost;

    @JsonAlias("NotCounted")
    private Boolean notCountedFlag;

    @JsonAlias("Recount1Qty")
    private Integer recount1Qty;

    @JsonAlias("Recount2Qty")
    private Integer recount2Qty;

    @JsonAlias("Recount3Qty")
    private Integer recount3Qty;
}
