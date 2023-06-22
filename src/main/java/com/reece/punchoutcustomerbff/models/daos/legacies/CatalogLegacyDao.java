package com.reece.punchoutcustomerbff.models.daos.legacies;

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

/**
 * Catalog representation in db.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@Entity
@Table(name = "catalog_legacy", schema = "public")
public class CatalogLegacyDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "id_customer")
	private String customerId;
	@Column(name = "id_product")
	private String productId;
	@Column(name = "id_branch")
	private String branchId;
}
