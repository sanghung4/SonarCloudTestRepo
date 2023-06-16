package com.reece.platform.eclipse.dto.inventory;

import lombok.Data;

@Data
public class EclipseNextLocationDTO {

    String locationId;

    public EclipseNextLocationDTO(KourierNextLocationDTO locationDTO) {
        locationId = locationDTO.getNextLocation();
    }
}
