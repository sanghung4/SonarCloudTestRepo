package com.reece.specialpricing.model.pojo;

import org.junit.Test;

public class TypeaheadSearchRequestTests {
    @Test
    public void validate_shouldSucceed(){
        var request = new TypeaheadSearchRequest();
        request.setEntity("customer");

        assert request.validate().size() == 0;

        request.setEntity("product");

        assert request.validate().size() == 0;
    }

    @Test
    public void validate_shouldFail(){
        var request = new TypeaheadSearchRequest();
        request.setEntity("customer,product");

        var result = request.validate();
        assert result.size() == 1;
        assert result.get(0).getMessage().equals("Invalid parameter: 'entity' not in value array: ['customer', 'product']");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("entity");
    }

    @Test
    public void cleanUserInputData_shouldTrimQuery(){
        var request = new TypeaheadSearchRequest("    1     ", "     2     ");

        request.cleanUserInputData();

        assert request.getEntity().equals("    1     ");
        assert request.getQuery().equals("2");
    }
}
