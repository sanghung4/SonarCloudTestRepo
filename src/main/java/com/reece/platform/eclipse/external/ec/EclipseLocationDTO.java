package com.reece.platform.eclipse.external.ec;

import lombok.Value;

import java.util.List;

@Value
public class EclipseLocationDTO {
    String locationId;
    List<EclipseLocationItemDTO> products;
}
