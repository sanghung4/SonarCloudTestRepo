package com.reece.specialpricing.snowflake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnowflakeSpecialPriceId implements Serializable {
    private String customerId;

    private String productId;

    private String branchId;
}
