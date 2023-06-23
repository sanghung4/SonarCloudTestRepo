package com.reece.punchoutcustomersync.dto;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.models.daos.AuditErrorDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for the Audit record
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuditDto {
  private UUID id;
  private String fileName;
  private String s3Location;
  private CustomerDto customer;
  private String uploadDatetime;
  private String ftpLocation;
  private String s3DateTime;
  private String ftpDateTime;
  private List<AuditErrorDto> errors;
}
