package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.MetricsLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface MetricsRepository extends JpaRepository<MetricsLogin, UUID>, QuerydslPredicateExecutor<MetricsLogin>, MetricsCustomRepository {

}
