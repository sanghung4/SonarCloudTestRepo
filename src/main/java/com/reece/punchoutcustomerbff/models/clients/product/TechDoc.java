package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Technical Documentation with key value format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechDoc {
  private String name;
  private String url;
}
