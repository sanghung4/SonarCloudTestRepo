package com.reece.platform.products.branches.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.reece.platform.products.branches.exception.*;
import com.reece.platform.products.branches.model.DTO.*;
import com.reece.platform.products.branches.service.BranchesService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.reece.platform.products.model.DTO.FeatureDTO;
import com.reece.platform.products.model.FeaturesEnum;
import com.reece.platform.products.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = { BranchesController.class })
@AutoConfigureMockMvc
public class BranchesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchesService branchesService;

    @MockBean
    private AccountService accountService;

    private BranchesController controller;

    @BeforeEach
    public void setup() throws Exception {
        controller = new BranchesController(branchesService, accountService);
    }

    @Test
    void getBranch_success() throws Exception {
        when(branchesService.getBranch(anyString())).thenReturn(new BranchResponseDTO());
        ResponseEntity<BranchResponseDTO> result = controller.getBranch("123123");
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getBranch_notFound() throws Exception {
        when(branchesService.getBranch(anyString())).thenThrow(new BranchNotFoundException());
        assertThrows(BranchNotFoundException.class, () -> controller.getBranch("123123"));
    }

    @Test
    void getGeolist_success() throws Exception {
        var response = new GeolistResponseDTO(
            5.0f,
            0.0f,
            Arrays.asList(new BranchResponseDTO(), new BranchResponseDTO())
        );
        when(branchesService.getBranches(any())).thenReturn(response);
        BranchesRequestDTO request = new BranchesRequestDTO();
        ResponseEntity<GeolistResponseDTO> result = controller.getBranches(request);
        assertEquals(result.getBody().getBranches().size(), 2);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void refreshWorkdayBranches_success() throws Exception {
        List<WorkdayBranchDTO> branchDTOS = new ArrayList<>();
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setIsEnabled(true);
        featureDTO.setName(FeaturesEnum.WORKDAY.name());
        when(accountService.getFeatures()).thenReturn(List.of(featureDTO));
        when(branchesService.refreshWorkdayBranches()).thenReturn(branchDTOS);
        ResponseEntity<List<WorkdayBranchDTO>> result = controller.refreshWorkdayBranches();
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), branchDTOS);
    }

    @Test
    void refreshWorkdayBranches_disabledFeature() throws Exception {
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setIsEnabled(false);
        featureDTO.setName(FeaturesEnum.WORKDAY.name());
        when(accountService.getFeatures()).thenReturn(List.of(featureDTO));
        assertThrows(WorkdayException.class, () -> controller.refreshWorkdayBranches());
        verify(branchesService, times(0)).refreshWorkdayBranches();
    }

    @Test
    void refreshWorkdayBranches_exception() throws Exception {
        when(branchesService.refreshWorkdayBranches()).thenThrow(new WorkdayException(""));
        assertThrows(WorkdayException.class, () -> controller.refreshWorkdayBranches());
    }
}
