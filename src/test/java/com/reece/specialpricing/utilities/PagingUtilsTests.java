package com.reece.specialpricing.utilities;

import com.reece.specialpricing.model.PaginationContext;
import org.javatuples.Pair;
import org.junit.Test;

import java.util.List;

public class PagingUtilsTests {
    @Test
    public void getNumberOfPages_shouldReturnCorrectValue(){
        assert PagingUtils.getNumberOfPages(100, 956) == 10;

        assert PagingUtils.getNumberOfPages(10, 1) == 1;

        assert PagingUtils.getNumberOfPages(10, 10) == 1;

        assert PagingUtils.getNumberOfPages(9, 10) == 2;

        assert PagingUtils.getNumberOfPages(11, 10) == 1;
    }

    @Test
    public void getNumberOfPages_shouldReturnOnePageEvenIfThereAreNoResults(){
        assert PagingUtils.getNumberOfPages(100, 0) == 1;
    }

    @Test
    public void getPageIndexRange_shouldReturnCorrectIndices(){
        assert PagingUtils.getPageIndexRange(1, 7, 13).equals(Pair.with(0, 6));
        assert PagingUtils.getPageIndexRange(1, 7, 5).equals(Pair.with(0, 4));

        assert PagingUtils.getPageIndexRange(2, 7, 45).equals(Pair.with(7, 13));
        assert PagingUtils.getPageIndexRange(2, 7, 10).equals(Pair.with(7, 9));

        assert PagingUtils.getPageIndexRange(2, 7, 4).equals(Pair.with(3, 3));
    }

    @Test
    public void getResultPage_shouldReturnAppropriatelySizedPageAtStartOfList(){
        List<Object> resultList = List.of("1", "2", "3", "4", "5", "6", "7");
        var pagingContext = new PaginationContext(1, 3, "doesntMatter", "doesntMatter");

        var methodResult = PagingUtils.getResultPage(pagingContext, resultList);
        assert methodResult.getNextPage() == 2;
        assert methodResult.getPageCount() == 3;
        assert methodResult.getResultCount() == resultList.size();
        assert methodResult.getResults().size() == 3;
        assert methodResult.getResults().get(0).equals("1");
        assert methodResult.getResults().get(1).equals("2");
        assert methodResult.getResults().get(2).equals("3");
    }

    @Test
    public void getResultPage_shouldReturnAppropriatelySizedPageInMiddleOfList(){
        List<Object> resultList = List.of("1", "2", "3", "4", "5", "6", "7");
        var pagingContext = new PaginationContext(2, 3, "doesntMatter", "doesntMatter");

        var methodResult = PagingUtils.getResultPage(pagingContext, resultList);
        assert methodResult.getNextPage() == 3;
        assert methodResult.getPageCount() == 3;
        assert methodResult.getResultCount() == resultList.size();
        assert methodResult.getResults().size() == 3;
        assert methodResult.getResults().get(0).equals("4");
        assert methodResult.getResults().get(1).equals("5");
        assert methodResult.getResults().get(2).equals("6");
    }

    @Test
    public void getResultPage_shouldReturnAppropriatelySizedPageAtEndOfList(){
        List<Object> resultList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
        var pagingContext = new PaginationContext(3, 3, "doesntMatter", "doesntMatter");

        var methodResult = PagingUtils.getResultPage(pagingContext, resultList);
        assert methodResult.getNextPage() == null;
        assert methodResult.getPageCount() == 3;
        assert methodResult.getResultCount() == resultList.size();
        assert methodResult.getResults().size() == 3;
        assert methodResult.getResults().get(0).equals("7");
        assert methodResult.getResults().get(1).equals("8");
        assert methodResult.getResults().get(2).equals("9");
    }

    @Test
    public void getResultPage_shouldReturnTruncatedPageAtTheEndOfTheList(){
        List<Object> resultList = List.of("1", "2", "3", "4", "5", "6", "7");
        var pagingContext = new PaginationContext(3, 3, "doesntMatter", "doesntMatter");

        var methodResult = PagingUtils.getResultPage(pagingContext, resultList);
        assert methodResult.getNextPage() == null;
        assert methodResult.getPageCount() == 3;
        assert methodResult.getResultCount() == resultList.size();
        assert methodResult.getResults().size() == 1;
        assert methodResult.getResults().get(0).equals("7");
    }

    @Test
    public void getResultPage_shouldGetLastPageIfPageNumberGreaterThanPageCount(){
        List<Object> resultList = List.of("1", "2", "3", "4", "5", "6", "7");
        var pagingContext = new PaginationContext(10, 3, "doesntMatter", "doesntMatter");

        var methodResult = PagingUtils.getResultPage(pagingContext, resultList);
        assert methodResult.getNextPage() == null;
        assert methodResult.getPageCount() == 3;
        assert methodResult.getResultCount() == resultList.size();
        assert methodResult.getResults().size() == 1;
        assert methodResult.getResults().get(0).equals("7");
    }
}
