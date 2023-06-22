package com.reece.punchoutcustomerbff.models.daos.legacies;

import com.reece.punchoutcustomerbff.models.clients.customer.CustomerClientRs;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customer_legacy", schema = "public")
public class CustomerLegacyDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "id_customer")
	private String customerId;
	@Column(name = "id_branch")
	private String branchId;
	@Column(name = "id_erp_account")
	private String erpAccountId;
	@Column(name = "name")
	private String name;
	@Column(name = "name_erp")
	private String erpName;
	@Column(name = "is_bill_to")
	private Boolean isBillTo;

	public static CustomerLegacyDao getFromCustomerClientRs(CustomerClientRs customerRs,
			String customerId) {
		return com.reece.punchoutcustomerbff.models.daos.legacies.CustomerLegacyDao.builder()
				.customerId(customerId)
				.name(customerRs.getCompanyName())
				.isBillTo(customerRs.getIsBillTo())
				.branchId(customerRs.getBranchId())
				.erpAccountId(customerRs.getErpAccountId())
				.erpName(customerRs.getErpName()).build();
	}
}
