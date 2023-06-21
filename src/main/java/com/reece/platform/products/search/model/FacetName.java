package com.reece.platform.products.search.model;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public enum FacetName {
    MFR_FULL_NAME,
    FLOW_RATE,
    PRODUCT_LINE,
    CATEGORY_1_NAME,
    CATEGORY_2_NAME,
    CATEGORY_3_NAME,
    LOW_LEAD_COMPLIANT_FLAG,
    HAZARDOUS_MATERIAL_FLAG,
    MERCURY_FREE_FLAG,
    WATER_SENSE_COMPLIANT_FLAG,
    ENERGY_STAR_FLAG,
    MATERIAL,
    COLORFINISH,
    SIZE,
    LENGTH,
    WIDTH,
    HEIGHT,
    DEPTH,
    VOLTAGE,
    TONNAGE,
    BTU,
    PRESSURE_RATING,
    TEMPERATURE_RATING,
    INLET_SIZE,
    CAPACITY,
    WATTAGE,
    IN_STOCK_LOCATION;

    private static final Map<String, FacetName> FACET_NAMES = Map.ofEntries(
        Map.entry("brand", FacetName.MFR_FULL_NAME),
        Map.entry("flowRate", FacetName.FLOW_RATE),
        Map.entry("line", FacetName.PRODUCT_LINE),
        Map.entry("category1", FacetName.CATEGORY_1_NAME),
        Map.entry("category2", FacetName.CATEGORY_2_NAME),
        Map.entry("category3", FacetName.CATEGORY_3_NAME),
        Map.entry("Low lead compliant", FacetName.LOW_LEAD_COMPLIANT_FLAG),
        Map.entry("Hazardous material", FacetName.HAZARDOUS_MATERIAL_FLAG),
        Map.entry("Mercury free", FacetName.MERCURY_FREE_FLAG),
        Map.entry("WaterSense compliant", FacetName.WATER_SENSE_COMPLIANT_FLAG),
        Map.entry("Energy Star", FacetName.ENERGY_STAR_FLAG),
        Map.entry("material", FacetName.MATERIAL),
        Map.entry("colorFinish", FacetName.COLORFINISH),
        Map.entry("size", FacetName.SIZE),
        Map.entry("length", FacetName.LENGTH),
        Map.entry("width", FacetName.WIDTH),
        Map.entry("height", FacetName.HEIGHT),
        Map.entry("depth", FacetName.DEPTH),
        Map.entry("voltage", FacetName.VOLTAGE),
        Map.entry("tonnage", FacetName.TONNAGE),
        Map.entry("btu", FacetName.BTU),
        Map.entry("pressureRating", FacetName.PRESSURE_RATING),
        Map.entry("temperatureRating", FacetName.TEMPERATURE_RATING),
        Map.entry("inletSize", FacetName.INLET_SIZE),
        Map.entry("capacity", FacetName.CAPACITY),
        Map.entry("wattage", FacetName.WATTAGE),
        Map.entry("inStockLocation", FacetName.IN_STOCK_LOCATION)
    );

    public static Optional<FacetName> fromClientName(String clientName) {
        return Optional.ofNullable(FACET_NAMES.get(clientName));
    }

    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
