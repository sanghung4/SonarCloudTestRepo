package com.reece.platform.mincron.dto.kerridge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearchItemDTO {

    @JsonProperty("ProdNum")
    private String ProdNum;

    private String proddesc;
    private String uom;
    private String dline2;
    private String dline3;
}
