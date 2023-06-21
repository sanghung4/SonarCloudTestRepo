package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reece.platform.accounts.model.DTO.CreditCardListResponseDTO;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = { PhoneTypesController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
public class PhoneTypesControllerTest {

    @Autowired
    private PhoneTypesController phoneTypesController;

    @Test
    void getCreditCardList_Success() throws Exception {
        var result = phoneTypesController.getPhoneTypes();
        assertEquals(result.size(), 3);
    }
}
