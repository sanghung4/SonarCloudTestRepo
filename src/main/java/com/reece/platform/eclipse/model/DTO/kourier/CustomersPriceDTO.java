package com.reece.platform.eclipse.model.DTO.kourier;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class CustomersPriceDTO {
    @Setter(onMethod_ = {@JsonSetter(value = "CustomerID")})
    @Getter(onMethod_ = {@JsonGetter(value = "customerId")})
    private String customerId;

    @Setter(onMethod_ = {@JsonSetter(value = "Branch")})
    @Getter(onMethod_ = {@JsonGetter(value = "branch")})
    private String branch;

    @Setter(onMethod_ = {@JsonSetter(value = "Products")})
    @Getter(onMethod_ = {@JsonGetter(value = "products")})
    private List<ProductPriceAndAvailabilityDTO> products;
}
