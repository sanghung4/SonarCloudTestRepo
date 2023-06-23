package com.reece.punchoutcustomersync.dto.kourier;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PriceRequestDto {
    @SerializedName("CustomerID")
    private String customerId;

    @SerializedName("Branch")
    private String branchId;

    @SerializedName("ProductID")
    private String productId;
}
