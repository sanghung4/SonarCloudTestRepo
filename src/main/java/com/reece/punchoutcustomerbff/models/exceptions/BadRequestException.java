package com.reece.punchoutcustomerbff.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Exception when the request doesn't has correct information.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BadRequestException extends Exception {
  private String field;
  private String code;
  private String message;
}
