package com.reece.punchoutcustomerbff.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Exception when try to create Audit.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class AuditCreateException extends Exception {
  private String field;
  private String code;
  private String message;
}
