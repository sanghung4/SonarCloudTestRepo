package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Locale;
import lombok.val;

@JsonSerialize(using = FieldType.Serializer.class)
@JsonDeserialize(using = FieldType.Deserializer.class)
public enum FieldType {
    TEXT,
    NUMBER,
    DATE,
    GEOLOCATION;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static class Deserializer extends JsonDeserializer<FieldType> {

        @Override
        public FieldType deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
            val textValue = p.getText();
            return FieldType.valueOf(textValue.toUpperCase(Locale.ROOT));
        }
    }

    public static class Serializer extends JsonSerializer<FieldType> {

        @Override
        public void serialize(FieldType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
        }
    }
}
