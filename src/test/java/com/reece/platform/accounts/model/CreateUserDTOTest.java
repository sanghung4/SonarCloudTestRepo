package com.reece.platform.accounts.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.model.DTO.CreateUserDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes={CreateUserDTO.class})
public class CreateUserDTOTest {

    private final String testDataPath = "src/test/resources/testData";


    @Test
    void setEmail_shouldForceEmailToLowercase() throws Exception {

        String expectedResponseJsonString = Files.readString(Paths.get(testDataPath, "create-user-request.json"));
        JSONObject expectedResponseJsonObject = new JSONObject(expectedResponseJsonString);

        String expectedEmailFormatBeforeSetEmail = "tEST@TEst.cOm";
        String expectedEmailFormatAfterSetEmail ="test@test.com";

        ObjectMapper objectMapper = new ObjectMapper();


        CreateUserDTO expectedResponse = objectMapper.readValue(expectedResponseJsonString, CreateUserDTO.class);


        assertTrue(expectedEmailFormatBeforeSetEmail.equals(expectedResponseJsonObject.get("email")),
                "The email value in the invite-user-request.json should contain capital letters in order " +
                        "to effectively test the setEmail() method which forces the email to lowercase");

        assertTrue(expectedResponse.getEmail().equals(expectedEmailFormatAfterSetEmail),
                "Email should be forced to lowercase before saving in okta and " +
                        "on the 'users' and 'account_requests' table");
    }
}
