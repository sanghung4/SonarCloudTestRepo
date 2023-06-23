package com.reece.punchoutcustomerbff.models.daos;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Customer representation in db.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customer", schema = "public")
public class CustomerDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "id_customer")
	private String customerId;
	@Column(name = "id_branch")
	private String branchId;
	@Column(name = "branch_name")
	private String branchName;
	@Column(name = "id_erp")
	private String erpId;
	@Column(name = "name")
	private String name;
	@Column(name = "name_erp")
	private String erpName;
	@Column(name = "is_bill_to")
	private Boolean isBillTo;
	@Column(name = "last_update")
	private Timestamp lastUpdate;
	@Column(name = "contact_name")
	private String contactName;
	@Column(name = "contact_phone")
	private String contactPhone;

	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<CustomerRegionDao> regions;

	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<CatalogDao> catalogs;

	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<AuditDao> audits;

}
