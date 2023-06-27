package com.reece.specialpricing.postgres;

import org.junit.Test;

public class ProductTests {
    @Test
    public void getComparisonField_shouldReturnCorrectFieldValues(){
        var product = new Product("value1", "value2");

        assert product.getComparisonField("id").equals("value1");
        assert product.getComparisonField("displayName").equals("value2");
        assert product.getComparisonField("anythingElse").equals("value2");
    }
}
