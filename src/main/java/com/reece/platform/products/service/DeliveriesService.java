package com.reece.platform.products.service;

import com.reece.platform.products.model.DTO.DeliveriesDeleteResponseDTO;
import com.reece.platform.products.model.repository.DeliveryDAO;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DeliveriesService {

    @Autowired
    public DeliveryDAO deliveryDAO;

    @Transactional
    public DeliveriesDeleteResponseDTO deleteDeliveries(UUID shipToAccountId) {
        return new DeliveriesDeleteResponseDTO(deliveryDAO.deleteByShipToId(shipToAccountId), true);
    }
}
