package com.reece.punchoutcustomerbff.models.daos;

import java.sql.Timestamp;
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
	private UUID id;
	@Column(name = "filename")
	private String fileName;
	@Column(name = "content")
	private String content;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer", referencedColumnName = "id")
	private CustomerDao customer;
	@Column(name = "upload_datetime")
	private Timestamp uploadDatetime;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", referencedColumnName = "id")
	private AuthorizedUserDao user;
}
