package com.reece.platform.eclipse.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.eclipse.dto.inventory.KourierShippingDetailsResponseDTO;
import com.reece.platform.eclipse.dto.inventory.ShippingDetailsResponseDTO;
import com.reece.platform.eclipse.service.EclipseService;
import com.reece.platform.eclipse.service.KourierService;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class PickingControllerTest {

    @Mock
    private EclipseService eclipseService;

    @Mock
    private KourierService kourierService;

    @InjectMocks
    private PickingController pickingController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pickingController = new PickingController(eclipseService, kourierService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(pickingController)
                .setCustomArgumentResolvers()
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void getShippingDetails_shouldReturnStatus200() throws Exception {
        List<String> shippingInstructions = new ArrayList<>();
        shippingInstructions.add("*THIS IS A TEST ORDER. DO NOT SHIP!*");
        ShippingDetailsResponseDTO shippingTextResponseDTO = new ShippingDetailsResponseDTO(
            "Testing",
            "Successful",
            "",
            "",
            shippingInstructions,
            true,
            true
        );
        KourierShippingDetailsResponseDTO shippingDetailsResponseDTO = getKourierShippingDetailsResponseDTO(
            shippingTextResponseDTO
        );

        when(kourierService.getShippingDetails(anyString())).thenReturn(shippingDetailsResponseDTO);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "Testing");

        mockMvc.perform(get("/picking/shipping").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(kourierService, times(1)).getShippingDetails(anyString());

        assertNotNull(shippingDetailsResponseDTO.getShippingtext().get(0).getInvoiceNumber());
        assertEquals("Testing", shippingDetailsResponseDTO.getShippingtext().get(0).getInvoiceNumber());
        assertNotSame("Failed", shippingDetailsResponseDTO.getShippingtext().get(0).getStatus());
        assertEquals("Successful", shippingDetailsResponseDTO.getShippingtext().get(0).getStatus());
    }

    @Test
    void getShippingDetails_shouldReturnStatusFailed() throws Exception {
        List<String> shippingInstructions = new ArrayList<>();
        shippingInstructions.add("");
        ShippingDetailsResponseDTO shippingTextResponseDTO = new ShippingDetailsResponseDTO(
            "Testing",
            "Failed",
            "ERP001",
            "Invalid Gen Number",
            shippingInstructions,
            false,
            false
        );
        KourierShippingDetailsResponseDTO shippingDetailsResponseDTO = getKourierShippingDetailsResponseDTO(
            shippingTextResponseDTO
        );

        when(kourierService.getShippingDetails(anyString())).thenReturn(shippingDetailsResponseDTO);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "Testing");

        mockMvc.perform(get("/picking/shipping").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(kourierService, times(1)).getShippingDetails(anyString());

        assertNotNull(shippingDetailsResponseDTO.getShippingtext().get(0).getInvoiceNumber());
        assertEquals("Testing", shippingDetailsResponseDTO.getShippingtext().get(0).getInvoiceNumber());
        assertNotSame(true, shippingDetailsResponseDTO.getShippingtext().get(0).getNoBackorder());
        assertEquals("Failed", shippingDetailsResponseDTO.getShippingtext().get(0).getStatus());
        assertSame("ERP001", shippingDetailsResponseDTO.getShippingtext().get(0).getErrorCode());
    }

    private KourierShippingDetailsResponseDTO getKourierShippingDetailsResponseDTO(
        ShippingDetailsResponseDTO shippingTextResponseDTO
    ) {
        List<ShippingDetailsResponseDTO> shippingTextResponseDTOList = new ArrayList<>();
        shippingTextResponseDTOList.add(shippingTextResponseDTO);
        KourierShippingDetailsResponseDTO kourierShippingDetailsResponseDTO = new KourierShippingDetailsResponseDTO(
            shippingTextResponseDTOList
        );
        return kourierShippingDetailsResponseDTO;
    }

    @Test
    void getShippingDetails_shouldReturnBadRequestStatus400() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "Testing");

        mockMvc.perform(get("/picking/shipping").params(requestParams)).andExpect(status().isBadRequest()).andReturn();
    }

    @SneakyThrows
    @Test
    public void validateBranch_validBranch() {
        mockMvc.perform(get("/picking/validateBranch/" + "Test_branchId")).andExpect(status().isOk()).andReturn();
        verify(eclipseService, times(1)).validateBranch("Test_branchId");
    }
}
