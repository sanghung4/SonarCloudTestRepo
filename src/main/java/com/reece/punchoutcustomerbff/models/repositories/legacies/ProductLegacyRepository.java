package com.reece.punchoutcustomerbff.models.repositories.legacies;

import com.reece.punchoutcustomerbff.models.daos.legacies.ProductLegacyDao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Connection of Product with the Database.
 */
@Repository
public interface ProductLegacyRepository extends JpaRepository<ProductLegacyDao, UUID> {
	Optional<ProductLegacyDao> findByProductId(String productId);
}
