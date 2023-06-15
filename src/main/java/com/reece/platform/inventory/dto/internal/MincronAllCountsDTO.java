package com.reece.platform.inventory.dto.internal;

import java.util.List;
import lombok.Data;

@Data
public class MincronAllCountsDTO {

    private Boolean moreThan100Counts;
    private List<MincronCountDTO> counts;
}
