package com.reece.platform.accounts.model.enums;

import lombok.Getter;

import java.util.Arrays;

public enum PhoneTypeEnum {
    MOBILE("mobilePhone", "Mobile"),
    HOME("homePhone", "Home"),
    OFFICE("primaryPhone", "Office");

    @Getter
    public final String oktaAttributeName;

    @Getter
    public final String displayName;

    public final String id;

    PhoneTypeEnum(String oktaAttributeName, String displayName) {
        this.oktaAttributeName = oktaAttributeName;
        this.displayName = displayName;
        this.id = name();
    }

    public String getId() {
        return this.name();
    }

    public String getName() { return this.name(); }

    public static PhoneTypeEnum getPhoneTypeByName(String name) {
        return Arrays.stream(PhoneTypeEnum.values())
                .filter(phoneType -> phoneType.name().equals(name))
                .findFirst()
                .get();
    }
}
