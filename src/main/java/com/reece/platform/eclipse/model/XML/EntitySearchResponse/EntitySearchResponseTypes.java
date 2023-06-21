package com.reece.platform.eclipse.model.XML.EntitySearchResponse;

import lombok.Data;

import java.util.List;

@Data
public class EntitySearchResponseTypes {
    private EntitySearchMetadata metadata;
    private List<EntitySearchResult> results;
}
