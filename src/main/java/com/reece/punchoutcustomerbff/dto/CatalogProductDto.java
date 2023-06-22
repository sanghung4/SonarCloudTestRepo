package com.reece.punchoutcustomerbff.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the catalog mapping to a product.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CatalogProductDto {
  private UUID id;
  private BigDecimal sellPrice;
  private BigDecimal listPrice;
  private String uom;
  private String lastPullDatetime;
  private Integer skuQuantity;
  private String partNumber;
  private ProductDto product;

}
