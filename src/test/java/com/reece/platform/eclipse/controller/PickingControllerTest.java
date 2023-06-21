package com.reece.platform.eclipse.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.exceptions.CloseOrderException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.exceptions.InvalidToteException;
import com.reece.platform.eclipse.exceptions.PickNotFoundException;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.generated.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(classes = { EclipseServiceApplication.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class PickingControllerTest {

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

    private PickingController pickingController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        pickingController = new PickingController(eclipseService);
    }

    @Test
    public void getPickingTasks_success() throws Exception {
        PickingTasksResponseDTO mockResponse = new PickingTasksResponseDTO();
        when(eclipseService.getPickingTasks(anyString(), anyString(), any(OrderMode.class))).thenReturn(mockResponse);
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.set("branchId", "1");
        requestParams.set("userId", "test");
        var result =
            this.mockMvc.perform(get("/picking/tasks").params(requestParams)).andExpect(status().isOk()).andReturn();
        var objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<PickingTasksResponseDTO>() {}
            );
        });
    }

    @Test
    public void getPickingTasks_failure_token() throws Exception {
        PickingTasksResponseDTO mockResponse = new PickingTasksResponseDTO();
        when(eclipseService.getPickingTasks(anyString(), anyString(), any(OrderMode.class)))
            .thenThrow(new EclipseTokenException());
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.set("branchId", "1");
        requestParams.set("userId", "test");
        this.mockMvc.perform(get("/picking/tasks").params(requestParams)).andExpect(status().isInternalServerError());
    }

    @Test
    public void assignPickingTasks_success() throws Exception {
        WarehousePickTaskList mockRequest = new WarehousePickTaskList();
        WarehousePickTaskList mockResponse = new WarehousePickTaskList();
        when(eclipseService.assignPickingTasks(any(WarehousePickTaskList.class))).thenReturn(mockResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<WarehousePickTaskList>() {}
            );
        });
    }

    @Test
    public void assignPickingTasks_failure_token() throws Exception {
        WarehousePickTaskList mockRequest = new WarehousePickTaskList();
        when(eclipseService.assignPickingTasks(any(WarehousePickTaskList.class)))
            .thenThrow(new EclipseTokenException());
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void getUserPicks_success() throws Exception {
        WarehouseTaskUserPicksDTO mockResponse = new WarehouseTaskUserPicksDTO();
        when(eclipseService.getUserPicks(anyString(), anyString())).thenReturn(mockResponse);
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.set("branchId", "1");
        requestParams.set("userId", "test");
        var result =
            this.mockMvc.perform(get("/picking/user").params(requestParams)).andExpect(status().isOk()).andReturn();
        var objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<WarehouseTaskUserPicksDTO>() {}
            );
        });
    }

    @Test
    public void getUserPicks_failure_token() throws Exception {
        WarehouseTaskUserPicksDTO mockResponse = new WarehouseTaskUserPicksDTO();
        when(eclipseService.getUserPicks(anyString(), anyString())).thenThrow(new EclipseTokenException());
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.set("branchId", "1");
        requestParams.set("userId", "test");
        this.mockMvc.perform(get("/picking/user").params(requestParams))
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void completeUserPick_success() throws Exception {
        WarehousePickComplete mockRequest = new WarehousePickComplete();
        WarehousePickComplete mockResponse = new WarehousePickComplete();
        when(eclipseService.completeUserPick(anyString(), any(WarehousePickComplete.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/user/pick/{pickId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<WarehousePickComplete>() {}
            );
        });
    }

    @Test
    public void completeUserPick_failure_token() throws Exception {
        WarehousePickComplete mockRequest = new WarehousePickComplete();
        WarehousePickComplete mockResponse = new WarehousePickComplete();
        when(eclipseService.completeUserPick(anyString(), any(WarehousePickComplete.class)))
            .thenThrow(new EclipseTokenException());
        var objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/user/pick/{pickId}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void completeUserPick_failure_tote() throws Exception {
        WarehousePickComplete mockRequest = new WarehousePickComplete();
        WarehousePickComplete mockResponse = new WarehousePickComplete();
        when(eclipseService.completeUserPick(anyString(), any(WarehousePickComplete.class)))
            .thenThrow(new InvalidToteException());
        var objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/user/pick/{pickId}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void completeUserPick_failure_invalid_pick() throws Exception {
        WarehousePickComplete mockRequest = new WarehousePickComplete();
        WarehousePickComplete mockResponse = new WarehousePickComplete();
        when(eclipseService.completeUserPick(anyString(), any(WarehousePickComplete.class)))
            .thenThrow(new PickNotFoundException("1"));
        var objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/user/pick/{pickId}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    public void updateStagingLocation_success() throws Exception {
        WarehouseToteTask mockRequest = new WarehouseToteTask();
        WarehouseToteTask mockResponse = new WarehouseToteTask();
        when(eclipseService.updateToteTask(any(WarehouseToteTask.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/user/tote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<WarehouseToteTask>() {}
            );
        });
    }

    @Test
    public void updateStagingLocation_failure_token() throws Exception {
        WarehouseToteTask mockRequest = new WarehouseToteTask();
        WarehouseToteTask mockResponse = new WarehouseToteTask();
        when(eclipseService.updateToteTask(any(WarehouseToteTask.class))).thenThrow(new EclipseTokenException());
        var objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/user/tote")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void updatePackageQuantities_success() throws Exception {
        WarehouseTotePackages mockRequest = new WarehouseTotePackages();
        WarehouseTotePackages mockResponse = new WarehouseTotePackages();
        when(eclipseService.updateTotePackages(any(WarehouseTotePackages.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/user/tote/package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<WarehouseTotePackages>() {}
            );
        });
    }

    @Test
    public void updatePackageQuantities_failure_token() throws Exception {
        WarehouseTotePackages mockRequest = new WarehouseTotePackages();
        WarehouseTotePackages mockResponse = new WarehouseTotePackages();
        when(eclipseService.updateTotePackages(any(WarehouseTotePackages.class)))
            .thenThrow(new EclipseTokenException());
        var objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                put("/picking/user/tote/package")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest))
            )
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void createCloseOrder_success() throws Exception {
        //Response packet
        CloseOrderResponseDTO closeOrderResponseDTO = new CloseOrderResponseDTO();
        closeOrderResponseDTO.setOrderId("S113643890.001");
        closeOrderResponseDTO.setOrderLocked(false);
        closeOrderResponseDTO.setPickerId("SF08466");
        closeOrderResponseDTO.setErrorCode("");
        closeOrderResponseDTO.setErrorMessage("");
        closeOrderResponseDTO.setOrderLocked(false);
        closeOrderResponseDTO.setStatus(true);
        closeOrderResponseDTO.setStillPicking(false);
        closeOrderResponseDTO.setMoreToPick(false);
        //Request packet
        CloseOrderRequestDTO closeOrderRequestDTO = new CloseOrderRequestDTO();
        closeOrderRequestDTO.setOrderId("S113643890.001");
        closeOrderRequestDTO.setPickerId("SF08466");
        //Object Mapper
        ObjectMapper objectMapper = new ObjectMapper();
        //When
        when(kourierService.closeOrder(closeOrderRequestDTO)).thenReturn(closeOrderResponseDTO);
        //Action
        MvcResult result =
            this.mockMvc.perform(
                    post("/picking/closeOrder/")
                        .content(objectMapper.writeValueAsString(closeOrderRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        assertEquals(objectMapper.writeValueAsString(closeOrderResponseDTO), result.getResponse().getContentAsString());
    }

    @Test
    public void createCloseOrder_failure_orderId_Missing() throws Exception {
        //Request packet
        CloseOrderRequestDTO closeOrderRequestDTO = new CloseOrderRequestDTO();
        closeOrderRequestDTO.setOrderId("");
        closeOrderRequestDTO.setPickerId("SF08466");

        when(kourierService.closeOrder(closeOrderRequestDTO))
            .thenThrow(new CloseOrderException("Missing orderId in CloseOrder request"));
        this.mockMvc.perform(
                post("/picking/closeOrder/")
                    .content(String.valueOf(new CloseOrderException("Missing orderId in CloseOrder request")))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void createCloseOrder_failure_pickerId_Missing() throws Exception {
        //Request packet
        CloseOrderRequestDTO closeOrderRequestDTO = new CloseOrderRequestDTO();
        closeOrderRequestDTO.setOrderId("S113643890.001");
        closeOrderRequestDTO.setPickerId("");

        when(kourierService.closeOrder(closeOrderRequestDTO))
            .thenThrow(new CloseOrderException("Missing pickerId in CloseOrder request"));
        this.mockMvc.perform(
                post("/picking/closeOrder/")
                    .content(String.valueOf(new CloseOrderException("Missing pickerId in CloseOrder request")))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void getSerialNumbers_success() throws Exception {
        ProductSerialNumbersResponseDTO mockResponse = new ProductSerialNumbersResponseDTO();
        when(eclipseService.getSerialNumbers(anyString())).thenReturn(mockResponse);
        var requestParams = new LinkedMultiValueMap<String, String>();
        requestParams.set("branchId", "1");
        requestParams.set("userId", "test");
        var result =
            this.mockMvc.perform(get("/picking/tasks/{warehouseId}/serialNumbers", "123"))
                .andExpect(status().isOk())
                .andReturn();
        var objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<ProductSerialNumbersResponseDTO>() {}
            );
        });
    }

    @Test
    public void updateSerialNumbers_success() throws Exception {
        WarehouseSerialNumbers mockRequest = new WarehouseSerialNumbers();
        ProductSerialNumbersResponseDTO mockResponse = new ProductSerialNumbersResponseDTO();
        when(eclipseService.updateSerialNumbers(anyString(), any(WarehouseSerialNumbers.class)))
            .thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/tasks/{warehouseId}/serialNumbers", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(mockResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void closeTask_success() throws Exception {
        WarehouseCloseTask mockRequest = new WarehouseCloseTask();
        WarehouseCloseTask mockResponse = new WarehouseCloseTask();
        when(eclipseService.closeTask(any(WarehouseCloseTask.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    put("/picking/tasks/close")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(mockResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void splitQuantity_success() throws Exception {
        SplitQuantityRequestDTO mockRequest = new SplitQuantityRequestDTO();
        SplitQuantityResponseDTO mockResponse = new SplitQuantityResponseDTO();
        when(kourierService.splitQuantity(any(SplitQuantityRequestDTO.class))).thenReturn(mockResponse);
        var objectMapper = new ObjectMapper();
        var result =
            this.mockMvc.perform(
                    post("/picking/splitQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(mockResponse), result.getResponse().getContentAsString());
    }
}
