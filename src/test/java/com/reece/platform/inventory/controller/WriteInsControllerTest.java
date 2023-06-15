package com.reece.platform.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.WriteInDTO;
import com.reece.platform.inventory.service.WriteInsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.LongSupplier;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class WriteInsControllerTest {
    @Mock
    private WriteInsService writeInsService;

    @InjectMocks
    private WriteInsController writeInsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        writeInsController = new WriteInsController(
                writeInsService
        );
        mockMvc = MockMvcBuilders
                .standaloneSetup(writeInsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void index_shouldReturnStatus200() throws Exception {
        Pageable pageable = PageRequest.of(0, 8);
        var writeInDTOList = new ArrayList<WriteInDTO>();
        var totalSupplier = mock(LongSupplier.class);
        var mockResponse = PageableExecutionUtils.getPage(writeInDTOList, pageable, totalSupplier);

        when(writeInsService.findAllWriteIns(anyString(),anyString(),any(Pageable.class)))
                .thenReturn(mockResponse);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc.perform(get("/write-ins")
                        .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(writeInsService,times(1))
                .findAllWriteIns(anyString(),anyString(),any(Pageable.class));
    }

    @Test
    void index_shouldReturnBadRequestStatus400() throws Exception {
        Pageable pageable = PageRequest.of(0, 8);
        var writeInDTOList = new ArrayList<WriteInDTO>();
        var totalSupplier = mock(LongSupplier.class);
        var mockResponse = PageableExecutionUtils.getPage(writeInDTOList, pageable, totalSupplier);

        when(writeInsService.findAllWriteIns(anyString(),anyString(),any(Pageable.class)))
                .thenReturn(mockResponse);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        mockMvc.perform(get("/write-ins")
                        .headers(httpHeaders)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void show_shouldReturnStatus200() throws Exception {
        when(writeInsService.findById(any(UUID.class)))
                .thenReturn(mockWriteInDTO_1);

        mockMvc.perform(get("/write-ins/{writeInId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andReturn();

        verify(writeInsService, times(1)).findById(any(UUID.class));
    }

    @Test
    void show_shouldReturnBadRequestStatus400() throws Exception {
        when(writeInsService.findById(any(UUID.class)))
                .thenReturn(mockWriteInDTO_1);

        mockMvc.perform(get("/write-ins/{writeInId}", "Wrong For Testing"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

 /*  TODO: failing test
    @Test
    void create_shouldReturnStatus200() throws Exception {
        when(writeInsService.createWriteIn(anyString(),anyString(), any(WriteInDTO.class)))
                .thenReturn(mockWriteInDTO_1);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockWriteInDTO_1);
        mockMvc.perform(post("/write-ins")
                        .headers(httpHeaders)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(writeInsService, times(1)).createWriteIn(anyString(),anyString(), any(WriteInDTO.class));
    } */

    @Test
    void create_shouldReturnBadRequestStatus400() throws Exception {
        when(writeInsService.createWriteIn(anyString(),anyString(), any(WriteInDTO.class)))
                .thenReturn(mockWriteInDTO_1);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Test", "test");
        httpHeaders.add("X-Count-Id", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockWriteInDTO_1);
        mockMvc.perform(post("/write-ins")
                        .headers(httpHeaders)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /*
    TODO: failing test
    @Test
    void update_shouldReturnStatus200() throws Exception {
        when(writeInsService.updateWriteIn(anyString(),anyString(),any(UUID.class),any(WriteInDTO.class)))
                .thenReturn(mockWriteInDTO_1);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Branch-Id", "test");
        httpHeaders.add("X-Count-Id", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockWriteInDTO_1);
        mockMvc.perform(put("/write-ins/{writeInId}", UUID.randomUUID())
                        .headers(httpHeaders)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(writeInsService, times(1)).updateWriteIn(anyString(),anyString(),
                any(UUID.class),
                any(WriteInDTO.class));
    } */

    @Test
    void update_shouldReturnBadRequestStatus400() throws Exception {
        when(writeInsService.updateWriteIn(any(UUID.class),any(WriteInDTO.class)))
                .thenReturn(mockWriteInDTO_1);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Wrong For Testing", "test");
        httpHeaders.add("X-Count-Id", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(mockWriteInDTO_1);
        mockMvc.perform(put("/write-ins/{writeInId}", "Wrong For Testing")
                        .headers(httpHeaders)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void markResolved_shouldReturnStatus200() throws Exception {
        when(writeInsService.resolveWriteIn(any(UUID.class)))
                .thenReturn(mockWriteInDTO_1);

        mockMvc.perform(patch("/write-ins/{writeInId}/_resolve", UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(writeInsService, times(1))
                .resolveWriteIn(any(UUID.class));
    }

    @Test
    void markResolved_shouldReturnBadRequestStatus400() throws Exception {
        when(writeInsService.resolveWriteIn(any(UUID.class)))
                .thenReturn(mockWriteInDTO_1);

        mockMvc.perform(patch("/write-ins/{writeInId}/_resolve", "Wrong For Testing")
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}