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
 * Audit representation in db.
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "audit", schema = "public")
public class AuditDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "s3_location")
	private String s3Location;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer", referencedColumnName = "id")
	private CustomerDao customer;
	@Column(name = "upload_datetime")
	private Timestamp uploadDatetime;
	@Column(name = "sftp_location")
	private String ftpLocation;
	@Column(name = "s3_date_time")
	private Timestamp s3DateTime;
	@Column(name = "sftp_date_time")
	private Timestamp ftpDateTime;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sync", referencedColumnName = "id")
	private SyncLogDao sync;

	@OneToMany(mappedBy="audit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<AuditErrorDao> errors;
}
