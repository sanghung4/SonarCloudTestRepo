package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class Facet {

    private static final String TYPE = "value";

    @Getter
    private Integer size;

    public String getType() {
        return TYPE;
    }

    public Facet size(int size) {
        this.size = size;
        return this;
    }
}
