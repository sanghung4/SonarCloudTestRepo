package com.reece.specialpricing.service;

import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.model.PaginationContext;

public interface TypeaheadService {
    PagedSearchResults get(String entity, String query, PaginationContext pagingContext);
}
