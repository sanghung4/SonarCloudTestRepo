package com.reece.platform.eclipse.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValidateCountResponseDTO {

    @JsonProperty("ValidateCount")
    private ValidateCountDTO ValidateCount;
}
