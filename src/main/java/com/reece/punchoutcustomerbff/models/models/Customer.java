package com.reece.punchoutcustomerbff.models.models;

import com.reece.punchoutcustomerbff.models.clients.customer.CustomerClientRs;
import com.reece.punchoutcustomerbff.models.daos.legacies.CustomerLegacyDao;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Customer model representation.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class Customer {
	private UUID id;
	private String customerId;
	private String branchId;
	private String erpAccountId;
	private String name;
	private String erpName;
	private Boolean isBillTo;

	/**
	 * Transform from CustomerDao to Customer.
	 *
	 * @param customerLegacyDao the Customer Data model representation.
	 * @return Customer.
	 */
	public static Customer getFromCustomerDao(CustomerLegacyDao customerLegacyDao) {
		return Customer.builder()
				.customerId(customerLegacyDao.getCustomerId())
				.name(customerLegacyDao.getName())
				.isBillTo(customerLegacyDao.getIsBillTo())
				.branchId(customerLegacyDao.getBranchId())
				.erpAccountId(customerLegacyDao.getErpAccountId())
				.erpName(customerLegacyDao.getErpName()).build();
	}

	/**
	 * Transform from customerRs to Customer.
	 *
	 * @param customerRs the Customer Data from AccountService representation.
	 * @param customerId id of customer.
	 * @return Customer.
	 */
	public static Customer getFromCustomerClientRs(CustomerClientRs customerRs,
			String customerId) {
		return Customer.builder()
				.customerId(customerId)
				.name(customerRs.getCompanyName())
				.isBillTo(customerRs.getIsBillTo())
				.branchId(customerRs.getBranchId())
				.erpAccountId(customerRs.getErpAccountId())
				.erpName(customerRs.getErpName()).build();
	}
}
