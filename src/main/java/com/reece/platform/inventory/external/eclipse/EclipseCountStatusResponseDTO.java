package com.reece.platform.inventory.external.eclipse;

import java.util.List;
import lombok.Data;

@Data
public class EclipseCountStatusResponseDTO {

    private List<EclipseCountStatusDTO> counts;
}
