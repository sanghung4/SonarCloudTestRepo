package com.reece.punchoutcustomerbff.models.models;

import com.reece.punchoutcustomerbff.models.daos.legacies.AuditLegacyDao;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Audit model representation.
 */
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Audit {
	private UUID id;
	private String name;
	private Timestamp timestamp;
	private String status;
	private String s3Location;
	private String fileCheckSum;
	private String errors;

	/**
	 * From AuditDat get Audit.
	 *
	 * @param AuditLegacyDao Audit of db representation.
	 * @return Audit.
	 */
	public static Audit getFromAuditDao(AuditLegacyDao auditNewDao) {
		return Audit.builder()
				.id(auditNewDao.getId())
				.name(auditNewDao.getName())
				.timestamp(auditNewDao.getTimestamp())
				.status(auditNewDao.getStatus())
				.s3Location(auditNewDao.getS3Location())
				.fileCheckSum(auditNewDao.getFileCheckSum())
				.errors(auditNewDao.getErrors()).build();
	}
}
