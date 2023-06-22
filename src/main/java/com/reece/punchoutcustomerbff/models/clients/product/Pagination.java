package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination to follow the amount of rows in the response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
  private Integer currentPage;
  private Integer pageSize;
  private Integer totalItemCount;
}
