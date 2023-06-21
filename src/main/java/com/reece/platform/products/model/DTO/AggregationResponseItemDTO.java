package com.reece.platform.products.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationResponseItemDTO {

    private String value;
    private Integer count;

    public boolean isVisibleCategory() {
        return !this.value.equals("TBC");
    }
}
