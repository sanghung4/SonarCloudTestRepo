package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.val;

public class Filter {

    public static class Combination {

        public enum Type {
            ALL,
            NONE;

            @JsonValue
            public String lowercase() {
                return name().toLowerCase(Locale.ROOT);
            }
        }

        @Getter
        private Type type;

        @Getter
        private List<Map<String, ?>> values;
    }

    private Filter() {}

    public static Combination all(Stream<Map<String, ?>> values) {
        val combination = new Combination();
        combination.type = Combination.Type.ALL;
        combination.values = values.collect(Collectors.toList());
        return combination;
    }

    public static Combination none(Stream<Map<String, ?>> values) {
        val combination = new Combination();
        combination.type = Combination.Type.NONE;
        combination.values = values.collect(Collectors.toList());
        return combination;
    }

    public static Map<String, List<String>> values(String fieldName, Collection<String> values) {
        return Map.of(fieldName, new ArrayList<>(values));
    }

    public static Map<String, String> value(String fieldName, String value) {
        return Map.of(fieldName, value);
    }

    public static Map<String, ArrayList<String>> value(String fieldName, ArrayList<String> value) {
        return Map.of(fieldName, value);
    }

    public static Map<String, List<String>> values(String fieldName, String... values) {
        return Map.of(fieldName, Arrays.asList(values));
    }
}
