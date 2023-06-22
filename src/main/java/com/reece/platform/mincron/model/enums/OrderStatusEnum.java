package com.reece.platform.mincron.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatusEnum {
    I("Invoiced"),
    R("Reviewed"),
    P("Priced"),
    C("Changed"),
    O("Open"),
    K("Reserved"),
    N("Pending");

    @Getter
    public final String orderStatus;

    public static String normalizeOrderStatus(OrderStatusEnum orderStatusEnum) {
        if (orderStatusEnum.equals(OrderStatusEnum.R) ||  orderStatusEnum.equals(OrderStatusEnum.P) || orderStatusEnum.equals(OrderStatusEnum.C)) {
            return "Shipped";
        } else return orderStatusEnum.getOrderStatus();
    }
}
