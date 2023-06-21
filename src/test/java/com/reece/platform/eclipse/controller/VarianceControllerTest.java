package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.model.DTO.VarianceDetailsDTO;
import com.reece.platform.eclipse.model.DTO.VarianceSummaryDTO;
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
import org.springframework.util.LinkedMultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EclipseServiceApplication.class)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class VarianceControllerTest {

    private VarianceController varianceController;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService; // Declared to circumvent @SpringBootTest issues

    @MockBean
    private KourierService kourierService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        varianceController = new VarianceController(kourierService);
    }

    @Test
    public void getVarianceSummary_success() throws Exception {
        VarianceSummaryDTO mockResponse = new VarianceSummaryDTO();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("countId", "test");

        when(kourierService.getVarianceSummary(any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/variance/summary")
                .queryParams(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getVarianceSummary_badRequest() throws Exception {
        VarianceSummaryDTO mockResponse = new VarianceSummaryDTO();

        when(kourierService.getVarianceSummary(any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/variance/summary"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getVarianceDetails_success() throws Exception {
        VarianceDetailsDTO mockResponse = new VarianceDetailsDTO();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("countId", "test");

        when(kourierService.getVarianceDetails(any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/variance/summary")
                        .queryParams(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getVarianceDetails_badRequest() throws Exception {
        VarianceDetailsDTO mockResponse = new VarianceDetailsDTO();

        when(kourierService.getVarianceDetails(any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/variance/summary"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
