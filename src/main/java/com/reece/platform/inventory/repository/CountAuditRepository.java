package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.CountAudit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountAuditRepository extends JpaRepository<CountAudit, UUID> {}
