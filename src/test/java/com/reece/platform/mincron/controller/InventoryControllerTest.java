package com.reece.platform.mincron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.mincron.dto.CountDTO;
import com.reece.platform.mincron.dto.LocationDTO;
import com.reece.platform.mincron.dto.MincronCountsDTO;
import com.reece.platform.mincron.dto.NextLocationDTO;
import com.reece.platform.mincron.dto.kerridge.MincronCountDTO;
import com.reece.platform.mincron.dto.kerridge.MincronUpdateCountRequestDTO;
import com.reece.platform.mincron.dto.variance.VarianceDetailsResponseDTO;
import com.reece.platform.mincron.dto.variance.VarianceSummaryDTO;
import com.reece.platform.mincron.service.MincronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryControllerTest {

    @Mock
    private MincronService mockMincronService;

    @InjectMocks
    private InventoryController inventoryController;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        inventoryController = new InventoryController(mockMincronService);
        mockMvc =
                MockMvcBuilders.standaloneSetup(inventoryController)
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                        .build();
    }

    @Test
    void getAllCounts_shouldReturnStatus200() throws Exception {
        when(mockMincronService.getAllCounts()).thenReturn(new MincronCountsDTO());

        mockMvc.perform(get("/inventory/counts"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).getAllCounts();
    }

    @Test
    void getAllCounts_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.getAllCounts()).thenReturn(new MincronCountsDTO());

        mockMvc.perform(get("/inventory/count"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getCount_shouldReturnStatus200() throws Exception {
        when(mockMincronService.validateCount(anyString(), anyString())).thenReturn(new CountDTO());

        mockMvc.perform(get("/inventory/branches/Testing/counts/Testing"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).validateCount(anyString(), anyString());
    }

    @Test
    void getCount_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.validateCount(anyString(), anyString())).thenReturn(new CountDTO());

        mockMvc.perform(get("/inventory/branches/Testing/counts/"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getLocations_shouldReturnStatus200() throws Exception {
        when(mockMincronService.getLocations(anyString(), anyString())).thenReturn(new ArrayList<String>());

        mockMvc.perform(get("/inventory/branches/Testing/counts/Testing/locations"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).getLocations(anyString(), anyString());
    }

    @Test
    void getLocations_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.getLocations(anyString(), anyString())).thenReturn(new ArrayList<String>());

        mockMvc.perform(get("/inventory/branches//counts/Testing/locations"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getLocation_shouldReturnStatus200() throws Exception {
        when(mockMincronService.getLocation(anyString(), anyString(), anyString())).thenReturn(new LocationDTO());

        mockMvc.perform(get("/inventory/branches/Testing/counts/Testing/locations/Testing"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).getLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getLocation_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.getLocation(anyString(), anyString(), anyString())).thenReturn(new LocationDTO());

        mockMvc.perform(get("/inventory/branches/Testing/counts//locations/Testing"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateCount_shouldReturnStatus204() throws Exception {
        doNothing().when(mockMincronService).updateCount(anyString(), anyString(), any(MincronUpdateCountRequestDTO.class));

        ObjectMapper objectMapper = new ObjectMapper();
        MincronCountDTO mincronCountDTO = new MincronCountDTO("Mexico", "test", 32);
        MincronUpdateCountRequestDTO mincronUpdateCountRequestDTO = new MincronUpdateCountRequestDTO();
        mincronUpdateCountRequestDTO.setUpdates(Collections.singletonList(mincronCountDTO));
        String requestJson = objectMapper.writeValueAsString(mincronUpdateCountRequestDTO);
        mockMvc.perform(put("/inventory/branches/Testing/counts/Testing")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        verify(mockMincronService, times(1)).updateCount(anyString(), anyString(), any(MincronUpdateCountRequestDTO.class));
    }

    @Test
    void updateCount_shouldReturnBadRequestStatus400() throws Exception {
        doNothing().when(mockMincronService).updateCount(anyString(), anyString(), any(MincronUpdateCountRequestDTO.class));

        mockMvc.perform(put("/inventory/branches/Testing/counts/Testing")
                        //missing content to trigger Bad Request
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getNextLocation_shouldReturnStatus200() throws Exception {
        when(mockMincronService.getNextLocation(anyString(), anyString(), anyString())).thenReturn(new NextLocationDTO());

        mockMvc.perform(get("/inventory/branches/Testing/counts/Testing/locations/Testing/_next"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).getNextLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getNextLocation_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.getNextLocation(anyString(), anyString(), anyString())).thenReturn(new NextLocationDTO());

        mockMvc.perform(get("/inventory/branches//counts/Testing/locations/Testing/_next"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getVarianceSummary_shouldReturnStatus200() throws Exception {
        when(mockMincronService.fetchVarianceSummary(anyString(), anyString())).thenReturn(new VarianceSummaryDTO());

        mockMvc.perform(get("/inventory/varianceSummary/Testing/Testing"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).fetchVarianceSummary(anyString(), anyString());
    }

    @Test
    void getVarianceSummary_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.fetchVarianceSummary(anyString(), anyString())).thenReturn(new VarianceSummaryDTO());

        mockMvc.perform(get("/inventory/varianceSummary//Testing"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getVarianceDetails_shouldReturnStatus200() throws Exception {
        when(mockMincronService.fetchVarianceDetails(anyString(), anyString())).thenReturn(new VarianceDetailsResponseDTO());

        mockMvc.perform(get("/inventory/varianceDetails/Testing/Testing"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockMincronService, times(1)).fetchVarianceDetails(anyString(), anyString());
    }

    @Test
    void getVarianceDetails_shouldReturnBadRequestStatus404() throws Exception {
        when(mockMincronService.fetchVarianceDetails(anyString(), anyString())).thenReturn(new VarianceDetailsResponseDTO());

        mockMvc.perform(get("/inventory/varianceDetails//Testing"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
