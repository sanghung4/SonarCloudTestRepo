package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.UploadPriceChangesResult;
import lombok.Data;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SpecialPriceChangeResponse {
    private List<SpecialPriceChangeSuccessfulUpload> successfulUploads;
    private List<SpecialPriceChangeSuccessfulUpload> successfulCreates;
    private List<SpecialPriceSuggestion> failedUpdateSuggestions;
    private List<SpecialPriceSuggestion> failedCreateSuggestions;
    public SpecialPriceChangeResponse(UploadPriceChangesResult uploadResults){
        this.failedUpdateSuggestions = uploadResults.getFailedUpdateSuggestions();
        this.failedCreateSuggestions = uploadResults.getFailedCreateSuggestions();
        this.successfulUploads = uploadResults
                .getSuccessfulUploadPaths()
                .stream()
                .map(upload -> new SpecialPriceChangeSuccessfulUpload(upload, Paths.get(upload).getFileName().toString()))
                .collect(Collectors.toList());
        this.successfulCreates = uploadResults
                .getSuccessfulCreatePaths()
                .stream()
                .map(upload -> new SpecialPriceChangeSuccessfulUpload(upload, Paths.get(upload).getFileName().toString()))
                .collect(Collectors.toList());
    }
}
