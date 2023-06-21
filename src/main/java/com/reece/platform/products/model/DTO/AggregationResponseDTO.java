package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class AggregationResponseDTO {

    private List<AggregationResponseItemDTO> productTypes;
    private List<AggregationResponseItemDTO> brands;
    private List<AggregationResponseItemDTO> lines;
    private List<AggregationResponseItemDTO> category;
    private List<AggregationResponseItemDTO> flowRate;
    private List<AggregationResponseItemDTO> environmentalOptions;
    private List<AggregationResponseItemDTO> inStockLocation;
    private List<AggregationResponseItemDTO> material;
    private List<AggregationResponseItemDTO> colorFinish;
    private List<AggregationResponseItemDTO> size;
    private List<AggregationResponseItemDTO> length;
    private List<AggregationResponseItemDTO> width;
    private List<AggregationResponseItemDTO> height;
    private List<AggregationResponseItemDTO> depth;
    private List<AggregationResponseItemDTO> voltage;
    private List<AggregationResponseItemDTO> tonnage;
    private List<AggregationResponseItemDTO> btu;
    private List<AggregationResponseItemDTO> pressureRating;
    private List<AggregationResponseItemDTO> temperatureRating;
    private List<AggregationResponseItemDTO> inletSize;
    private List<AggregationResponseItemDTO> capacity;
    private List<AggregationResponseItemDTO> wattage;
}
