package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of Audit with the Database.
 */
@Repository
public interface AuthorizedUserRepository extends JpaRepository<AuthorizedUserDao, UUID> {

  @Query("select distinct u from AuthorizedUserDao u where u.email = ?1")
  List<AuthorizedUserDao> findByEmail(String email);

}
