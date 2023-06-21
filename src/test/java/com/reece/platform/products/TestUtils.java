package com.reece.platform.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.model.ErpUserInformation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class TestUtils {

    private final String TEST_DATA_PATH = "src/test/resources/testData";
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String loadTestFile(String fileName) {
        try {
            return Files.readString(Paths.get(TEST_DATA_PATH, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public <T> T loadResponseJson(String fileName, TypeReference<T> clazz) {
        try {
            val json = Files.readString(Paths.get(TEST_DATA_PATH, fileName));
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String jsonStringify(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String randomAuthHeader() {
        return "Bearer " + UUID.randomUUID();
    }

    public boolean randomBoolean() {
        return Math.random() >= 0.5;
    }

    public ErpUserInformation randomErpUserInformation() {
        return new ErpUserInformation(
            UUID.randomUUID().toString().replaceAll("-", ""),
            UUID.randomUUID().toString().replaceAll("-", "").substring(0, 13),
            String.valueOf((int) Math.floor(Math.random() * 100_000)),
            "Test User",
            "ECLIPSE",
            String.valueOf((int) Math.floor(Math.random() * 100_000))
        );
    }

    public String randomProductId() {
        val randomInt = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.format("MSC-%d", randomInt);
    }
}
