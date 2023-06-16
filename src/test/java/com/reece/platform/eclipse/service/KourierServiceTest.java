package com.reece.platform.eclipse.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.reece.platform.eclipse.dto.inventory.KourierShippingDetailsResponseDTO;
import com.reece.platform.eclipse.dto.inventory.ShippingDetailsResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class KourierServiceTest {

    @InjectMocks
    private KourierService kourierService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EclipseService eclipseService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        kourierService = new KourierService(new RestTemplate(), eclipseService);
        MockitoAnnotations.initMocks(this);
        restTemplate = mock(RestTemplate.class);
        eclipseService = mock(EclipseService.class);
    }

    @Test
    void shippingDetails_success() throws Exception {
        val url = UriComponentsBuilder
            .newInstance()
            .pathSegment("ARS", "SHIPPING", "INSTRUCTIONS")
            .build()
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("null");
        val eclipseRequest = new HttpEntity<>(any(KourierShippingDetailsResponseDTO.class), headers);
        List<String> shippingInstructions = new ArrayList<>();
        shippingInstructions.add("*THIS IS A TEST ORDER. DO NOT SHIP!*");
        ShippingDetailsResponseDTO shippingDetailsResponseDTO = new ShippingDetailsResponseDTO(
            "Testing",
            "Successful",
            "",
            "",
            shippingInstructions,
            true,
            true
        );
        KourierShippingDetailsResponseDTO kourierShippingDetailsResponseDTO = getKourierShippingDetailsResponseDTO(
            shippingDetailsResponseDTO
        );
        ResponseEntity<KourierShippingDetailsResponseDTO> responseEntity = new ResponseEntity<KourierShippingDetailsResponseDTO>(
            kourierShippingDetailsResponseDTO,
            HttpStatus.OK
        );

        when(
            restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                eclipseRequest,
                ArgumentMatchers.<Class<KourierShippingDetailsResponseDTO>>any()
            )
        )
            .thenReturn(responseEntity);

        assertNotNull(responseEntity.getBody().getShippingtext().get(0).getInvoiceNumber());
        assertEquals("Testing", responseEntity.getBody().getShippingtext().get(0).getInvoiceNumber());
        assertNotSame("Failed", responseEntity.getBody().getShippingtext().get(0).getStatus());
        assertEquals("Successful", responseEntity.getBody().getShippingtext().get(0).getStatus());
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
    void shippingDetails_failure() {
        val url = UriComponentsBuilder
            .newInstance()
            .pathSegment("ARS", "SHIPPING", "INSTRUCTIONS")
            .build()
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        val eclipseRequest = new HttpEntity<>(any(KourierShippingDetailsResponseDTO.class), headers);
        ResponseEntity<KourierShippingDetailsResponseDTO> responseEntity = new ResponseEntity<KourierShippingDetailsResponseDTO>(
            HttpStatus.BAD_REQUEST
        );

        when(
            restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                eclipseRequest,
                ArgumentMatchers.<Class<KourierShippingDetailsResponseDTO>>any()
            )
        )
            .thenThrow(HttpClientErrorException.class);
    }
}
