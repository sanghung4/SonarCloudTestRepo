package com.reece.platform.eclipse.enums;

public enum SearchInputTypeEnum {
    KEYWORD(1),
    ID(2);

    private final int value;

    SearchInputTypeEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
