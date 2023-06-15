package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.Branch;
import com.reece.platform.inventory.model.ERPSystemName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID>, QuerydslPredicateExecutor<Branch> {
    Optional<Branch> findByErpSystemAndErpBranchNum(ERPSystemName erpSystem, String erpBranchNum);
}
