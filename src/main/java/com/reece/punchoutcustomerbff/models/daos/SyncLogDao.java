package com.reece.punchoutcustomerbff.models.daos;

import java.sql.Timestamp;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "start_datetime")
	private Timestamp startDatetime;
	@Column(name = "end_datetime")
	private Timestamp endDatetime;
	@Column(name = "status")
	private String status;
}
