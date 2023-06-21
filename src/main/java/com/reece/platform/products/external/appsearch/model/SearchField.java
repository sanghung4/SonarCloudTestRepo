package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@EqualsAndHashCode
public class SearchField {

    @Getter
    private Integer weight;

    public SearchField weight(int weight) {
        this.weight = weight;
        return this;
    }
}
