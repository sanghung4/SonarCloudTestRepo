package com.reece.platform.products.insite.repository;

import com.reece.platform.products.insite.entity.WishList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListDAO extends JpaRepository<WishList, UUID> {
    List<WishList> findAllByCustomerErpAccountId(String erpAccountId);
}
