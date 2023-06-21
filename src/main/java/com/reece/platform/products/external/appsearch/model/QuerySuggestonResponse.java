package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

@Data
public class QuerySuggestonResponse {

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Meta {

        private String requestId;
    }

    @Data
    public static class Results {

        @Data
        public static class Document {

            private String suggestion;
        }

        private List<Document> documents;
    }

    private Meta meta;
    private Results results;
}
