package com.reece.specialpricing.postgres;

import org.junit.Test;

public class SpecialPriceTests {
    @Test
    public void getComparisonField_shouldReturnCorrectFieldValues(){
        var specialPrice = new SpecialPrice();
        specialPrice.setDisplayName("value1");
        specialPrice.setManufacturer("value2");
        specialPrice.setBranch("value3");
        specialPrice.setCustomerDisplayName("value4");
        specialPrice.setManufacturerReferenceNumber("value5");
        specialPrice.setPriceLine("value6");
        specialPrice.setProductId("value7");

        assert specialPrice.getComparisonField("displayName").equals("value1");
        assert specialPrice.getComparisonField("manufacturer").equals("value2");
        assert specialPrice.getComparisonField("branch").equals("value3");
        assert specialPrice.getComparisonField("customerDisplayName").equals("value4");
        assert specialPrice.getComparisonField("manufacturerReferenceNumber").equals("value5");
        assert specialPrice.getComparisonField("priceLine").equals("value6");
        assert specialPrice.getComparisonField("somethingElse").equals("value7");
    }
}
