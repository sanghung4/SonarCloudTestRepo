package com.reece.platform.mincron.dto.kerridge;

import java.util.List;
import lombok.Data;

@Data
public class CountLocationsResponseDTO extends MincronErrorDTO {

    private Integer CountLocations;
    private List<LocationCodeDTO> locations;
}
