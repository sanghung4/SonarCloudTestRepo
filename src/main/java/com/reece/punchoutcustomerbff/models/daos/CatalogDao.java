package com.reece.punchoutcustomerbff.models.daos;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Table(name = "catalog", schema = "public")
public class CatalogDao {
	@Id
	private UUID id;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer", referencedColumnName = "id")
	private CustomerDao customer;
	@Column(name = "status")
	private String status;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "name")
	private String name;
	@Column(name = "last_update")
	private Timestamp lastUpdate;
	@Column(name = "date_archived")
	private Timestamp dateArchived;
	@Column(name = "proc_system")
	private String procSystem;

	@OneToMany(mappedBy="catalog", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<CatalogProductDao> mappings;

}
