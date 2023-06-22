package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Technical specifications with key value format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechSpec {
  private String name;
  private String value;
}
