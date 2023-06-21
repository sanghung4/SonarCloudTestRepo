package com.reece.platform.products.orders.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WebStatus {
    SUBMITTED(false),
    READY_FOR_PICKUP(true),
    PICKED_UP(false),
    PICKUP_NOW(false),
    IN_PROCESS(false),
    MANIFESTED(false),
    DELIVERED(true),
    SHIPPED(true),
    DIRECT(false),
    INVOICED(false);

    @Getter
    private final boolean notifyOnChange;
}
