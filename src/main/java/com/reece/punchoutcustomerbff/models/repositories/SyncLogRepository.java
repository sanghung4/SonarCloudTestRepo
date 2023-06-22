package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of SyncLog with the Database.
 */
@Repository
public interface SyncLogRepository extends JpaRepository<SyncLogDao, UUID> {
}
