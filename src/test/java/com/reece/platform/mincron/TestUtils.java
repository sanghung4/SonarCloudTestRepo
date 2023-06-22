package com.reece.platform.mincron;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@UtilityClass
public class TestUtils {

    private final String TEST_DATA_PATH = "src/test/resources/testData";
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String loadResponseJsonString(String fileName) {
        try {
            return Files.readString(Paths.get(TEST_DATA_PATH, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T loadResponseJson(String fileName, Class<T> clazz) {
        try {
            val json = Files.readString(Paths.get(TEST_DATA_PATH, fileName));
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> ResponseEntity<T> loadResponseJsonResponseEntity(String fileName, Class<T> clazz, HttpStatus httpStatus) {
        try {
            val json = Files.readString(Paths.get(TEST_DATA_PATH, fileName));
            return new ResponseEntity<T>(OBJECT_MAPPER.readValue(json, clazz), httpStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T loadResponseJson(String fileName, TypeReference<T> clazz) {
        try {
            val json = Files.readString(Paths.get(TEST_DATA_PATH, fileName));
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String randomAuthHeader() {
        return "Bearer " + UUID.randomUUID();
    }

    public boolean randomBoolean() {
        return Math.random() >= 0.5;
    }
}
