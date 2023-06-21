package com.reece.platform.products.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MincronOrderStatus {
    INVOICED("invoice"),
    SHIPPED("order"),
    OPEN("order"),
    RESERVED("order"),
    PENDING("order");

    @Getter
    private final String orderType;
}
