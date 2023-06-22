package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filters about products with key value format.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchFilter {
  private String attributeType;
  private String attributeValue;
}
