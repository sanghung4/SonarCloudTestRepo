package com.reece.punchoutcustomersync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an auditing error.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuditErrorDto {
  private String partNumber;
  private String error;
  private String errorDateTime;
}
