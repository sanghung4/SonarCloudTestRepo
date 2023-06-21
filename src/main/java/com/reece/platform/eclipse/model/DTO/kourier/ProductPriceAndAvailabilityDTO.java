package com.reece.platform.eclipse.model.DTO.kourier;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductPriceAndAvailabilityDTO {

    @Setter(onMethod_ = {@JsonSetter(value = "ProductID")})
    @Getter(onMethod_ = {@JsonGetter(value = "productId")})
    private String productId;

    @Setter(onMethod_ = {@JsonSetter(value = "CatalogID")})
    @Getter(onMethod_ = {@JsonGetter(value = "catalogId")})
    private String catalogId;

    @Setter(onMethod_ = {@JsonSetter(value = "SellPrice")})
    @Getter(onMethod_ = {@JsonGetter(value = "sellPrice")})
    private Double sellPrice;

    @Setter(onMethod_ = {@JsonSetter(value = "OrderUOM")})
    @Getter(onMethod_ = {@JsonGetter(value = "orderUom")})
    private String orderUom;

    @Setter(onMethod_ = {@JsonSetter(value = "OrderPerQty")})
    @Getter(onMethod_ = {@JsonGetter(value = "orderPerQty")})
    private Integer orderPerQty;

    @Setter(onMethod_ = {@JsonSetter(value = "TotalAvailableQty")})
    @Getter(onMethod_ = {@JsonGetter(value = "totalAvailableQty")})
    private Integer totalAvailableQty;

    @Setter(onMethod_ = {@JsonSetter(value = "BranchAvailableQty")})
    @Getter(onMethod_ = {@JsonGetter(value = "branchAvailableQty")})
    private Integer branchAvailableQty;
}
