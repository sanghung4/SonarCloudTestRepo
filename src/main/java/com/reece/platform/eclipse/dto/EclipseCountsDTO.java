package com.reece.platform.eclipse.dto;

import java.util.List;
import lombok.Data;

@Data
public class EclipseCountsDTO {

    private boolean moreThan100Counts;
    private List<EclipseCountSummaryDTO> counts;

}
