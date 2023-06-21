package com.reece.platform.accounts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { RejectionReasonsController.class, GlobalExceptionHandler.class })
public class RejectionReasonsControllerTest {

    @Autowired
    private RejectionReasonsController rejectionReasonsController;

    @Test
    void getRejectionReasons_Success() throws Exception {
        var result = rejectionReasonsController.getRejectionReasons();
        assertEquals(result.size(), 3);
    }
}
