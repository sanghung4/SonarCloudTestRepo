package com.reece.platform.mincron.dto.kerridge;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class ProductSearchResponseDTO extends MincronErrorDTO {

    @JsonProperty("LastItem")
    private String LastItem;

    private List<ProductSearchItemDTO> results;
}
