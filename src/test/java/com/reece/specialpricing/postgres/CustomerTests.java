package com.reece.specialpricing.postgres;

import org.junit.Test;

public class CustomerTests {
    @Test
    public void getComparisonField_shouldReturnCorrectFieldValues(){
        var customer = new Customer("value1", "value2");

        assert customer.getComparisonField("id").equals("value1");
        assert customer.getComparisonField("displayName").equals("value2");
        assert customer.getComparisonField("anythingElse").equals("value2");
    }
}
