package com.reece.punchoutcustomerbff.models.clients.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Part Number for each customer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPartNumber {
  private String customer;
  private List<String> partNumbers;
}
