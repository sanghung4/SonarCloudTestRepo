package com.reece.specialpricing.service;

import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.model.UploadPriceChangesResult;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.model.pojo.SpecialPriceChangeRequest;

import javax.validation.Valid;
import java.util.Set;

public interface SpecialPricingService {
    UploadPriceChangesResult createAndUpdatePrices(@Valid SpecialPriceChangeRequest changes) throws CSVUploadException;
    PagedSearchResults getPrices(String customerId, String productId, String priceLine, PaginationContext pagingData);
    Set<String> getPriceLines(String customerId, String productId);
}
