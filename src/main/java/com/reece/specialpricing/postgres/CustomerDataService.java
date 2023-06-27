package com.reece.specialpricing.postgres;

import com.reece.specialpricing.postgres.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDataService extends JpaRepository<Customer, String>, QuerydslPredicateExecutor<Customer> {
    List<Customer> findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase(String displayName, String id);
}
