package com.reece.punchoutcustomersync.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a record in the output CSV.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OutputCsvRecord {

  private String productName;
  private String productDescription;
  private String imageFullsize;
  private String imageThumb;
  private BigDecimal productPrice;
  private BigDecimal listPrice;
  private String partNumber;
  private String unitOfMeasure;
  private String manufacturer;
  private String manufacturerPartNumber;
  private String categoryLevel1Name;
  private String categoryLevel2Name;
  private String categoryLevel3Name;
  private String unspsc;
  private Integer deliveryInDays;
  private String buyerId;

}
