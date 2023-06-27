package com.reece.specialpricing.utilities;

import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.model.PaginationContext;
import org.javatuples.Pair;

import java.util.List;

public class PagingUtils {
    public static int getNumberOfPages(int maxPageCount, int listSize){
        if(listSize == 0){
            return 1;
        }
        return (int) Math.ceil((double) listSize / maxPageCount);
    }

    public static Pair<Integer, Integer> getPageIndexRange(int pageNumber, int maxPageCount, int listSize){
        var startIndex = maxPageCount * (pageNumber - 1);
        if(startIndex > listSize - 1){
            startIndex = listSize - 1;
        }

        var endIndex = startIndex + maxPageCount - 1;
        if(endIndex > listSize - 1){
            endIndex = listSize - 1;
        }

        return Pair.with(startIndex, endIndex);
    }

    public static PagedSearchResults getResultPage(PaginationContext pagingData, List<Object> results){
        var pageCount = PagingUtils.getNumberOfPages(pagingData.getPageSize(), results.size());
        if(pagingData.getPage() > pageCount){
            pagingData.setPage(pageCount);
        }
        var nextPage = pagingData.getPage() == pageCount ? null : pagingData.getPage() + 1;
        var pageIndexes = PagingUtils.getPageIndexRange(pagingData.getPage(), pagingData.getPageSize(), results.size());
        var resultPage = results.size() == 0 ? List.of() : results.subList(pageIndexes.getValue0(), pageIndexes.getValue1() + 1);
        return new PagedSearchResults(pageCount, results.size(), nextPage, resultPage);
    }
}
