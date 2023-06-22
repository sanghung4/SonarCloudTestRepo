package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CatalogStatusDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of CatalogStatus with the Database.
 */
@Repository
public interface CatalogStatusRepository extends JpaRepository<CatalogStatusDao, UUID> {
}
