package com.reece.platform.accounts.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.model.DTO.InviteUserDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes={InviteUserDTO.class})
public class InviteUserDTOTest {

    private final String testDataPath = "src/test/resources/testData";

    @Test
    void setEmail_shouldForceEmailToLowercase() throws Exception {

        String expectedResponseJsonString = Files.readString(Paths.get(testDataPath, "invite-user-request.json"));
        JSONObject expectedResponseJsonObject = new JSONObject(expectedResponseJsonString);

        String expectedEmailFormatBeforeSetEmail = "tEST@TEst.cOm";
        String expectedEmailFormatAfterSetEmail ="test@test.com";

        ObjectMapper objectMapper = new ObjectMapper();


        InviteUserDTO expectedResponse = objectMapper.readValue(expectedResponseJsonString, InviteUserDTO.class);


        assertTrue(expectedEmailFormatBeforeSetEmail.equals(expectedResponseJsonObject.get("email")),
                "The email value in the invite-user-request.json should contain capital letters in order " +
                        "to effectively test the setEmail() method which forces the email to lowercase");

        assertTrue(expectedResponse.getEmail().equals(expectedEmailFormatAfterSetEmail),
                "Email should be forced to lowercase before saving on the 'invited_users table");
    }
}
