package com.reece.punchoutcustomerbff.models.daos.legacies;

import com.reece.punchoutcustomerbff.models.dtos.AuditRqDto;
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

/**
 * Audit representation in db.
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "audit_legacy", schema = "public")
public class AuditLegacyDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "file_name")
	private String name;
	@Column
	private Timestamp timestamp;
	@Column
	private String status;
	@Column(name = "s3_location")
	private String s3Location;
	@Column(name = "file_checksum")
	private String fileCheckSum;
	@Column
	private String errors;

	public static AuditLegacyDao getFromAuditRqDto(AuditRqDto auditRqDto) {
		return AuditLegacyDao.builder()
				.name(auditRqDto.getName())
				.status(auditRqDto.getStatus())
				.s3Location(auditRqDto.getS3Location())
				.fileCheckSum(auditRqDto.getFileCheckSum())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.errors(auditRqDto.getErrors()).build();
	}
}
