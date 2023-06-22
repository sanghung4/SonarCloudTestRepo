package com.reece.punchoutcustomerbff.models.repositories.legacies;

import com.reece.punchoutcustomerbff.models.daos.legacies.CustomerLegacyDao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLegacyRepository extends JpaRepository<CustomerLegacyDao, UUID> {
	Optional<CustomerLegacyDao> findByCustomerId(String customerId);
}
