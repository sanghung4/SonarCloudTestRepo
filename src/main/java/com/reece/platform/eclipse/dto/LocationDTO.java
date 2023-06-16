package com.reece.platform.eclipse.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class LocationDTO {

    private String locationId;
    private Integer totalQuantity;
    private Integer itemCount;
    private List<LocationItemDTO> items;
    private boolean additionalItemsExist; // not used
}
