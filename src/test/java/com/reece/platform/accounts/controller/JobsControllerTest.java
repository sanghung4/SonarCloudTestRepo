package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.service.AccountService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(classes = { JobsController.class })
@AutoConfigureMockMvc
public class JobsControllerTest {



    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void refreshAllAccounts_success() throws Exception {
        mockMvc.perform(get("/jobs/refreshAccountAll")).andExpect(status().isOk());
        verify(accountService, times(1)).refreshAccounts();
    }
}
