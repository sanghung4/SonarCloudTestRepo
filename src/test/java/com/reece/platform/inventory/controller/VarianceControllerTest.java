package com.reece.platform.inventory.controller;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.NextLocationDTO;
import com.reece.platform.inventory.dto.UpdateCountDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationsDTO;
import com.reece.platform.inventory.service.VarianceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
class VarianceControllerTest {

    @Mock
    private VarianceService mockVarianceService;

    @InjectMocks
    private VarianceController varianceController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        varianceController = new VarianceController(mockVarianceService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(varianceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void getVarianceSummary_shouldReturnStatus200() throws Exception {
        when(mockVarianceService.getVarianceSummary(anyString(), anyString())).thenReturn(mockVarianceSummaryDTO);

        mockMvc
            .perform(get("/variance/summary").header("X-Count-Id", "test").header("X-Branch-Id", "test"))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockVarianceService, times(1)).getVarianceSummary(anyString(), anyString());
    }

    @Test
    void getVarianceSummary_shouldReturnBadRequestStatus400() throws Exception {
        when(mockVarianceService.getVarianceSummary(anyString(), anyString())).thenReturn(mockVarianceSummaryDTO);

        mockMvc
            .perform(get("/variance/summary").header("Wrong For Testing", "test"))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void loadVarianceDetails_shouldReturnStatus200() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc.perform(post("/variance/_load").headers(httpHeaders)).andExpect(status().isOk()).andReturn();

        verify(mockVarianceService, times(1)).loadVarianceDetails(anyString(), anyString());
    }

    @Test
    void loadVarianceDetails_shouldReturnBadRequestStatus400() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc.perform(post("/variance/_load").headers(httpHeaders)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void getVarianceLocations_shouldReturnStatus200() throws Exception {
        when(mockVarianceService.getVarianceLocations(anyString(), anyString())).thenReturn(new VarianceLocationsDTO());

        mockMvc
            .perform(get("/variance/branches/{branchId}/counts/{countId}/locations", "Testing", "Testing"))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockVarianceService, times(1)).getVarianceLocations(anyString(), anyString());
    }

    @Test
    void getVarianceLocations_shouldReturnErrorStatus4xx() throws Exception {
        when(mockVarianceService.getVarianceLocations(anyString(), anyString())).thenReturn(new VarianceLocationsDTO());

        mockMvc
            .perform(post("/variance/branches/{branchId}/counts/{countId}/locations", "Testing", "Testing"))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    void getVarianceLocation_shouldReturnStatus200() throws Exception {
        when(mockVarianceService.getVarianceLocation(anyString(), anyString(), anyString()))
            .thenReturn(mockVarianceLocationDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(get("/variance/locations/{locationId}", "Testing").headers(httpHeaders))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockVarianceService, times(1)).getVarianceLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getVarianceLocation_shouldReturnBadRequestStatus400() throws Exception {
        when(mockVarianceService.getVarianceLocation(anyString(), anyString(), anyString()))
            .thenReturn(mockVarianceLocationDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(get("/variance/locations/{locationId}", "Testing").headers(httpHeaders))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getVarianceNextLocation_shouldReturnStatus200() throws Exception {
        when(mockVarianceService.getVarianceNextLocation(anyString(), anyString(), anyString()))
            .thenReturn(new NextLocationDTO("Testing"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(get("/variance/locations/{locationId}/_next", "Testing").headers(httpHeaders))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockVarianceService, times(1)).getVarianceNextLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getVarianceNextLocation_shouldReturnBadRequestStatus400() throws Exception {
        when(mockVarianceService.getVarianceNextLocation(anyString(), anyString(), anyString()))
            .thenReturn(new NextLocationDTO("Testing"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(get("/variance/locations/{locationId}/_next", "Testing").headers(httpHeaders))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    //TODO: Can fix by changing UpdateCountDTO to @Data and add @AllArgsConstructor and @NoArgsConstructor
    /*
    TODO: failing test
    @Test
    void updateVarianceCount_shouldReturnNoContentStatus203() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        var mockUpdateCountDTO = new UpdateCountDTO("Testing", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockUpdateCountDTO);
        mockMvc.perform(post("/variance/locations/{locationId}", "Testing")
                        .headers(httpHeaders)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        verify(mockVarianceService, times(1))
                .stageVarianceCount(anyString(), anyString(), anyString(), anyString(),anyInt());
    }
*/

    @Test
    void updateVarianceCount_shouldReturnBadRequestStatus400() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        var mockUpdateCountDTO = new UpdateCountDTO("Testing", 1, "Testing");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockUpdateCountDTO);
        mockMvc
            .perform(
                post("/variance/locations/{locationId}", "Testing")
                    .headers(httpHeaders)
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void commitVarianceLocation_shouldReturnNoContentStatus203() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(post("/variance/locations/{locationId}/_commit", "Testing").headers(httpHeaders))
            .andExpect(status().isNoContent())
            .andReturn();

        verify(mockVarianceService, times(1)).commitVarianceCount(anyString(), anyString(), anyString());
    }

    @Test
    void commitVarianceLocation_shouldReturnBadRequestStatus400() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc
            .perform(post("/variance/locations/{locationId}/_commit", "Testing").headers(httpHeaders))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
