package com.reece.platform.inventory.dto;

import lombok.Value;

import java.util.List;

@Value
public class LocationsDTO {
    long totalLocations;
    long totalCounted;
    List<LocationSummaryDTO> locations;
}
