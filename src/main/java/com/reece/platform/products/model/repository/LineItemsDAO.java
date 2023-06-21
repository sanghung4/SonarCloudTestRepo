package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.LineItems;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemsDAO extends JpaRepository<LineItems, UUID> {
    Optional<LineItems> findByIdAndCartId(UUID id, UUID cartId);
    Optional<LineItems> findByErpPartNumberAndCartId(String partNumber, UUID cartId);
    List<LineItems> findAllByCartId(UUID cartId);
    List<LineItems> findAllByCartIdOrderByIdAsc(UUID cartId);
    long deleteAllByCartId(UUID cartId);
    void deleteByCartIdAndErpPartNumber(UUID cartId, String erpPartNumber);

    @Query(
        value = "select * from line_items l where l.cart_id = :cartId  order by price_last_updated_at",
        nativeQuery = true
    )
    List<LineItems> findAllSortedLineItem(UUID cartId);
}
