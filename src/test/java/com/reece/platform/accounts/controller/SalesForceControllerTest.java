package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.reece.platform.accounts.exception.SalesForceException;
import com.reece.platform.accounts.model.DTO.SFMCResponseToken;
import com.reece.platform.accounts.model.DTO.SMFCResponseDTO;
import com.reece.platform.accounts.service.SFMCService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { SalesForceController.class })
@AutoConfigureMockMvc
public class SalesForceControllerTest {

    @MockBean
    private SFMCService salesForceService;

    @Autowired
    private SalesForceController salesForceController;

    @Test
    void deleteContacts_success() throws SalesForceException {
        List<String> contactKeys = new ArrayList<>();
        contactKeys.add("TEST_317-531-5555");
        contactKeys.add("TEST_317-531-5556");
        SFMCResponseToken token = new SFMCResponseToken();
        token.setAccess_token("Bearer test token");

        SMFCResponseDTO SMFCResponseDTO = new SMFCResponseDTO();
        SMFCResponseDTO.setOperationID(650250);
        SMFCResponseDTO.setOperationInitiated(true);

        when(salesForceService.getToken()).thenReturn(token);
        when(salesForceService.deleteContacts(contactKeys, token.getAccess_token())).thenReturn(SMFCResponseDTO);

        var response = salesForceController.deleteContacts(contactKeys);
        assertEquals(response.getBody().getOperationID(), 650250);
    }
}
