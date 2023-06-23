package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of SyncLog with the Database.
 */
@Repository
public interface SyncLogRepository extends JpaRepository<SyncLogDao, UUID> {
    @Query(value="SELECT s.id, s.start_datetime, s.end_datetime, s.status " +
            "FROM sync_log s " +
            "WHERE s.status != 'FAILED' " +
            "ORDER BY s.start_datetime DESC " +
            "LIMIT 1", nativeQuery = true)
    SyncLogDao getMostRecentSyncLog();
}
