package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(classes={TasksController.class})
@AutoConfigureMockMvc
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void refreshAccounts() throws Exception {
        MvcResult result = this.mockMvc.perform(put("/tasks/refresh-accounts")).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        verify(accountService, times(1)).refreshAccounts();
    }
}