package com.reece.punchoutcustomersync.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the auditing payload that is given to show that
 * a customer upload has completed.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuditInputDto {
  private String fileName;
  private String s3Location;
  private String customerId;
  private String catalogId;
  private String s3DateTime;
  private String syncId;
  private String ftpLocation;
  private String ftpDateTime;
  private String encodedOutputCSV;
  private List<AuditErrorDto> errors = new ArrayList<>();

}
