package com.reece.specialpricing.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDataService extends JpaRepository<Product, String>, QuerydslPredicateExecutor<Product> {
    List<Product> findByIdContainingIgnoreCase(String id);
}
