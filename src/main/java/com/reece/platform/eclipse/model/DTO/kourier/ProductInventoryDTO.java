package com.reece.platform.eclipse.model.DTO.kourier;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ProductInventoryDTO {
    @Setter(onMethod_ = {@JsonSetter(value = "ProductID")})
    @Getter(onMethod_ = {@JsonGetter(value = "productId")})
    private String productId;

    @Setter(onMethod_ = {@JsonSetter(value = "ProductDescription")})
    @Getter(onMethod_ = {@JsonGetter(value = "productDescription")})
    private String productDescription;

    @Setter(onMethod_ = {@JsonSetter(value = "Inventory")})
    @Getter(onMethod_ = {@JsonGetter(value = "inventory")})
    private List<ProductInventoryBranchDTO> inventory;
}
