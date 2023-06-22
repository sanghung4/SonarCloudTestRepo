package com.reece.punchoutcustomerbff.models.clients.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class that describe the products response from ProductService.
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRs {
  private Pagination pagination;
  private Aggregation aggregates;
  private List<ProductClientRs> products;
  private List<ProductSearchFilter> selectedAttributes;
  private List<ProductSearchFilter> selectedCategories;
  private Integer categoryLevel;
}
