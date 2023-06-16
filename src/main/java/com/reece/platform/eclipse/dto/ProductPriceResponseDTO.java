package com.reece.platform.eclipse.dto;

import lombok.Data;

@Data
public class ProductPriceResponseDTO {

    private String priceSheetId;

    private String productId;

    private String erpBranchNum;

    private String uom;

    private Integer pricePer;

    private Double listPrice;

    private Double cmp;

    private Double stdCost;

    private Double repCost;

    private String matrixId;

    private Double rateCardPrice;

    private String rateCardName;

    private String matchedBranch;

    private String matchedClass;

    private String matchedGroup;

    private String correlationId;

    private String customerId;
}
