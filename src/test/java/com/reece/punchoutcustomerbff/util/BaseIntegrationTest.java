package com.reece.punchoutcustomerbff.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void mockAdminLoggedIn() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken('1', null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Object toObject(MvcResult response, Class clazz)
            throws UnsupportedEncodingException, JsonProcessingException {
        String json = response.getResponse().getContentAsString();
        return new ObjectMapper().readValue(json, clazz);
    }
}
