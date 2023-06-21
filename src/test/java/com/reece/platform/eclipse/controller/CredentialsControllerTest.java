package com.reece.platform.eclipse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.external.ec.EclipseConnectService;
import com.reece.platform.eclipse.external.ec.EclipseCredentials;
import com.reece.platform.eclipse.external.ec.EclipseCredentialsStore;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.util.TokenUtils;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EclipseServiceApplication.class,GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class CredentialsControllerTest {

    @MockBean
    private EclipseCredentialsStore eclipseCredentialsStore;

    @MockBean
    private  EclipseConnectService eclipseConnectService;

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private TokenUtils tokenUtils;

    @MockBean
    private FileTransferService fileTransferService;

    @Autowired
    private MockMvc mockMvc;

    private CredentialsController credentialsController;

    @BeforeEach
    public void setup() throws Exception {
        credentialsController = new CredentialsController(eclipseCredentialsStore,eclipseConnectService);
    }

    @Test
    void setCredentials_success()  throws Exception  {
        String authHeader="123";
        EclipseCredentials eclipseCredentials = new EclipseCredentials("123","bbc","def");
        val userId = "123";
        doNothing().when(eclipseCredentialsStore).putCredentials(userId, eclipseCredentials);
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .header("Authorization", authHeader)
                        .content(objectMapper.writeValueAsString(eclipseCredentials))

                ).andExpect(status().isCreated()).
                andReturn();

    }

    @Test
    void validate_Success() throws Exception {
        String authHeader="Test Header";
        String sessionIdHeader="Test session header";
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = new EclipseCredentials("TEST", "1234","123");
        when(eclipseCredentialsStore.getCredentials(userId, sessionId)).thenReturn(credentials);
        when(eclipseConnectService.validateCredentials(credentials)).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(get("/credentials/_valid")
                        .header("Authorization", authHeader)
                        .header("X-Session-Id", sessionIdHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials))

                ).andExpect(status().isNoContent()).
                andReturn();

    }

    @Test
    void validate_failure() throws Exception{
        String authHeader="Test Header";
        String sessionIdHeader="Test session header";
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = new EclipseCredentials("TEST", "1234","123");
        when(eclipseCredentialsStore.getCredentials(userId, sessionId)).thenReturn(credentials);
        when(eclipseConnectService.validateCredentials(credentials)).thenReturn(false);
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(get("/credentials/_valid")
                        .header("Authorization", authHeader)
                        .header("X-Session-Id", sessionIdHeader)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials))

                ).andExpect(status().isUnauthorized()).
                andReturn();
    }

}
