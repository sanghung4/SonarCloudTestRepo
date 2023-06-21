package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class ResultField {

    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class Raw {

        @Getter
        private Integer size;

        public Raw size(int size) {
            this.size = size;
            return this;
        }
    }

    @Getter
    private Raw raw = new Raw();

    public ResultField raw(Raw raw) {
        this.raw = raw;
        return this;
    }
}
