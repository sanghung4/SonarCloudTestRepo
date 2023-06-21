package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.service.AccountService;
import com.reece.platform.accounts.service.ErpService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = { CreditCardController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
public class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreditCardController creditCardController;

    @MockBean
    private ErpService erpService;

    private String accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID().toString();
    }

    @Test
    void getCreditCardList_Success() throws Exception {
        CreditCardListResponseDTO creditCardListResponseDTO = new CreditCardListResponseDTO();

        when(erpService.getCreditCardList(accountId)).thenReturn(creditCardListResponseDTO);
        var result = creditCardController.getCreditCardList(accountId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deleteCreditCard_Success() throws Exception {
        String creditCardId = "123";
        doNothing().when(erpService).deleteCreditCard(eq(accountId), eq(creditCardId));
        var result = creditCardController.deleteCreditCard(accountId, creditCardId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void addCreditCard_Success() throws Exception {
        EntityUpdateSubmitResponseDTO entityUpdateSubmitResponseDTO = new EntityUpdateSubmitResponseDTO();
        EntityUpdateSubmitRequestDTO updateSubmitRequestDTO = new EntityUpdateSubmitRequestDTO();
        when(erpService.updateCreditCardList(accountId, updateSubmitRequestDTO))
            .thenReturn(entityUpdateSubmitResponseDTO);
        var result = creditCardController.addCreditCard(accountId, updateSubmitRequestDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getCreditCardSetupURL_Success() throws Exception {
        ElementSetUpUrlResponseDTO responseDTO = new ElementSetUpUrlResponseDTO();
        ElementSetupUrlDTO elementSetupUrlDTO = new ElementSetupUrlDTO();
        when(erpService.getCreditCardSetupUrl(accountId, elementSetupUrlDTO)).thenReturn(responseDTO);
        var result = creditCardController.getCreditCardSetupURL(accountId, elementSetupUrlDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getCreditCardElementInfo_Success() throws Exception {
        ElementSetupQueryResponseDTO responseDTO = new ElementSetupQueryResponseDTO();
        String elementSetupId = "123";
        when(erpService.getCreditCardElementInfo(accountId, elementSetupId)).thenReturn(responseDTO);
        var result = creditCardController.getCreditCardElementInfo(accountId, elementSetupId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }
}
