package com.reece.platform.eclipse.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProductSearchResultDTO {

    private String lastItem;
    private List<ProductSearchResultItemDTO> items;
}
