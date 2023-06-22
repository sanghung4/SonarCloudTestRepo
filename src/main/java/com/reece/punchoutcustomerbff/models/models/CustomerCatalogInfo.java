package com.reece.punchoutcustomerbff.models.models;

import com.reece.punchoutcustomerbff.models.daos.legacies.CatalogLegacyDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Info about customer in the catalogs.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCatalogInfo {
	private String customerId;
	private String branchId;

	public static CustomerCatalogInfo getFromCustomerCatalogDao(CatalogLegacyDao catalog) {
		return CustomerCatalogInfo.builder()
				.customerId(catalog.getCustomerId())
				.branchId(catalog.getBranchId()).build();
	}
}
