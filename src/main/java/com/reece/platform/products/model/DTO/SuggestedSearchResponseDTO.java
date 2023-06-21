package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class SuggestedSearchResponseDTO {

    private List<String> suggestions;
    private String suggestedProductSearchResponse;
}
