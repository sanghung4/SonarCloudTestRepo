package com.reece.punchoutcustomerbff.models.daos;

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
 * AuthorizedUser representation in db.
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "authorized_user", schema = "public")
public class AuthorizedUserDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "email")
	private String email;
	@Column(name = "admin")
	private boolean admin;

	@Column(name = "salt")
	private String salt;

	@Column(name = "password")
	private String password;

}
