package com.reece.platform.inventory.model;

import lombok.Data;

@Data
public class EclipseSearchMetadata {
    private Integer startIndex;
    private Integer pageSize;
    private Integer totalItems;
}
