package com.reece.punchoutcustomerbff.models.repositories.legacies;

import com.reece.punchoutcustomerbff.models.daos.legacies.CatalogLegacyDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of Catalog with the Database Last.
 */
@Repository
public interface CatalogLegacyRepository extends JpaRepository<CatalogLegacyDao, UUID> {
	List<CatalogLegacyDao> findAllByCustomerIdAndBranchId(String customerId, String branchId);

	Boolean existsByCustomerIdAndBranchIdAndProductId(String customerId, String branchId,
			String productId);

	@Query(value = "SELECT gen_random_uuid() as id, cl.id_customer, '' as id_product, cl.id_branch FROM catalog_legacy cl GROUP BY cl.id_customer, cl.id_branch;", nativeQuery = true)
	List<CatalogLegacyDao> findAllCustomersByBranch();
}
