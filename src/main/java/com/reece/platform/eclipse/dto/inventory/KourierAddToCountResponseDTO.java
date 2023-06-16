package com.reece.platform.eclipse.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class KourierAddToCountResponseDTO {

    @JsonProperty("ADDCOUNT_RESP")
    private List<KourierErrorDTO> addCountResp;
}