package com.reece.platform.mincron.model;

import lombok.Value;

import java.util.List;

@Value
public class ProductSearchResultDTO {
    String lastItem;
    List<ProductSearchResultItemDTO> items;
}
