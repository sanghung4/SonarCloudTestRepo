package com.reece.platform.products.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MincronAgingEnum {
    MONTH_TO_DATE("1", "Total"),
    CURRENT("2", "Current"),
    DAYS_30("3", "31-60"),
    DAYS_60("4", "61-90"),
    DAYS_90("5", "91-120"),
    FUTURE("6", "Future"),
    DAYS_120("7", "Over120");

    @Getter
    public final String agingCode;

    @Getter
    public final String name;

    public static MincronAgingEnum fromAgingCode(String agingCode) {
        if (agingCode != null) {
            if (agingCode.equals("1")) {
                return MONTH_TO_DATE;
            }
            if (agingCode.equals("2")) {
                return CURRENT;
            }
            if (agingCode.equals("3")) {
                return DAYS_30;
            }
            if (agingCode.equals("4")) {
                return DAYS_60;
            }
            if (agingCode.equals("5")) {
                return DAYS_90;
            }
            if (agingCode.equals("6")) {
                return FUTURE;
            }
            if (agingCode.equals("7")) {
                return DAYS_120;
            }
        }
        return null;
    }
}
