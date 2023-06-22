package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of CustomerRegion with the Database.
 */
@Repository
public interface CustomerRegionRepository extends JpaRepository<CustomerRegionDao, UUID> {
}
