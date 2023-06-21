package com.reece.platform.products.search.model;

import java.util.Locale;

public enum SearchFieldName {
    VENDOR_PART_NBR,
    CLEAN_WEB_DESCRIPTION,
    CLEAN_PRODUCT_BRAND,
    CLEAN_VENDOR_PART_NBR,
    WEB_DESCRIPTION,
    CUSTOMER_PART_NUMBER,
    MFR_FULL_NAME,
    ERP_PRODUCT_ID,
    SEARCH_KEYWORD_TEXT;

    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
