package com.reece.platform.eclipse.model.DTO;

import io.micrometer.core.instrument.search.Search;

public enum SearchTypeEnum {
    ACTIVE_PRIMARY(1),
    ALL_PRIMARY(2),
    CATALOG_INDEX(3);

    private final int value;

    SearchTypeEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
