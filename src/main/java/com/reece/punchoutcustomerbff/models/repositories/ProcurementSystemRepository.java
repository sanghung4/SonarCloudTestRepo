package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of ProcurementSystem with the Database.
 */
@Repository
public interface ProcurementSystemRepository extends JpaRepository<ProcurementSystemDao, UUID> {
}
