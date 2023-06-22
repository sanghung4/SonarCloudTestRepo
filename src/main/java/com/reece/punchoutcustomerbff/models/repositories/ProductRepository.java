package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of Product with the Database.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductDao, UUID> {

  @Query("select distinct p from ProductDao p where p.partNumber in ?1 order by p.partNumber")
  List<ProductDao> findByPartNumbers(List<String> partNumbers);

}
