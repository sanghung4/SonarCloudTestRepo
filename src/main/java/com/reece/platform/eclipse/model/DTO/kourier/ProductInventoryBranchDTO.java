package com.reece.platform.eclipse.model.DTO.kourier;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductInventoryBranchDTO {

    @Setter(onMethod_ = {@JsonSetter(value = "Branch")})
    @Getter(onMethod_ = {@JsonGetter(value = "branchId")})
    private String branchId;

    @Setter(onMethod_ = {@JsonSetter(value = "BranchName")})
    @Getter(onMethod_ = {@JsonGetter(value = "branchName")})
    private String branchName;

    @Setter(onMethod_ = {@JsonSetter(value = "AvailableOnHand")})
    @Getter(onMethod_ = {@JsonGetter(value = "availableOnHand")})
    private Integer availableOnHand;
}
