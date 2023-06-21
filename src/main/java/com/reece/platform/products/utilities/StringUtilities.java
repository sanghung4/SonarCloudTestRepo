package com.reece.platform.products.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtilities {

    public String trimString(String string) {
        if (string == null) {
            return null;
        }

        return string.trim();
    }

    public String formatProductNumber(String productNumber) {
        String matcher = "(MSC-?\s*)";
        return productNumber.replaceAll(matcher, "");
    }
}
