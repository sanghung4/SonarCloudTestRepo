package com.reece.platform.eclipse.model.XML.EntitySearchResponse;

import lombok.Data;

@Data
public class EntitySearchResult {
    private Boolean isBillTo;
    private String name;
    private String id;
    private String homeBranch;
}
