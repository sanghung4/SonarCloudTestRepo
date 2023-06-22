package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of counts of values.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationItem {
  private String value;
  private Integer count;
}
