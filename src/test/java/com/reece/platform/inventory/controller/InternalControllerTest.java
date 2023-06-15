package com.reece.platform.inventory.controller;

import static com.reece.platform.inventory.util.TestCommon.mockCountDTO;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.service.InternalService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

class InternalControllerTest {

    @Mock
    private InternalService mockInternalService;

    @InjectMocks
    private InternalController internalController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        internalController = new InternalController(mockInternalService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(internalController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void loadCount_shouldReturnStatus200() throws Exception {
        when(mockInternalService.loadMincronCount(anyString(), anyString())).thenReturn(mockCountDTO);

        mockMvc
            .perform(put("/internal/branches/{branchId}/counts/{countId}/_load", "Testing", "Testing"))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockInternalService, times(1)).loadMincronCount(anyString(), anyString());
    }

    @Test
    void loadCount_shouldReturnErrorStatus4xx() throws Exception {
        when(mockInternalService.loadMincronCount(anyString(), anyString())).thenReturn(mockCountDTO);

        mockMvc
            .perform(post("/internal/branches/{branchId}/counts/{countId}/_load", "Testing", "Testing"))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    void eclipseLoadCount_shouldReturnStatus200() throws Exception {
        mockMvc.perform(put("/internal/eclipse/_load")).andExpect(status().isOk()).andReturn();

        verify(mockInternalService, times(1)).loadEclipseAPICount(null, null);
    }

    @Test
    void deleteCounts_shouldReturnStatus200() throws Exception {
        Integer val = Integer.valueOf("0");
        when(mockInternalService.deleteCounts(eq(ERPSystemName.MINCRON), anyInt())).thenReturn(val);

        mockMvc
            .perform(put("/internal/counts/_delete?erpSystem=MINCRON&endDateOffset=7"))
            .andExpect(status().isOk())
            .andReturn();

        verify(mockInternalService, times(1)).deleteCounts(eq(ERPSystemName.MINCRON), anyInt());
    }
}
