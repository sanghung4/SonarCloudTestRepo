package com.reece.platform.inventory.external.eclipse;

import java.util.List;
import lombok.Data;

@Data
public class EclipseUpdateCountsDTO {

    private List<EclipseUpdateCountDTO> updates;
}
