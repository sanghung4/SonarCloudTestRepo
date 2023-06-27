package com.reece.specialpricing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UploadPriceChangesResult {
    private List<String> successfulUploadPaths;
    private List<String> successfulCreatePaths;
    private List<SpecialPriceSuggestion> failedUpdateSuggestions;
    private List<SpecialPriceSuggestion> failedCreateSuggestions;
}
