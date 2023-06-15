package com.reece.platform.inventory.dto;

import static java.util.stream.Collectors.toList;

import com.reece.platform.inventory.external.eclipse.EclipseLocationItemDTO;
import com.reece.platform.inventory.external.eclipse.EclipseProductDTO;
import com.reece.platform.inventory.external.mincron.MincronLocationDTO;
import java.util.Collections;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
public class LocationDTO {

    String id;
    String code;
    long totalProducts;
    long totalCounted;
    boolean committed;
    List<LocationProductDTO> products;

    public static LocationDTO fromMincronLocationDTO(String locId) {
        return new LocationDTO(locId, "", 0, 0, false, Collections.emptyList());
    }

    public static LocationDTO fromEclipseLocationItemDTOs(String locationId, List<EclipseProductDTO> eclipseProducts) {
        val totalProducts = eclipseProducts.size();
        return new LocationDTO(
            locationId,
            "",
            totalProducts,
            0,
            false,
            eclipseProducts.stream().map(LocationProductDTO::fromEclipseProductDTO).collect(toList())
        );
    }
}
