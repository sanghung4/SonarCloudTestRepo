package com.reece.specialpricing.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialPricingDataService extends JpaRepository<SpecialPrice, SpecialPriceId>, QuerydslPredicateExecutor<SpecialPrice> {
    List<SpecialPrice> findByCustomerIdAndProductId(String customerId, String productId);
    List<SpecialPrice> findByCustomerId(String customerId);
    List<SpecialPrice> findByProductId(String productId);
}
