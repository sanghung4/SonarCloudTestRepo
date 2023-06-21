package com.reece.platform.products.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionRequestDTO {

    private String term;
    private String engine;
    private Integer size;
    private String erpSystem;
    private String state;
}
