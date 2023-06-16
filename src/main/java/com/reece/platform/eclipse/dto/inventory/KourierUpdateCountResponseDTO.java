package com.reece.platform.eclipse.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class KourierUpdateCountResponseDTO {

    @JsonProperty("POSTCOUNTRESP")
    private List<KourierErrorDTO> postCountResp;
}
