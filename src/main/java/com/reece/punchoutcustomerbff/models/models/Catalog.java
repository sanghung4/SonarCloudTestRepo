package com.reece.punchoutcustomerbff.models.models;

import com.reece.punchoutcustomerbff.models.daos.legacies.CatalogLegacyDao;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Catalog model representation.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Catalog {
	private UUID id;
	private String customerId;
	private String productId;
	private String branchId;

	/**
	 * From CatalogDao get Catalog.
	 *
	 * @param catalogLegacyDao Catalog of Db representation.
	 * @return Catalog.
	 */
	public static Catalog getFromCatalogDao(CatalogLegacyDao catalogLegacyDao) {
		return Catalog.builder()
				.id(catalogLegacyDao.getId())
				.customerId(catalogLegacyDao.getCustomerId())
				.productId(catalogLegacyDao.getProductId())
				.branchId(catalogLegacyDao.getBranchId()).build();
	}
}
