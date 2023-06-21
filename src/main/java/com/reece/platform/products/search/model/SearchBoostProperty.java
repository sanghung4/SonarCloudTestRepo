package com.reece.platform.products.search.model;

import java.util.Locale;

public enum SearchBoostProperty {
    PRODUCT_SEARCH_BOOST_1_NBR,
    PRODUCT_SEARCH_BOOST_2_NBR,
    PRODUCT_SEARCH_BOOST_3_NBR,
    PRODUCT_SEARCH_BOOST_4_NBR;

    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
