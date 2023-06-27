package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StructuredPrice {
    private String type;
    private double value;
    private String currency;
    private String displayName;
}
