package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.OrdersPendingApproval;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersPendingApprovalDAO extends JpaRepository<OrdersPendingApproval, UUID> {
    Optional<OrdersPendingApproval> findByCartId(UUID cartId);
    void deleteAllByCartId(UUID cartId);
    Optional<OrdersPendingApproval> findByOrderId(String orderId);
}
