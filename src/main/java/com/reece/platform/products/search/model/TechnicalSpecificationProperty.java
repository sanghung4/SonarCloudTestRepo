package com.reece.platform.products.search.model;

import java.util.Locale;

public enum TechnicalSpecificationProperty {
    BTU,
    CAPACITY,
    COLORFINISH,
    DEPTH,
    FLOW_RATE,
    HEIGHT,
    INLET_SIZE,
    LENGTH,
    MATERIAL,
    PRESSURE_RATING,
    SIZE,
    TEMPERATURE_RATING,
    VOLTAGE,
    WATTAGE,
    WIDTH,
    TONNAGE;

    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
