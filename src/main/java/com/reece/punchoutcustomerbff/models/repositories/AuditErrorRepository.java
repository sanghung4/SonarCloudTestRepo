package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.AuditErrorDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditErrorRepository extends JpaRepository<AuditErrorDao, UUID> {

}
