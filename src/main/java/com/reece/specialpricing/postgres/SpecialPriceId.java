package com.reece.specialpricing.postgres;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialPriceId implements Serializable {
    private String customerId;

    private String productId;

    private String branch;
}
