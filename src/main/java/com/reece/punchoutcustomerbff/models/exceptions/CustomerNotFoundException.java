package com.reece.punchoutcustomerbff.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Exception when the customer is not found.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class CustomerNotFoundException extends Exception {
  private String code;
  private String message;
}
