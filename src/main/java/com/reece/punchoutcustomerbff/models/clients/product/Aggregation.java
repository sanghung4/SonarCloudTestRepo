package com.reece.punchoutcustomerbff.models.clients.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represent info of aggregation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aggregation {
  private List<AggregationItem> productTypes;
  private List<AggregationItem> brands;
  private List<AggregationItem> lines;
  private List<AggregationItem> category;
  private List<AggregationItem> flowRate;
  private List<AggregationItem> environmentalOptions;
  private List<AggregationItem> inStockLocation;
  private List<AggregationItem> material;
  private List<AggregationItem> colorFinish;
  private List<AggregationItem> size;
  private List<AggregationItem> length;
  private List<AggregationItem> width;
  private List<AggregationItem> height;
  private List<AggregationItem> depth;
  private List<AggregationItem> voltage;
  private List<AggregationItem> tonnage;
  private List<AggregationItem> btu;
  private List<AggregationItem> pressureRating;
  private List<AggregationItem> temperatureRating;
  private List<AggregationItem> inletSize;
  private List<AggregationItem> capacity;
  private List<AggregationItem> wattage;
}
