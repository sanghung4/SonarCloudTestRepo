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
 * SyncLog representation in db.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@Entity
@Table(name = "sync_log", schema = "public")
public class SyncLogDao {
	@Id
	private UUID id;
	@Column(name = "start_datetime")
	private Timestamp startDatetime;
	@Column(name = "end_datetime")
	private Timestamp endDatetime;
	@Column(name = "status")
	private String status;

	@OneToMany(mappedBy="sync", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<AuditDao> audits;

}
