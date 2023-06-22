package com.reece.platform.mincron.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.as400.access.AS400;
import com.ibm.as400.data.ProgramCallDocument;
import com.reece.platform.mincron.exceptions.AccountNotFoundException;
import com.reece.platform.mincron.model.*;
import com.reece.platform.mincron.service.MincronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MincronController.class, GlobalExceptionHandler.class})
public class MincronControllerTest {

    @MockBean
    private MincronService mincronService;

    @MockBean
    private AS400 as400;

    @MockBean
    private ProgramCallDocument programCallDocument;

    private MincronController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        GetAccountResponseDTO getAccountResponseDTO = new GetAccountResponseDTO();
        getAccountResponseDTO.setErpAccountId("test"); getAccountResponseDTO.setStreet1("test"); getAccountResponseDTO.setStreet2("test"); getAccountResponseDTO.setCity("test"); getAccountResponseDTO.setCompanyName("test");
        getAccountResponseDTO.setState("test"); getAccountResponseDTO.setZip("test");
        when(mincronService.getAccount("test", false)).thenReturn(getAccountResponseDTO);
        when(mincronService.getAccount("error", false)).thenThrow(new AccountNotFoundException("test", HttpStatus.NOT_FOUND));
        controller = new MincronController(mincronService);
    }

    @Test
    public void getAccount_success() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/accounts/{accountId}", "test")).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> mapper.readValue(result.getResponse().getContentAsString(), GetAccountResponseDTO.class));
    }

    @Test
    public void getAccount_notFound() throws Exception {
        this.mockMvc.perform(get("/accounts/{accountId}", "error")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void createContact_success() throws Exception {
        when(mincronService.getAccount(any(), any())).thenReturn(new GetAccountResponseDTO());
        ResponseEntity<CreateContactResponseDTO> response = controller.createContact("123123", new CreateContactRequestDTO());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void updateContact_success() throws Exception {
        when(mincronService.updateContact(any(), any(), any())).thenReturn(new EditContactResponseDTO());
        ResponseEntity<EditContactResponseDTO> response = controller.updateContact("123123", "123123", new EditContactRequestDTO());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteContact_success() {
        when(mincronService.deleteContact(any(), any())).thenReturn(new DeleteContactResponseDTO());
        ResponseEntity<DeleteContactResponseDTO> response = controller.deleteContact("123123", "123123");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}

