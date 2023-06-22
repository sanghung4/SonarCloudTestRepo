package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dimensions about the product package.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageDimensions {
  private double height;
  private double length;
  private double volume;
  private String volumeUnitOfMeasure;
  private double width;
  private double weight;
  private String weightUnitOfMeasure;
}
