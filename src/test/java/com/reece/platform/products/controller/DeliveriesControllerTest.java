package com.reece.platform.products.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.products.model.DTO.DeliveriesDeleteResponseDTO;
import com.reece.platform.products.service.DeliveriesService;
import com.reece.platform.products.service.DeliveriesServiceTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = DeliveriesController.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class DeliveriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveriesService deliveriesService;

    private UUID CART_ID = UUID.randomUUID();

    private static final String AUTH_TOKEN =
        "Bearer eyJraWQiOiItYlVObXhLMndvLUR4OFg5elRlb0drTU1DZWFlMW8wRnVjYzdNVWV1QkZ3IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjVhNzNyOEtQRHJXLWJZMkJQenlKWUJQRnFucVZqdlJEcEdMU1k0c1lYYkkiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjE1ODE5NzMwLCJleHAiOjE2MTU5MDYxMzAsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MmQzMXBpejVLS0Y2c2I0eDciLCJzY3AiOlsiZW1haWwiLCJvcGVuaWQiLCJwcm9maWxlIl0sInN1YiI6InNkZmtqYnJhc250QHRlc3QuY29tIiwiZWNvbW1Vc2VySWQiOiI5ZmQxNjJiOS1iMzU0LTRkMDctYWM1My0yMDkxYjFmMmJlYzIiLCJlY29tbVBlcm1pc3Npb25zIjpbImVkaXRfcHJvZmlsZSIsImVkaXRfbGlzdCIsInN1Ym1pdF9xdW90ZV9vcmRlciIsImludml0ZV91c2VyIiwidmlld19pbnZvaWNlIiwiYXBwcm92ZV9jYXJ0IiwibWFuYWdlX3BheW1lbnRfbWV0aG9kcyIsImFwcHJvdmVfYWNjb3VudF91c2VyIiwibWFuYWdlX3JvbGVzIiwic3VibWl0X2NhcnRfd2l0aG91dF9hcHByb3ZhbCJdfQ.Ew3owigdqaPNQDUiG1kOxqkSZFFRHxFB2qAlzPiAIkT4ruXTqad9-cGZGBkhRYLjxhuuIJ2tdilb1nwuxNKEcZbxOBTrXkrfubh-_FrDkHjVVa59mvcXbgd1gCcADd3bs_wZCzpyJDBF_LQ3h5cYOtgvwubMQ-71Hi127fP-SgVwYmb38bMVY4IKTkja-vCeoixKGjk8p_1YRCvdpfnGxJaP9vgUqGP1-ebnN5EzO0WlkC7IHtnjXcHZdUHtIKA6NCkvlCI127_Rh8gCU6mRVoQdHjKqya8zvSkNuKAz8NijsJAk3gLSJo4Fxv_7qedBDW5XbmBaaHhw-pBezn0JSQ";

    @Test
    void deleteDelivery_happyPath() throws Exception {
        when(deliveriesService.deleteDeliveries(any())).thenReturn(new DeliveriesDeleteResponseDTO(5, true));
        mockMvc
            .perform(delete("/deliveries/{shipToAccountId}", CART_ID).header("authorization", AUTH_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }
}
