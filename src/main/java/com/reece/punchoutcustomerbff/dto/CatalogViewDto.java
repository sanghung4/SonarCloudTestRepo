package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used for listing the product mappings within a catalog.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CatalogViewDto {

  private String catalogId;
  private Integer page;
  private Integer totalPages;
  private long totalItems;
  private Integer resultsPerPage;
  private CatalogDto catalog;
  private CustomerDto customer;
  private List<CatalogProductDto> results = new ArrayList<>();
}
