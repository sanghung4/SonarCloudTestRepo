package com.reece.platform.products.external.appsearch.model;

import java.util.*;
import lombok.Value;

@Value
public class QuerySuggestionRequest {

    @Value
    public static class Types {

        @Value
        public static class Documents {

            List<String> fields;
        }

        Documents documents;
    }

    String query;
    Types types;
    Integer size;

    public QuerySuggestionRequest(String query, Integer size, List<String> fields) {
        this.query = query;
        this.size = size;
        this.types = new Types(new Types.Documents(fields));
    }
}
