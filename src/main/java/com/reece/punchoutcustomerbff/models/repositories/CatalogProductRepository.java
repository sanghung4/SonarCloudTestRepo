package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of CatalogProduct with the Database.
 */
@Repository
public interface CatalogProductRepository extends JpaRepository<CatalogProductDao, UUID> {
    @Query("select distinct c from CatalogProductDao c" + " where c.catalog.id = ?1")
    Page<CatalogProductDao> findWithProducts(UUID catalogId, Pageable pageable);

    @Query("select distinct c from CatalogProductDao c" + " where c.catalog.id = ?1")
    List<CatalogProductDao> findWithProducts(UUID catalogId);
}
