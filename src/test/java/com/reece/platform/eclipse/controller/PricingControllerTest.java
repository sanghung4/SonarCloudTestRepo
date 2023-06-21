package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.model.DTO.ProductPriceResponseDTO;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EclipseServiceApplication.class)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class PricingControllerTest {

    private PricingController pricingController;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private KourierService kourierService;

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService; // Declared to circumvent @SpringBootTest issues

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        pricingController = new PricingController(kourierService);
    }

    @Test
    public void getProductPrice_success() throws Exception {
        ProductPriceResponseDTO mockResponse = new ProductPriceResponseDTO();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("productId", "testProdId");
        requestParams.add("branch", "testBranch");

        when(kourierService.getProductPrice(any(), any(), any(), any(), any(),
                any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/pricing/productPrice")
                .queryParams(requestParams)
        )
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    public void getProductPrice_emptyBranch() throws Exception {
        ProductPriceResponseDTO mockResponse = new ProductPriceResponseDTO();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("productId", "testProdId");
        requestParams.add("branch", "");

        this.mockMvc.perform(
                get("/pricing/productPrice")
                        .params(requestParams)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProductPrice_emptyProductId() throws Exception {
        ProductPriceResponseDTO mockResponse = new ProductPriceResponseDTO();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("productId", "");
        requestParams.add("branch", "testBranch");

        this.mockMvc.perform(
                get("/pricing/productPrice")
                        .params(requestParams)
        )
                .andExpect(status().isBadRequest());
    }
}
