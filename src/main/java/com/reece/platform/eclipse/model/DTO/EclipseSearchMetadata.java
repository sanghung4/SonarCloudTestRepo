package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

@Data
public class EclipseSearchMetadata {
    private Integer startIndex;
    private Integer pageSize;
    private Integer totalItems;
}
