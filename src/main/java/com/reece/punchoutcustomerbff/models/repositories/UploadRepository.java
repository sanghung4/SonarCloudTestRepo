package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of Upload with the Database.
 */
@Repository
public interface UploadRepository extends JpaRepository<UploadDao, UUID> {

}
