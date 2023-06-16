package com.reece.platform.mincron.dto.kerridge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountLocationItemDTO {

    @JsonProperty("item#")
    private String itemNumber;

    @JsonProperty("prod#")
    private String productNumber;

    @JsonProperty("proddesc")
    private String productDescription;

    @JsonProperty("cat#")
    private String categoryNumber;

    @JsonProperty("tag#")
    private String tagNumber;

    private String uom;
    private String dline2;
    private String dline3;
}
