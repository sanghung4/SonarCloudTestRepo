package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.TerritoryExclusionDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of TerritoryExclusion with the Database.
 */
@Repository
public interface TerritoryExclusionRepository extends JpaRepository<TerritoryExclusionDao, UUID> {
}
