package com.reece.platform.eclipse.dto.inventory;

import java.util.List;
import lombok.Data;

@Data
public class KourerCountLocationsDTO extends KourierErrorDTO {

    private String countLocations;
    private List<KourierCountLocationDTO> locations;
}
