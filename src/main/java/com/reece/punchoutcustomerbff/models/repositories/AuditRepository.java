package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.AuditDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of AuditNew with the Database.
 */
@Repository
public interface AuditRepository extends JpaRepository<AuditDao, UUID> {
}
