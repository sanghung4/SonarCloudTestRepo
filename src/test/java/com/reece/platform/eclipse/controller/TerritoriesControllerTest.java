package com.reece.platform.eclipse.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.generated.Territory;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EclipseServiceApplication.class)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class TerritoriesControllerTest {
    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService; // Declared to circumvent @SpringBootTest issues

    @MockBean
    private KourierService kourierService;

    private TerritoriesController territoriesController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        territoriesController = new TerritoriesController(eclipseService);
    }


    @Test
    public void getTerritoryById_success() throws Exception {
        String territoryId = "TESTID";
        Territory mockResponse = new Territory();
        when(eclipseService.getTerritoryById(anyString())).thenReturn(mockResponse);
        var result = this.mockMvc
                .perform(
                        get("/territories/" + territoryId)
                )
                .andExpect(status().isOk())
                .andReturn();
        var objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Territory>() {});
        });
    }


    @Test
    public void getTerritoryById_token_failure() throws Exception {
        String territoryId = "TESTID";
        when(eclipseService.getTerritoryById(anyString())).thenThrow(new EclipseTokenException());
        this.mockMvc
                .perform(
                        get("/territories/" + territoryId)
                )
                .andExpect(status().isInternalServerError());
    }
}
