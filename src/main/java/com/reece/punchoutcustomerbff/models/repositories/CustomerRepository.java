package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CustomerDao;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of Customer with the Database.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerDao, UUID> {
  @Query("select distinct c from CustomerDao c"
      + " left join fetch c.regions"
      + " left join fetch c.catalogs cat"
      + " where cat.status != 'ARCHIVED'"
      + " order by c.name"
  )
  List<CustomerDao> retrieveAllCustomersWithCatalogs();

  @Query("select distinct c from CustomerDao c"
          + " where c.lastUpdate > ?1"
          + " order by c.name"
  )
  List<CustomerDao> retrieveAllCustomersUpdatedSince(Timestamp updatedSince);
}
