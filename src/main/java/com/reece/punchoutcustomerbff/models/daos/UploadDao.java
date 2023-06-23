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

/**
 * Upload representation in db.
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "upload", schema = "public")
public class UploadDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "content")
	private String content;
	@Column(name = "id_customer")
	private String customerId;
	@Column(name = "upload_datetime")
	private Timestamp uploadDatetime;
	@Column(name = "id_user")
	private UUID userId;
}
