package com.reece.punchoutcustomerbff.models.daos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CatalogStatus representation in db.
 */
@ToString
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "catalog_status", schema = "public")
public class CatalogStatusDao {

  @Id
  @Column(name = "id")
  private String id;
}
