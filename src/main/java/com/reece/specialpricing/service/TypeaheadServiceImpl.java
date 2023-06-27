package com.reece.specialpricing.service;

import com.reece.specialpricing.model.DynamicSortable;
import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.postgres.CustomerDataService;
import com.reece.specialpricing.postgres.ProductDataService;
import com.reece.specialpricing.snowflake.SnowflakeCustomerDataService;
import com.reece.specialpricing.utilities.PagingUtils;
import com.reece.specialpricing.utilities.SortingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TypeaheadServiceImpl implements TypeaheadService {

    @Autowired
    private CustomerDataService customerDataService;

    @Autowired
    private ProductDataService productDataService;

    @Autowired
    private SnowflakeCustomerDataService snowflakeCustomerDataService;

    @Override
    public PagedSearchResults get(String entity, String query, PaginationContext pagingContext) {
        var results = entity.equals("customer")
                ? customerDataService.findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase(query, query)
                : productDataService.findByIdContainingIgnoreCase(query);
        var sortedResults = SortingUtils.dynamicSort(
                results
                        .stream()
                        .map(r -> (DynamicSortable) r).collect(Collectors.toList()),
                pagingContext.getOrderDirection(),
                pagingContext.getOrderBy()
        );
        return PagingUtils.getResultPage(pagingContext, sortedResults);
    }
}
