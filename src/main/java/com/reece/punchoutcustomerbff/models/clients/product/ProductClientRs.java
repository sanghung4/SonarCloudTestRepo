package com.reece.punchoutcustomerbff.models.clients.product;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that describe the each product from ProductService.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductClientRs {
  private String id;
  private String partNumber;
  private String name;
  private List<String> taxonomy;
  private List<String> categories;
  private String manufacturerName;
  private String manufacturerNumber;
  private String productType;
  private float price;
  private String cmp;
  private List<TechDoc> technicalDocuments;
  private List<String> environmentalOptions;
  private String upc;
  private String unspsc;
  private String seriesModelFigureNumber;
  private String productOverview;
  private String featuresAndBenefits;
  private List<TechSpec> techSpecifications;
  private ImageUrls imageUrls;
  private PackageDimensions packageDimensions;
  private String erp;
  private int minIncrementQty;
  private Map<String, String> productSearchBoost;
  private String lastUpdateDate;
  private String productSoldCount;
  private String searchKeywordText;
  private List<String> customerNumber;
  private List<String> customerPartNumber;
  private List<CustomerPartNumber> customerPartNumbers;
  private List<String> inStockLocation;
  private List<String> productBranchExclusion;
  private String status;
  private List<String> territoryExclusionList;
}
