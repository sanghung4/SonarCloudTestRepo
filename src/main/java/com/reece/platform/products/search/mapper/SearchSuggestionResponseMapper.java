package com.reece.platform.products.search.mapper;

import com.reece.platform.products.external.appsearch.model.QuerySuggestonResponse;
import com.reece.platform.products.model.DTO.SearchSuggestionResponseDTO;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class SearchSuggestionResponseMapper {

    public SearchSuggestionResponseDTO querySuggestionResponseToSearchSuggestionResponseDTO(
        QuerySuggestonResponse response
    ) {
        val searchSuggestionResponse = new SearchSuggestionResponseDTO();
        val suggestions = response
            .getResults()
            .getDocuments()
            .stream()
            .map(QuerySuggestonResponse.Results.Document::getSuggestion)
            .collect(Collectors.toList());
        searchSuggestionResponse.setSuggestions(suggestions);

        return searchSuggestionResponse;
    }
}
