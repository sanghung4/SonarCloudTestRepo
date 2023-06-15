package com.reece.platform.inventory.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.util.TestCommon;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

class PickingControllerTest {

    @Mock
    private EclipseService mockEclipseService;

    @InjectMocks
    private PickingController pickingController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pickingController = new PickingController(mockEclipseService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(pickingController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void getPickingTasks_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.getPickingTasks(anyString(), anyString())).thenReturn(new PickingTasksResponseDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("branchId", "Testing");
        requestParams.add("userId", "Testing");

        mockMvc.perform(get("/picking/tasks").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockEclipseService, times(1)).getPickingTasks(anyString(), anyString());
    }

    @Test
    void getPickingTasks_shouldReturnBadRequestStatus400() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "Testing");
        requestParams.add("userId", "Testing");

        mockMvc.perform(get("/picking/tasks").params(requestParams)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void assignPickingTasks_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.assignPickingTasks(any(PickTasksListDTO.class))).thenReturn(new PickTasksListDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(new PickTasksListDTO());
        mockMvc
            .perform(put("/picking/tasks").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).assignPickingTasks(any(PickTasksListDTO.class));
    }

    @Test
    void assignPickingTasks_shouldReturnBadRequestStatus400() throws Exception {
        mockMvc
            .perform(put("/picking/tasks").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getUserPicks_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.getUserPicks(anyString(), anyString(), anyString()))
            .thenReturn(new WarehouseUserPicksDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("branchId", "Testing");
        requestParams.add("userId", "Testing");
        requestParams.add("orderId", "Testing");

        mockMvc.perform(get("/picking/user").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockEclipseService, times(1)).getUserPicks(anyString(), anyString(), anyString());
    }

    @Test
    void completeUserPick_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.completeUserPick(anyString(), any(WarehousePickCompleteDTO.class)))
            .thenReturn(new WarehousePickCompleteDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(TestCommon.createSampleWarehousePickCompleteDTO());
        mockMvc
            .perform(
                put("/picking/user/pick/{pickId}", "Testing")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).completeUserPick(anyString(), any(WarehousePickCompleteDTO.class));
    }

    @Test
    void completeUserPick_shouldReturnBadRequestStatus400() throws Exception {
        mockMvc
            .perform(put("/picking/user/pick/{pickId}", "Testing").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getSerialNumber_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.getSerialNumber(anyString())).thenReturn(new ProductSerialNumbersResponseDTO());

        mockMvc
            .perform(get("/picking/pick/{warehouseId}/serialNumbers", "Testing"))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).getSerialNumber(anyString());
    }

    @Test
    void getSerialNumber_shouldReturnErrorStatus4xx() throws Exception {
        mockMvc
            .perform(post("/picking/pick/{warehouseId}/serialNumbers", "Testing"))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    void updateSerialNumbers_shouldReturnStatus200() throws Exception {
        when(mockEclipseService.updateSerialNumbers(anyString(), any(ProductSerialNumberRequestDTO.class)))
            .thenReturn(new ProductSerialNumbersResponseDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(TestCommon.createSampleProductSerialNumberRequestDTO());
        mockMvc
            .perform(
                put("/picking/pick/{warehouseId}/serialNumbers", "Testing")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).updateSerialNumbers(anyString(), any(ProductSerialNumberRequestDTO.class));
    }

    @Test
    void updateSerialNumbers_shouldReturnBadRequestStatus400() throws Exception {
        mockMvc
            .perform(
                put("/picking/pick/{warehouseId}/serialNumbers", "Testing").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void stagePickTask_200() throws Exception {
        when(mockEclipseService.stagePickTask(any(WarehouseToteTaskDTO.class))).thenReturn(new WarehouseToteTaskDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(TestCommon.createSampleWarehouseStagePickDTO());
        mockMvc
            .perform(put("/picking/tasks/stageLocation").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).stagePickTask(any(WarehouseToteTaskDTO.class));
    }

    @Test
    void stagePickTask_BadRequest_400() throws Exception {
        mockMvc
            .perform(put("/picking/tasks/stageLocation").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void stagePickTotePackages_200() throws Exception {
        when(mockEclipseService.stagePickTotePackages(any(WarehouseTotePackagesDTO.class)))
            .thenReturn(new WarehouseTotePackagesDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(TestCommon.createSampleWarehouseTotePackagesDTO());
        mockMvc
            .perform(
                put("/picking/tasks/stage/totePackages").content(requestJson).contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).stagePickTotePackages(any(WarehouseTotePackagesDTO.class));
    }

    @Test
    void closeTask_200() throws Exception {
        when(mockEclipseService.closePickTask(any(WarehouseCloseTaskRequestDTO.class)))
            .thenReturn(new WarehouseCloseTaskDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(TestCommon.createSampleWarehouseCloseTaskRequestDTO());
        mockMvc
            .perform(put("/picking/close").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockEclipseService, times(1)).closePickTask(any(WarehouseCloseTaskRequestDTO.class));
    }

    @Test
    void closeTask_BadRequest_400() throws Exception {
        mockMvc
            .perform(put("/picking/close").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void stagePickTotePackages_BadRequest_400() throws Exception {
        mockMvc
            .perform(put("/picking/tasks/stage/totePackages").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
