package com.reece.punchoutcustomerbff.models.repositories.legacies;

import com.reece.punchoutcustomerbff.models.daos.legacies.AuditLegacyDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of Audit with the Database.
 */
@Repository
public interface AuditLegacyRepository extends JpaRepository<AuditLegacyDao, UUID> {
}
