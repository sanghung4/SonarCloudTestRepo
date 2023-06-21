package com.reece.platform.products.controller;

import com.reece.platform.products.model.DTO.SearchSuggestionResponseDTO;
import com.reece.platform.products.search.SearchService;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("search-suggestions")
@RequiredArgsConstructor
public class SearchSuggestionsController {

    private final SearchService searchService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchSuggestionResponseDTO getSuggestedSearch(
        @RequestParam(defaultValue = "") @NotBlank String searchTerm,
        @RequestParam(defaultValue = "") @NotBlank String engine,
        @RequestParam(defaultValue = "7") @Min(0) @Max(20) int size,
        @RequestParam(required = false) String state
    ) {
        return searchService.getSuggestedSearch(searchTerm, engine, size, state);
    }
}
