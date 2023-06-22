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
 * ProcurementSystem representation in db.
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "procurement_system", schema = "public")
public class ProcurementSystemDao {
  @Id
  @Column(name = "name")
  private String name;
}
