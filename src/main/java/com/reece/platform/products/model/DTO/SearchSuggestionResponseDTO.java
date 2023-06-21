package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchSuggestionResponseDTO {

    private List<String> suggestions;
    private List<AggregationResponseItemDTO> topCategories;
    private List<ProductDTO> topProducts;

    public SearchSuggestionResponseDTO(SuggestedSearchResponseDTO response) {
        this.suggestions = response.getSuggestions();
    }
}
