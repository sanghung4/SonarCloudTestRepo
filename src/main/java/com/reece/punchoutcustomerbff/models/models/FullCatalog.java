package com.reece.punchoutcustomerbff.models.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * FullCatalog model representation.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class FullCatalog {
  private Customer account;
  private String branchId;
  private List<Product> products;
}
