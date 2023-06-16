package com.reece.platform.eclipse.dto;

import lombok.Data;

@Data
public class VarianceDetailDTO {

    private String location;
    private String erpProductID;
    private String productDescription;
    private Integer countQty;
    private Integer onHandQty;
    private Integer qtyDeviance;
    private Double onHandCost;
    private Double percentDeviance;
    private Double countedCost;
    private Boolean notCountedFlag;
    private Integer recount1Qty;
    private Integer recount2Qty;
    private Integer recount3Qty;
}
