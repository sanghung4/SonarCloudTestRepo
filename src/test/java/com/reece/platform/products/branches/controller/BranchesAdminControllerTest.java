package com.reece.platform.products.branches.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.model.DTO.UpdateBranchDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.service.AuthorizationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { BranchesAdminController.class })
@AutoConfigureMockMvc
@EnableWebMvc
public class BranchesAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchesService branchesService;

    @MockBean
    private AuthorizationService authorizationService;

    private String AUTH_TOKEN =
        "Bearer eyJraWQiOiJFYklyU0VBTzdyNEdYcF9LdnVDNExXNjRsU29jRWdaMDlpSmZpbFhhNndzIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkhZVXNDOUJxaHBIS2Z1TWppZzNHMHBuQzdWWHJRT3hMaGYxRTBtMWF0MmsiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjY3MDY4NTkwLCJleHAiOjE2NjcxNTQ5OTAsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1OXRvb2U3NnlLM1paWXY0eDciLCJzY3AiOlsicHJvZmlsZSIsImVtYWlsIiwib3BlbmlkIl0sImF1dGhfdGltZSI6MTY2NzA2ODU4Nywic3ViIjoibW9yc2NvLWVuZ2luZWVyLWxvY2FsQHJlZWNlLmNvbSIsImVjb21tVXNlcklkIjoiMTYzMjQ2OGItMzAyNy00NzdhLTgzNDItMTVmNzA2YjY2NzliIiwiZWNvbW1QZXJtaXNzaW9ucyI6WyJtYW5hZ2VfYnJhbmNoZXMiLCJhcHByb3ZlX2FjY291bnRfdXNlciIsIm1hbmFnZV9yb2xlcyIsImFwcHJvdmVfYWxsX3VzZXJzIiwidmlld19pbnZvaWNlIiwic3VibWl0X3F1b3RlX29yZGVyIiwiZWRpdF9wcm9maWxlIiwiZWRpdF9saXN0IiwibWFuYWdlX3BheW1lbnRfbWV0aG9kcyIsInN1Ym1pdF9jYXJ0X3dpdGhvdXRfYXBwcm92YWwiLCJpbnZpdGVfdXNlciIsInRvZ2dsZV9mZWF0dXJlcyIsImFwcHJvdmVfY2FydCJdfQ.isehc92F7wzRqCKEuZzKFkMCtklq9pP8EWcNy44a0bntHpMeF0ML3IcIvlyTUk7QXF8z";
    private String NO_AUTH_TOKEN = "";

    @BeforeEach
    public void setup() throws Exception {
        when(authorizationService.userCanManageBranches(AUTH_TOKEN)).thenReturn(true);
        when(authorizationService.userCanManageBranches(NO_AUTH_TOKEN)).thenReturn(false);
    }

    @Test
    void getBranches_success() throws Exception {
        List<BranchResponseDTO> branches = new ArrayList<>();
        when(branchesService.getAllBranches()).thenReturn(branches);
        MvcResult result = mockMvc
            .perform(get("/admin/branches").header("Authorization", AUTH_TOKEN))
            .andExpect(status().isOk())
            .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<BranchResponseDTO>>() {}
            )
        );

        verify(branchesService, times(1)).getAllBranches();
    }

    @Test
    void getBranches_unauthorized() throws Exception {
        mockMvc
            .perform(get("/admin/branches").header("Authorization", NO_AUTH_TOKEN))
            .andExpect(status().isUnauthorized())
            .andReturn();

        verify(branchesService, times(0)).getAllBranches();
    }

    @Test
    void updateBranches_success() throws Exception {
        var id = "4ffa269b-d6c4-4263-a967-dea0a1443743";
        BranchResponseDTO branchResponseDTO = new BranchResponseDTO();
        when(branchesService.updateBranch(any(), any())).thenReturn(branchResponseDTO);

        UpdateBranchDTO body = new UpdateBranchDTO();
        body.setIsActive(true);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc
                .perform(put("/admin/branches/{branchId}", id).header("Authorization", AUTH_TOKEN)
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(branchesService, times(1)).updateBranch(any(), any());
    }

    @Test
    void updateBranches_unauthorized() throws Exception {
        var id = "4ffa269b-d6c4-4263-a967-dea0a1443743";
        BranchResponseDTO branchResponseDTO = new BranchResponseDTO();
        when(branchesService.updateBranch(any(), any())).thenReturn(branchResponseDTO);

        UpdateBranchDTO body = new UpdateBranchDTO();
        body.setIsActive(true);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc
                .perform(put("/admin/branches/{branchId}", id).header("Authorization", NO_AUTH_TOKEN)
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(branchesService, times(0)).updateBranch(any(), any());
    }
}
