package com.reece.punchoutcustomerbff.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a product
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {

  private UUID id;
  private String name;
  private String description;
  private String imageFullSize;
  private String partNumber;
  private String manufacturer;
  private String categoryLevel1Name;
  private String categoryLevel2Name;
  private String categoryLevel3Name;
  private String unspsc;
  private String imageThumb;
  private String manufacturerPartNumber;
  private Integer deliveryInDays;
  private String maxSyncDatetime;

}
