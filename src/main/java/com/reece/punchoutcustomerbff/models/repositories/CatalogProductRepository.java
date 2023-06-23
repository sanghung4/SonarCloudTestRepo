package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Connection of CatalogProduct with the Database.
 */
@Repository
public interface CatalogProductRepository extends JpaRepository<CatalogProductDao, UUID> {

  @Query("select distinct m from CatalogProductDao m where m.catalog.id = ?1")
  List<CatalogProductDao> findAllByCatalogId(UUID catalogId);


  @Query("select distinct m from CatalogProductDao m " +
          "left join fetch m.product p " +
          "where m.catalog.id = ?1 and " +
            "(m.lastPullDatetime is null " +
            "or m.lastPullDatetime < ?2 " +
            "or p.maxSyncDatetime is null " +
            "or p.maxSyncDatetime < ?2)"
  )
  List<CatalogProductDao> findAllByCatalogIdWithLastPullDateBefore(UUID catalogId, Timestamp yesterday);
}
