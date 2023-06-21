package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import lombok.val;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Engine {

    public enum Type {
        DEFAULT,
        ELASTICSEARCH,
        META;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private String name;

    @JsonDeserialize(using = TypeDeserializer.class)
    private Type type;

    private String language;
    private Long documentCount;
    private List<String> sourceEngines;

    public static class TypeDeserializer extends JsonDeserializer<Type> {

        @Override
        public Type deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            val textValue = p.getText();
            return Type.valueOf(textValue.toUpperCase(Locale.ROOT));
        }
    }
}
