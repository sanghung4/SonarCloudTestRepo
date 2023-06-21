package com.reece.platform.products.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilitiesTest {

    @Test
    void trimString_success() {
        String input = " TEST ";
        assertEquals(StringUtilities.trimString(input), "TEST");
    }

    @Test
    void formatProductNumber_success() {
        String input = "MSC-1234";
        assertEquals(StringUtilities.formatProductNumber(input), "1234");
    }
}
