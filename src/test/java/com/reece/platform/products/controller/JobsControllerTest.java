package com.reece.platform.products.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.search.CreateIndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(JobsController.class)
public class JobsControllerTest {

    public static final String testEngineName = "test-engine";

    @MockBean
    private BranchesService branchesService;

    @MockBean
    private CreateIndexService createIndexService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void buildIndex_success() throws Exception {
        mockMvc
            .perform(post(String.format("/jobs/search-engine/%s/_full", testEngineName)))
            .andExpect(status().isCreated());

        verify(createIndexService, times(1)).buildAndPopulateNewEngine(testEngineName);
    }

    @Test
    public void updateIndex_success() throws Exception {
        mockMvc
            .perform(put(String.format("/jobs/search-engine/%s/_update", testEngineName)))
            .andExpect(status().isNoContent());

        verify(createIndexService, times(1)).updateCurrentEngine(testEngineName);
    }

    @Test
    public void syncBranchData_success() throws Exception {
        mockMvc.perform(post("/jobs/branches/_sync")).andExpect(status().isOk());

        verify(branchesService, times(1)).syncWithSnowflake();
    }
}
