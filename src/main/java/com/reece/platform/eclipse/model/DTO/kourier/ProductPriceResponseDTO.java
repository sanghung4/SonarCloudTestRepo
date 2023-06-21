package com.reece.platform.eclipse.model.DTO.kourier;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductPriceResponseDTO {
    @Setter(onMethod_ = {@JsonSetter(value = "CustomersPrice")})
    @Getter(onMethod_ = {@JsonGetter(value = "customersPrice")})
    private CustomersPriceDTO customersPrice;
}
