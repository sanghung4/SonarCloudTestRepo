package com.reece.specialpricing.utilities;

public enum ExcludedPriceLineEnum {
    ICPCORP("MSC-ICPCORP"), REFRIG("MSC-REFRIG"), OWCORN("MSC-OWCORN");
    public final String label;

    ExcludedPriceLineEnum(String label) {
        this.label = label;
    }
}
