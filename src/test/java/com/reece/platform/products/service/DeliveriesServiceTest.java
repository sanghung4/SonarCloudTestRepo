package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reece.platform.products.model.repository.DeliveryDAO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class DeliveriesServiceTest {

    @Mock
    private DeliveryDAO deliveryDAO;

    private DeliveriesService deliveriesService;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        deliveriesService = new DeliveriesService();
        deliveriesService.deliveryDAO = deliveryDAO;
    }

    @Test
    void deliveriesDelete_happyPath() {
        when(deliveryDAO.deleteByShipToId(any())).thenReturn(5L);

        assertDoesNotThrow(() -> {
            deliveriesService.deleteDeliveries(UUID.randomUUID());
        });

        verify(deliveryDAO, times(1)).deleteByShipToId(any());
    }
}
