package com.reece.specialpricing.model.pojo;

import org.junit.Test;

public class SpecialPriceSearchRequestTests {
    @Test
    public void validate_shouldSucceed(){
        var request = new SpecialPriceSearchRequest();
        request.setCustomerId("1");
        request.setProductId("1");

        assert request.validate().size() == 0;

        request.setCustomerId(null);
        request.setProductId("1");

        assert request.validate().size() == 0;

        request.setCustomerId("1");
        request.setProductId(null);

        assert request.validate().size() == 0;
    }

    @Test
    public void validate_shouldFail(){
        var request = new SpecialPriceSearchRequest();

        var result = request.validate();
        assert result.size() == 1;
        assert result.get(0).getMessage().equals("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("customerId or productId");

        request.setCustomerId("      ");
        result = request.validate();
        assert result.size() == 1;
        assert result.get(0).getMessage().equals("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("customerId or productId");

        request.setCustomerId(null);
        request.setProductId("      ");
        result = request.validate();
        assert result.size() == 1;
        assert result.get(0).getMessage().equals("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("customerId or productId");

        request.setCustomerId("     ");
        request.setProductId("      ");
        result = request.validate();
        assert result.size() == 1;
        assert result.get(0).getMessage().equals("Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid");
        assert result.get(0).getExceptionType().equals("InvalidParameter");
        assert result.get(0).getErrorField().equals("customerId or productId");
    }

    @Test
    public void cleanUserInputData_shouldTrimCustomerIdAndProductId(){
        var request = new SpecialPriceSearchRequest("    1     ", "     2     ", "     3     ");

        request.cleanUserInputData();

        assert request.getCustomerId().equals("1");
        assert request.getProductId().equals("2");
        assert request.getPriceLine().equals("3");
    }

    @Test
    public void cleanUserInputData_shouldTrimProductIdWhenCustomerIdNull(){
        var request = new SpecialPriceSearchRequest(null, "     2     ", null);

        request.cleanUserInputData();

        assert request.getCustomerId() == null;
        assert request.getProductId().equals("2");
    }

    @Test
    public void cleanUserInputData_shouldTrimCustomerIdWhenProductIdNull(){
        var request = new SpecialPriceSearchRequest("    1     ", null, null);

        request.cleanUserInputData();

        assert request.getCustomerId().equals("1");
        assert request.getProductId() == null;
    }
}
