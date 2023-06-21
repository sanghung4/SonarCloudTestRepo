package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.*;
import lombok.val;

@lombok.Data
public class SearchResponse {

    @lombok.Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Meta {

        @lombok.Data
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class Page {

            private int current;
            private int totalPages;
            private int totalResults;
            private int size;
        }

        @lombok.Data
        public static class Engine {

            private String name;
            private String type;
        }

        private List<String> alerts;
        private List<String> warnings;
        private Page page;
        private Engine engine;
        private String requestId;
        private Integer precision;
    }

    @lombok.Data
    @JsonDeserialize(using = Result.Deserialiser.class)
    public static class Result {

        private static final String NULL_CHARACTERS_SNOWFLAKE = "\\\\N";
        public static final String NULL_CHARACTERS_MINCRON = "\u0000";

        private static class Deserialiser extends StdDeserializer<Result> {

            public Deserialiser() {
                this(null);
            }

            public Deserialiser(Class<?> vc) {
                super(vc);
            }

            @Override
            public Result deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
                val result = new Result();
                val node = p.getCodec().readTree(p);

                val fields = new HashMap<String, Object>();

                for (var it = node.fieldNames(); it.hasNext();) {
                    val fieldName = it.next();
                    if (fieldName.equals("_meta")) {
                        val meta = (ObjectNode) node.get(fieldName);
                        result.id = meta.get("id").asText();
                        result.engine = meta.get("engine").asText();
                        result.score = meta.get("score").asDouble();
                    } else {
                        if (fieldName.startsWith("_")) {
                            continue;
                        }
                        val resultField = (ObjectNode) node.get(fieldName);
                        val rawValue = resultField.get("raw");
                        if (!rawValue.isNull()) {
                            if (rawValue.isArray()) {
                                val listValue = new ArrayList<String>();
                                for (val item : rawValue) {
                                    listValue.add(item.textValue());
                                }
                                fields.put(fieldName.toLowerCase(), Collections.unmodifiableList(listValue));
                            } else {
                                val stringValue = resultField.get("raw").asText();
                                fields.put(fieldName.toLowerCase(), stringValue);
                            }
                        }
                    }
                }

                result.fields = Collections.unmodifiableMap(fields);

                return result;
            }
        }

        private String id;
        private String engine;
        private double score;
        private Map<String, ?> fields;

        // TODO: is this necessary, or has the data effort cleaned up these null characters?
        public boolean isValueValid(String key) {
            if (!fields.containsKey(key)) {
                return false;
            }

            val value = fields.get(key);
            return (
                !value.equals(NULL_CHARACTERS_SNOWFLAKE) && !value.equals("") && !value.equals(NULL_CHARACTERS_MINCRON)
            );
        }

        public Optional<String> getStringValue(String key) {
            if (!isValueValid(key)) {
                return Optional.empty();
            }

            return Optional.of((String) fields.get(key));
        }

        public Optional<List<String>> getListValue(String key) {
            if (!isValueValid(key)) {
                return Optional.empty();
            }

            val value = fields.get(key);

            if (value instanceof List) {
                return Optional.of((List<String>) value);
            }

            return Optional.empty();
        }
    }

    @lombok.Data
    public static class Facet {

        @lombok.Data
        public static class Data {

            private String value;
            private int count;
        }

        private String type;
        private List<Data> data;
    }

    private Meta meta;
    private List<Result> results;
    private Map<String, List<Facet>> facets;
}
