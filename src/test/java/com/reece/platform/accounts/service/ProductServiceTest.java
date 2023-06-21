package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.model.DTO.CartDeleteResponseDTO;
import com.reece.platform.accounts.model.DTO.DeliveriesDeleteResponseDTO;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.model.enums.FeaturesEnum;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { ProductsService.class, RestTemplate.class, ObjectMapper.class })
public class ProductServiceTest {

    @Autowired
    private ProductsService productsService;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        Feature feature = new Feature(UUID.randomUUID(), FeaturesEnum.WATERWORKS.name(), true);

        ReflectionTestUtils.setField(
            productsService,
            "productServiceUrl",
            "http://ecomm-dev-products-core-service:8080"
        );
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void deleteCartsByShipToId_success() throws Exception {
        UUID shipToAccountId = UUID.randomUUID();
        CartDeleteResponseDTO cartDeleteResponseDTO = new CartDeleteResponseDTO();
        cartDeleteResponseDTO.setCartsDeleted(12343);
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-products-core-service:8080/cart"))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(cartDeleteResponseDTO))
            );

        var response = productsService.deleteCartsByShipToId(shipToAccountId);
        assertEquals(response.get().getCartsDeleted(), 12343);
    }

    @Test
    void deleteDeliveries_success() throws Exception {
        UUID shipToId = UUID.randomUUID();
        DeliveriesDeleteResponseDTO deliveriesDeleteResponseDTO = new DeliveriesDeleteResponseDTO();
        deliveriesDeleteResponseDTO.setDeletedCount(123);
        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-products-core-service:8080/deliveries/" + shipToId)
            )
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(deliveriesDeleteResponseDTO))
            );

        var response = productsService.deleteDeliveries(shipToId);
        assertEquals(response.get().getDeletedCount(), 123);
    }
}
