package com.reece.platform.inventory.dto.variance;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class VarianceLocationsDTO {

    Long totalLocations;
    Long totalCounted;
    List<VarianceLocationSummaryDTO> locations;

    public VarianceLocationsDTO(List<VarianceLocationSummaryDTO> locations, int totalLocations) {
        this.locations = locations;
        this.totalLocations = (long) totalLocations;
    }
}
