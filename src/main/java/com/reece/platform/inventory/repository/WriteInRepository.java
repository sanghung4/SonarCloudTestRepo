package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.WriteIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WriteInRepository extends JpaRepository<WriteIn, UUID>, QuerydslPredicateExecutor<WriteIn>, WriteInCustomRepository {
}
