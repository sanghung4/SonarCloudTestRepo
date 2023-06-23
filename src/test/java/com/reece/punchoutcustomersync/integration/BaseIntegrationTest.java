package com.reece.punchoutcustomersync.integration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration.properties")
public abstract class BaseIntegrationTest {
    @Autowired
    public MockMvc mockMvc;

    @PersistenceContext
    public EntityManager entityManager;

    @Value("${management.apikey}")
    protected String apiKey;

    public Object toObject(MvcResult response, Class clazz)
            throws UnsupportedEncodingException, JsonProcessingException {
        String json = response.getResponse().getContentAsString();
        return new ObjectMapper().readValue(json, clazz);
    }
}