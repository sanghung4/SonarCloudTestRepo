package com.reece.platform.mincron.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShipmentMethodEnum {
    D("Direct"),
    O("Our Truck"),
    P("Pickup"),
    S("Shipped");

    @Getter
    public final String shipMethod;

    public static boolean isDelivery(ShipmentMethodEnum shipmentMethodEnum) {
        if (shipmentMethodEnum.equals(ShipmentMethodEnum.O) ||  shipmentMethodEnum.equals(ShipmentMethodEnum.S)) {
            return true;
        } else return false;
    }
}
