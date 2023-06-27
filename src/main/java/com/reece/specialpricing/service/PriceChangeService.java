package com.reece.specialpricing.service;

import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.UploadPriceChangesResult;
import com.reece.specialpricing.model.exception.CSVUploadException;

import java.util.List;

public interface PriceChangeService {
    UploadPriceChangesResult uploadPriceChanges(List<SpecialPriceSuggestion> changes) throws CSVUploadException;
}
