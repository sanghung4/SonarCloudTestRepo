package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

@Data
public class PaginatedResponse<T> {

    @Data
    public static class Meta {

        @Data
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class Page {

            private int current;
            private int totalPages;
            private int totalResults;
            private int size;
        }

        private Page page;
    }

    private Meta meta;
    private List<T> results;
}
