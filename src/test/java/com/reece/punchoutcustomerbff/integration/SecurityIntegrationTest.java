package com.reece.punchoutcustomerbff.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.punchoutcustomerbff.dto.LoginResponseDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.rest.SecurityRest;
import com.reece.punchoutcustomerbff.service.SecurityService;
import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Integration test for validate the correct process of security.
 *
 * @author luis.bolivar
 * @see SecurityRest
 * @see SecurityService
 */
@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
public class SecurityIntegrationTest extends BaseIntegrationTest {

    /**
     * email for authenticate.
     */
    private static final String EMAIL = "foo@bar.com";
    /**
     * password for authenticate.
     */
    private static final String PASS = "$1$ZE4Tho*7";
    /**
     * Endpoint for login.
     */
    private static final String URL_LOGIN = "/security/login";

    /**
     * Login ok.
     * @throws Exception For mock trying call to endpoint.
     */
    @Test
    public void givenCorrectUserAndPassWhenTryLoginThenReturnLoginOk() throws Exception {
        final String passwordSalted = Md5Crypt.md5Crypt(PASS.getBytes(), PASS);
        final AuthorizedUserDao user = AuthorizedUserDao
            .builder()
            .admin(Boolean.TRUE)
            .email(EMAIL)
            .password(passwordSalted)
            .salt(PASS)
            .build();

        this.entityManager.persist(user);

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_LOGIN)
                    .header("Origin", "*")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.getUserRequest(EMAIL, PASS))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        final LoginResponseDto login = new ObjectMapper().readValue(TestUtils.getResponse(rs), LoginResponseDto.class);
        Assertions.assertTrue(login.isSuccess(), "Login ok.");
    }

    /**
     * Login nok, with password incorrect.
     * @throws Exception For mock trying call to endpoint.
     */
    @Test
    public void givenInCorrectPasswordAndEmailWhenTryLoginThenReturnLoginNok() throws Exception {
        final String passwordSalted = Md5Crypt.md5Crypt(PASS.getBytes(), PASS);
        final AuthorizedUserDao user = AuthorizedUserDao
            .builder()
            .admin(Boolean.TRUE)
            .email(EMAIL)
            .password(passwordSalted)
            .salt(PASS)
            .build();
        this.entityManager.persist(user);

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_LOGIN)
                    .header("Origin", "*")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.getUserRequest(EMAIL, "$1$ZE4Tho*8"))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        final LoginResponseDto login = new ObjectMapper().readValue(TestUtils.getResponse(rs), LoginResponseDto.class);
        Assertions.assertFalse(login.isSuccess(), "Login not ok, with password incorrect.");
    }
}
