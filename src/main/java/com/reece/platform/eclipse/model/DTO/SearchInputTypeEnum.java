package com.reece.platform.eclipse.model.DTO;

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
