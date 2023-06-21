package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.OrderStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusDAO extends JpaRepository<OrderStatus, UUID> {}
