package com.reece.platform.mincron.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProgramCallNumberEnum {

    GET_CONTRACT_LIST("AIR7080"),
    GET_CONTRACT_HEADER("AIR7060"),
    GET_CONTRACT_ITEM_LIST("AIR7071"),
    GET_ORDER_HEADER("AIR7130"),
    GET_INVOICE_LIST("AIR6220"),
    GET_ORDER_ITEM_LIST("AIR7140"),
    GET_ORDER_LIST("AIR7150");

    @Getter
    public final String programCallNumber;
}
