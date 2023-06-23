package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of CatalogNew with the Database.
 */
@Repository
public interface CatalogRepository extends JpaRepository<CatalogDao, UUID> {

  /**
   * Returns all catalogs for the given customer that are not in a state of ARCHIVED,
   * ordered by date from newest to oldest.
   * @param customerId The ID of the customer.
   * @return List<CatalogDao> A list of catalog entities.
   */
  @Query("select distinct c from CatalogDao c where c.customer.id = ?1"
      + " and c.status != 'ARCHIVED' order by c.lastUpdate ASC")
  List<CatalogDao> retrieveSyncEligibleCatalogs(UUID customerId);

  /**
   * Returns the catalog of the given ID along with all of its product mappings.
   * @param catalogId The ID of the catalog
   * @return List<CatalogDao> A list of catalog entities.
   */
  @Query("select distinct c from CatalogDao c"
    + " left join fetch c.mappings m where c.id = ?1 order by m.partNumber")
  List<CatalogDao> retrieveWithMappings(UUID catalogId);

}
