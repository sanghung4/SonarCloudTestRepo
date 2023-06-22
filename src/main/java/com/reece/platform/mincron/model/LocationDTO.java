package com.reece.platform.mincron.model;

import lombok.Value;

import java.util.List;

@Value
public class LocationDTO {
    String locationId;
    int totalQuantity;
    int itemCount;
    List<LocationItemDTO> items;
    boolean additionalItemsExist;
}
