package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.Cart;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDAO extends JpaRepository<Cart, UUID> {
    List<Cart> findAllByOwnerIdAndShipToId(UUID ownerId, UUID shipToId);
    Optional<List<Cart>> findAllByApproverIdAndApprovalState(UUID approverId, UUID approvalState);
    List<Cart> findAllByOwnerId(UUID ownerId);
    List<Cart> findAllByShipToId(UUID shipToAccountId);
}
