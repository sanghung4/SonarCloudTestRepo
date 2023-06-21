package com.reece.platform.eclipse.model.XML.EntitySearchResponse;

import lombok.Data;

@Data
public class EntitySearchMetadata {
    private int startIndex;
    private int pageSize;
    private int totalItems;
}
