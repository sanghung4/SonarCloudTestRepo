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

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "audit_error", schema = "public")
public class AuditErrorDao {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_id", referencedColumnName = "id")
  private AuditDao audit;

  @Column(name = "part_number")
  private String partNumber;

  @Column(name = "error")
  private String error;

  @Column(name = "error_date_time")
  private Timestamp errorDateTime;

}
