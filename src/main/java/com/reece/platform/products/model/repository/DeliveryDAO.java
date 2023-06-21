package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDAO extends JpaRepository<Delivery, UUID> {
    long deleteByShipToId(UUID shipToId);
}
