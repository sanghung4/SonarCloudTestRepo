package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.CountDTO;
import com.reece.platform.mincron.model.LocationDTO;
import com.reece.platform.mincron.model.LocationItemDTO;
import com.reece.platform.mincron.service.MincronService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {InventoryController.class, GlobalExceptionHandler.class})
public class InventoryControllerTest {
    @MockBean
    private MincronService mincronService;

    private InventoryController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        controller = new InventoryController(mincronService);
    }

    @Test
    public void validateCount_success() throws Exception {
        val testBranchNum = "001";
        val testCountId = "000";
        val testBranchName = "TEST BRANCH";
        val testCount = new CountDTO(testBranchNum, testCountId, testBranchName);

        when(mincronService.validateCount(testBranchNum, testCountId)).thenReturn(testCount);

        this.mockMvc
                .perform(get("/inventory/branches/{branchId}/counts/{countId}", testBranchNum, testCountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.branchNumber").value(testBranchNum))
                .andExpect(jsonPath("$.countId").value(testCountId))
                .andExpect(jsonPath("$.branchName").value(testBranchName));
    }

    @Test
    public void validateCount_notFound() throws Exception {
        val testBranchNum = "001";
        val testCountId = "000";
        val errorText = "This is a test Not Found error message";

        when(mincronService.validateCount(testBranchNum, testCountId)).thenThrow(new MincronException(errorText, HttpStatus.NOT_FOUND));

        this.mockMvc
                .perform(get("/inventory/branches/{branchId}/counts/{countId}", testBranchNum, testCountId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorText));
    }

    @Test
    public void getItemsAtLocation_success() throws Exception {
        val testBranchNum = "001";
        val testCountId = "000";
        val testLocationId = "TESTLOC01";

        val testLocationItem = new LocationItemDTO("019287", "4HWCAP", "4\\\" SDR26 HW PVC CAP G", "82576", "5700461", "EA");
        val testLocation = new LocationDTO(testLocationId, 1, 1, Collections.singletonList(testLocationItem), false);

        when(mincronService.getItemsAtLocation(testBranchNum, testCountId, testLocationId)).thenReturn(testLocation);

        this.mockMvc
                .perform(get("/inventory/branches/{branchId}/counts/{countId}/locations/{locationId}", testBranchNum, testCountId, testLocationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalQuantity").value(1))
                .andExpect(jsonPath("$.itemCount").value(1))
                .andExpect(jsonPath("$.items[0].itemNum").value(testLocationItem.getItemNum()));
    }

    @Test
    public void getItemsAtLocation_error() throws Exception {
        val testBranchNum = "001";
        val testCountId = "000";
        val testLocationId = "TESTLOC01";
        val errorText = "";
        val errorCode = "2";

        when(mincronService.getItemsAtLocation(testBranchNum, testCountId, testLocationId)).thenThrow(new MincronException(errorText, HttpStatus.BAD_REQUEST, errorCode));

        this.mockMvc
                .perform(get("/inventory/branches/{branchId}/counts/{countId}/locations/{locationId}", testBranchNum, testCountId, testLocationId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(errorCode));
    }
}
