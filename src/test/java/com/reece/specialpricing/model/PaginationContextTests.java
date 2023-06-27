package com.reece.specialpricing.model;

import org.junit.Test;

public class PaginationContextTests {
    @Test
    public void validate_shouldReturnExceptionIfInvalidPriceSearch(){
        var context = new PaginationContext();
        context.setOrderBy("somethingWrong");
        context.setOrderDirection("asc,desc");
        var result = context.validate(true);

        assert result.size() == 2;
        assert result.get(0).getMessage().equals("Invalid parameter: 'orderBy' not in value array: ['displayName', 'manufacturer', 'branch', 'customerDisplayName', 'manufacturerReferenceNumber', 'priceLine','currentPrice','standardCost','typicalPrice','rateCardPrice','recommendedPrice']");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("orderBy");
        assert result.get(1).getMessage().equals("Invalid parameter: 'orderDirection' not in value array: ['asc', 'desc']");
        assert result.get(1).getExceptionType().equals("InvalidParameter");
        assert result.get(1).getErrorField().equals("orderDirection");
    }

    @Test
    public void validate_shouldReturnExceptionIfInvalidTypeaheadSearch(){
        var context = new PaginationContext();
        context.setOrderBy("somethingWrong");
        context.setOrderDirection("asc,desc");
        var result = context.validate(false);

        assert result.size() == 2;
        assert result.get(0).getMessage().equals("Invalid parameter: 'orderBy' not in value array: ['id', 'displayName']");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("orderBy");
        assert result.get(1).getMessage().equals("Invalid parameter: 'orderDirection' not in value array: ['asc', 'desc']");
        assert result.get(1).getExceptionType().equals("InvalidParameter");
        assert result.get(1).getErrorField().equals("orderDirection");
    }

    @Test
    public void validate_shouldSucceedIfOrderByInListWhenPriceSearch(){
        var context = new PaginationContext();
        var result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("displayName");
        result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("manufacturer");
        result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("branch");
        result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("customerDisplayName");
        result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("manufacturerReferenceNumber");
        result = context.validate(true);
        assert result.size() == 0;

        context.setOrderBy("priceLine");
        result = context.validate(true);
        assert result.size() == 0;
    }

    @Test
    public void validate_shouldSucceedIfOrderByInListWhenTypeaheadSearch(){
        var context = new PaginationContext();
        var result = context.validate(false);
        assert result.size() == 0;

        context.setOrderBy("displayName");
        result = context.validate(false);
        assert result.size() == 0;

        context.setOrderBy("id");
        result = context.validate(false);
        assert result.size() == 0;
    }

    @Test
    public void validate_shouldSucceedIfOrderDirectionInList(){
        var context = new PaginationContext();
        var result = context.validate(true);
        assert result.size() == 0;

        context.setOrderDirection("asc");
        result = context.validate(true);
        assert result.size() == 0;
        result = context.validate(false);
        assert result.size() == 0;

        context.setOrderDirection("desc");
        result = context.validate(true);
        assert result.size() == 0;
        result = context.validate(false);
        assert result.size() == 0;
    }
}
