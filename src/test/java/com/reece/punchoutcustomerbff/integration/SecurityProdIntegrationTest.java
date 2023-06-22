package com.reece.punchoutcustomerbff.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
@ActiveProfiles(profiles = "external-prod")
public class SecurityProdIntegrationTest extends BaseIntegrationTest {

    /**
     * Endpoint to verify security access.
     */
    private static final String URL_VERIFY = "/security/verify";

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        this.mockMvc =
            MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    /**
     * Redirect when haven't token.
     *
     * @throws Exception for mockMVc impl.
     */
    @Test
    public void givenNotAuthenticationTokenWhenLoginThenUnauthorizedOk() throws Exception {
        MvcResult rs = mockMvc
            .perform(MockMvcRequestBuilders.get(URL_VERIFY).header(TestUtils.H_CORS_NAME, TestUtils.H_CORS_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andReturn();

        Assertions.assertEquals("", rs.getResponse().getContentAsString(), "empty result");
        Assertions.assertEquals(401, rs.getResponse().getStatus(), "status unauthorized");
    }

    /**
     * Redirect when token.
     *
     * @throws Exception for mockMVc impl.
     */
    @Test
    public void givenAuthenticationTokenWhenLoginThenOk() throws Exception {
        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URL_VERIFY)
                    .header(TestUtils.H_CORS_NAME, TestUtils.H_CORS_VALUE)
                    .with(SecurityMockMvcRequestPostProcessors.jwt())
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

        final ResultDto result = new ObjectMapper().readValue(TestUtils.getResponse(rs), ResultDto.class);

        Assertions.assertNull(result.getMessage(), "empty message.");
        Assertions.assertTrue(result.isSuccess(), "is success.");
        Assertions.assertEquals(200, rs.getResponse().getStatus(), "status success.");
    }
}
